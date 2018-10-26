package com.joinyon.androidguide.notice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.joinyon.androidguide.Constant;
import com.joinyon.androidguide.service.AutoNoticeService;

/**
 * 自动提醒广播
 */
public class AutoNoticeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("AAA", action);

        if (Intent.ACTION_BOOT_COMPLETED.equals(action)
                || Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
            Log.e("AAA", "AutoUpdateReceiver===onReceive开机广播");
        } else if (Constant.ALARM_ACTION.equals(action)) {//是定时器的广播
            Intent it = new Intent(context, AutoNoticeService.class);
            it.putExtra("flag","0");//发通知
            context.startService(it);
        }
    }
}
