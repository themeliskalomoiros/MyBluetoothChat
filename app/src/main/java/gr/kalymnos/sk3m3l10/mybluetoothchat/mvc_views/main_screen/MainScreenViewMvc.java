package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.main_screen;


import android.support.v7.widget.Toolbar;

import java.util.List;

import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.ViewMvc;

public interface MainScreenViewMvc extends ViewMvc {

    interface OnBluetoothScanClickListener {
        void onBluetoothScan();
    }

    interface OnDeviceItemClickListener {
        void onDeviceItemClicked(int position);
    }

    public Toolbar getToolbar();

    void bindBluetoothDeviceNames(List<String> deviceNames);

    void setOnBluetoothScanClickListener(OnBluetoothScanClickListener listener);

    void setOnDeviceItemClickListener(OnDeviceItemClickListener listener);

    void showLoadingIndicator();

    void hideLoadingIndicator();

}
