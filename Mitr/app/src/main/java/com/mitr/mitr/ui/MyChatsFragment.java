package com.mitr.mitr.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mitr.mitr.R;
import com.mitr.mitr.web.Handled;
import com.mitr.mitr.web.MyGetThread;
import com.mitr.mitr.web.MyHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MyChatsFragment extends Fragment implements Handled {

    ListView listView ;
    ArrayList<String> chatComments;
    ArrayList<Integer> chatIds;
    ArrayAdapter<String> adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_chats, container, false);

        chatComments = new ArrayList<>();
        chatIds = new ArrayList<>();

        listView = (ListView) view.findViewById(R.id.list);
        String[] values = new String[] {
        };

        ArrayList<String> lst = new ArrayList<String>(Arrays.asList(values));

        adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, lst);

        listView.setAdapter(adapter);

        Button button = (Button) view.findViewById(R.id.btnAsk);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("new", "true");
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int itemPosition = position;
                String itemValue = (String) listView.getItemAtPosition(position);
                int clickId = chatIds.get(itemPosition);

                Intent intent = new Intent(MyChatsFragment.this.getActivity(), ChatActivity.class);
                intent.putExtra("new", "false");
                intent.putExtra("chatId", clickId);
                startActivity(intent);
            }
        });


        HashMap<String, String> map = new HashMap<>();
        String android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        map.put("DEVICE_ID", android_id);
        new MyGetThread(map, "http://52.25.115.191:8080/mitra/chat/getUserChats", new MyHandler(this)).start();

        return view;
    }

    @Override
    public void handlerCallback(Message message) {
        parseAndSaveChat(message.getData().getString("result"));
    }

    public void parseAndSaveChat(String content) {
        chatComments.clear();
        chatIds.clear();

        try{
            JSONObject jsonTop = new JSONObject(content);
            JSONArray jsonArray = jsonTop.getJSONArray("chats");
            JSONObject jsonObject;
            JSONArray jsonComments;
            JSONObject firstComment;


            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = (JSONObject) jsonArray.get(i);
                jsonComments = (JSONArray) jsonObject.getJSONArray("comments");
                firstComment = (JSONObject) jsonComments.get(0);

                chatComments.add((String) firstComment.get("comment"));
                chatIds.add(jsonObject.getInt("chatId"));

                Log.d("MyTest", (String) firstComment.get("comment"));
//                this.addPointr(new Pointr(num, lat, lng));
            }

            for (String c: chatComments) {
                this.adapter.add(c);
            }

            this.adapter.notifyDataSetChanged();
        }
        catch (JSONException ex) {
            Log.d("MyTest", "JSON experienced an error");
            Log.d("MyTest", ex.getMessage());
        }

    }
}
