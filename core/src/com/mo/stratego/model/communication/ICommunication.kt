package com.mo.stratego.model.communication

/**
 * Interface for inter-device communication.
 */
interface ICommunication {
    /**
     * Whether or not the communication service is currently scanning for
     * other devices.
     */
    var isScanning: Boolean

    /**
     * Whether or not the communication service is connected to another device.
     */
    var isConnected: Boolean

    /**
     * Start scanning for other devices.
     *
     */
    fun startScan()

    /**
     * Stop scanning for other devices.
     *
     */
    fun stopScan()


    /**
     * @return Returns a list of names of the available devices.
     */
    fun getAvailableDevices(): List<String>

    /**
     * Connect to another device.
     *
     */
    // TODO add param
    fun connect()

    /**
     * Disconnect from other devices.
     *
     */
    fun disconnect()


}