package com.mo.stratego.model.communication

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
}
