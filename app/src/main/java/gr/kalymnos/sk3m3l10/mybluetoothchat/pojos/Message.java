package gr.kalymnos.sk3m3l10.mybluetoothchat.pojos;

/*
 *
 *  A chat message
 *
 * */

public class Message {

    private String text;
    private boolean sendByLocalUser;

    public Message(String text, boolean sendByLocalUser) {
        this.sendByLocalUser = sendByLocalUser;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public boolean isSendByLocalUser() {
        return sendByLocalUser;
    }
}
