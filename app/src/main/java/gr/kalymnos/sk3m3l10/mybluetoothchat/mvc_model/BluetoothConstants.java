package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import java.util.UUID;

public interface BluetoothConstants {

    UUID SECURE_UUID = UUID.fromString("a80ea0da-e1a2-11e8-9f32-f2801f1b9fd1");
    int DISCOVERABLE_TIME_IN_SECONDS = 3600;

    interface Actions {
        String ACTION_REQUEST_ENABLE = BluetoothAdapter.ACTION_REQUEST_ENABLE;
        String ACTION_DISCOVERY_FINISHED = BluetoothAdapter.ACTION_DISCOVERY_FINISHED;
        String ACTION_REQUEST_DISCOVERABLE = BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE;
        String ACTION_DISCOVERY_STARTED = BluetoothAdapter.ACTION_DISCOVERY_STARTED;
        String ACTION_DEVICE_FOUND = BluetoothDevice.ACTION_FOUND;
    }

    interface Extras {
        String EXTRA_DISCOVERABLE_DURATION = BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION;
        String EXTRA_DEVICE = BluetoothDevice.EXTRA_DEVICE;
        String EXTRA_CLASS = BluetoothDevice.EXTRA_CLASS;
    }

    interface RequestCodes {
        int REQUEST_CODE_ENABLE_BT = 155;
        int REQUEST_CODE_DISCOVERABLE = 156;
    }
}
