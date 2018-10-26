package com.joinyon.androidguide.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import com.joinyon.androidguide.R;
import com.joinyon.androidguide.android.NotifyActivity;

public class AutoNoticeService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getStringExtra("flag").equals("0")) {
            normal();
        }

        return super.onStartCommand(intent, flags, startId);

    }

    /**
     * 发普通通知
     */
    public void normal() {
        // 第一步 获取NotificationManager实例
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://joinyon.top/"));
        Intent intent = new Intent(this, NotifyActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        // 第二步 实例Notification对象
        Notification notification = new NotificationCompat.Builder(this, "0")
                .setContentText("普通通知的内容")
                .setContentTitle("普通通知的标题")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                //.setWhen(System.currentTimeMillis())
                // 第三步 关联Intent
                .setContentIntent(pendingIntent)//点击跳转
                .setAutoCancel(true)//点击通知头自动取消
                .setTicker("您有新的普通通知了！")
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.iphone_ringtone))// 声音
                .setLights(0xff00ff00, 300, 1000)// LED
                //.setVibrate()// 震动
                //.setDefaults(NotificationCompat.DEFAULT_ALL)//设置铃声及震动效果
                .build();
        //第四步 发送通知
        notificationManager.notify(1, notification);


    }
}
