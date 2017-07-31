package com.pengfei.fastopen.utils;

import android.content.Context;

import com.pengfei.fastopen.activity.BaseApplication;

/**
 * Created by mengfei on 2017/7/31.
 */

public class ActivityManager {

    public static android.app.ActivityManager getActivityManager() {
        return (android.app.ActivityManager) BaseApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
    }
}
