package com.pengfei.fastopen.dao;

import android.content.SharedPreferences;

/**
 * 这个是SettingActivity的管理类，用于记录设置和获取设置信息
 * Created by mengfei on 2016/8/15.
 */
public class SettingManager {

    private static final String IS_AUTO_EXIT = "isAutoExit";//是否自动退出
    private static final String SHOW_APP_VIEW = "showAppView";//显示的布局

    public static final String SHOW_APP_GRID = "grid";
    public static final String SHOW_APP_LIST = "list";

    private static volatile SettingManager mInstance;

    private SharedPreferences settingSPF;

    private SettingManager() {
        settingSPF = SharedPreferencesManager.getSharePreferences("setting");
    }

    /**
     * 设置App的视图布局
     */
    public void setShowAppView(String appView) {
        SharedPreferences.Editor editor = settingSPF.edit();
        editor.putString(SHOW_APP_VIEW, appView);
        editor.apply();
    }

    /**
     * 获取APP的视图布局
     */
    public String getShowAppView() {
        return settingSPF.getString(SHOW_APP_VIEW, SHOW_APP_LIST);
    }

    /**
     * 设置是否打开应用自动退出
     */
    public void setIsAutoExit(boolean isAutoExit) {
        SharedPreferences.Editor editor = settingSPF.edit();
        editor.putBoolean(IS_AUTO_EXIT, isAutoExit);
        editor.apply();
    }

    public boolean isAutoExit() {
        return settingSPF.getBoolean(IS_AUTO_EXIT, false);
    }


    public static SettingManager getInstance() {
        if (mInstance == null) {
            synchronized (SettingManager.class) {
                if (mInstance == null) {
                    mInstance = new SettingManager();
                }
            }
        }
        return mInstance;
    }

}
