package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.chat_screen;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import gr.kalymnos.sk3m3l10.mybluetoothchat.R;
import gr.kalymnos.sk3m3l10.mybluetoothchat.pojos.Message;

public class ChatScreenViewMvcImpl implements ChatScreenViewMvc {

    private View root;
    private Toolbar toolbar;
    private FloatingActionButton sendFab;
    private EditText insertedTextField;

    private RecyclerView recyclerView;
    private MessagesAdapter adapter;

    public ChatScreenViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        initializeViews(inflater, container);
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void setOnSendClickListener(OnSendClickListener listener) {
        if (listener != null) {
            sendFab.setOnClickListener((view) -> listener.onSendClicked(insertedTextField.getText().toString()));
        }
    }

    @Override
    public void bindMessages(List<Message> messages) {
        adapter.addMessages(messages);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View getRootView() {
        return root;
    }

    private void initializeViews(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.activity_message, container, false);
        toolbar = root.findViewById(R.id.toolbar);
        sendFab = root.findViewById(R.id.sendButton);
        insertedTextField = root.findViewById(R.id.editText);
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        adapter = new MessagesAdapter(root.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }
}
