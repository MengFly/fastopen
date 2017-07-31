package com.pengfei.fastopen.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.pengfei.fastopen.Constant;
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
                switch (item.itemName) {
                    case "评价应用":
                        IntentUtils.openApplicationMarket(mContext, getPackageName());
                        return true;
                    case "查看应用源代码":
                        IntentUtils.openBrowser(mContext, Constant.SOURCE);
                        return true;
                    default:
                        return false;

                }
            }
        });
        initData();
    }

    private void initData() {
        settingLists = new ArrayList<>();
        SettingAdapter.SettingItem extraApks = new SettingAdapter.SettingItem<>(getString(R.string.see_extra_apk), R.drawable.ic_extra_doc, getExtraList(), true);
        SettingAdapter.SettingItem unInstallApp = new SettingAdapter.SettingItem<>(getString(R.string.see_uninstall_app), R.drawable.ic_uninstall, SaveAppDBManager.getUnInstallApp(), true);
        SettingAdapter.SettingItem evaluate = new SettingAdapter.SettingItem<>(getString(R.string.evaluate_app), R.drawable.ic_evaluate, Collections.emptyList(), false);
        SettingAdapter.SettingItem seeSource = new SettingAdapter.SettingItem<>(getString(R.string.see_source), R.drawable.ic_source, Collections.emptyList(), false);
        SettingAdapter.SettingItem license = new SettingAdapter.SettingItem<>(getString(R.string.license), R.drawable.ic_license, getLicenseList(), true);
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
        licenses.add(new LicenseEntity("LitePal for Android", getString(R.string.linceseLitePal), "https://github.com/LitePalFramework/LitePal"));
        return licenses;
    }
}
