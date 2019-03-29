package com.example.aoge.skillexchange;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;


public class MeActivity extends Fragment {
    private ImageButton btnLogout;
    private ImageView background;
    private CircleImageButton headImage;
    private TextView username;
    private TextView uemail;
    private TextView can;
    private TextView want;
    private TextView location;
    private Button btnEdit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_me,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        username = (TextView)getView().findViewById(R.id.txt_p_username);
        uemail = (TextView)getView().findViewById(R.id.txt_p_email_show);
        can = (TextView)getView().findViewById(R.id.txt_p_can_show);
        want = (TextView)getView().findViewById(R.id.txt_p_want_show);
        location = (TextView)getView().findViewById(R.id.txt_p_location_show);

        username.setText(UserInformation.uName);
        uemail.setText(UserInformation.userinformation);
        can.setText(UserInformation.ucan);
        want.setText(UserInformation.uwant);
        location.setText(UserInformation.loan);



        background = (ImageView)getView().findViewById(R.id.imv_me_background);
        background.setImageResource(R.drawable.background);

        headImage = (CircleImageButton) getView().findViewById(R.id.img_p_headportrait);
        headImage.setImageResource(Integer.parseInt(UserInformation.head));

        btnLogout = (ImageButton)getView().findViewById(R.id.imb_logout);
        btnLogout.setImageResource(R.drawable.logout);
        btnLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure to log out?"); builder.setTitle("Tip"); builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    File file = new File(UserInformation.ph+"userinfo.txt");
                    if(file.exists()){
                        file.delete();
                    }else{
                        Toast.makeText(getActivity(),
                            "Failed!", Toast.LENGTH_LONG)
                            .show();
                    }
                    ActivityCollector.finishAll();
//                    dialog.dismiss();
//                    Toast.makeText(getActivity(),
//                            "Success! User your email to log in.", Toast.LENGTH_LONG)
//                            .show();
                }
            });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        btnEdit = (Button)getView().findViewById(R.id.btn_p_edit);
        btnEdit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getView().getContext(), EditUserIfoActivity.class));
            }
        });
    }
}
