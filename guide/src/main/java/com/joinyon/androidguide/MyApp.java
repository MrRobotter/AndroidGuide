package com.joinyon.androidguide;

import android.app.Application;
import android.content.Context;

import com.hdl.CrashExceptioner;

/**
 * 作者： JoinYon on 2018/8/28.
 * 邮箱：2816886869@qq.com
 */
public class MyApp extends Application {
    private static MyApp application;

    public static MyApp get(Context context) {
        return (MyApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        application = this;
        super.onCreate();
        //CrashExceptioner.init(this);
    }

    public static MyApp getInstance() {
        return application;
    }

}
