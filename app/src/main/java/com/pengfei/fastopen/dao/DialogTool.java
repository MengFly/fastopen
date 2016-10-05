package com.pengfei.fastopen.dao;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.widget.TextView;

import com.pengfei.fastopen.R;

/**
 * 提供一些现实Dialog的方法
 * Created by mengfei on 2016/8/15.
 */
public class DialogTool {

    public static void showTextDialog(Context context, Drawable logo, String title, String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        TextView t = new TextView(context);
        t.setGravity(Gravity.CENTER);
        t.setPadding(10, 10, 10, 10);
        t.setText(message);
        t.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
        t.setTextColor(context.getResources().getColor(R.color.text_white));
        builder.setIcon(logo);
        builder.setTitle(title);
        builder.setView(t);
        builder.setPositiveButton("确定", null);
        builder.create().show();
    }

    public static void showChoseItemDialog(Context context, int itemRes, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(itemRes,listener);
        builder.create().show();
    }
}
