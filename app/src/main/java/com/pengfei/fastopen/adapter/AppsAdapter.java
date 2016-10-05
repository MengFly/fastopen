package com.pengfei.fastopen.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.pengfei.fastopen.R;
import com.pengfei.fastopen.entity.AppBean;

import java.util.ArrayList;
import java.util.List;

public class AppsAdapter extends CommonAdapter<AppBean> {


    private List<AppBean> showList;

    public AppsAdapter(Context montext, List<AppBean> mList, int mItemLayoutResorce) {
        super(montext, mList, mItemLayoutResorce);
        showList = new ArrayList<>(mList);
    }

    @Override
    public void bindItemDatas(ViewHolder holder, AppBean bean) {
        ImageView appIcon = (ImageView) holder.getView(R.id.item_app_icon_iv);
        TextView appName = (TextView) holder.getView(R.id.item_app_title_tv);
        TextView appLastOpenTime = (TextView) holder.getView(R.id.item_app_last_time_tv);
        appIcon.setImageDrawable(bean.getAppIcon());
        appName.setText(bean.getAppName());
        appLastOpenTime.setText(bean.getLastOpenTime());
    }

}
