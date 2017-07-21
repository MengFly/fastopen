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


    public static AlertDialog getExtraAppsDialog(final Context context) {
        final String[] appsItems = getExtraApps();
        return getItemsDialog("已导出的应用", context, appsItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] items = new String[]{"安装", "删除"};
                final String fileName = appsItems[which];
                getItemsDialog(fileName, context, items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                IntentUtils.installApp(context, new File(AppFileManager.getExtraAppDir(), fileName));
                                break;
                            case 1:
                                if (FileUtils.deleteFile(new File(AppFileManager.getExtraAppDir(), fileName))) {
                                    ((BaseActivity) context).showToast("删除失败");
                                } else {
                                    ((BaseActivity) context).showToast("文件已删除");
                                }
                                break;
                        }
                    }
                }).create().show();
            }
        }).create();
    }

    private static AlertDialog.Builder getItemsDialog(String title, Context context, String[] items, DialogInterface.OnClickListener listener) {
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
