package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_controllers.HandlerConstants;

public class BluetoothServiceImpl extends BluetoothService {
    public BluetoothServiceImpl(Handler handler) {
        super(handler);
    }

    @Override
    protected void manageServersConnectedSocket(BluetoothSocket socket) {

    }

    @Override
    protected void manageClientsConnectedSocket(String deviceName, BluetoothSocket bluetoothSocket) {
        Message message = handler.obtainMessage();
        message.what = HandlerConstants.CONNECTION_SUCCESS;
        Bundle bundle = new Bundle();
        bundle.putString(BluetoothConstants.Extras.EXTRA_DEVICE, deviceName);
        bundle.putParcelable(BluetoothConstants.Extras.EXTRA_SOCKET_WRAPPER,
                new ParcelableBluetoothSocketWrapper(bluetoothSocket));
        message.setData(bundle);
        message.sendToTarget();
    }
}
