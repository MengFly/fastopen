package com.pengfei.fastopen.activity;

import android.app.Application;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BaseApplication extends Application {

    protected static String TAG = "BaseApplication";

    private static BaseApplication mApp = null;

    private List<BaseActivity> mActivityManager;
    //线程池
    private ExecutorService threadPoolExecutor;

    @Override
    public void onCreate() {
        super.onCreate();
        mActivityManager = new LinkedList<>();
        threadPoolExecutor = Executors.newFixedThreadPool(3);
        TAG = this.getClass().getSimpleName();
        mApp = this;
    }

    public void executeThread(Thread thread) {
        threadPoolExecutor.submit(thread);
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
