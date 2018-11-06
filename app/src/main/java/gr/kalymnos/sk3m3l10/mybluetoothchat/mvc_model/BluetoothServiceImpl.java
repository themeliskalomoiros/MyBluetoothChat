package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model;

import android.bluetooth.BluetoothSocket;
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
    protected void manageClientsConnectedSocket(BluetoothSocket bluetoothSocket) {
        Message message = handler.obtainMessage();
        message.what=HandlerConstants.CONNECTION_SUCCESS;
        handler.sendMessage(message);
    }
}
