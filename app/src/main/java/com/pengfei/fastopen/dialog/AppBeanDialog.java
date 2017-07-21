package com.pengfei.fastopen.dialog;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.pengfei.fastopen.R;
import com.pengfei.fastopen.adapter.ShowAppInfoAdapter;
import com.pengfei.fastopen.entity.AppBean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 显示App信息的Dialog
 * Created by mengfei on 2017/7/20.
 */
public class AppBeanDialog extends AppCompatDialog {
    //用于显示App中的所有信息
    private ExpandableListView appELV;

    public AppBeanDialog(Context context, AppBean bean) {
        super(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        initView(bean);
    }

    private void initView(AppBean bean) {
        setContentView(R.layout.dialog_app);
        ((ImageView) findViewById(R.id.iv_app_icon)).setImageDrawable(bean.getAppIcon());
        ((TextView) findViewById(R.id.tv_app_name)).setText(bean.getAppName());
        ((TextView) findViewById(R.id.tv_app_package)).setText(bean.getPackageName());
        appELV = ((ExpandableListView) findViewById(R.id.elv_app_info));
        appELV.setGroupIndicator(null);
        appELV.setAdapter(new ShowAppInfoAdapter(getContext(), getShowList(bean)));
    }

    //获取到显示信息的List
    private List<ShowAppInfoAdapter.ParentItem> getShowList(AppBean bean) {
        List<ShowAppInfoAdapter.ParentItem> showList = new ArrayList<>();
        ShowAppInfoAdapter.ParentItem version = new ShowAppInfoAdapter.ParentItem(getShowVersion(bean), R.drawable.ic_version, null);
        ShowAppInfoAdapter.ParentItem length = new ShowAppInfoAdapter.ParentItem(getShowLength(bean), R.drawable.ic_file_length, null);
        ShowAppInfoAdapter.ParentItem createTime = new ShowAppInfoAdapter.ParentItem(getShowDate("创建时间 ", bean.getFirstInstallTime()), R.drawable.ic_install, null);
        ShowAppInfoAdapter.ParentItem updateTime = new ShowAppInfoAdapter.ParentItem(getShowDate("更新时间 ", bean.getLastUpdateTime()), R.drawable.ic_update, null);
        ShowAppInfoAdapter.ParentItem permission = new ShowAppInfoAdapter.ParentItem("应用权限", R.drawable.ic_permission, bean.requestPermission());
        showList.add(version);
        showList.add(length);
        showList.add(createTime);
        showList.add(updateTime);
        showList.add(permission);
        return showList;
    }

    //根据日期获取要显示的日期的文字
    private String getShowDate(String s, String date) {
        return s + date;
    }

    // 获取到文件的大小
    private String getShowLength(AppBean bean) {
        long length = new File(bean.getAppFileDir()).length();
        if (length > 1024 * 1024) {
            return "文件大小 " + String.format(Locale.CHINA, "%2.2f", length / 1024.0 / 1024) + "M";
        } else {
            return "文件大小 " + String.format(Locale.CHINA, "%2.2f", length / 1024.0) + "K";
        }
    }

    private String getShowVersion(AppBean bean) {
        return "版本 " + bean.getVersionName();
    }
}
