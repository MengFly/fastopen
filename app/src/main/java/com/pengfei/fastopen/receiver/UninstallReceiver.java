package com.pengfei.fastopen.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.widget.Toast;

import com.pengfei.fastopen.activity.BaseApplication;
import com.pengfei.fastopen.db.SaveAppDBManager;
import com.pengfei.fastopen.entity.AppBean;
import com.pengfei.fastopen.entity.SaveAppEntity;
import com.pengfei.fastopen.utils.AppManager;
import com.pengfei.fastopen.utils.ImageUtils;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * 用于监听系统卸载应用的Receiver
 * Created by mengfei on 2017/7/22.
 */
public class UninstallReceiver extends BroadcastReceiver {
    private static final String TAG = "UninstallReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
            String packageName = getPackageName(intent);
            //这里做卸载应用后的逻辑
            doUnInstallApp(packageName);
        } else if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            //应用添加后的逻辑
            String packageName = getPackageName(intent);
            doInstallApp(packageName);
        }
    }

    //应用被安装后的逻辑
    private void doInstallApp(String packageName) {
        SaveAppEntity appEntity = SaveAppDBManager.findAppEntity(packageName);
        PackageInfo packageInfo = AppManager.getPackageInfo(packageName);
        AppBean bean = AppManager.getAppBean(packageInfo);
        if (appEntity == null) {
            SaveAppEntity entity = SaveAppEntity.createSaveAppByAppBean(bean, false);
            SaveAppDBManager.saveApp(entity);
        }
        //如果为空，获取到AppBean, 并进行保存
        else {
            //如果数据库中存在消息，则证明之前的时候安装过这个App
            appEntity.setUninstall(false);
            appEntity.setIcon(ImageUtils.changeDrawableToByte(bean.getAppIcon()));
            appEntity.setName(bean.getAppName());
            SaveAppDBManager.updateApp(appEntity);
        }

    }

    //当应用被卸载之后要做的事情
    private void doUnInstallApp(String packageName) {
        SaveAppEntity appEntity = SaveAppDBManager.findAppEntity(packageName);
        if (appEntity == null) {// 保存
            SaveAppDBManager.saveApp(new SaveAppEntity(packageName, "未知", null, true));
        } else {
            appEntity.setUninstall(true);
            SaveAppDBManager.updateApp(appEntity);
        }
    }

    private String getPackageName(Intent intent) {
        return intent.getDataString().substring("package:".length());
    }
}
