package com.pengfei.fastopen.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.pengfei.fastopen.R;
import com.pengfei.fastopen.adapter.SettingAdapter;
import com.pengfei.fastopen.db.SaveAppDBManager;
import com.pengfei.fastopen.entity.LicenseEntity;
import com.pengfei.fastopen.utils.AppFileManager;
import com.pengfei.fastopen.utils.IntentUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 用户设置界面
 * Created by mengfei on 2017/7/23.
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener {

    private ExpandableListView settingsELV;
    private List<SettingAdapter.SettingItem> settingLists;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_settings);
        settingsELV = (ExpandableListView) findViewById(R.id.elv_settings);
        settingsELV.addHeaderView(getLayoutInflater().inflate(R.layout.layout_setting_header, null));
        settingsELV.setGroupIndicator(null);
        settingsELV.setDividerHeight(4);
        settingsELV.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < settingLists.size(); i++) {
                    if (groupPosition != i) {
                        settingsELV.collapseGroup(i);
                    }
                }
            }
        });
        settingsELV.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                SettingAdapter.SettingItem item = settingLists.get(groupPosition);
                if (item.itemName.equals("评价应用")) {
                    IntentUtils.openApplicationMarket(mContext, getPackageName());
                    return true;
                } else if (item.itemName.equals("查看应用源代码")) {
                    IntentUtils.openBrowser(mContext, "https://github.com/mengfly/fastopem");
                    return true;
                } else {
                    return false;
                }
            }
        });
        initData();
    }

    private void initData() {
        settingLists = new ArrayList<>();
        SettingAdapter.SettingItem extraApks = new SettingAdapter.SettingItem<>("查看导出的安装包", R.drawable.ic_extra_doc, getExtraList(), true);
        SettingAdapter.SettingItem unInstallApp = new SettingAdapter.SettingItem<>("查看已卸载的App", R.drawable.ic_uninstall, SaveAppDBManager.getUnInstallApp(), true);
        SettingAdapter.SettingItem evaluate = new SettingAdapter.SettingItem<>("评价应用", R.drawable.ic_evaluate, Collections.emptyList(), false);
        SettingAdapter.SettingItem seeSource = new SettingAdapter.SettingItem<>("查看应用源代码", R.drawable.ic_source, Collections.emptyList(), false);
        SettingAdapter.SettingItem license = new SettingAdapter.SettingItem<>("Lisence", R.drawable.ic_license, getLicenseList(), true);
        settingLists.add(extraApks);
        settingLists.add(unInstallApp);
        settingLists.add(license);
        settingLists.add(seeSource);
        settingLists.add(evaluate);
        SettingAdapter adapter = new SettingAdapter(mContext, settingLists);
        settingsELV.setAdapter(adapter);
    }

    public List<String> getExtraList() {
        List<String> list = new ArrayList<>();
        Collections.addAll(list, AppFileManager.getExtraAppDir().list());
        return list;
    }

    @Override
    public void onClick(View v) {

    }

    public List<LicenseEntity> getLicenseList() {
        List<LicenseEntity> licenses = new ArrayList<>();
        licenses.add(new LicenseEntity("LitePal for Android", "LitePal is an open source Android library that allows developers to use SQLite database extremely easy. You can finish most of the database operations without writing even a SQL statement, including create or upgrade tables, crud operations, aggregate functions, etc. The setup of LitePal is quite simple as well, you can integrate it into your project in less than 5 minutes.", "https://github.com/LitePalFramework/LitePal"));
        return licenses;
    }
}
