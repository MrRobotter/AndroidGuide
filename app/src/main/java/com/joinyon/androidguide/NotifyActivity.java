package com.joinyon.androidguide;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

public class NotifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
    }

    public void hide(View view) {
    }

    /**
     * 普通通知
     *
     * @param view
     */
    public void normal(View view) {
        // 第一步 获取NotificationManager实例
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://joinyon.top/"));
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
                //.setLights()// LED
                //.setVibrate()// 震动
                //.setDefaults(NotificationCompat.DEFAULT_ALL)//设置铃声及震动效果
                .build();
        //第四步 发送通知
        notificationManager.notify(1, notification);


    }

    public void hang(View view) {
        // 第一步 获取NotificationManager实例
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://joinyon.top/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // 第二步 实例Notification对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1");
        Notification notification = builder.setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(Notification.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle("悬挂式通知")
                .setAutoCancel(true)
                // .setContentIntent(pendingIntent)
                .setContentText("这是一个悬挂式通知！")
                //第三步
                .setFullScreenIntent(pendingIntent, true)
                .build();
        //第四步 发送通知
        notificationManager.notify(1, notification);
    }

    public void collapsed(View view) {
        // 第一步 获取NotificationManager实例
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://joinyon.top/"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        //设置未下拉的样式R.layout.collapsed
        RemoteViews collapsed = new RemoteViews(getPackageName(), R.layout.collapsed);
        collapsed.setTextViewText(R.id.collapsed_text, "关闭状态");

        //设置下拉后的样式TR.layout.show
        RemoteViews show = new RemoteViews(getPackageName(), R.layout.show);
        // 第二步 实例Notification对象
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "0");
        Notification notification = builder.setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pendingIntent)
                .setContentText("JoinYon's Blog")
                //第三步
                .setCustomContentView(collapsed)
                .setCustomBigContentView(show)
                .setDefaults(NotificationCompat.DEFAULT_ALL)//设置铃声及震动效果
                .build();
        //第四步 发送通知
        notificationManager.notify(0, notification);

    }
}
