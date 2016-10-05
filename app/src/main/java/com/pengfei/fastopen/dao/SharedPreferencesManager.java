package com.pengfei.fastopen.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.pengfei.fastopen.activity.BaseApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * 获取SharePreferences
 * Created by mengfei on 2016/8/14.
 */
public class SharedPreferencesManager {

    private static Map<String, SharedPreferences> preferencesMap = new HashMap<>();

    public static SharedPreferences getSharePreferences(String spfName) {
        if (preferencesMap.containsKey(spfName)) {
            return preferencesMap.get(spfName);
        } else {
            SharedPreferences spf = BaseApplication.getInstance().getSharedPreferences(spfName, Context.MODE_PRIVATE);
            preferencesMap.put(spfName, spf);
            return spf;
        }
    }

}
