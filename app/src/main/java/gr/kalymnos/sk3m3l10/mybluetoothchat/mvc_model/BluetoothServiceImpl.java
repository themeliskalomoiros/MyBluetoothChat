package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_controllers.HandlerConstants;

public class BluetoothServiceImpl extends BluetoothService {

    private static BluetoothService instance = null;

    private BluetoothServiceImpl(Context context) {
        super(context);
    }

    public static synchronized BluetoothService getInstance(Context context) {
        if (instance == null) {
            instance = new BluetoothServiceImpl(context);
        }
        return instance;
    }

    @Override
    protected void manageServersConnectedSocket(BluetoothSocket socket) {

    }

    @Override
    protected void manageClientsConnectedSocket(String deviceName, BluetoothSocket bluetoothSocket) {

    }
}
