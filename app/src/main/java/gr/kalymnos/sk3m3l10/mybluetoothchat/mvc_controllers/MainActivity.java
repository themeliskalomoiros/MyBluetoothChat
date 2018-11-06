package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_controllers;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.FakeBluetoothServiceImpl;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.main_screen.MainScreenViewMvc;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.main_screen.MainScreenViewMvcImpl;
import gr.kalymnos.sk3m3l10.mybluetoothchat.utils.BluetoothDeviceUtils;
import gr.kalymnos.sk3m3l10.mybluetoothchat.R;

import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService.ACTION_DEVICE_FOUND;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService.ACTION_DISCOVERY_FINISHED;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService.ACTION_DISCOVERY_STARTED;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService.EXTRA_DEVICE;

public class MainActivity extends AppCompatActivity implements MainScreenViewMvc.OnDeviceItemClickListener,
        MainScreenViewMvc.OnBluetoothScanClickListener {

    private MainScreenViewMvc viewMvc;

    private BluetoothService bluetoothService;
    private List<BluetoothDevice> devices = new ArrayList<>();

    private final BroadcastReceiver discoverDevicesReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_DISCOVERY_STARTED:
                    viewMvc.showLoadingIndicator();
                    Toast.makeText(context, R.string.discovery_started_label, Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_DEVICE_FOUND:
                    BluetoothDevice foundDevice = intent.getParcelableExtra(EXTRA_DEVICE);
                    devices.add(foundDevice);
                    break;
                case ACTION_DISCOVERY_FINISHED:
                    viewMvc.hideLoadingIndicator();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
        bluetoothService = new FakeBluetoothServiceImpl(new Handler());
        showPairedDevicesToList();
    }

    @Override
    public void onBluetoothScanClicked() {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeviceItemClicked(int position) {
        Toast.makeText(this, "Clicked item at position " + position, Toast.LENGTH_SHORT).show();
    }

    private void showPairedDevicesToList() {
        Set<BluetoothDevice> pairedDevices = bluetoothService.getPairedDevices();
        devices.addAll(pairedDevices);
        viewMvc.bindBluetoothDeviceNames(BluetoothDeviceUtils.getDeviceNamesList(devices));
    }

    private void setupUi() {
        initializeViewMvc();
        setSupportActionBar(viewMvc.getToolbar());
        setContentView(viewMvc.getRootView());
    }

    private void initializeViewMvc() {
        viewMvc = new MainScreenViewMvcImpl(LayoutInflater.from(this), null);
        viewMvc.setOnBluetoothScanClickListener(this);
        viewMvc.setOnDeviceItemClickListener(this);
    }
}
