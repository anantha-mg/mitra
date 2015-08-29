package com.mitr.mitr;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends Activity {
    List<ChatItem> chats;
    TextView txtWindow;
    EditText txtChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        this.chats = new ArrayList<ChatItem>();

        this.txtWindow = (TextView) findViewById(R.id.txtWindow);
        this.txtChat = (EditText) findViewById(R.id.txtChat);
    }

    public void addChat(ChatItem chat) {
        this.chats.add(chat);
        this.drawChat();
    }

    public void drawChat() {
        this.txtWindow.setText("");
        String text = "";
        for (ChatItem c : chats) {
            text += c.getSender() + ": " + c.getMessage() + "\n";
        }

        this.txtWindow.setText(text);
    }

    public void onSend(View view) {
        String msg = this.txtChat.getText().toString();
        ChatItem item = new ChatItem(MyCentral.user, msg);
        this.addChat(item);
    }
}
