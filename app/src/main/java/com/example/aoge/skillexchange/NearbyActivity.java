package com.example.aoge.skillexchange;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NearbyActivity extends Fragment {
    private ListView NearbyShowView;
    private JSONArray jsonArray;
    private List<Map<String, Object>> list;
    private TextView city;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_nearby,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        NearbyShowView = (ListView)getView().findViewById(R.id.nearby_ll_show);

        city = (TextView)getView().findViewById(R.id.txt_n_city);
        city.setText(UserInformation.loan);

        list = new ArrayList<Map<String, Object>>();

        new Thread(new Runnable() {
            @Override
            public void run() {
                NearbyUserRequest();
            }
        }).start();

    }

    public void NearbyUserRequest(){


        //request url
        String url = "http://106.14.117.91:8080/SkillsExchangeServer/ShowUserServlet";    //注①
        String tag = "NearbyUser";    //注②




        //get the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getView().getContext());

        //cancel the request queue that marked by "Register" in order to not request again.
        requestQueue.cancelAll(tag);

        //build StringRequest and set the request method "POST"(default "GET")
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String username,email,gender,location,can,want;
                            jsonArray = (JSONArray) new JSONObject(response).get("nearbyshow");  //注③

                            for(int i=0;i<jsonArray.length();i++){
                                Map<String, Object> map = new HashMap<String, Object>();

                                JSONObject subObiect = new JSONObject();
                                subObiect = (JSONObject)jsonArray.get(i);

                                map.put("username",subObiect.getString("username"));
                                map.put("email",subObiect.getString("email"));
                                map.put("gender",subObiect.getString("gender"));
                                map.put("location",subObiect.getString("location"));
                                map.put("can",subObiect.getString("can"));
                                map.put("want",subObiect.getString("want"));
                                map.put("headpicture",subObiect.getString("headpicture"));

                                list.add(map);
//                                System.out.println(map);
//                                System.out.println(list);

//                                System.out.println(username+email+gender+location+can+want);
                            }
                            addViewItem();
                        } catch (JSONException e) {
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getView().getContext(),
                        "Something wrong!!", Toast.LENGTH_LONG)
                        .show();
                Log.e("TAG", error.getMessage(), error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Mark", "nearby");  //set the parameter.
                params.put("Email", UserInformation.userinformation);  //set the parameter.
                params.put("Location", "重庆市");
                return params;
            }
        };

        //set the tag.
        request.setTag(tag);

        //add the request to queue.
        requestQueue.add(request);
    }

    public void addViewItem(){
        SimpleAdapter adapter = new SimpleAdapter(getContext(), list,
                R.layout.item_nearby_show, new String[] { "username", "can",
                "want","headpicture" }, new int[] {
                R.id.txt_n_username,
                R.id.txt_n_mainskill,
                R.id.txt_n_mainwant ,
                R.id.img_n_show1});
        // 给ListView设置适配器
        NearbyShowView.setAdapter(adapter);

        NearbyShowView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                UserInformation user = new UserInformation((String)list.get(arg2).get("username"),(String)list.get(arg2).get("email"),(String)list.get(arg2).get("gender"),(String)list.get(arg2).get("location"),(String)list.get(arg2).get("can"),(String)list.get(arg2).get("want"),(String)list.get(arg2).get("headpicture"));
                Intent intent = new Intent(getView().getContext(), DetailActivity.class);
                // 放入需要传递的对象
                intent.putExtra("key", user);
                // 启动意图
                startActivity(intent);

//                                          bundle.putInt("photo", username[arg2]);
//                                          bundle.putString("message", message[arg2]);
//                                          Intent intent = new Intent();
//                                          intent.putExtras(bundle);
//                                          intent.setClass(MainActivity.this, MoveList.class);
//                                          Log.i("message", message[arg2]);
//                                          startActivity(intent);
            }
        });

    }

}
