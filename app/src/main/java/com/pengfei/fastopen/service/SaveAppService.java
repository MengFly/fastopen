package com.pengfei.fastopen.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;

import com.pengfei.fastopen.entity.AppBean;
import com.pengfei.fastopen.entity.SaveAppEntity;
import com.pengfei.fastopen.utils.AppManager;
import com.pengfei.fastopen.utils.ImageUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个服务用语保存用户App
 * Created by mengfei on 2017/7/22.
 */
public class SaveAppService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public SaveAppService() {
        super("SaveAppService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //需要保存的App，默认的筛选规则是：
        //1) 可以被打开
        //2) 可以被卸载
        List<SaveAppEntity> needSaveApps = new ArrayList<>();
        List<PackageInfo> allPackages = AppManager.getPackages();
        for (PackageInfo packageInfo : allPackages) {
            AppBean bean = AppManager.getAppBean(packageInfo);
            // 可以被打开或者可以被卸载
            if (bean.getStartIntent() != null || bean.isCanUninstall()) {
                SaveAppEntity entity = SaveAppEntity.createSaveAppByAppBean(bean, false);
                needSaveApps.add(entity);
            }
        }
        // 向数据库中保存数据
        DataSupport.saveAll(needSaveApps);
    }
}
