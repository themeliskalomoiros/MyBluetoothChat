package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_controllers;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.FakeBluetoothServiceImpl;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.main_screen.MainScreenViewMvc;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.main_screen.MainScreenViewMvcImpl;

public class MainActivity extends AppCompatActivity implements MainScreenViewMvc.OnDeviceItemClickListener,
        MainScreenViewMvc.OnBluetoothScanClickListener {

    private MainScreenViewMvc viewMvc;
    private BluetoothService bluetoothService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUi();
        bluetoothService = new FakeBluetoothServiceImpl(new Handler());
    }

    @Override
    public void onBluetoothScanClicked() {
        Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeviceItemClicked(int position) {
        Toast.makeText(this, "Clicked item at position " + position, Toast.LENGTH_SHORT).show();
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
