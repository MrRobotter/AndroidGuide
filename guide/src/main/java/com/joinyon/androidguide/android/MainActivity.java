package com.joinyon.androidguide.android;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.joinyon.androidguide.R;

import java.io.File;
import java.io.FileOutputStream;

/**
 * https://github.com/yubo725/flutter-osc/tree/master/android/app 开源中国app
 */

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        imageView.setOnLongClickListener(this);

        Drawable drawable = imageView.getDrawable();
        Log.e("AAA", drawable.toString());
    }

    @Override
    public boolean onLongClick(View v) {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                }
            }
        }
//        String fileDir = Environment.DIRECTORY_PICTURES + "/" + "images/" + "戴鼠微信公众号.jpeg";
//        if (ImageSaveUtil.saveBitmap(imageView, fileDir)) {
//
//            Toast.makeText(this, "图片以保存至" + fileDir, Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "图片保存失败！", Toast.LENGTH_SHORT).show();
//        }
        viewSaveToImage(imageView);

        return false;
    }

    private void viewSaveToImage(View view) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);

        FileOutputStream fos;
        String imagePath = "";
        File file = null;
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED);
            if (isHasSDCard) {
                // SD卡根目录
                File sdRoot = Environment.getExternalStorageDirectory();
                file = new File(sdRoot, "戴鼠智能微信公众号.png");
                fos = new FileOutputStream(file);
                imagePath = file.getAbsolutePath();
            } else
                throw new Exception("创建文件失败!");

            cachebmp.compress(Bitmap.CompressFormat.PNG, 90, fos);

            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("imagePath=" + imagePath, "AAA");
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);//扫描单个文件
        intent.setData(Uri.fromFile(file));//给图片的绝对路径
        this.sendBroadcast(intent);

        Toast.makeText(this, "保存成功！", Toast.LENGTH_SHORT).show();
        view.destroyDrawingCache();
    }

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }
}
