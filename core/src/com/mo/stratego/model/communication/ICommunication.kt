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
     * Event listener.
     */
    var listener: ICommunicationEventListener?

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
     * @param device Name of device
     */
    fun connect(device: String)

    /**
     * Disconnect from other devices.
     */
    fun disconnect()

    /**
     * DisableS service.
     */
    fun disable()

    /**
     * Write data to the output stream.
     * @param data Data to send
     */
    fun writeData(data: ByteArray)

    /**
     * Stop and dispose the service.
     */
    fun stopService()
}