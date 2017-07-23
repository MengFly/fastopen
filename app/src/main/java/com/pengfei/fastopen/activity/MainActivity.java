package com.pengfei.fastopen.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.pengfei.fastopen.R;
import com.pengfei.fastopen.adapter.AppsAdapter;
import com.pengfei.fastopen.adapter.SpinnerAdapter;
import com.pengfei.fastopen.dialog.AppBeanDialog;
import com.pengfei.fastopen.entity.AppBean;
import com.pengfei.fastopen.thread.GetAppThread;
import com.pengfei.fastopen.utils.AppManager;
import com.pengfei.fastopen.utils.ClipbordUtils;
import com.pengfei.fastopen.utils.DialogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends BaseActivity implements
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        AdapterView.OnItemSelectedListener,
        View.OnTouchListener,
        SearchView.OnQueryTextListener, AbsListView.OnScrollListener {

    private ListView totalAppsLV;//所有的App(线性布局)
    private TextView appCount;
    private View headView;

    private List<AppBean> totalApps;//所有的App的数据
    private List<AppBean> showApps;//用于显示的App数据
    private AppsAdapter appAdapter;
    SearchView searchView;
    //用于选择进行筛选的App的类型
    private AppCompatSpinner appStyle;

    int lastPress = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_main);
        totalAppsLV = (ListView) findViewById(R.id.lv_apps);
        searchView = (SearchView) findViewById(R.id.sv_);
        appStyle = (AppCompatSpinner) findViewById(R.id.as_app_style);
        initListView();
        setPress2Exit(true);
        initDatas();
        initListener();
    }

    private void initListView() {
        appCount = (TextView) getLayoutInflater().inflate(R.layout.item_bottom, null, false);
        headView = getLayoutInflater().inflate(R.layout.top_header, null, false);
        totalAppsLV.addHeaderView(headView);
        totalAppsLV.addFooterView(appCount);
    }

    private void initListener() {
        totalAppsLV.setOnItemClickListener(this);
        totalAppsLV.setOnItemLongClickListener(this);
        appStyle.setOnItemSelectedListener(this);
        searchView.setOnQueryTextListener(this);
        totalAppsLV.setOnTouchListener(this);
    }

    private void initDatas() {
        totalApps = new ArrayList<>();
        showApps = new ArrayList<>();
        appAdapter = new AppsAdapter(mContext, showApps, R.layout.item_app_linear);
        totalAppsLV.setAdapter(appAdapter);
        appStyle.setAdapter(new SpinnerAdapter(mContext));
        GetAppThread thread = new GetAppThread(new GetAppThread.CallBack() {
            @Override
            public void onCallback(List<AppBean> beanList) {
                addAppList(beanList);
            }
        });
        BaseApplication.getInstance().executeThread(thread);
    }

    // 向列表里面添加App
    private void addAppList(final List<AppBean> beanList) {
        if (!isFinishing() && appAdapter != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    totalApps.addAll(beanList);
                    refreshAppList(beanList, false);
                    appCount.setText("App个数 ： " + String.valueOf(showApps.size()) + "个");
                }
            });
        }
    }

    private void hideSearchView() {
        if (searchView.getAlpha() == 1.0f) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(searchView, "alpha", 1.0f, 0.0f);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    searchView.setVisibility(View.INVISIBLE);
                    headView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animation.setDuration(1000);
            animation.start();
        }
    }

    private void showSearchView() {
        if (searchView.getAlpha() == 0.0f) {
            ObjectAnimator animation = ObjectAnimator.ofFloat(searchView, "alpha", 0.0f, 1.0f);
            animation.setDuration(1000);
            animation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    searchView.setVisibility(View.VISIBLE);
                    headView.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animation.setInterpolator(new AccelerateInterpolator());
            animation.start();
        }
    }

    //刷新显示的App
    private void refreshAppList(List<AppBean> beanList, boolean isClear) {
        if (isClear) {
            showApps.clear();
        }
        for (AppBean bean : beanList) {
            if (appStyle.getSelectedItemPosition() == 1 && bean.isSystem()) {
                //系统应用
                showApps.add(bean);
            } else if (appStyle.getSelectedItemPosition() == 0 && !bean.isSystem()) {
                //用户应用
                showApps.add(bean);
            } else if (appStyle.getSelectedItemPosition() == 2) {
                //全部应用
                showApps.add(bean);
            } else if (appStyle.getSelectedItemPosition() == 3 && bean.getStartIntent() != null) {
                //可打开的应用
                showApps.add(bean);
            } else if (appStyle.getSelectedItemPosition() == 4 && bean.isCanUninstall()) {
                //可卸载的应用
                showApps.add(bean);
            }
        }
        sortApps();
        appCount.setText("App个数 ： " + String.valueOf(showApps.size()) + "个");
    }

    // 设置要显示的App列表
    private void setApps(List<AppBean> apps) {
        showApps.clear();
        showApps.addAll(apps);
        sortApps();
        appCount.setText("App个数 ： " + String.valueOf(showApps.size()) + "个");
    }

    @Override
    protected void onRestart() {
        if (appStyle != null && appStyle.getSelectedItemPosition() > 4) {
            appStyle.setSelection(0);
        }
        super.onRestart();
    }

    public void sortApps() {
        if (appAdapter != null) {
            Collections.sort(showApps);
            appAdapter.notifyDataSetChanged();
        }
    }

    //====================================== 这里是listener的重写方法
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position < 1 || position > showApps.size()) {
            return;
        }
        AppBean bean = showApps.get(position - 1);
        new AppBeanDialog(mContext, bean).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int select = position - 1;
        if (select < 0 || select >= showApps.size()) return true;
        ClipbordUtils.setClipBordText(showApps.get(select).getAppName());
        showToast("App 名称已经复制");
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position <= 4) {
            refreshAppList(totalApps, true);
        } else {
            //应用设置
            if (position == 5) {
                startActivity(new Intent(mContext, SettingsActivity.class));
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastPress = (int) event.getY();
                return false;
            case MotionEvent.ACTION_MOVE:
                if (event.getY() - lastPress >= 10) {
                    showSearchView();
                } else if (event.getY() - lastPress <= -20) {
                    hideSearchView();
                }
                return false;
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String filterStr = newText.trim();
        if (TextUtils.isEmpty(filterStr)) {
            refreshAppList(totalApps, true);
        } else {
            setApps(AppManager.getFilterApps(filterStr, totalApps));
        }
        return true;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem == 1) {
            showSearchView();
        }
    }
}
