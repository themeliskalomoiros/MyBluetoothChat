package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_controllers;

import android.os.Handler;

/*
*
* This class provides its own handler.
* Activities that want to receive messages will implement this.
*
* */

public interface HandlerProvider {
    Handler getHandler();
}
