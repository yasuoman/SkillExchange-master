package com.example.aoge.skillexchange;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends CheckPermissionsActivity {

    private HomeActivity homefragment;
    private NearbyActivity nearbyfragment;
    private ChatActivity chatfragment;
    private MeActivity mefragment;
    private Fragment[] fragments;
    private int lastfragment;
    public static MainActivity mActivity;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mActivity = this;
        if(UserInformation.permission == -1) {
            Toast.makeText(getApplicationContext(),
                    "Sorry, this App can't work if you don't allow the required permission.", Toast.LENGTH_LONG)
                    .show();
        }else{
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    initFragment();
//                }
//            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    initUserIfo();
                }
            }).start();

        }
    }

    private void initFragment(){
        homefragment = new HomeActivity();
        nearbyfragment = new NearbyActivity();
        chatfragment = new ChatActivity();
        mefragment = new MeActivity();
        fragments = new Fragment[]{homefragment,nearbyfragment,chatfragment,mefragment};
        lastfragment = UserInformation.firstShow;
        getSupportFragmentManager().beginTransaction().replace(R.id.mainview,fragments[UserInformation.firstShow]).show(fragments[UserInformation.firstShow]).commit();
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bnv);
        bottomNavigationView.setSelectedItemId(bottomNavigationView.getMenu().getItem(UserInformation.firstShow).getItemId());
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(changeFragment);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId())
            {
                case R.id.home:
                {
                    if(lastfragment!=0)
                    {
                        switchFragment(lastfragment,0);
                        lastfragment=0;

                    }

                    return true;
                }
                case R.id.nearby:
                {
                    if(lastfragment!=1)
                    {
                        switchFragment(lastfragment,1);
                        lastfragment=1;

                    }

                    return true;
                }
                case R.id.chat:
                {
                    if(lastfragment!=2)
                    {
                        switchFragment(lastfragment,2);
                        lastfragment=2;

                    }

                    return true;
                }
                case R.id.me:
                {
                    if(lastfragment!=3)
                    {
                        switchFragment(lastfragment,3);
                        lastfragment=3;

                    }

                    return true;
                }
            }
            return false;
        }
    };

    private void switchFragment(int lastfragment,int index)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if(fragments[index].isAdded()==false)
        {
            transaction.add(R.id.mainview,fragments[index]);


        }
        transaction.show(fragments[index]).commitAllowingStateLoss();


    }

    public void initUserIfo(){
        //request url
        String url = "http://106.14.117.91:8080/SkillsExchangeServer/UserselfServlet";    //注①
        String tag = "MeUser";    //注②

        //get the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        //cancel the request queue that marked by "Register" in order to not request again.
        requestQueue.cancelAll(tag);



        //build StringRequest and set the request method "POST"(default "GET")
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
                            UserInformation.uName = jsonObject.getString("username");
                            UserInformation.ucan = jsonObject.getString("can");
                            UserInformation.uwant = jsonObject.getString("want");
                            UserInformation.loan = jsonObject.getString("location");
                            UserInformation.head = jsonObject.getString("headpicture");
                            initFragment();
                        } catch (JSONException e) {
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,
                        "Something wrong!", Toast.LENGTH_LONG)
                        .show();
                Log.e("TAG", error.getMessage(), error);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("email", UserInformation.userinformation);  //set the parameter.
                params.put("email", UserInformation.userinformation);
                return params;
            }
        };

        //set the tag.
        request.setTag(tag);

        //add the request to queue.
        requestQueue.add(request);
    }




}
