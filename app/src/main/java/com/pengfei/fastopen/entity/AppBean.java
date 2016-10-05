package com.pengfei.fastopen.entity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.pengfei.fastopen.dao.DateTool;
import com.pengfei.fastopen.dao.SharedPreferencesManager;

import java.util.Date;

public class AppBean implements Comparable<AppBean> {

    static SharedPreferences useCountSPF = SharedPreferencesManager.getSharePreferences("AppUseCount");
    static SharedPreferences timeSPF = SharedPreferencesManager.getSharePreferences("AppOpenTime");

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


    public void setUseCount(int useCount) {
//        this.useCount = useCount;
        SharedPreferences.Editor useCountEdit = useCountSPF.edit();
        useCountEdit.putInt(getAppName(), useCount);
        useCountEdit.apply();
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

    public String getLastOpenTime() {
        Date lastOpenTime = getLastOpenTime(timeSPF, getAppName());
        if (lastOpenTime != null) {
            return DateTool.getDateStr(lastOpenTime);
        } else {
            return null;
        }
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

    public int getUseCount() {
        return useCountSPF.getInt(appName, 0);
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

    public void setLastOpenTime(Date lastOpenTime) {
        SharedPreferences.Editor timeEditor = timeSPF.edit();
        timeEditor.putLong(getAppName(), lastOpenTime.getTime());
        timeEditor.apply();
    }



    @Override
    public int compareTo(@NonNull AppBean another) {
        //先根据使用情况进行排序，再根据使用频率进行排序
        if (getLastOpenTime() != null && another.getLastOpenTime() == null) {
            return -1;
        } else if (getLastOpenTime() == null && another.getLastOpenTime() != null) {
            return 1;
        } else {
            if (getUseCount() > another.getUseCount()) {//根据使用频率来进行排序
                return -1;
            } else if (getUseCount() < another.getUseCount()) {
                return 1;
            } else {
                return 0;
            }
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("应用名称 : ");
        sb.append(appName);
        sb.append("\n");
        sb.append("版本信息 : ");
        sb.append(getVersionName());
        sb.append("\n");
        sb.append("应用包名 : ");
        sb.append(getPackageName());
        sb.append("\n");
        sb.append("上次打开时间 : ");
        sb.append(getLastOpenTime() == null ? "未通过此应用使用过" : getLastOpenTime());
        sb.append("\n");
        sb.append("应用安装时间 : ");
        sb.append(getFirstInstallTime());
        sb.append("\n");
        sb.append("最近更新时间 : ");
        sb.append(getLastUpdateTime());
        sb.append("\n");
        sb.append("累计使用次数 : ");
        sb.append(getUseCount());
        sb.append(" 次\n");
        return sb.toString();
    }
}
