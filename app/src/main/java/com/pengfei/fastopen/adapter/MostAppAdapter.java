package com.pengfei.fastopen.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.pengfei.fastopen.R;
import com.pengfei.fastopen.entity.AppBean;

import java.util.List;

/**
 * Created by mengfei on 2016/8/13.
 */
public class MostAppAdapter extends CommonAdapter<AppBean> {

    public MostAppAdapter(Context montext, List<AppBean> mList, int mItemLayoutResorce) {
        super(montext, mList, mItemLayoutResorce);
    }

    @Override
    public void bindItemDatas(ViewHolder holder, AppBean bean) {
        ImageView appIcon = (ImageView) holder.getView(R.id.item_most_app_icon_iv);
        TextView appName = (TextView) holder.getView(R.id.item_most_app_title_tv);
        appIcon.setImageDrawable(bean.getAppIcon());
        appName.setText(bean.getAppName());
    }
}
