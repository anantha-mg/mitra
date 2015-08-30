package com.mitr.mitr.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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


public class QuestionsFragment extends Fragment implements Handled {

    ListView listView ;
    ArrayList<String> chatComments;
    ArrayList<Integer> chatIds;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_questions, container, false);

        chatComments = new ArrayList<>();
        chatIds = new ArrayList<>();

        listView = (ListView) rootView.findViewById(R.id.list);
        String[] values = new String[] {
        };

        ArrayList<String> lst = new ArrayList<String>(Arrays.asList(values));

        adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, lst);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                int itemPosition = position;
                String itemValue = (String) listView.getItemAtPosition(position);
                int clickId = chatIds.get(itemPosition);

                Intent intent = new Intent(QuestionsFragment.this.getActivity(), ChatActivity.class);
                intent.putExtra("new", "false");
                intent.putExtra("chatId", clickId);
                startActivity(intent);
            }
        });

        HashMap<String, String> map = new HashMap<>();
        String android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        new MyGetThread(map, "http://52.25.115.191:8080/mitra/chat/getOtherChats", new MyHandler(this)).start();


        return rootView;
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
