package com.joinyon.androidguide;

import android.app.Application;

import com.hdl.CrashExceptioner;

/**
 * 作者： JoinYon on 2018/8/28.
 * 邮箱：2816886869@qq.com
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //CrashExceptioner.init(this);
    }
}
