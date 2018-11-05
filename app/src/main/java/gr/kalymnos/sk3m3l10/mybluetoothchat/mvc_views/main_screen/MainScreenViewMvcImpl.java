package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.main_screen;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import gr.kalymnos.sk3m3l10.mybluetoothchat.R;

public class MainScreenViewMvcImpl implements MainScreenViewMvc {

    private View root;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private BluetoothDevicesAdapter adapter;

    private OnBluetoothScanClickListener onBluetoothScanClickListener;
    private FloatingActionButton scanFab;

    public MainScreenViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        initializeViews(inflater, container);
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void setOnBluetoothScanClickListener(final OnBluetoothScanClickListener listener) {
        if (listener != null) {
            scanFab.setOnClickListener((view) -> listener.onBluetoothScan());
        }
    }

    @Override
    public void setOnDeviceItemClickListener(OnDeviceItemClickListener listener) {
        if (listener != null) {
            adapter.setOnItemClickListener(listener);
        }
    }

    @Override
    public void showLoadingIndicator() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void bindBluetoothDeviceNames(List<String> deviceNames) {
        adapter.addDeviceNames(deviceNames);
        adapter.notifyDataSetChanged();
    }

    @Override
    public View getRootView() {
        return root;
    }

    private void initializeViews(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.activity_main, container, false);
        progressBar = root.findViewById(R.id.progressBar);
        toolbar = root.findViewById(R.id.toolbar);
        scanFab = root.findViewById(R.id.scanFab);
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView = root.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(root.getContext());
        adapter = new BluetoothDevicesAdapter(root.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

}
