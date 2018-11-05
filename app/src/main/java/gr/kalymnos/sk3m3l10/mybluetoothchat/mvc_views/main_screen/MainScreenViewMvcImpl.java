package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.main_screen;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import java.util.List;

import gr.kalymnos.sk3m3l10.mybluetoothchat.R;

public class MainScreenViewMvcImpl implements MainScreenViewMvc {

    private View root;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Toolbar toolbar;
    private FloatingActionButton scanFab;

    public MainScreenViewMvcImpl(LayoutInflater inflater, ViewGroup container) {
        initializeViews(inflater, container);
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void setOnBluetoothScanClickListener(OnBluetoothScanClickListener listener) {

    }

    @Override
    public void bindBluetoothDeviceNames(List<String> deviceNames) {

    }

    @Override
    public View getRootView() {
        return root;
    }

    private void initializeViews(LayoutInflater inflater, ViewGroup container) {
        root = inflater.inflate(R.layout.activity_main, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        progressBar = root.findViewById(R.id.progressBar);
        toolbar = root.findViewById(R.id.toolbar);
        scanFab = root.findViewById(R.id.scanFab);
    }
}
