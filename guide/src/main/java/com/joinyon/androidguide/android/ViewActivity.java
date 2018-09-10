package com.joinyon.androidguide.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.joinyon.androidguide.R;

public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
    }
}
