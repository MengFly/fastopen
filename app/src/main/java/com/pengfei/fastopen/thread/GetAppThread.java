package com.pengfei.fastopen.thread;

import android.content.pm.PackageInfo;

import com.pengfei.fastopen.utils.AppManager;
import com.pengfei.fastopen.entity.AppBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 获取到App的Thread
 * Created by mengfei on 2017/7/20.
 */
public class GetAppThread extends Thread {
    private CallBack callBack;

    public GetAppThread(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void run() {
        List<PackageInfo> packages = AppManager.getPackages();
        //每当获取到5个应用的时候进行一次回调
        List<AppBean> appBeanList = new ArrayList<>(5);
        for (PackageInfo aPackage : packages) {
            AppBean bean = AppManager.getAppBean(aPackage);
            if (bean != null) {
                appBeanList.add(bean);
            }
        }
        //如果temp里面还有剩余的app，则返回
        callBack.onCallback(appBeanList);

    }

    //通过回调返回获取到的App
    public interface CallBack {
        void onCallback(List<AppBean> beanList);
    }
}
