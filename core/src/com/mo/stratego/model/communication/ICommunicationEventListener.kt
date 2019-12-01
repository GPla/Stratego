package com.mo.stratego.model.communication

/**
 * Events of the communication interface.
 */
interface ICommunicationEventListener {
    /**
     * Data received from connected device.
     * @param data Data
     */
    fun onDataReceived(data: ByteArray?)

    /**
     * Sending data to connected device.
     */
    fun onDataWrite()

    /**
     * Connection is trying to be established.
     */
    fun onConnecting()

    /**
     * An error occurred.
     * @param msg Error message
     */
    fun onError(msg: String)

    /**
     * Connection has been established to another device.
     * @param name Device name
     */
    fun onConnected(name: String)
}