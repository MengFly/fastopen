package com.pengfei.fastopen.thread;

import com.pengfei.fastopen.entity.AppBean;
import com.pengfei.fastopen.utils.AppFileManager;
import com.pengfei.fastopen.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * 保存App到本地
 * Created by mengfei on 2017/7/20.
 */
public class CopyAppToLocalThread extends Thread {

    private AppBean bean;
    private CallBack callBack;

    public CopyAppToLocalThread(AppBean bean, CallBack callBack) {
        this.bean = bean;
        this.callBack = callBack;
    }

    @Override
    public void run() {
        File sourceFile = new File(bean.getAppFileDir());
        File outFile = AppFileManager.getAppFileName(bean);
        if (outFile == null) {
            callBack.onCallBack(false, null, "同版本的安装包已经存在了");
            return;
        }
        try {
            FileUtils.copyFile(sourceFile, outFile);
            callBack.onCallBack(true, outFile, null);
        } catch (IOException e) {
            callBack.onCallBack(false, null, e.getMessage());
            e.printStackTrace();
        }
    }

    public interface CallBack {
        void onCallBack(boolean isCreated, File targetFile, String errorMessage);
    }

}
