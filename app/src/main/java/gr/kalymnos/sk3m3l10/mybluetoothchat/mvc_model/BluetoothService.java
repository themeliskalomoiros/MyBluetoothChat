package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.Set;
import java.util.UUID;
import android.os.Handler;

public abstract class BluetoothService {

    protected static final String TAG = "BluetoothService";

    private static final UUID INSECURE_UUID = UUID.fromString("8b10b125-076a41a2b1ca-7c37b56bc3a7");

    public static final String ACTION_REQUEST_ENABLE = BluetoothAdapter.ACTION_REQUEST_ENABLE;
    public static final int REQUEST_CODE_ENABLE_BT = 155;

    private BluetoothAdapter bluetoothAdapter;
    private Handler handler;

    public BluetoothService(Handler handler) {
        this.handler = handler;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public UUID getUuid() {
        return INSECURE_UUID;
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

}
