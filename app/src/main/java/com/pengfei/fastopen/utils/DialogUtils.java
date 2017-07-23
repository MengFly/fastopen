package com.pengfei.fastopen.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.pengfei.fastopen.activity.BaseActivity;

import java.io.File;

/**
 * 用于显示一些Dialog的工具类
 * Created by mengfei on 2017/7/21.
 */
public class DialogUtils {



    public static AlertDialog.Builder getItemsDialog(String title, Context context, String[] items, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setItems(items, listener);
        return builder;
    }

    private static String[] getExtraApps() {
        File extraDir = AppFileManager.getExtraAppDir();
        return extraDir.list();
    }
}
