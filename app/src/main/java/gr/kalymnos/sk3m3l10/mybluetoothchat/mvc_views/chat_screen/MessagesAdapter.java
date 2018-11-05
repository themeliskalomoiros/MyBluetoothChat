package gr.kalymnos.sk3m3l10.mybluetoothchat.mvc_views.chat_screen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import gr.kalymnos.sk3m3l10.mybluetoothchat.R;
import gr.kalymnos.sk3m3l10.mybluetoothchat.pojos.Message;

class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageHolder> {

    private Context context;
    private List<Message> messages;

    public MessagesAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void addMessages(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item_message, viewGroup, false);
        return new MessageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder messageHolder, int i) {
        if (messages != null && messages.size() > 0) {
            messageHolder.bindMessage(messages.get(i).getText());
        }
    }

    @Override
    public int getItemCount() {
        if (messages != null && messages.size() > 0) {
            return messages.size();
        }
        return 0;
    }

    class MessageHolder extends RecyclerView.ViewHolder {

        private TextView messageTextView;

        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = (TextView) itemView;
        }

        void bindMessage(String msg) {
            messageTextView.setText(msg);
        }
    }
}
