package com.pengfei.fastopen.utils;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.drawable.Drawable;

import com.pengfei.fastopen.activity.BaseApplication;
import com.pengfei.fastopen.entity.AppBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppManager {

    public static PackageManager getPackageManager() {
        return BaseApplication.getInstance().getPackageManager();
    }

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

    public static PackageInfo getPackageInfo(String packageName) {
        PackageManager packageManager = getPackageManager();
        try {
            return packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
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

    // 获取App的权限
    public static List<String> requestPermission(String packageName) {
        PackageInfo packageInfo;
        PackageManager pm = AppManager.getPackageManager();
        try {
            packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
        String[] permissions = packageInfo.requestedPermissions;
        if (permissions == null) {
            return Collections.emptyList();
        }

        List<String> permissionsList = new ArrayList<>(permissions.length);

        for (String permissionName : permissions) {
            PermissionInfo permissionInfo = getPermissionInfo(pm, permissionName);
            CharSequence permissionLabel = null;
            CharSequence permissionDesc = null;
            if (permissionInfo != null) {
                permissionLabel = permissionInfo.loadLabel(pm);
                permissionDesc = permissionInfo.loadDescription(pm);
            }
            if (permissionLabel != null && permissionDesc != null) {
                permissionsList.add(0, permissionName + "\n" + TextUtils.getText(permissionLabel) + "\n" + TextUtils.getText(permissionDesc));
            } else {
                permissionsList.add(permissionName);
            }

        }
        return permissionsList;
    }

    //获取Permission的详细信息
    private static PermissionInfo getPermissionInfo(PackageManager pm, String permissionName) {
        try {
            return pm.getPermissionInfo(permissionName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
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
