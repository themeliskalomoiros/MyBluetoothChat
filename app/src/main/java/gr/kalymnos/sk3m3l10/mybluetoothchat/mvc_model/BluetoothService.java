package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;

import java.util.Set;
import java.util.UUID;

public abstract class BluetoothService {

    protected static final String TAG = "BluetoothService";

    private static final UUID INSECURE_UUID = UUID.fromString("8b10b125-076a41a2b1ca-7c37b56bc3a7");

    public static final String ACTION_REQUEST_ENABLE = BluetoothAdapter.ACTION_REQUEST_ENABLE;
    public static final String ACTION_DISCOVERY_STARTED = BluetoothAdapter.ACTION_DISCOVERY_STARTED;
    public static final String ACTION_DISCOVERY_FINISHED = BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
    public static final String ACTION_DEVICE_FOUND = BluetoothDevice.ACTION_FOUND;
    public static final String EXTRA_DEVICE = BluetoothDevice.EXTRA_DEVICE;
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
