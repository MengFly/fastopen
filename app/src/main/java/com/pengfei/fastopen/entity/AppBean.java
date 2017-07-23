package com.pengfei.fastopen.entity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.pengfei.fastopen.utils.AppManager;
import com.pengfei.fastopen.utils.DateTool;
import com.pengfei.fastopen.utils.SPUtils;

import java.util.Date;
import java.util.List;

public class AppBean implements Comparable<AppBean> {
    private static final String TOP_SP = "top_sp";

    private PackageInfo appInfo;
    //    private int useCount;//(使用频率，使用次数)
    private String appName;//（应用的名称）
    //    private Date lastOpenTime;// （上次打开时间）
    private Drawable appIcon;//（App的Icon）
    private Intent startIntent;//获取启动的intent
    private Date firstInstallTime;//第一次安装时间
    private Date lastUpdateTime;//最后更新时间

    public AppBean(PackageInfo appInfo) {
        this.appInfo = appInfo;
        firstInstallTime = new Date(appInfo.firstInstallTime);
        this.lastUpdateTime = new Date(appInfo.lastUpdateTime);
    }


    public Intent getStartIntent() {
        return startIntent;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    /**
     * 获取应用的安装包路径
     *
     * @return 应用的安装包路径
     */
    public String getAppFileDir() {
        return appInfo.applicationInfo.sourceDir;
    }

    // 第一次安装时间
    public String getFirstInstallTime() {
        if (firstInstallTime != null) {
            return DateTool.getDateStr(firstInstallTime);
        } else {
            return null;
        }
    }

    // 最后一次安装时间
    public String getLastUpdateTime() {
        if (firstInstallTime != null) {
            return DateTool.getDateStr(lastUpdateTime);
        } else {
            return null;
        }
    }

    //是否置顶
    public boolean isTop() {
        return (boolean) SPUtils.get(TOP_SP, appName, false);
    }

    //设置是否置顶
    public void setIsTop(boolean isTop) {
        SPUtils.put(TOP_SP, appName, isTop);
    }

    //获取App名称
    public String getAppName() {
        return appName;
    }

    //设置APP的显示Icon
    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    //设置App的名称
    public void setAppName(String appName) {
        this.appName = appName;
    }

    // 设置启动App的Intent
    public void setStartIntent(Intent startIntent) {
        this.startIntent = startIntent;
    }

    // 获取App的Package
    public String getPackageName() {
        return appInfo.packageName;
    }

    // 获取App版本号
    public String getVersionName() {
        return appInfo.versionName;
    }

    // 返回是否为系统应用
    public boolean isSystem() {
        return ((appInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    //是否可以被卸载
    // 如果不是系统应用或者是如果之后被更新过，那么这个应用就可以被卸载
    public boolean isCanUninstall() {
        return !isSystem() || !getFirstInstallTime().equals(getLastUpdateTime());
    }

    //是否是新安装的应用
    public boolean isNewInstall() {
        //如果第一次安装时间在1天之内
        return ((System.currentTimeMillis() - firstInstallTime.getTime()) / 1000 - 60 * 60 * 24) < 0;
    }

    //是否新更新的应用
    public boolean isNewUpdate() {
        //如果第一次安装时间在1天之内
        return ((System.currentTimeMillis() - lastUpdateTime.getTime()) / 1000 - 60 * 60 * 24) < 0;
    }

    // 获取App的权限
    public List<String> requestPermission() {
        return AppManager.requestPermission(getPackageName());
    }

    @Override
    public int compareTo(@NonNull AppBean another) {
        if (isTop()) {
            return -1;
        }
        if (another.isTop()) {
            return 1;
        }
        if (isNewInstall()) {
            return -1;
        }
        if (isNewUpdate()) {
            return -1;
        }
        if (another.isNewInstall()) {
            return 1;
        }
        if (another.isNewUpdate()) {
            return 1;
        }
        return 0;
    }
}
