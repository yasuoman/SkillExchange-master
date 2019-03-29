package com.example.aoge.skillexchange;

/**
 * Created by dell on 2019/3/24.
 */

import com.example.aoge.skillexchange.ActivityCollector;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

}