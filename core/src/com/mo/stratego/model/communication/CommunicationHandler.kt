package com.mo.stratego.model.communication

import com.badlogic.gdx.Gdx
import com.mo.stratego.model.Move
import com.mo.stratego.model.map.StartingGrid
import com.mo.stratego.model.player.StartNumber
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

    private val json = Json(JsonConfiguration.Stable)

    /**
     * Init.
     * @param iCom Communication Handler
     */
    fun init(iCom: ICommunication) {
        this.iCom = iCom
        iCom.listener = this
    }

    override fun onDataReceived(data: ByteArray?) {
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
    }

    override fun onConnected(name: String) {
        EventBus.getDefault().post(OnConnectedEvent(name))
    }

    // todo desc
    fun serialize(obj: Any) {
        var jsonData = when (obj) {
            is StartNumber  -> json.stringify(StartNumber.serializer(), obj)
            is Move         -> json.stringify(Move.serializer(), obj)
            is StartingGrid -> json.stringify(StartingGrid.serializer(), obj)
            else            -> null
        }

        jsonData?.also {
            Gdx.app.log("bth", "data send: $it")
            // add \n as delimiter
            iCom.writeData(it.toByteArray() + '\n'.toByte())
        }
    }


    // todo desc
    fun deserialize(str: String): Any? {
        val jsonData = json.parseJson(str).jsonObject
        try {
            val name = jsonData["className"]?.primitive?.contentOrNull
            Gdx.app.log("bth", "name: $name")
            return when (name) {
                StartNumber::class.java.name  ->
                    json.parse(StartNumber.serializer(), str)
                Move::class.java.name         ->
                    json.parse(Move.serializer(), str)
                StartingGrid::class.java.name ->
                    json.parse(StartingGrid.serializer(), str)
                else                          -> null
            }
        } catch (e: Exception) {
            Gdx.app.log("bth", "parse error: $e")
            return null
        }

    }
}
