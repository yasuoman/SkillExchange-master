package com.example.aoge.skillexchange;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends BaseActivity {
    private Button btnRegister;
    private EditText inputFullName;
    private EditText inputEmail;
    private EditText inputPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inputFullName = findViewById(R.id.namereg);
        inputEmail = findViewById(R.id.emailreg);
        inputPassword = findViewById(R.id.passwordreg);
        btnRegister = findViewById(R.id.btnRegister);
    }

    /**
     * Press the button register, go to Registration form
     *
     * @param view from the activity_register.xml
     */
    public void btnRegister(View view) {

        String name = inputFullName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();

        // Check for empty data and email format in the form
        if(name.isEmpty() || email.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(),
                    "Please enter your details!", Toast.LENGTH_LONG)
                    .show();
        }else if(!emailFormat(email)){
            Toast.makeText(getApplicationContext(),
                    R.string.enter_emailcredentials, Toast.LENGTH_LONG)
                    .show();
        }else if(UserInformation.permission == -1){
            Toast.makeText(getApplicationContext(),
                    "Sorry, this App can't work if you don't allow the required permission.", Toast.LENGTH_LONG)
                    .show();
        }else{
            // Avoid repeated clicks by disabling the button
            btnRegister.setClickable(false);
            //Register the user
            RegisterRequest(name, email, password);
        }
    }


    /**
     * Press the button to log in, go to toLogin form
     *
     * @param view from the activity_register.xml
     */
    public void btntoLogin(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    /**
     * link to server to depend whether the username is valid.
     * @param username
     * @param email
     * @param password
     */
    public void RegisterRequest(final String username, final String email, final String password) {
        //request url
        String url = "http://106.14.117.91:8080/SkillsExchangeServer/RegisterServlet";    //注①
        String tag = "Register";    //注②

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
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String result = jsonObject.getString("Result");
                            if (result.equals("success")) {
                                Toast.makeText(getApplicationContext(),
                                        "Success! User your email to log in.", Toast.LENGTH_LONG)
                                        .show();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "The email has been used, please change another one.", Toast.LENGTH_LONG)
                                        .show();
                                btnRegister.setClickable(true);
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
                btnRegister.setClickable(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Username", username);  //put the parameter.
                params.put("Email", email);
                params.put("Password", password);
                return params;
            }
        };

        //set the tag.
        request.setTag(tag);

        //add the request to queue.
        requestQueue.add(request);
    }

    /**
     * Check the email format.
     * @param email
     */
    public boolean emailFormat(String email)
    {
        boolean tag = true;
        String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(pattern1);
        Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }


}
