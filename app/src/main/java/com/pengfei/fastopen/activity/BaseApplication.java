package com.pengfei.fastopen.activity;

import android.app.Application;

import java.util.LinkedList;
import java.util.List;

public class BaseApplication extends Application {

    protected static String TAG = "BaseApplication";

    private static BaseApplication mApp = null;

    private List<BaseActivity> mActivityManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mActivityManager = new LinkedList<BaseActivity>();
        TAG = this.getClass().getSimpleName();
        mApp = this;
    }

    /**
     * 获取Application的实例
     */
    public static BaseApplication getInstance() {
        return mApp;
    }

    /**
     * 添加一个Activity
     */
    public void addActivity(BaseActivity activity) {
        this.mActivityManager.add(activity);
    }

    /**
     * 移除Activity
     */
    public void removeActivity(BaseActivity activity) {
        this.mActivityManager.remove(activity);
    }

    /**
     * 程序推出的时候调用的方法，会结束所有的Activity
     */
    public void exit() {
        for (BaseActivity baseActivity : mActivityManager) {
            baseActivity.finish();
        }
    }

}
