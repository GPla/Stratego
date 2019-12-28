package com.mo.stratego.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import com.badlogic.gdx.Gdx
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothConfiguration
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService.OnBluetoothScanCallback
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothStatus
import com.mo.stratego.model.communication.ICommunication
import com.mo.stratego.model.communication.ICommunicationEventListener
import com.mo.stratego.util.Constants
import java.util.*


/**
 * Class that handles the android bluetooth communication.
 */
class BluetoothHandler(context: Context) :
    ICommunication {

    override var isScanning: Boolean = false
    override var isConnected: Boolean = false
    override var listener: ICommunicationEventListener? = null

    private val service: BluetoothService
    private val adapter = BluetoothAdapter.getDefaultAdapter()
    private val availableDevices = mutableListOf<BluetoothDevice>()

    /**
     * Initializes the bluetooth service.
     * From https://github.com/douglasjunior/AndroidBluetoothLibrary.
     */
    init {
        val conf = BluetoothConfiguration()
        conf.context = context
        conf.bluetoothServiceClass = BluetoothClassicExtendedService::class.java
        conf.bufferSize = 2048
        conf.characterDelimiter = '\n'
        conf.deviceName = "Stratego"
        conf.callListenersInMainThread = false
        conf.uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
        BluetoothService.init(conf)

        service = BluetoothService.getDefaultInstance()

        service.setOnScanCallback(object : OnBluetoothScanCallback {
            override fun onDeviceDiscovered(device: BluetoothDevice,
                                            rssi: Int) {
                //device found, add to list
                device.name?.also {
                    availableDevices.add(device)
                    Gdx.app.log(Constants.TAG_BLUETOOTH,
                                "device found: ${device.name}")
                }
            }

            override fun onStartScan() {
                isScanning = true
                Gdx.app.log(Constants.TAG_BLUETOOTH, "scan started")
            }

            override fun onStopScan() {
                isScanning = false
                Gdx.app.log(Constants.TAG_BLUETOOTH, "scan stopped")
            }
        })

        // Call ICommunicationEventListener
        service.setOnEventCallback(object :
                                       BluetoothService.OnBluetoothEventCallback {
            override fun onDataRead(buffer: ByteArray?, length: Int) {
                Gdx.app.log(Constants.TAG_BLUETOOTH, "data received ($length)")
                listener?.onDataReceived(buffer)
            }

            override fun onStatusChange(status: BluetoothStatus?) {
                Gdx.app.log(Constants.TAG_BLUETOOTH, "status:  $status")
                when (status) {
                    BluetoothStatus.CONNECTING -> listener?.onConnecting()
                }
            }

            override fun onDataWrite(buffer: ByteArray?) {
                Gdx.app.log(Constants.TAG_BLUETOOTH,
                            "data write (${buffer?.size})")
                listener?.onDataWrite()
            }

            override fun onToast(message: String?) {
                isConnected = false
                Gdx.app.log(Constants.TAG_BLUETOOTH, "message: $message")
                listener?.onError(message ?: "Error")
            }

            override fun onDeviceName(deviceName: String?) {
                isConnected = true
                Gdx.app.log(Constants.TAG_BLUETOOTH, "device name: $deviceName")
                listener?.onConnected(deviceName ?: "")
            }
        })

    }

    /**
     * Enables Bluetooth if disabled and scans for other devices.
     */
    override fun startScan() {
        availableDevices.clear()

        // turn on bluetooth
        if (!adapter.isEnabled)
            adapter.enable()

        if (isScanning)
            service.stopScan()

        // scan for devices
        service.startScan()
    }

    /**
     * Stops the scan and leaves bluetooth in the state as is.
     */
    override fun stopScan() {
        service.stopScan()
    }

    /**
     * @return Returns a list of names of the available devices.
     */
    override fun getAvailableDevices(): List<String> =
            availableDevices.map { it.name }

    override fun connect(device: String) {
        val dev = availableDevices.find { it.name == device } ?: return
        service.connect(dev)
    }

    override fun disconnect() {
        service.disconnect()
    }

    /**
     * Disables bluetooth.
     */
    override fun disable() {
        adapter.disable()
    }

    override fun writeData(data: ByteArray) {
        service.write(data)
    }

    override fun stopService() {
        service.stopService()
    }

    /**
     * Close listener. Connections to this device cannot be established.
     */
    fun closeListener() {
        (service as BluetoothClassicExtendedService).closeListener()
    }
}