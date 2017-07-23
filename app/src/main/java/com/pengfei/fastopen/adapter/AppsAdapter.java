package com.pengfei.fastopen.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pengfei.fastopen.R;
import com.pengfei.fastopen.activity.BaseActivity;
import com.pengfei.fastopen.activity.BaseApplication;
import com.pengfei.fastopen.activity.MainActivity;
import com.pengfei.fastopen.adapter.base.CommonAdapter;
import com.pengfei.fastopen.adapter.base.ViewHolder;
import com.pengfei.fastopen.entity.AppBean;
import com.pengfei.fastopen.thread.CopyAppToLocalThread;
import com.pengfei.fastopen.utils.IntentUtils;

import java.io.File;
import java.util.List;

public class AppsAdapter extends CommonAdapter<AppBean> {

    public AppsAdapter(Context montext, List<AppBean> mList, int mItemLayoutResorce) {
        super(montext, mList, mItemLayoutResorce);
    }

    @Override
    public void bindItemDatas(ViewHolder holder, final AppBean bean) {
        ImageView appIcon = (ImageView) holder.getView(R.id.iv_app_icon);
        TextView appName = (TextView) holder.getView(R.id.tv_app_name);
        TextView appPackage = (TextView) holder.getView(R.id.tv_app_package);
        ImageButton menu = (ImageButton) holder.getView(R.id.ib_item_menu);
        ImageView top = (ImageView) holder.getView(R.id.iv_top);
        top.setVisibility(bean.isTop() ? View.VISIBLE : View.INVISIBLE);
        appIcon.setImageDrawable(bean.getAppIcon());
        if (bean.isNewInstall()) {
            appName.setText(getShowItemSpannable(bean.getAppName(), " (最近安装)"));
        } else {
            if (bean.isNewUpdate()) {
                appName.setText(getShowItemSpannable(bean.getAppName(), " (最近更新)"));
            } else {
                appName.setText(bean.getAppName());
            }
        }
        appPackage.setText(bean.getPackageName());
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PopupMenu menu = new PopupMenu(mContext, v);
                menu.inflate(R.menu.app_item_menu);
                initMenuVisible(menu.getMenu(), bean);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_open_app:
                                mContext.startActivity(bean.getStartIntent());
                                return true;
                            case R.id.menu_share:
                                IntentUtils.shareFile(mContext, new File(bean.getAppFileDir()), "分享 " + bean.getAppName());
                                return true;
                            case R.id.menu_out:
                                doOutApp(bean);
                                return true;
                            case R.id.menu_uninstall:
                                IntentUtils.uninstallApp(mContext, bean.getPackageName());
                                return true;
                            case R.id.menu_show_market:
                                IntentUtils.openApplicationMarket(mContext, bean.getPackageName());
                                return true;
                            case R.id.menu_show_system:
                                IntentUtils.openSystemApp(mContext, bean.getPackageName());
                                return true;
                            case R.id.menu_top:
                                if (item.getTitle().equals(mContext.getText(R.string.menu_top))) {
                                    bean.setIsTop(true);
                                } else {
                                    bean.setIsTop(false);
                                }
                                ((BaseActivity) mContext).showToast("已" + item.getTitle());
                                ((MainActivity) mContext).sortApps();
                                return true;
                        }
                        return false;
                    }
                });
                menu.show();
            }
        });
    }

    private CharSequence getShowItemSpannable(String appName, String s) {
        SpannableString showStr = new SpannableString(appName + s);
        showStr.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.colorAccent)),
                appName.length(), appName.length() + s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        showStr.setSpan(new RelativeSizeSpan(0.7f),
                appName.length(), appName.length() + s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return showStr;

    }

    //初始化menu中的item是否可见
    private void initMenuVisible(Menu menu, AppBean bean) {
        if (bean.getStartIntent() == null) {
            menu.findItem(R.id.menu_open_app).setVisible(false);
            menu.findItem(R.id.menu_show_market).setVisible(false);
        }
        if (bean.getAppFileDir() == null) {
            menu.findItem(R.id.menu_share).setVisible(false);
            menu.findItem(R.id.menu_out).setVisible(false);
        }
        if (!bean.isCanUninstall()) {//不能被卸载
            menu.findItem(R.id.menu_uninstall).setVisible(false);
        }
        if (bean.isTop()) {
            menu.findItem(R.id.menu_top).setTitle(mContext.getString(R.string.menu_un_top));
        } else {
            menu.findItem(R.id.menu_top).setTitle(mContext.getString(R.string.menu_top));
        }
    }

    //导出App
    private void doOutApp(AppBean bean) {
        ((MainActivity) mContext).showProDialog(0, null, "正在导出...");
        Thread outThread = new CopyAppToLocalThread(bean, new CopyAppToLocalThread.CallBack() {
            @Override
            public void onCallBack(final boolean isCreated, File targetFile, final String errorMessage) {
                ((MainActivity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity) mContext).hintProDialog();
                        if (isCreated) {
                            ((MainActivity) mContext).showToast("APP导出成功");
                        } else {
                            ((MainActivity) mContext).showToast(errorMessage);
                        }
                    }
                });

            }
        });
        BaseApplication.getInstance().executeThread(outThread);
    }

}
