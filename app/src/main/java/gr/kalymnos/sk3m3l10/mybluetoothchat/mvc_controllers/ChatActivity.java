package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_controllers;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.ParcelableBluetoothSocketWrapper;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.chat_screen.ChatScreenViewMvc;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.chat_screen.ChatScreenViewMvcImpl;

public class ChatActivity extends AppCompatActivity implements ChatScreenViewMvc.OnSendClickListener,
        HandlerProvider {

    private ChatScreenViewMvc viewMvc;
    private BluetoothSocket bluetoothSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothSocket = getBluetoothSocket();
        setupUi();
    }

    @Override
    public void onSendClicked(String msg) {
        Toast.makeText(this, "clicked!", Toast.LENGTH_SHORT).show();
    }

    private void setupUi() {
        initializeViewMvc();
        setSupportActionBar(viewMvc.getToolbar());
        getSupportActionBar().setTitle(getDeviceName());
        setContentView(viewMvc.getRootView());
    }

    private void initializeViewMvc() {
        viewMvc = new ChatScreenViewMvcImpl(LayoutInflater.from(this), null);
        viewMvc.setOnSendClickListener(this);
    }

    private String getDeviceName() {
        Bundle extras = getIntent().getExtras();
        boolean bundleIncludesName = extras != null && extras.containsKey(BluetoothConstants.Extras.EXTRA_DEVICE);
        if (bundleIncludesName) {
            return extras.getString(BluetoothConstants.Extras.EXTRA_DEVICE);
        }
        return null;
    }

    private BluetoothSocket getBluetoothSocket() {
        Bundle extras = getIntent().getExtras();
        boolean bundleIncludesParcelableSocketWrapper = extras != null && extras.containsKey(BluetoothConstants.Extras.EXTRA_SOCKET_WRAPPER);
        if (bundleIncludesParcelableSocketWrapper) {
            ParcelableBluetoothSocketWrapper wrapper = extras.getParcelable(BluetoothConstants.Extras.EXTRA_SOCKET_WRAPPER);
            return wrapper.getSocket();
        }
        return null;
    }

    @Override
    public Handler getHandler() {
        return new Handler((msg) -> {
            switch (msg.what) {

            }
            // True if no further handling is desired
            return true;
        });
    }
}
