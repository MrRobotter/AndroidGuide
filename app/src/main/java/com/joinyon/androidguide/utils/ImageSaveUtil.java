package com.joinyon.androidguide.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 作者： JoinYon on 2018/6/14.
 * 邮箱：2816886869@qq.com
 * 保存图片的工具
 */

public class ImageSaveUtil {
    public static boolean saveBitmap(ImageView view, String filePath) {
        Drawable drawable = view.getDrawable();
        if (drawable == null) {
            return false;
        }

        FileOutputStream outputStream = null;
        File file = new File(filePath);
        if (file.isDirectory()) {
            return false;
        }
        try {
            outputStream = new FileOutputStream(file);
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

//    public static boolean saveBitmap(View view, String filePath) {
//
//        // 创建对应大小的bitmap
//        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
//                Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        view.draw(canvas);
//
//        //存储
//        FileOutputStream outStream = null;
//        File file = new File(filePath);
//        if (file.isDirectory()) {//如果是目录不允许保存
//            return false;
//        }
//        try {
//            outStream = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
//            outStream.flush();
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//
//        } finally {
//            try {
//                bitmap.recycle();
//                if (outStream != null) {
//                    outStream.close();
//                }
//
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
