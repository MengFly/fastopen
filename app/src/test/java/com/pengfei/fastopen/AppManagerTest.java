package com.pengfei.fastopen;

import com.pengfei.fastopen.activity.BaseApplication;
import com.pengfei.fastopen.utils.AppManager;
import com.pengfei.fastopen.entity.AppBean;

import org.junit.Test;

import java.util.List;

/**
 * 测试APPManager里面方法的类
 * Created by mengfei on 2016/8/15.
 */
public class AppManagerTest {

    @Test(timeout = 1000)
    public void testGetApp() {
        List<AppBean> apps = AppManager.getApps(BaseApplication.getInstance());
//        Assert.assertNotNull(apps);
    }
}
