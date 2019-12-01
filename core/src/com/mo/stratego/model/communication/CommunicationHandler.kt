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
    }

    override fun onDataReceived(data: ByteArray?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDataWrite() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnecting() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onError(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnected(name: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}