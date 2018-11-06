package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.main_screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gr.kalymnos.sk3m3l10.mybluetoothchat.R;

class BluetoothDevicesAdapter extends RecyclerView.Adapter<BluetoothDevicesAdapter.DeviceHolder> {

    private Context context;
    private List<String> deviceNames;

    private MainScreenViewMvc.OnDeviceItemClickListener onDeviceItemClickListener;

    public BluetoothDevicesAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void addDeviceNames(List<String> deviceNames) {
        this.deviceNames = deviceNames;
    }

    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item_device, viewGroup, false);
        return new DeviceHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceHolder deviceHolder, int i) {
        if (deviceNames != null && deviceNames.size() > 0) {
            deviceHolder.bindDeviceName(deviceNames.get(i));
        }
    }

    @Override
    public int getItemCount() {
        if (deviceNames != null && deviceNames.size() > 0) {
            return deviceNames.size();
        }
        return 0;
    }

    public void setOnItemClickListener(MainScreenViewMvc.OnDeviceItemClickListener listener) {
        onDeviceItemClickListener = listener;
    }

    class DeviceHolder extends RecyclerView.ViewHolder {

        private TextView deviceName;

        public DeviceHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener((view) -> {
                if (onDeviceItemClickListener != null) {
                    onDeviceItemClickListener.onDeviceItemClicked(getAdapterPosition());
                }
            });
            deviceName = itemView.findViewById(R.id.device_name);
        }

        public void bindDeviceName(String deviceName) {
            this.deviceName.setText(deviceName);
        }
    }
}
