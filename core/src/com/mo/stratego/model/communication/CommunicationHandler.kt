package com.mo.stratego.model.communication

import com.badlogic.gdx.Gdx
import com.mo.stratego.model.Move
import com.mo.stratego.model.map.StartingGrid
import com.mo.stratego.model.player.StartNumber
import com.mo.stratego.util.Constants
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.greenrobot.eventbus.EventBus

/**
 * Handles the communication between the core module and the platform specific
 * modules. Provides wrapper methods for the [ICommunication] interface with
 * additional features. Broadcasts [ICommunicationEventListener] event's on the
 * [EventBus].
 */
object CommunicationHandler : ICommunicationEventListener {

    lateinit var iCom: ICommunication
        private set

    private lateinit var json: Json

    var connectedDeviceName: String? = null
        private set

    /**
     * Init.
     * @param iCom Communication Handler
     */
    fun init(iCom: ICommunication) {
        this.iCom = iCom
        iCom.listener = this
        json = Json(JsonConfiguration.Stable)
    }

    override fun onDataReceived(data: ByteArray?) {
        val data = data?.toString(Charsets.UTF_8) ?: return
        Gdx.app.log(Constants.TAG_BLUETOOTH, "proxy rec: $data")
        EventBus.getDefault().post(DataReceivedEvent(data))
    }

    override fun onDataWrite() {
        EventBus.getDefault().post(DataWriteEvent())
    }

    override fun onConnecting() {
        EventBus.getDefault().post(OnConnectingEvent())
    }

    override fun onError(msg: String) {
        EventBus.getDefault().post(OnErrorEvent(msg))
        connectedDeviceName = null
    }

    override fun onConnected(name: String) {
        connectedDeviceName = name
        EventBus.getDefault().post(OnConnectedEvent(name))
    }

    /**
     * Serialize object to json and sends it via iCom, if connected.
     * @param obj Object
     */
    fun serialize(obj: Any) {
        if (!iCom.isConnected)
            return

        var jsonData = when (obj) {
            is StartNumber -> json.stringify(StartNumber.serializer(), obj)
            is Move -> json.stringify(Move.serializer(), obj)
            is StartingGrid -> json.stringify(StartingGrid.serializer(), obj)
            is ControlMessage -> json.stringify(ControlMessage.serializer(),
                                                obj)
            else -> null
        }

        jsonData?.also {
            // add \n as message delimiter
            iCom.writeData(it.toByteArray() + '\n'.toByte())
            Gdx.app.log(Constants.TAG_BLUETOOTH, "data send: $it")
        }
    }


    /**
     * Deserialize json object to class instance.
     * @param str Json string
     * @return Instance of deserialized json.
     */
    fun deserialize(str: String): Any? {
        val jsonData = json.parseJson(str).jsonObject
        try {
            val name = jsonData["className"]?.primitive?.contentOrNull
            return when (name) {
                StartNumber::class.java.name ->
                    json.parse(StartNumber.serializer(), str)
                Move::class.java.name ->
                    json.parse(Move.serializer(), str)
                StartingGrid::class.java.name ->
                    json.parse(StartingGrid.serializer(), str)
                ControlMessage::class.java.name ->
                    json.parse(ControlMessage.serializer(), str)
                else -> null
            }
        } catch (e: Exception) {
            Gdx.app.log(Constants.TAG_BLUETOOTH, "parse error: $e")
            return null
        }

    }
}
