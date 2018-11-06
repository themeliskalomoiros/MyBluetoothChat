package gr.kalymnos.sk3m3l10.mybluetoothchat.utils;

import android.bluetooth.BluetoothDevice;

import java.util.ArrayList;
import java.util.List;

public class BluetoothDeviceUtils {
    public static List<String> getDeviceNamesList(List<BluetoothDevice> devices) {
        List<String> list = new ArrayList<>();
        for (BluetoothDevice device : devices) {
            list.add(device.getName());
        }
        return list;
    }
}
