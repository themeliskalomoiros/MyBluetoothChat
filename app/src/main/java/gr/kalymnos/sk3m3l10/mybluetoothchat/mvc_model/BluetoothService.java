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
    private BluetoothAdapter bluetoothAdapter;
    private Thread serverThread, clientThread;

    protected BluetoothService() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public UUID getUuid() {
        return BluetoothConstants.SECURE_UUID;
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

    public void startServerMode() {
        if (serverThread == null) {
            serverThread = new ServerThread();
            serverThread.start();
        }
    }

    public void stopServerMode() {
        if (serverThread != null) {
            ((ServerThread) serverThread).cancel();
            serverThread = null;
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
            ((ClientThread) clientThread).cancel();
            clientThread = null;
        }
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

    protected abstract void manageClientsConnectedSocket(String deviceName, BluetoothSocket bluetoothSocket);

    protected abstract void manageServersConnectedSocket(BluetoothSocket socket);

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

    private class ClientThread extends Thread {
        private final BluetoothSocket bluetoothSocket;

        private final BluetoothDevice connectedDevice;

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
        }

        @Override
        public void run() {
            // Cancel discovery because it otherwise slows down the connection
            cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                bluetoothSocket.connect();
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
            manageClientsConnectedSocket(connectedDevice.getName(), bluetoothSocket);
        }

        // Closes the client socket and causes the thread to finish
        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }

    }

}
