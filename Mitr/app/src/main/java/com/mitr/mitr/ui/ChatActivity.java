package com.mitr.mitr.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.mitr.mitr.ChatItem;
import com.mitr.mitr.MyCentral;
import com.mitr.mitr.R;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity{
    List<ChatItem> chats;
    TextView txtWindow;
    EditText txtChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);

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
        if (msg == null || msg.equals("")) return;
        ChatItem item = new ChatItem(MyCentral.user, msg);
        this.addChat(item);

        this.txtChat.setText("");

        View v = this.getCurrentFocus();
        if (v != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        this.txtChat.clearFocus();
        this.txtWindow.requestFocus();

        String device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String comment = msg;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
