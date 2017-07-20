package com.pengfei.fastopen.entity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import com.pengfei.fastopen.utils.DateTool;
import com.pengfei.fastopen.utils.SPUtils;

import java.util.Date;

public class AppBean implements Comparable<AppBean>{
    private static final String TOP_SP = "top_sp";

    private PackageInfo appInfo;
    //    private int useCount;//(使用频率，使用次数)
    private String appName;//（应用的名称）
    //    private Date lastOpenTime;// （上次打开时间）
    private Drawable appIcon;//（App的Icon）
    private Intent startIntent;//获取启动的intent
    private Date firstInstallTime;//第一次安装时间

    private Date lastUpdateTime;//最后更新时间

    public AppBean() {
    }

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

    private Date getLastOpenTime(SharedPreferences spf, String appName) {
        long time = spf.getLong(appName, -1);
        if (time == -1) {
            return null;
        } else {
            return new Date(time);
        }
    }

    public String getFirstInstallTime() {
        if (firstInstallTime != null) {
            return DateTool.getDateStr(firstInstallTime);
        } else {
            return null;
        }
    }

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

    public void setIsTop(boolean isTop) {
        SPUtils.put(TOP_SP, appName, isTop);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setStartIntent(Intent startIntent) {
        this.startIntent = startIntent;
    }

    public String getPackageName() {
        return appInfo.packageName;
    }

    public String getVersionName() {
        return appInfo.versionName;
    }

    public boolean isSystem() {
        return ((appInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    @Override
    public int compareTo(AppBean another) {
        if (isTop()) {
            return -1;
        }
        if (another.isTop()) {
            return 1;
        }
        else return 0;
    }
}
