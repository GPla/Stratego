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
import java.util.*


/**
 * Class that handles the android bluetooth communication.
 */
class BluetoothHandler(context: Context) :
    ICommunication {

    override var isScanning: Boolean = false
    override var isConnected: Boolean = false
    private lateinit var service: BluetoothService
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
        conf.callListenersInMainThread = true
        conf.uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")
        BluetoothService.init(conf)

        service = BluetoothService.getDefaultInstance()

        service.setOnScanCallback(object : OnBluetoothScanCallback {
            override fun onDeviceDiscovered(device: BluetoothDevice,
                                            rssi: Int) {
                //device found, add to list
                device.name?.also {
                    availableDevices.add(device)
                    Gdx.app.log("bth", "device found: ${device.name}")
                }
            }

            override fun onStartScan() {
                isScanning = true
                Gdx.app.log("bth", "scan started")
            }

            override fun onStopScan() {
                isScanning = false
                Gdx.app.log("bth", "scan stopped")
            }
        })

        service.setOnEventCallback(object :
                                       BluetoothService.OnBluetoothEventCallback {
            override fun onDataRead(buffer: ByteArray?, length: Int) {
                Gdx.app.log("bth", "data received ($length)")
            }

            override fun onStatusChange(status: BluetoothStatus?) {
                Gdx.app.log("bth", "status:  $status")
            }

            override fun onDataWrite(buffer: ByteArray?) {
                Gdx.app.log("bth", "data write")
            }

            override fun onToast(message: String?) {
                Gdx.app.log("bth", "message: $message")
            }

            override fun onDeviceName(deviceName: String?) {
                Gdx.app.log("bth", "device name: $deviceName")

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
     * Disable bluetooth.
     */
    override fun disable() {
        adapter.disable()
    }
}