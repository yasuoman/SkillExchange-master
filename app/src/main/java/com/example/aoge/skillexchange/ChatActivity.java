package com.example.aoge.skillexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatActivity extends Fragment {
    private TextView cmess;
    private Button button;
    private List<Map<String, Object>> list;
    private ListView ChatShowView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_chat,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ChatShowView = (ListView)getView().findViewById(R.id.chat_ll_show);
        list = new ArrayList<Map<String, Object>>();
        cmess = (TextView)getView().findViewById(R.id.txt_c_message);
        cmess.setText("Message");
        addItem();
    }

    public void addItem(){

        UserInformation.adapter = new SimpleAdapter(getContext(), UserInformation.historyList,
                R.layout.item_message_show, new String[] {"username", "image",
                "content" ,"time"}, new int[] {
                R.id.txt_mess_username,
                R.id.img_mess_show,
                R.id.txt_mess_lastmessage,
                R.id.txt_mess_time
        });
        // 给ListView设置适配器
        ChatShowView.setAdapter(UserInformation.adapter);

        ChatShowView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Intent intent = new Intent(getView().getContext(), MsgActivity.class);
                // 放入需要传递的对象
                intent.putExtra("talktoemail", (String)UserInformation.historyList.get(arg2).get("talkto"));
                intent.putExtra("talktoname", (String)UserInformation.historyList.get(arg2).get("username"));
                intent.putExtra("talktoimage", (String)UserInformation.historyList.get(arg2).get("image"));
                // 启动意图
                startActivity(intent);
            }
        });





//        Map<String, Object> map = new HashMap<String, Object>();
//
//        map.put("username","peipei");
//
//        map.put("message","How are you doing?");
//        map.put("headpicture",String.valueOf(R.drawable.woman2));
//
//        list.add(map);
//
//
//
//        SimpleAdapter adapter = new SimpleAdapter(getContext(), list,
//                R.layout.item_message_show, new String[] {"username", "message",
//                "headpicture"}, new int[] {
//                R.id.txt_mess_username,
//                R.id.txt_mess_lastmessage,
//                R.id.img_mess_show
//        });
//        // 给ListView设置适配器
//        ChatShowView.setAdapter(adapter);
    }
}
