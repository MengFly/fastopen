package com.pengfei.fastopen.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;

import com.pengfei.fastopen.R;
import com.pengfei.fastopen.adapter.AppsAdapter;
import com.pengfei.fastopen.dao.DialogTool;
import com.pengfei.fastopen.dao.SettingManager;
import com.pengfei.fastopen.dao.ShareTool;
import com.pengfei.fastopen.entity.AppBean;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private SettingManager settingManager;

    private ListView totalAppsLV;//所有的App(线性布局)
    private GridView totalAppsGv;

    private List<AppBean> totalApps;//所有的App的数据
    private List<AppBean> showApps;//用于显示的App数据

    private AppsAdapter totalAppAdapter;
    private AppsAdapter getTotalAppAdapterGrid;//显示桌面布局的Adapter

    private SearchView searchAppSV;//搜索App

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_main);
        settingManager = SettingManager.getInstance();
        searchAppSV = (SearchView) findViewById(R.id.act_main_search_app_et);
        totalAppsLV = (ListView) findViewById(R.id.act_main_apps_lv);
        totalAppsGv = (GridView) findViewById(R.id.act_main_apps_gv);
        getShowList(settingManager.getShowAppView()).setFocusable(false);
        initDatas();
        initListener();
    }

    private void initListener() {
        totalAppsLV.setOnItemClickListener(this);
        totalAppsLV.setOnItemLongClickListener(this);
        totalAppsLV.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        totalAppsLV.setOnTouchListener(new ShowOrHideActionBarListener());

        totalAppsGv.setOnItemClickListener(this);
        totalAppsGv.setOnItemLongClickListener(this);
        totalAppsGv.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        totalAppsGv.setOnTouchListener(new ShowOrHideActionBarListener());

        searchAppSV.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getShowList(settingManager.getShowAppView()).clearChoices();
                String filterStr = newText.trim();
                setApps(getFilterApps(filterStr), false, true);
                return true;
            }
        });
    }

    //获取筛选后的App
    private List<AppBean> getFilterApps(String filterStr) {
        List<AppBean> filterApps = new ArrayList<>();
        for (AppBean appBean : totalApps) {
            if (appBean.getAppName().toUpperCase().contains(filterStr.toUpperCase())) {
                filterApps.add(appBean);
            }
        }
        return filterApps;
    }

    /**
     * 获取显示的视图
     */
    public AbsListView getShowList(String showAppView) {
        if (showAppView.equals(SettingManager.SHOW_APP_LIST)) {
            return totalAppsLV;
        } else {
            return totalAppsGv;
        }
    }

    public AppsAdapter getShowAdapter(String showAppView) {
        if (showAppView.equals(SettingManager.SHOW_APP_LIST)) {
            return totalAppAdapter;
        } else {
            return getTotalAppAdapterGrid;
        }
    }

    //显示桌面布局
    private void showAsGridView() {
        totalAppsLV.setVisibility(View.GONE);
        totalAppsGv.setVisibility(View.VISIBLE);
    }

    //显示线性布局
    private void showAsLinearView() {
        totalAppsGv.setVisibility(View.GONE);
        totalAppsLV.setVisibility(View.VISIBLE);
    }

    //更新本地存储的应用启动次数
    private void updateLocalDatas(AppBean bean) {
        Date date = new Date();
        bean.setLastOpenTime(date);
        int count = bean.getUseCount() + 1;//在之前启动的次数上面加一
        bean.setUseCount(count);
        Collections.sort(showApps);
        getShowList(settingManager.getShowAppView()).setItemChecked(showApps.indexOf(bean), true);
        getShowAdapter(settingManager.getShowAppView()).notifyDataSetChanged();
    }


    private void initDatas() {
        totalApps = new ArrayList<>();
        showApps = new ArrayList<>();
        totalAppAdapter = new AppsAdapter(mContext, showApps, R.layout.item_app_linear);
        getTotalAppAdapterGrid = new AppsAdapter(mContext, showApps, R.layout.item_app_grid);
        totalAppsLV.setAdapter(totalAppAdapter);
        totalAppsGv.setAdapter(getTotalAppAdapterGrid);
        if (settingManager.getShowAppView().equals(SettingManager.SHOW_APP_LIST)) {
            showAsLinearView();
        } else {
            showAsGridView();
        }
        new GetAppTask().execute();
//        openAppThread();//开启一个线程去获取App
    }

//    private void openAppThread() {
//        new Thread() {
//            @Override
//            public void run() {
//                totalApps = AppManager.getApps(mContext);
//                setApps(totalApps);
//            }
//        }.start();
//    }


    // 设置App数据
    private void setApps(List<AppBean> apps, boolean isAddEnd, boolean isSort) {
        if (!isAddEnd) {
            showApps.clear();
        }
        showApps.addAll(apps);
        if (isSort) {
            Collections.sort(showApps);
        }
        getShowAdapter(settingManager.getShowAppView()).notifyDataSetChanged();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (settingManager.getShowAppView().equals(SettingManager.SHOW_APP_LIST)) {
            showAsLinearView();
        } else {
            showAsGridView();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_search:
                hintActionBar();
                searchAppSV.onActionViewExpanded();
                return true;
            case R.id.menu_main_setting:
                Intent intent = new Intent(mContext, BaseSettingActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_main_show_as_linear://显示线性布局
                if (settingManager.getShowAppView().equals(SettingManager.SHOW_APP_GRID)) {
                    showAsLinearView();
                    settingManager.setShowAppView(SettingManager.SHOW_APP_LIST);
                }
                return true;
            case R.id.menu_main_show_as_grid://显示桌面布局
                if (settingManager.getShowAppView().equals(SettingManager.SHOW_APP_LIST)) {
                    showAsGridView();
                    settingManager.setShowAppView(SettingManager.SHOW_APP_GRID);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                exit2PressBack();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean isExit = false;

    //双击退出应用
    private void exit2PressBack() {
        if (!isExit) {
            showToast("再按一次退出应用");
            isExit = true;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            BaseApplication.getInstance().exit();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        AppBean bean = showApps.get(position);
        updateLocalDatas(bean);//更新本地信息
        Intent intent = bean.getStartIntent();
        startActivity(intent);
        if (settingManager.isAutoExit()) {//如果设置了自动退出，那么就在开启其他应用的时候退出应用
            BaseApplication.getInstance().exit();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final AppBean app = showApps.get(position); //分享App
        DialogTool.showChoseItemDialog(mContext, R.array.main_item_long_click, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ShareTool.shareFile(mContext, new File(app.getAppFileDir()), "分享APP : " + app.getAppName());
                        break;
                    case 1:
                        DialogTool.showTextDialog(mContext, app.getAppIcon(), app.getAppName(), app.toString());
                        break;
                }
            }
        });
        return true;
    }


    private class ShowOrHideActionBarListener implements View.OnTouchListener {
        int downY;
        int moveY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downY = (int) event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    moveY = (int) event.getRawY();
                    if (moveY - downY > 60) {
                        showActionBar();
                        downY = moveY;
                    } else if (moveY - downY < -60) {
                        hintActionBar();
                        downY = moveY;
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    downY = 0;
                    moveY = 0;
            }
            return false;
        }
    }

    private void hintActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void showActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            searchAppSV.onActionViewCollapsed();
        }
    }

    /**
     * 获取App的AsyncTask
     */
    private class GetAppTask extends AsyncTask<Void, List<AppBean>, List<AppBean>> {

        @Override
        protected void onProgressUpdate(List<AppBean>... values) {
            setApps(values[0], true, false);
        }

        @Override
        protected List<AppBean> doInBackground(Void... params) {
            PackageManager packageManager = getPackageManager();
            List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
            int appCount = 0;
            ArrayList<AppBean> showApps = new ArrayList<>();//要一步一步显示的Apps
            ArrayList<AppBean> totalApps = new ArrayList<>();//总共的Apps
            for (PackageInfo info : packageInfos) {
                Intent startIntent = packageManager.getLaunchIntentForPackage(info.packageName);
                if (startIntent != null) {
                    AppBean bean = getAppFromPackAgeInfo(info, packageManager);
                    bean.setStartIntent(startIntent);
                    showApps.add(bean);
                    totalApps.add(bean);
                    if (appCount == 5) {//每获取5个应用刷新一下视图
                        publishProgress(new ArrayList<>(showApps));
                        showApps.clear();
                        appCount = 0;
                    }
                    appCount++;
                    if (isCancelled()) break;
                }
            }
            return totalApps;
        }

        private AppBean getAppFromPackAgeInfo(PackageInfo info, PackageManager packageManager) {
            AppBean bean = new AppBean(info);
            String appName = getApplicationName(info.packageName, packageManager);
            Drawable appIcon = info.applicationInfo.loadIcon(packageManager);
            bean.setAppIcon(appIcon);
            bean.setAppName(appName);
            return bean;
        }

        @Override
        protected void onPostExecute(List<AppBean> appBeen) {
            super.onPostExecute(appBeen);
            totalApps.addAll(appBeen);
            setApps(appBeen, false, true);
        }

        /**
         * 获得应用名称
         */
        public String getApplicationName(String packName, PackageManager packageManager) {
            String applicationName = null;
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packName, 0);
                applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return applicationName;
        }

    }
}
