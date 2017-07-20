package com.pengfei.fastopen.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.pengfei.fastopen.activity.BaseApplication;

/**
 * 剪贴板的管理类
 * Created by mengfei on 2017/7/20.
 */
public class ClipbordUtils {

    public static void setClipBordText(CharSequence text) {
        ClipboardManager clipboard = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(ClipData.newPlainText("text", text));
    }

    public static CharSequence getText() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = clipboard.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(getContext());
        }
        return null;
    }

    private static Context getContext() {
      return BaseApplication.getInstance();
    }
}
