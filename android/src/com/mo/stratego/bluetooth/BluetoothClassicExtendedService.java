/*
 * MIT License
 *
 * Copyright (c) 2015 Douglas Nassif Roma Junior
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.mo.stratego.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothConfiguration;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothService;
import com.github.douglasjunior.bluetoothclassiclibrary.BluetoothStatus;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * Code adapted from Android Open Source Project
 */
//TODO make discoverable
public class BluetoothClassicExtendedService extends BluetoothService {

    private static final String TAG = BluetoothClassicExtendedService.class.getSimpleName();

    // Unique UUID for this application
    //private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    // Member fields
    private final BluetoothAdapter mAdapter;
    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mScanReceiver = new BroadcastReceiver() {
        @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN,
                                     Manifest.permission.BLUETOOTH})
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed
                // already
                final int RSSI = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

                if (onScanCallback != null)
                    runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            onScanCallback.onDeviceDiscovered(device, RSSI);
                        }
                    });
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                stopScan();
            }
        }
    };

    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    protected BluetoothClassicExtendedService(BluetoothConfiguration config) {
        super(config);
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        mStatus = BluetoothStatus.NONE;
    }

    /**
     * Start the ConnectThread to initiate a connection to a remote device.
     *
     * @param device The BluetoothDevice to connect
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    public synchronized void connect(BluetoothDevice device) {
        if (D)
            Log.d(TAG, "connect to: " + device);

        disconnect();

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        updateState(BluetoothStatus.CONNECTING);
    }

    @Override
    public void disconnect() {
        if (D)
            Log.d(TAG, "disconnect");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread listening for connections
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
    }

    // TODO desc
    public void closeListener() {
        // Cancel any thread listening for connections
        if (mAcceptThread != null) {
            mAcceptThread.cancel();
            mAcceptThread = null;
        }
    }

    /**
     * Start the ConnectedThread to begin managing a Bluetooth connection
     *
     * @param socket The BluetoothSocket on which the connection was made
     * @param device The BluetoothDevice that has been connected
     */
    @RequiresPermission(Manifest.permission.BLUETOOTH)
    private synchronized void connected(BluetoothSocket socket, final BluetoothDevice device) {
        if (D)
            Log.d(TAG, "connected");

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        // Send the name of the connected device back to the UI Activity
        if (onEventCallback != null)
            runOnMainThread(new Runnable() {

                @RequiresPermission(Manifest.permission.BLUETOOTH)
                @Override
                public void run() {
                    onEventCallback.onDeviceName(device.getName());
                }

            });

        updateState(BluetoothStatus.CONNECTED);
    }

    /**
     * Stop all threads
     */
    public synchronized void stopService() {
        if (D)
            Log.d(TAG, "stop");

        disconnect();

        if (BluetoothService.mDefaultServiceInstance == this)
            BluetoothService.mDefaultServiceInstance = null;
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see
     */
    public synchronized void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;
        // Synchronize a copy of the ConnectedThread

        if (mStatus != BluetoothStatus.CONNECTED)
            return;

        r = mConnectedThread;

        // Perform the write unsynchronized
        r.write(out);
    }

    /**
     * Indicate that the connection attempt failed and notify the UI Activity.
     */
    private void connectionFailed() {
        updateState(BluetoothStatus.NONE);

        // Send a failure message back to the Activity
        if (onEventCallback != null)
            runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    onEventCallback.onToast("Could not connect to device");
                }
            });
    }

    /**
     * Indicate that the connection was lost and notify the UI Activity.
     */
    private void connectionLost() {
        updateState(BluetoothStatus.NONE);

        // Send a failure message back to the Activity

        if (onEventCallback != null)
            runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    onEventCallback.onToast("Connection lost");
                }
            });
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN,
                                 Manifest.permission.BLUETOOTH})
    @Override
    public void startScan() {
        if (onScanCallback != null)
            runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    onScanCallback.onStartScan();
                }
            });


        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mConfig.context.registerReceiver(mScanReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mConfig.context.registerReceiver(mScanReceiver, filter);

        // MODIFIED: hacky way to make device discoverable
        // from https://stackoverflow.com/a/30269261/7938503
        Method method;
        try {
            method = mAdapter.getClass().getMethod("setScanMode", int.class,
                                                   int.class);
            method.invoke(mAdapter,
                          BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE,
                          120);
            Log.e("invoke", "method invoke successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // MODIFIED: added open listener on scan
        // Create bluetooth master
        // opens thread to accept connections
        if (mAcceptThread == null) {
            mAcceptThread = new AcceptThread();
            mAcceptThread.start();
        }

        if (mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();
        }

        mAdapter.startDiscovery();
    }

    @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH_ADMIN,
                                 Manifest.permission.BLUETOOTH})
    @Override
    public void stopScan() {
        try {
            mConfig.context.unregisterReceiver(mScanReceiver);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }

        if (mAdapter.isDiscovering()) {
            mAdapter.cancelDiscovery();
        }

        if (onScanCallback != null)
            runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    onScanCallback.onStopScan();
                }
            });
    }

    /**
     * This thread runs while attempting to make an outgoing connection with a device. It runs straight through; the
     * connection either succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        @RequiresPermission(Manifest.permission.BLUETOOTH)
        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // Get a BluetoothSocket for a connection with the given BluetoothDevice
            try {
                tmp = device.createRfcommSocketToServiceRecord(mConfig.uuid);
            } catch (Exception e) {
                Log.e(TAG, "create() failed", e);
            }

            // MODIFIED: removed audio stuff

            mmSocket = tmp;
        }

        @RequiresPermission(allOf = {Manifest.permission.BLUETOOTH,
                                     Manifest.permission.BLUETOOTH_ADMIN})
        public void run() {
            Log.i(TAG, "BEGIN mConnectThread");
            setName("ConnectThread");

            // Always cancel discovery because it will slow down a connection
            if (mAdapter.isDiscovering())
                mAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket
            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();
            } catch (Exception e) {
                e.printStackTrace();
                connectionFailed();
                // Close the socket
                try {
                    mmSocket.close();
                } catch (Exception e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
                return;
            }

            // Reset the ConnectThread because we're done
            synchronized (BluetoothClassicExtendedService.this) {
                mConnectThread = null;

                // MODIFIED: added close listener
                if (mAcceptThread != null)
                    mAcceptThread.cancel();
                mAcceptThread = null;
            }

            // Start the connected thread
            connected(mmSocket, mmDevice);
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (Exception e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
            try {
                interrupt();
            } catch (Exception e) {
                Log.e(TAG, "interrupt() of Thread failed", e);
            }
        }
    }

    // MODIFIED: Added from
    // https://developer.android.com/guide/topics/connectivity/bluetooth#java
    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            Log.d(TAG, "create AcceptThread");
            BluetoothAdapter bluetoothAdapter =
                    BluetoothAdapter.getDefaultAdapter();

            // Use a temporary object that is later assigned to mmServerSocket
            // because mmServerSocket is final.
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code.
                tmp = bluetoothAdapter
                        .listenUsingRfcommWithServiceRecord(mConfig.deviceName,
                                                            mConfig.uuid);
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run() {
            Log.d(TAG, "BEGIN AcceptThread");
            BluetoothSocket socket = null;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }

                if (socket != null) {
                    // A connection was accepted. Perform work associated with
                    // the connection in a separate thread.
                    // Reset the ConnectThread because we're done
                    synchronized (BluetoothClassicExtendedService.this) {
                        mAcceptThread = null;
                        if (mConnectThread != null)
                            mConnectThread.cancel();
                        mConnectThread = null;
                    }

                    // Start the connected thread
                    connected(socket, socket.getRemoteDevice());

                    try {
                        mmServerSocket.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Socket's close() method failed.");
                    }
                    break;
                }
            }
        }

        // Closes the connect socket and causes the thread to finish.
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }


    /**
     * This thread runs during a connection with a remote device. It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private boolean canceled = false;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "create ConnectedThread");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (Exception e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "BEGIN mConnectedThread");
            byte temp;
            final byte[] buffer = new byte[mConfig.bufferSize];
            int i = 0;
            byte byteDelimiter = (byte) mConfig.characterDelimiter;
            // Keep listening to the InputStream while connected
            while (!canceled) {
                try {
                    // Read from the InputStream
                    int read = mmInStream.read();
                    temp = (byte) read;

                    if (temp == byteDelimiter) {
                        if (i > 0) {
                            dispatchBuffer(buffer, i);
                            i = 0;
                        }
                        continue;
                    }
                    if (i == buffer.length - 1) {
                        dispatchBuffer(buffer, i);
                        i = 0;
                    }
                    buffer[i] = temp;
                    i++;
                    //System.out.println("read: " + new String(buffer, 0 , i));
                } catch (Exception e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }

        }

        private void dispatchBuffer(byte[] buffer, int i) {
            final byte[] data = new byte[i];
            System.arraycopy(buffer, 0, data, 0, i);
            if (onEventCallback != null) {
                runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        onEventCallback.onDataRead(data, data.length);
                    }
                });
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(final byte[] buffer) {
            try {
                mmOutStream.write(buffer);
                mmOutStream.flush();

                if (onEventCallback != null)
                    runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            onEventCallback.onDataWrite(buffer);
                        }
                    });
            } catch (Exception e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            canceled = true;
            try {
                mmSocket.close();
            } catch (Exception e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
            try {
                mmInStream.close();
            } catch (Exception e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
            try {
                interrupt();
            } catch (Exception e) {
                Log.e(TAG, "interrupt() of Thread failed", e);
            }
        }
    }

}