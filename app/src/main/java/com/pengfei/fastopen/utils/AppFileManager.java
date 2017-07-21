package com.pengfei.fastopen.utils;

import android.os.Environment;

import com.pengfei.fastopen.entity.AppBean;

import java.io.File;

/**
 * App文件的管理类
 * Created by mengfei on 2017/7/20.
 */
public class AppFileManager {

    //获取到导出的应用位置
    public static File getExtraAppDir() {
        File downLoadFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File localAppDir = new File(downLoadFile, "mengfly-app-local");
        if (!localAppDir.exists()) {
            localAppDir.mkdirs();
        }
        return localAppDir;
    }

    public static File getAppFileName(AppBean bean) {
        File file = new File(getExtraAppDir(), bean.getAppName() + bean.getVersionName() + ".apk");
        if (file.exists()) {
            return null;
        }
        return file;
    }
}
