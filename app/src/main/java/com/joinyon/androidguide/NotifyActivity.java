package com.joinyon.androidguide;

import android.app.Notification;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NotifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
    }


    public void show(View view) {
        //一、获取状态通知栏管理
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("您有新的通知提醒！")
                .setContentText("测试内容")
                .setTicker("通知来了！")
                .setSmallIcon(R.mipmap.ic_launcher);//设置通知小ICON


        Notification notification=builder.build();
        notificationManager.notify(1,notification);


    }

    public void hide(View view) {
    }
}
