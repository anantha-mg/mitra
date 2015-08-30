package com.mitr.mitr.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mitr.mitr.ChatItem;
import com.mitr.mitr.MyCentral;
import com.mitr.mitr.R;
import com.mitr.mitr.web.Handled;
import com.mitr.mitr.web.MyGetThread;
import com.mitr.mitr.web.MyHandler;
import com.mitr.mitr.web.MyPostThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChatActivity extends AppCompatActivity implements Handled{
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

        if (getIntent().getStringExtra("new").equals("false")) {
            HashMap<String, String> map = new HashMap<>();
            String android_id = Settings.Secure.getString(this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            map.put("DEVICE_ID", android_id);
            map.put("CHAT_ID", String.valueOf(getIntent().getExtras().get("chatId")));
            new MyGetThread(map, "http://52.25.115.191:8080/mitra/chat/getChat", new MyHandler(this)).start();
        }
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


        HashMap<String, String> map = new HashMap<>();
        map.put("DEVICE_ID", device_id);
        map.put("COMMENT", msg);

        if (getIntent().getStringExtra("new").equals("true")) {
            new MyPostThread(map, "http://52.25.115.191:8080/mitra/chat/addNewChat").start();
            Toast.makeText(this, "New chat started", Toast.LENGTH_SHORT);
            this.finish();
        } else {
            map.put("CHAT_ID",String.valueOf(getIntent().getExtras().get("chatId")));
            new MyPostThread(map, "http://52.25.115.191:8080/mitra/chat/addComment").start();
//            Toast.makeText(this, "New chat started", Toast.LENGTH_SHORT);
//            this.finish();
        }
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

    @Override
    public void handlerCallback(Message message) {
        parseAndSaveChat(message.getData().getString("result"));
    }

    public void parseAndSaveChat(String content) {
        ArrayList<ChatItem> comments = new ArrayList<>();

        try{
            JSONObject jsonTop = new JSONObject(content);
            JSONObject jsonChat = (JSONObject)jsonTop.get("chat");
            JSONArray jsonCommentArray = (JSONArray)jsonChat.getJSONArray("comments");
            for (int i = 0; i < jsonCommentArray.length(); i++) {
                JSONObject jsonComment = (JSONObject)jsonCommentArray.get(i);
                String deviceId = jsonComment.getString("user");
                if (deviceId.equals(Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID))) {
                    deviceId = MyCentral.user;
                }
                comments.add(new ChatItem(deviceId,jsonComment.getString("comment")));
            }


            for(ChatItem c : comments) {
                this.addChat(c);
            }
        }
        catch (JSONException ex) {
            Log.d("MyTest", "JSON experienced an error");
            Log.d("MyTest", ex.getMessage());
        }

    }
}
