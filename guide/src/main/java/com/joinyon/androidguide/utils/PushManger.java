package com.example.administrator.cangaroo.manager;

import android.content.Context;

import com.example.administrator.cangaroo.base.CangarooApplication;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by admin on 2017/7/5.
 * 极光管理
 */

public class PushManger {
    private static PushManger pushManager = new PushManger();

    public static PushManger getInstance() {
        return pushManager;
    }

    private PushManger() {
    }

    public void startPush(Context context) {
        JPushInterface.init(context);
        JPushInterface.setDebugMode(true);
        JPushInterface.stopCrashHandler(context);
        if (JPushInterface.isPushStopped(context)) {
            JPushInterface.resumePush(context);
        }
    }

    public void setAlias(String alias) {

        try {
            JPushInterface.setAlias(CangarooApplication.getInstance(), alias, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getRegistrationId() {
        return JPushInterface.getRegistrationID(CangarooApplication.getInstance());
    }

}
