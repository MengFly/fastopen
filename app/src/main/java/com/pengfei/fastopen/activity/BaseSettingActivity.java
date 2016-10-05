package com.pengfei.fastopen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.pengfei.fastopen.R;
import com.pengfei.fastopen.dao.SettingManager;

/**
 * 设置界面
 * Created by mengfei on 2016/8/14.
 */
public class BaseSettingActivity extends BaseActivity {

    private SettingManager settingManager;
    private Switch isAutoExitS;

    private RadioGroup showViewGroup;
    private RadioButton showAsLinear;
    private RadioButton showAsGrid;

    private TextView aboutAppTV;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_base_setting);
        settingManager = SettingManager.getInstance();
        aboutAppTV = (TextView) findViewById(R.id.act_base_setting_about_app);
        isAutoExitS = (Switch) findViewById(R.id.act_base_setting_is_auto_exit);

        showViewGroup = (RadioGroup) findViewById(R.id.act_base_setting_show_view_rg);
        showAsLinear = (RadioButton) findViewById(R.id.act_base_setting_linear_rb);
        showAsGrid = (RadioButton) findViewById(R.id.act_base_setting_grid_rb);

        initDatas();
        initListener();
    }

    private void initDatas() {
        isAutoExitS.setChecked(settingManager.isAutoExit());
        if (settingManager.getShowAppView().equals(SettingManager.SHOW_APP_LIST)) {
            showAsLinear.setChecked(true);
        } else {
            showAsGrid.setChecked(true);
        }
    }


    private void initListener() {
        aboutAppTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AboutAppActivity.class);
                startActivity(intent);
            }
        });
        isAutoExitS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingManager.setIsAutoExit(isChecked);
            }
        });
        showViewGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.act_base_setting_linear_rb) {
                    settingManager.setShowAppView(SettingManager.SHOW_APP_LIST);
                } else {
                    settingManager.setShowAppView(SettingManager.SHOW_APP_GRID);
                }
            }
        });
    }

}
