package com.pengfei.fastopen.dao;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.pengfei.fastopen.entity.AppBean;

import java.util.ArrayList;
import java.util.List;

public class AppManager {

    private static final String TAG = "AppManager";

    public static List<AppBean> getApps(Context context) {
        ArrayList<AppBean> apps = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        for (PackageInfo info : packageInfos) {
            Intent startIntent = packageManager.getLaunchIntentForPackage(info.packageName);
            if (startIntent != null) {
                AppBean bean = new AppBean(info);
                String appName = getApplicationName(info.packageName, packageManager);
                Drawable appIcon = info.applicationInfo.loadIcon(packageManager);
                bean.setAppIcon(appIcon);
                bean.setAppName(appName);
                bean.setStartIntent(startIntent);
                apps.add(bean);
            }
        }
        return apps;
    }


    /**
     * 获得应用名称
     *
     * @return 应用的名称
     */
    public static String getApplicationName(String packName, PackageManager packageManager) {
        String applicationName = null;
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packName, 0);
            applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return applicationName;
    }

}
