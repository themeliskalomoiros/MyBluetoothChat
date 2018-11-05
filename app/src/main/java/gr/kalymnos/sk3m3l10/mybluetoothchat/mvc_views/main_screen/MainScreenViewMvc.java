package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.main_screen;

import android.widget.Toolbar;

import java.util.List;

import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.ViewMvc;

public interface MainScreenViewMvc extends ViewMvc {

    interface OnBluetoothScanClickListener {
        void onBluetoothScan();
    }

    public Toolbar getToolbar();

    void setOnBluetoothScanClickListener(OnBluetoothScanClickListener listener);

    void bindBluetoothDeviceNames(List<String> deviceNames);
}
