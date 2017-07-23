package com.pengfei.fastopen.entity;

import android.content.ContentValues;
import android.graphics.drawable.Drawable;

import com.pengfei.fastopen.utils.ImageUtils;

import org.litepal.crud.DataSupport;

/**
 * 用于向本地数据库中存储的App的信息
 * 这个item类主要是为了完成用户记录用户卸载的应用
 * Created by mengfei on 2017/7/22.
 */
public class SaveAppEntity extends DataSupport {

    public static SaveAppEntity createSaveAppByAppBean(AppBean bean, boolean isUninstall) {
        String packageName = bean.getPackageName();
        String appName = bean.getAppName();
        byte[] iconBytes = ImageUtils.changeDrawableToByte(bean.getAppIcon());
        return new SaveAppEntity(packageName, appName, iconBytes, isUninstall);
    }

    //应用的packageName，这是最主要的
    private String packageName;

    //应用名称
    private String name;

    //应用的icon
    private byte[] icon;

    // 标志应用是否已经被卸载
    private boolean isUninstall;

    public SaveAppEntity(){}

    public SaveAppEntity(String packageName, String name, byte[] icon, boolean isUninstall) {
        this.packageName = packageName;
        this.name = name;
        this.icon = icon;
        this.isUninstall = isUninstall;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getIcon() {
        return icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public boolean isUninstall() {
        return isUninstall;
    }

    public void setUninstall(boolean uninstall) {
        isUninstall = uninstall;
    }
}
