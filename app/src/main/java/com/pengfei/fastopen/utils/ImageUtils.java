package com.pengfei.fastopen.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 提供一些图片操作的方法
 * Created by mengfei on 2017/7/22.
 */
public class ImageUtils {

    /**
     * 将drawable对象转换成字符数组
     *
     * @param drawable 要进行转换的drawable
     * @return 转换后的byte数组
     */
    public static byte[] changeDrawableToByte(Drawable drawable) {
        Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] output = os.toByteArray();
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static Bitmap changeByteToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
