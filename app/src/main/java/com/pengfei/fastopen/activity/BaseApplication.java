package com.pengfei.fastopen.activity;

import android.app.Application;
import android.content.Intent;

import com.pengfei.fastopen.service.SaveAppService;
import com.pengfei.fastopen.utils.SPUtils;

import org.litepal.LitePal;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class BaseApplication extends Application {

    protected static String TAG = "BaseApplication";

    //KEY 用于检查应用是否是第一次被打开
    public static final String APP_CONFIG_IS_FIRST_OPEN = "is_first_open";

    private static BaseApplication mApp = null;

    private List<BaseActivity> mActivityManager;
    //线程池
    private ExecutorService threadPoolExecutor;

    @Override
    public void onCreate() {
        super.onCreate();
        mActivityManager = new LinkedList<>();
        LitePal.initialize(this);
        threadPoolExecutor = Executors.newFixedThreadPool(3);
        TAG = this.getClass().getSimpleName();
        mApp = this;
        initDataBase();
    }

    //初始化数据库信息
    private void initDataBase() {
        boolean isFirstOpen = (boolean) SPUtils.get(SPUtils.N_APP_CONFIG, APP_CONFIG_IS_FIRST_OPEN, true);
        if (isFirstOpen) {
            //如果是第一次被打开，那么就进行数据的保存
            startService(new Intent(this, SaveAppService.class));
            SPUtils.put(SPUtils.N_APP_CONFIG, APP_CONFIG_IS_FIRST_OPEN, false);
        }
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
