package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

public class FakeBluetoothServiceImpl extends BluetoothService {
    public FakeBluetoothServiceImpl(Handler handler) {
        super(handler);
    }

    @Override
    protected void manageServersConnectedSocket(BluetoothSocket socket) {

    }

    @Override
    protected void manageClientsConnectedSocket(BluetoothSocket bluetoothSocket) {

    }
}
