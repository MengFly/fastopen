package com.pengfei.fastopen.utils;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.pengfei.fastopen.activity.BaseApplication;
import com.pengfei.fastopen.entity.AppBean;

import java.util.ArrayList;
import java.util.List;

public class AppManager {

    private static final String TAG = "AppManager";

    public static List<PackageInfo> getPackages() {
        PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
        return packageManager.getInstalledPackages(0);
    }

    public static AppBean getAppBean(PackageInfo info) {
        PackageManager packageManager = BaseApplication.getInstance().getPackageManager();
        Intent startIntent = packageManager.getLaunchIntentForPackage(info.packageName);
        AppBean bean = new AppBean(info);
        String appName = getApplicationName(info.packageName, packageManager);
        Drawable appIcon = info.applicationInfo.loadIcon(packageManager);
        bean.setAppIcon(appIcon);
        bean.setAppName(appName);
        bean.setStartIntent(startIntent);
        return bean;
    }


    /**
     * 获得应用名称
     *
     * @return 应用的名称
     */
    private static String getApplicationName(String packName, PackageManager packageManager) {
        String applicationName = null;
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packName, 0);
            applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return applicationName;
    }

    /**
     * 获取筛选后的App
     *
     * @param filterStr 删选的字字符串
     */
    public static List<AppBean> getFilterApps(String filterStr, List<AppBean> totalApps) {
        List<AppBean> filterApps = new ArrayList<>();
        for (AppBean appBean : totalApps) {
            if (appBean.getAppName().toUpperCase().contains(filterStr.toUpperCase())) {
                filterApps.add(appBean);
            }
        }
        return filterApps;
    }
}
