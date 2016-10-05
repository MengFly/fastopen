package com.pengfei.fastopen.dao;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * 封装了一些分享功能
 * Created by mengfei on 2016/8/14.
 */
public class ShareTool {

    /**
     * 分享文字
     * @param context context
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
     * @param context context
     * @param shareFile 要分享的文件
     * @param choseText 分享文件的时候显示的提示语
     */
    public static void shareFile(Context context, File shareFile, String choseText) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(shareFile));
        context.startActivity(Intent.createChooser(shareIntent, choseText));
    }
}
