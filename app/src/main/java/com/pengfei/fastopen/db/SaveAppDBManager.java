package com.pengfei.fastopen.db;

import android.content.ContentValues;

import com.pengfei.fastopen.entity.SaveAppEntity;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * SaveAppEntity 数据库的管理类
 * Created by mengfei on 2017/7/23.
 */

public class SaveAppDBManager {

    public static SaveAppEntity findAppEntity(String packageName) {
        List<SaveAppEntity> saveAppEntities = DataSupport.where("packageName=?", packageName).find(SaveAppEntity.class);
        if (saveAppEntities.isEmpty()) {
            return null;
        } else {
            return saveAppEntities.get(0);
        }
    }

    public static void saveApp(SaveAppEntity entity) {
        entity.save();
    }

    public static void updateApp(SaveAppEntity entity) {
        ContentValues updateValues = new ContentValues();
        updateValues.put("name", entity.getName());
        updateValues.put("icon", entity.getIcon());
        updateValues.put("isUninstall", entity.isUninstall());
        DataSupport.updateAll(SaveAppEntity.class, updateValues, "packageName=?", entity.getPackageName());
    }

    public static List<SaveAppEntity> getUnInstallApp() {
        return DataSupport.where("isUninstall=?", String.valueOf(1)).find(SaveAppEntity.class);
    }

    public static void deleteApp(String packageName) {
        DataSupport.deleteAll(SaveAppEntity.class, "packageName=?", packageName);
    }
}
