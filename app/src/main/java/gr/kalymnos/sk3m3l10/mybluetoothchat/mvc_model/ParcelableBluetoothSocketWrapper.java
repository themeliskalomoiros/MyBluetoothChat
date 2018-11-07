package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_model;

import android.bluetooth.BluetoothSocket;
import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableBluetoothSocketWrapper implements Parcelable {
    private BluetoothSocket socket;

    public ParcelableBluetoothSocketWrapper(BluetoothSocket socket) {
        this.socket = socket;
    }

    public BluetoothSocket getSocket() {
        return socket;
    }

    protected ParcelableBluetoothSocketWrapper(Parcel in) {
    }

    public static final Creator<ParcelableBluetoothSocketWrapper> CREATOR = new Creator<ParcelableBluetoothSocketWrapper>() {
        @Override
        public ParcelableBluetoothSocketWrapper createFromParcel(Parcel in) {
            return new ParcelableBluetoothSocketWrapper(in);
        }

        @Override
        public ParcelableBluetoothSocketWrapper[] newArray(int size) {
            return new ParcelableBluetoothSocketWrapper[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
