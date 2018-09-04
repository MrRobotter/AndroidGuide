package com.joinyon.androidguide.HRichEditorView.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * 作者： JoinYon on 2018/8/27.
 * 邮箱：2816886869@qq.com
 */
public class BaseActivity extends AppCompatActivity {
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    public void setTitle(String title) {
        this.title = title;
    }
}
