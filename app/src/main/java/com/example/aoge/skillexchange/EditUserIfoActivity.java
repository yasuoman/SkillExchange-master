package com.example.aoge.skillexchange;

import android.content.ContentResolver;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class EditUserIfoActivity extends BaseActivity {

    private CircleImageButton circleButton;
    private RadioGroup gender;
    private Button ConfirmButton;
    private EditText edtCan,edtWant;
    private String gd = "male";
    private String head = UserInformation.head;

    private ImageUtils imageUtils = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_ifo);

//        Intent intent = getIntent();
//        email = intent.getStringExtra("email");

        circleButton = (CircleImageButton) findViewById(R.id.img_e_headportrait);
        circleButton.setImageResource(Integer.parseInt(UserInformation.head));
        edtCan = (EditText)findViewById(R.id.edt_e_can);
        edtWant = (EditText)findViewById(R.id.edt_e_want);
        edtCan.setText(UserInformation.ucan);
        edtWant.setText(UserInformation.uwant);

        // 实例化控件
        gender = (RadioGroup) findViewById(R.id.e_sex);

        // 方法一监听事件,通过获取点击的id来实例化并获取选中状态的RadioButton控件
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 获取选中的RadioButton的id
                int id = group.getCheckedRadioButtonId();
                // 通过id实例化选中的这个RadioButton
                RadioButton choise = (RadioButton) findViewById(id);
                // 获取这个RadioButton的text内容
                gd = choise.getText().toString();
//                Toast.makeText(FirstLoginActivity.this, "你的性别为：" + output, Toast.LENGTH_SHORT).show();
            }
        });

        ConfirmButton = (Button) findViewById(R.id.btnconfirm);
    }

    /**
     * Press the button Login, go to Login form
     *
     * @param view from the activity_login.xml
     */
    public void btnConfirm(View view) {
        String cando = edtCan.getText().toString().trim();
        String wantdo = edtWant.getText().toString().trim();
        ConfirmButton.setClickable(false);
        if(cando.isEmpty() || wantdo.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    R.string.enter_credentials, Toast.LENGTH_LONG)
                    .show();
        }else{
            EditIfoRequest(UserInformation.userinformation,gd,cando,wantdo,head);
        }
    }


    /**
     * link to server to depend whether the username and password are right.
     * @param email
     * @param
     */
    public void EditIfoRequest(final String email, final String gender,final String can, final String want, final String headpicture) {
        //request url
        String url = "http://106.14.117.91:8080/SkillsExchangeServer/FirstLoginServlet";    //注①
        String tag = "FirstLogin";    //注②

        //get the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //cancel the request queue that marked by "Register" in order to not request again.
        requestQueue.cancelAll(tag);

        //build StringRequest and set the request method "POST"(default "GET")
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");  //注③
                            String result = jsonObject.getString("Result");
                            if (result.equals("success")) {
                                Toast.makeText(getApplicationContext(),
                                        "Success!", Toast.LENGTH_LONG)
                                        .show();
                                UserInformation.firstShow = 3;
                                MainActivity.mActivity.finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Something wrong! Please try again.", Toast.LENGTH_LONG)
                                        .show();
                                ConfirmButton.setClickable(true);
                            }
                        } catch (JSONException e) {
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        "No internet!", Toast.LENGTH_LONG)
                        .show();
                Log.e("TAG", error.getMessage(), error);
                ConfirmButton.setClickable(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("AccountNumber", UserInformation.userinformation);  //set the parameter.
                params.put("Gender", gender);
                params.put("Can", can);
                params.put("Want", want);
                params.put("HeadPicture", head);
                return params;
            }
        };

        //set the tag.
        request.setTag(tag);

        //add the request to queue.
        requestQueue.add(request);
    }

    public void btnBack(View view){
        finish();
    }

    public void EselectHead(View view){
        Intent intent = new Intent(this,ChooseHeadActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(data!=null){
//            Bundle bundle = getIntent().getExtras();
//            int itemp= bundle.getInt("number");
            String number = data.getStringExtra("number");

            switch (number) {
                case "1": {
                    circleButton.setImageResource(R.drawable.man1);
                    head = String.valueOf(R.drawable.man1);
                }

                break;
                case "2": {
                    circleButton.setImageResource(R.drawable.man2);
                    head = String.valueOf(R.drawable.man2);
                }
                break;
                case "3": {
                    circleButton.setImageResource(R.drawable.man3);
                    head = String.valueOf(R.drawable.man3);
                }
                break;
                case "4": {
                    circleButton.setImageResource(R.drawable.man4);
                    head = String.valueOf(R.drawable.man4);
                }
                break;
                case "5": {
                    circleButton.setImageResource(R.drawable.man5);
                    head = String.valueOf(R.drawable.man5);
                }
                break;
                case "6": {
                    circleButton.setImageResource(R.drawable.woman1);
                    head = String.valueOf(R.drawable.woman1);
                }
                break;
                case "7": {
                    circleButton.setImageResource(R.drawable.woman2);
                    head = String.valueOf(R.drawable.woman2);
                }
                break;
                case "8":{
                    circleButton.setImageResource(R.drawable.woman3);
                    head = String.valueOf(R.drawable.woman3);
                }
                break;
                case "9":{
                    circleButton.setImageResource(R.drawable.woman4);
                    head = String.valueOf(R.drawable.woman4);
                }
                break;
                case "10":{
                    circleButton.setImageResource(R.drawable.woman5);
                    head = String.valueOf(R.drawable.woman5);
                }
                break;
            }

        }
        super.onActivityResult(requestCode,resultCode,data);
    }



}

