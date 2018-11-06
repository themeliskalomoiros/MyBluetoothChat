package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_controllers;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import gr.kalymnos.sk3m3l10.mybluetoothchat.R;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.FakeBluetoothServiceImpl;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.main_screen.MainScreenViewMvc;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.main_screen.MainScreenViewMvcImpl;
import gr.kalymnos.sk3m3l10.mybluetoothchat.utils.BluetoothDeviceUtils;

import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService.ACTION_DEVICE_FOUND;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService.ACTION_DISCOVERY_FINISHED;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService.ACTION_DISCOVERY_STARTED;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService.ACTION_REQUEST_DISCOVERABLE;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService.ACTION_REQUEST_ENABLE;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService.DISCOVERABLE_TIME_IN_SECONDS;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService.EXTRA_DEVICE;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService.EXTRA_DISCOVERABLE_DURATION;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService.REQUEST_CODE_DISCOVERABLE;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService.REQUEST_CODE_ENABLE_BT;

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
        setupBluetoothRadio();
        getAndDisplayPairedDevices();
        registerDiscoverDevicesReceiver();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    Snackbar.make(viewMvc.getRootView(), R.string.bluetooth_enabled_label, Snackbar.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Snackbar.make(viewMvc.getRootView(), R.string.bluetooth_enabled_canceld_label, Snackbar.LENGTH_LONG).show();
                }
                break;
            case REQUEST_CODE_DISCOVERABLE:
                if (resultCode == RESULT_OK) {
                    Snackbar.make(viewMvc.getRootView(), R.string.device_discoverable_enabled_label, Snackbar.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_CANCELED) {
                    Snackbar.make(viewMvc.getRootView(), R.string.device_discoverable_disabled_label, Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(discoverDevicesReceiver);
    }

    @Override
    public void onBluetoothScanClicked() {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeviceItemClicked(int position) {
        Toast.makeText(this, "Clicked item at position " + position, Toast.LENGTH_SHORT).show();
    }

    private void setupBluetoothRadio() {
        if (!bluetoothService.isBluetoothSupported()) {
            Snackbar.make(viewMvc.getRootView(), R.string.bluetooth_not_supported_label, Snackbar.LENGTH_LONG).show();
        }
        if (!bluetoothService.isBluetoothEnabled()) {
            startActivityForResult(new Intent(ACTION_REQUEST_ENABLE), REQUEST_CODE_ENABLE_BT);
        }
        Intent discoverableIntent = new Intent(ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_TIME_IN_SECONDS);
        startActivityForResult(discoverableIntent, REQUEST_CODE_DISCOVERABLE);
    }

    private void getAndDisplayPairedDevices() {
        Set<BluetoothDevice> pairedDevices = bluetoothService.getPairedDevices();
        devices.addAll(pairedDevices);
        viewMvc.bindBluetoothDeviceNames(BluetoothDeviceUtils.getDeviceNamesList(devices));
    }

    private void registerDiscoverDevicesReceiver() {
        IntentFilter filterFound = new IntentFilter(ACTION_DEVICE_FOUND);
        IntentFilter filterDiscoveryStarted = new IntentFilter(ACTION_DISCOVERY_STARTED);
        IntentFilter filterDiscoveryFinished = new IntentFilter(ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoverDevicesReceiver, filterDiscoveryStarted);
        registerReceiver(discoverDevicesReceiver, filterFound);
        registerReceiver(discoverDevicesReceiver, filterDiscoveryFinished);
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
