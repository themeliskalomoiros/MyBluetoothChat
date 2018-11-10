package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_controllers;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import gr.kalymnos.sk3m3l10.mybluetoothchat.R;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothServiceImpl;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.main_screen.MainScreenViewMvc;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.main_screen.MainScreenViewMvcImpl;
import gr.kalymnos.sk3m3l10.mybluetoothchat.utils.BluetoothDeviceUtils;

import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.Actions.ACTION_CLIENT_CONNECTED;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.Actions.ACTION_DEVICE_FOUND;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.Actions.ACTION_DISCOVERY_FINISHED;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.Actions.ACTION_DISCOVERY_STARTED;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.Actions.ACTION_REQUEST_DISCOVERABLE;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.Actions.ACTION_REQUEST_ENABLE;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.Actions.ACTION_SERVER_CONNECTED;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.DISCOVERABLE_TIME_IN_SECONDS;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.Extras.EXTRA_DEVICE;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.Extras.EXTRA_DISCOVERABLE_DURATION;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.RequestCodes.REQUEST_CODE_DISCOVERABLE;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.RequestCodes.REQUEST_CODE_ENABLE_BT;

public class MainActivity extends AppCompatActivity implements MainScreenViewMvc.OnDeviceItemClickListener,
        MainScreenViewMvc.OnBluetoothScanClickListener {

    private static final int REQUEST_CODE_COARSE_LOCATION = 123;

    private MainScreenViewMvc viewMvc;

    private BluetoothService bluetoothService;
    private Set<BluetoothDevice> devices = new LinkedHashSet<>();

    private final BroadcastReceiver deviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_DISCOVERY_STARTED:
                    viewMvc.showLoadingIndicator();
                    break;
                case ACTION_DEVICE_FOUND:
                    BluetoothDevice foundDevice = intent.getParcelableExtra(EXTRA_DEVICE);
                    devices.add(foundDevice);
                    viewMvc.bindBluetoothDeviceNames(BluetoothDeviceUtils.getDeviceNamesList(devices));
                    break;
                case ACTION_DISCOVERY_FINISHED:
                    viewMvc.hideLoadingIndicator();
                    break;
            }
        }
    };

    private final BroadcastReceiver connectionEventReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent chatActivityIntent = new Intent(MainActivity.this, ChatActivity.class);

            switch (intent.getAction()) {
                case ACTION_CLIENT_CONNECTED:
                    startActivity(chatActivityIntent);
                    break;
                case ACTION_SERVER_CONNECTED:
                    startActivity(chatActivityIntent);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
        bluetoothService = BluetoothServiceImpl.getInstance(getApplicationContext());
        setupBluetoothRadio();
        getAndDisplayPairedDevices();
        registerDeviceReceiver();
        registerConnectionReceiver();
        // Because via bluetooth you are able to get a device's location android
        // needs to get user's permission at runtime
        checkLocationPermissionToStartDiscoverDevices();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (bluetoothService.isDeviceDiscoverable()){
            bluetoothService.startServerMode();
        }
    }

    private void checkLocationPermissionToStartDiscoverDevices() {
        boolean permissionNotGranted = ContextCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (permissionNotGranted) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_COARSE_LOCATION);
        } else {
            bluetoothService.startDiscovery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_COARSE_LOCATION:
                boolean permissionGranted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (permissionGranted) {
                    bluetoothService.startDiscovery();
                } else {
                    Snackbar.make(viewMvc.getRootView(), R.string.location_permition_not_granted, Snackbar.LENGTH_LONG).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    // Reaching this point Bluetooth enabled for the first time.
                    // Start a discovery automatically.
                    bluetoothService.startDiscovery();
                    askUserToSetDeviceAsDiscoverable();
                } else if (resultCode == RESULT_CANCELED) {
                    Snackbar enableBluetoothSnackbar = Snackbar.make(viewMvc.getRootView(), R.string.bluetooth_enabled_canceld_label, Snackbar.LENGTH_INDEFINITE);
                    enableBluetoothSnackbar.setAction(R.string.enable_label, (view) -> startActivityForResult(new Intent(ACTION_REQUEST_ENABLE), REQUEST_CODE_ENABLE_BT));
                    enableBluetoothSnackbar.show();
                }
                break;

            case REQUEST_CODE_DISCOVERABLE:
                // According to the docs " your activity then receives a call to the onActivityResult
                // with the result code equal to the ducation that the device is discoverable."
                if (resultCode == DISCOVERABLE_TIME_IN_SECONDS) {
                    bluetoothService.startServerMode();
                } else if (resultCode == RESULT_CANCELED) {
                    Snackbar whyEnableDiscoverabillitySnackbar = Snackbar.make(viewMvc.getRootView(), R.string.device_discoverable_disabled_label, Snackbar.LENGTH_LONG);
                    whyEnableDiscoverabillitySnackbar.setAction(R.string.enable_label, (view) -> askUserToSetDeviceAsDiscoverable());
                    whyEnableDiscoverabillitySnackbar.show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceivers();
        bluetoothService.stopServerMode();
        bluetoothService.stopClientMode();
    }

    private void setupBluetoothRadio() {
        if (!bluetoothService.isBluetoothSupported()) {
            Snackbar.make(viewMvc.getRootView(), R.string.bluetooth_not_supported_label, Snackbar.LENGTH_LONG).show();
        }
        if (!bluetoothService.isBluetoothEnabled()) {
            startActivityForResult(new Intent(ACTION_REQUEST_ENABLE), REQUEST_CODE_ENABLE_BT);
        } else {
            askUserToSetDeviceAsDiscoverable();
        }
    }

    @Override
    public void onBluetoothScanClicked() {
        boolean permissionForLocationNotGranted = ContextCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (permissionForLocationNotGranted) {
            Snackbar locationPermissionSnackbar = Snackbar.make(viewMvc.getRootView(), R.string.location_permition_not_granted, Snackbar.LENGTH_INDEFINITE);
            locationPermissionSnackbar.setAction(R.string.ask_permition_label, (view) -> checkLocationPermissionToStartDiscoverDevices());
            locationPermissionSnackbar.show();
            return;
        }

        if (bluetoothService.isDiscovering()) {
            Snackbar.make(viewMvc.getRootView(), R.string.discovery_in_progress_label, Snackbar.LENGTH_SHORT).show();
        } else {
            bluetoothService.startDiscovery();
        }
    }

    @Override
    public void onDeviceItemClicked(int position) {
        if (devices.size()>0){
            List<BluetoothDevice> devicesList = new ArrayList<>(devices);
            bluetoothService.startClientMode(devicesList.get(position));
        }
    }

    private void askUserToSetDeviceAsDiscoverable() {
        Intent discoverableIntent = new Intent(ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_TIME_IN_SECONDS);
        startActivityForResult(discoverableIntent, REQUEST_CODE_DISCOVERABLE);
    }

    private void getAndDisplayPairedDevices() {
        devices.addAll(bluetoothService.getPairedDevices());
        viewMvc.bindBluetoothDeviceNames(BluetoothDeviceUtils.getDeviceNamesList(devices));
    }

    private void registerDeviceReceiver() {
        IntentFilter filterFound = new IntentFilter(ACTION_DEVICE_FOUND);
        IntentFilter filterDiscoveryStarted = new IntentFilter(ACTION_DISCOVERY_STARTED);
        IntentFilter filterDiscoveryFinished = new IntentFilter(ACTION_DISCOVERY_FINISHED);
        registerReceiver(deviceReceiver, filterDiscoveryStarted);
        registerReceiver(deviceReceiver, filterFound);
        registerReceiver(deviceReceiver, filterDiscoveryFinished);
    }

    private void registerConnectionReceiver() {
        IntentFilter clientConnectedFilter = new IntentFilter(ACTION_CLIENT_CONNECTED);
        IntentFilter serverConnectedFilter = new IntentFilter(ACTION_SERVER_CONNECTED);
        LocalBroadcastManager.getInstance(this).registerReceiver(connectionEventReceiver, clientConnectedFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(connectionEventReceiver, serverConnectedFilter);
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

    private void unregisterReceivers() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(deviceReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(connectionEventReceiver);
    }
}
