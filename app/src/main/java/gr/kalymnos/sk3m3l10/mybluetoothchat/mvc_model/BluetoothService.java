package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import gr.kalymnos.sk3m3l10.mybluetoothchat.BuildConfig;

public abstract class BluetoothService {

    protected static final String TAG = "BluetoothService";

    private static final UUID SECURE_UUID = UUID.fromString("a80ea0da-e1a2-11e8-9f32-f2801f1b9fd1");

    public static final String ACTION_REQUEST_ENABLE = BluetoothAdapter.ACTION_REQUEST_ENABLE;
    public static final String ACTION_REQUEST_DISCOVERABLE = BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE;
    public static final String EXTRA_DISCOVERABLE_DURATION = BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION;
    public static final int DISCOVERABLE_TIME_IN_SECONDS = 3600;
    public static final String ACTION_DISCOVERY_STARTED = BluetoothAdapter.ACTION_DISCOVERY_STARTED;
    public static final String ACTION_DISCOVERY_FINISHED = BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
    public static final String ACTION_DEVICE_FOUND = BluetoothDevice.ACTION_FOUND;
    public static final String EXTRA_DEVICE = BluetoothDevice.EXTRA_DEVICE;
    public static final String EXTRA_CLASS = BluetoothDevice.EXTRA_CLASS;
    public static final int REQUEST_CODE_ENABLE_BT = 155;
    public static final int REQUEST_CODE_DISCOVERABLE = 156;

    private BluetoothAdapter bluetoothAdapter;
    protected Handler handler;

    public BluetoothService(Handler handler) {
        this.handler = handler;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public UUID getUuid() {
        return SECURE_UUID;
    }

    public boolean isBluetoothSupported() {
        if (bluetoothAdapter != null) {
            return true;
        }
        return false;
    }

    public boolean isBluetoothEnabled() {
        if (bluetoothAdapter.isEnabled()) {
            return true;
        }
        return false;
    }

    public Set<BluetoothDevice> getPairedDevices() {
        return bluetoothAdapter.getBondedDevices();
    }

    public boolean startDiscovery() {
        return bluetoothAdapter.startDiscovery();
    }

    public void cancelDiscovery() {
        bluetoothAdapter.cancelDiscovery();
    }

    public boolean isDiscovering() {
        return bluetoothAdapter.isDiscovering();
    }

    private class ServerThread extends Thread {
        private final BluetoothServerSocket serverSocket;

        ServerThread() {
            // Using temp varialbe because serverSocket is final
            BluetoothServerSocket tmp = null;
            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(BuildConfig.APPLICATION_ID, getUuid());
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            serverSocket = tmp;
        }

        @Override
        public void run() {
            BluetoothSocket socket;
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    socket = serverSocket.accept();
                } catch (IOException e) {
                    Log.e(TAG, "Socket's accept() method failed", e);
                    break;
                }
                if (socket != null) {
                    // A connection was accepted. Perform work associated with the connection
                    // in a seperate thread.
                    manageServersConnectedSocket(socket);
                    try {
                        // We do not need any more connections because we will chat only with
                        // this device, so we close the server socket because it consumes hell of resources.
                        serverSocket.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Could not close() the server socket");
                    }
                    break;
                }
            }

        }

        // Closes the connect socket and causes the thread to finish
        public void cancel() {
            try {
                serverSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    protected abstract void manageServersConnectedSocket(BluetoothSocket socket);

}
