package com.pengfei.fastopen.dialog;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.widget.ImageView;
import android.widget.TextView;

import com.pengfei.fastopen.R;
import com.pengfei.fastopen.entity.AppBean;
import com.pengfei.fastopen.utils.DateTool;

import java.io.File;
import java.util.Date;
import java.util.Locale;

/**
 * 显示App信息的Dialog
 * Created by mengfei on 2017/7/20.
 */
public class AppBeanDialog extends AppCompatDialog {
    public AppBeanDialog(Context context, AppBean bean) {
        super(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        initView(bean);
    }

    private void initView(AppBean bean) {
        setContentView(R.layout.dialog_app);
        ((ImageView) findViewById(R.id.iv_app_icon)).setImageDrawable(bean.getAppIcon());
        ((TextView) findViewById(R.id.tv_app_name)).setText(bean.getAppName());
        ((TextView) findViewById(R.id.tv_app_package)).setText(bean.getPackageName());
        ((TextView) findViewById(R.id.tv_version)).setText(getShowVersion(bean));
        ((TextView) findViewById(R.id.tv_file_length)).setText(getShowLength(bean));
        ((TextView) findViewById(R.id.tv_create_date)).setText(getShowDate("创建时间 ", bean.getFirstInstallTime()));
        ((TextView) findViewById(R.id.tv_update_date)).setText(getShowDate("更新时间 ", bean.getLastUpdateTime()));
    }

    private CharSequence getShowDate(String s, String date) {
        return s + date;
    }

    private CharSequence getShowLength(AppBean bean) {
        long length = new File(bean.getAppFileDir()).length();
        if (length > 1024 * 1024) {
            return "文件大小 " + String.format(Locale.CHINA, "%2.2f", length / 1024.0 / 1024) + "M";
        } else {
            return "文件大小 " + String.format(Locale.CHINA, "%2.2f", length / 1024.0) + "K";
        }
    }

    private CharSequence getShowVersion(AppBean bean) {
        return "版本 " + bean.getVersionName();
    }
}
