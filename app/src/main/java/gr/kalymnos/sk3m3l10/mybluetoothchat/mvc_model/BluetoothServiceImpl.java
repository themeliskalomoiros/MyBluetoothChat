package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.Actions.ACTION_CLIENT_CONNECTED;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.Actions.ACTION_SERVER_CONNECTED;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.Extras.EXTRA_DEVICE_NAME;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.Extras.EXTRA_SOCKET_WRAPPER;

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
        Intent serverData = new Intent(ACTION_SERVER_CONNECTED);
        serverData.putExtras(bundleServerData(socket));
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(serverData);
    }

    @Override
    protected void manageClientsConnectedSocket(String deviceName, BluetoothSocket socket) {
        Intent clientData = new Intent(ACTION_CLIENT_CONNECTED);
        clientData.putExtras(bundleClientData(deviceName, socket));
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(clientData);
    }

    @NonNull
    private Bundle bundleServerData(BluetoothSocket socket) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SOCKET_WRAPPER, new ParcelableBluetoothSocketWrapper(socket));
        return bundle;
    }

    @NonNull
    private Bundle bundleClientData(String deviceName, BluetoothSocket bluetoothSocket) {
        Bundle extras = new Bundle();
        extras.putString(EXTRA_DEVICE_NAME, deviceName);
        extras.putParcelable(EXTRA_SOCKET_WRAPPER, new ParcelableBluetoothSocketWrapper(bluetoothSocket));
        return extras;
    }
}
