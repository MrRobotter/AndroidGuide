package com.joinyon.androidguide.android;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.joinyon.androidguide.R;
import com.joinyon.circularpercenring.CircularPercentRing;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Android如何制作自己的依赖库上传至github供别人下载使用
 * https://blog.csdn.net/xuchao_blog/article/details/62893851
 * <p>
 * <p>
 * Android 发布项目到 JCenter 遇到的各种坑
 * https://www.jianshu.com/p/c518a10fdaed
 * <p>
 * <p>
 * 教你如何将自己的项目可作为别人的依赖compile
 * https://blog.csdn.net/lty406910111/article/details/73920367
 */

public class WebActivity extends AppCompatActivity {
    private CircularPercentRing circularPercentRing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        initView();
    }

    private void initView() {
        circularPercentRing = findViewById(R.id.ring);
        int[] colors = {0xFF11F020, 0xFFFFDC40, 0xFFE9151F};
        circularPercentRing.setColors(colors);
        circularPercentRing.setRoundWidth(50);
        circularPercentRing.update(89.0f, 1500);
    }
}
