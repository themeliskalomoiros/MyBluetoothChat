package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_controllers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.Toast;

import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothService;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothServiceImpl;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.chat_screen.ChatScreenViewMvc;
import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.chat_screen.ChatScreenViewMvcImpl;

import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.Actions.ACTION_DISCONNECTED;
import static gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model.BluetoothConstants.Actions.ACTION_MESSAGE_RECEIVED;

public class ChatActivity extends AppCompatActivity implements ChatScreenViewMvc.OnSendClickListener {

    private ChatScreenViewMvc viewMvc;
    private BluetoothService bluetoothService;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_MESSAGE_RECEIVED:
                    // TODO: Display the message
                    Toast.makeText(context, intent.getStringExtra(BluetoothConstants.Extras.EXTRA_MESSAGE), Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_DISCONNECTED:
                    // TODO: Display the message
                    Toast.makeText(context, "Disconnected!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bluetoothService = BluetoothServiceImpl.getInstance(getApplicationContext());
        setupUi();
        registerMessageReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }

    private void registerMessageReceiver() {
        IntentFilter messageFilter = new IntentFilter(ACTION_MESSAGE_RECEIVED);
        IntentFilter disconnectionFilter = new IntentFilter(ACTION_DISCONNECTED);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, disconnectionFilter);
    }

    @Override
    public void onSendClicked(String msg) {
        bluetoothService.write(msg.getBytes());
    }

    private void setupUi() {
        initializeViewMvc();
        setSupportActionBar(viewMvc.getToolbar());
        viewMvc.bindToolbarTitle(bluetoothService.getConnectedDeviceName());
        setContentView(viewMvc.getRootView());
    }

    private void initializeViewMvc() {
        viewMvc = new ChatScreenViewMvcImpl(LayoutInflater.from(this), null);
        viewMvc.setOnSendClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        bluetoothService.releaseChatResources();
    }
}
