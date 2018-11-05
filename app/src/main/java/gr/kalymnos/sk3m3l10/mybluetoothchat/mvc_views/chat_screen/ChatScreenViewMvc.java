package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.chat_screen;

import android.support.v7.widget.Toolbar;

import java.util.List;

import gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.ViewMvc;

public interface ChatScreenViewMvc extends ViewMvc {

    interface OnSendClickListener{
        void onSendClicked(String msg);
    }

    Toolbar getToolbar();

    void setOnSendClickListener(OnSendClickListener listener);

    void bindMessages(List<String> messages);

    String getInsertedMessage();
}
