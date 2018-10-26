package com.joinyon.androidguide.notice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.joinyon.androidguide.R;

public class AlarmActivity extends AppCompatActivity {
    private TextView tvTime;
    private int h;
    private int m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        final TimePicker timePicker;
        timePicker = findViewById(R.id.timePicker);
        tvTime = findViewById(R.id.tvTime);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                tvTime.setText(hourOfDay + ":" + minute);
                h = hourOfDay;
                m = minute;
            }
        });
    }


    public void setNotice(View view) {
        AlarmManagerUtil.setAlarm(this, 1, h, m, 0, 0);

    }
}
