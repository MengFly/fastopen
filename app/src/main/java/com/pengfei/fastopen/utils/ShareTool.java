package com.pengfei.fastopen.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import java.io.File;
import java.util.List;

/**
 * 封装了一些分享功能
 * Created by mengfei on 2016/8/14.
 */
public class ShareTool {

    /**
     * 分享文字
     *
     * @param context   context
     * @param shareText 要分享的文字
     * @param choseText 分享文字的时候显示的提示语
     */
    public static void shareText(Context context, String shareText, String choseText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        context.startActivity(Intent.createChooser(shareIntent, choseText));
    }

    /**
     * 分享文字
     *
     * @param context   context
     * @param shareFile 要分享的文件
     * @param choseText 分享文件的时候显示的提示语
     */
    public static void shareFile(Context context, File shareFile, String choseText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareFile));
        context.startActivity(Intent.createChooser(shareIntent, choseText));
    }

    /**
     * 卸载应用
     */
    public static void uninstallApp(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        context.startActivity(intent);
    }

    /**
     * 通过包名 在应用商店打开应用
     *
     * @param packageName 包名
     */
    public static void openApplicationMarket(Context context, String packageName) {
        String str = "market://details?id=" + packageName;
        Intent localIntent = new Intent(Intent.ACTION_VIEW);
        localIntent.setData(Uri.parse(str));
        if (isUsedIntentActivity(context, localIntent)) {
            context.startActivity(Intent.createChooser(localIntent, "选择应用"));
        } else {
            Toast.makeText(context, "没有安装相应的应用商店", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 返回目标的Intent时候存在支持的Activity
     *
     * @param intent 目标的Intent
     * @return 是否存在可以前往的Activity或者是Service
     */
    public static boolean isUsedIntentActivity(Context context, Intent intent) {
        PackageManager manager = context.getPackageManager();
        List list = manager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

}
