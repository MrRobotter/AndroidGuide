package com.joinyon.androidguide.notice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 自动提醒广播
 */
public class AutoNoticeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("AAA", intent.getAction());
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())
                || Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())) {
            Log.e("AAA", "AutoUpdateReceiver===onReceive开机广播");
            AlarmManagerUtil.setAlarm(context, 1, 00, 02, 0, 0);
        }
    }
}
