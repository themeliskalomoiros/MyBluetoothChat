package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

import gr.kalymnos.sk3m3l10.mybluetoothchat.BuildConfig;

public abstract class BluetoothService {

    protected static final String TAG = "BluetoothService";
    private BluetoothAdapter bluetoothAdapter;
    private ServerThread serverThread;
    private ClientThread clientThread;
    private Context context;

    protected BluetoothService(Context context) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.context = context;
    }

    protected abstract void manageClientsConnectedSocket(BluetoothSocket bluetoothSocket);

    protected abstract void manageServersConnectedSocket(BluetoothSocket socket);

    public abstract void write(String message);

    public abstract void releaseChatResources();


    protected Context getContext() {
        return context;
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

    public boolean isDeviceDiscoverable() {
        if (bluetoothAdapter.getScanMode() == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            return true;
        }
        return false;
    }

    public String getConnectedDeviceName() {
        if (clientThread != null) {
            return clientThread.getConnectedDeviceName();
        } else if (serverThread != null) {
            return serverThread.getConnectedDeviceName();
        }
        return null;
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

    public UUID getUuid() {
        return BluetoothConstants.SECURE_UUID;
    }


    private class ServerThread extends Thread {
        private final BluetoothServerSocket serverSocket;
        private BluetoothSocket socket;
        private static final String TAG = "skemelio " + "ServerThread";

        ServerThread() {
            // Using temp varialbe because serverSocket is final
            BluetoothServerSocket tmp = null;
            try {
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(BuildConfig.APPLICATION_ID, getUuid());
            } catch (IOException e) {
                Log.e(TAG, "Socket's listen() method failed", e);
            }
            serverSocket = tmp;
            Log.d(TAG, "new instance created.");
        }

        @Override
        public void run() {
            // Keep listening until exception occurs or a socket is returned.
            while (true) {
                try {
                    Log.d(TAG, "accepting connections from other devices.");
                    socket = serverSocket.accept();
                    Log.d(TAG, "a connection was accepted.");
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
                        // this device, so we close the server socket ( also consumibg hell of resources).
                        serverSocket.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Could not close() the server socket");
                    }
                    break;
                }
            }

        }

        private String getConnectedDeviceName() {
            return socket.getRemoteDevice().getName();
        }

        // Closes the connect socket and causes the thread to finish
        public void cancel() {
            try {
                Log.d(TAG,"Attempt to close server socket");
                serverSocket.close();
                Log.d(TAG,"Server socket closed.");
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    public void startServerMode() {
        if (serverThread == null) {
            serverThread = new ServerThread();
            serverThread.start();
        }
    }

    public void stopServerMode() {
        if (serverThread != null) {
            serverThread.cancel();
            serverThread = null;
        }
    }

    private class ClientThread extends Thread {
        private final BluetoothSocket bluetoothSocket;
        private final BluetoothDevice connectedDevice;
        private static final String TAG = "skemelio " + "ClientThread";

        public ClientThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to bluetoothSocket
            // because bluetoothSocket is final
            BluetoothSocket tmp = null;
            connectedDevice = device;

            try {
                tmp = device.createRfcommSocketToServiceRecord(getUuid());
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }

            bluetoothSocket = tmp;
            Log.d(TAG, "ClientThread: new instance created.");
        }

        @Override
        public void run() {
            // Cancel discovery because it otherwise slows down the connection
            cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                Log.d(TAG, "Connecting to server...");
                bluetoothSocket.connect();
                Log.d(TAG, "Connection success.");
            } catch (IOException e) {
                // Unable to connect; close the socket and return
                try {
                    bluetoothSocket.close();
                } catch (IOException e1) {
                    Log.e(TAG, "Could not close the client socket", e1);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            manageClientsConnectedSocket(bluetoothSocket);
        }

        private String getConnectedDeviceName() {
            return connectedDevice.getName();
        }

        // Closes the client socket and causes the thread to finish
        public void cancel() {
            try {
                Log.d(TAG,"Attempt to close socket.");
                bluetoothSocket.close();
                Log.d(TAG,"Socket closed.");
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    public void startClientMode(BluetoothDevice deviceToConnect) {
        if (clientThread == null) {
            clientThread = new ClientThread(deviceToConnect);
            clientThread.start();
        }
    }

    public void stopClientMode() {
        if (clientThread != null) {
            clientThread.cancel();
            clientThread = null;
            Log.d(TAG, "Stopped client mode.");
        }
    }

}
