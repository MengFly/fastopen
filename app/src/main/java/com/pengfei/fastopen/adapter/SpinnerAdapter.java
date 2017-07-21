package com.pengfei.fastopen.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.pengfei.fastopen.R;
import com.pengfei.fastopen.adapter.base.CommonAdapter;
import com.pengfei.fastopen.adapter.base.ViewHolder;
import com.pengfei.fastopen.entity.SpinnerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于在主界面显示的Spinner的Adapter，
 * 这个Spinner主要用1>筛选应用 2>作为menu
 * Created by mengfei on 2017/7/21.
 */
public class SpinnerAdapter extends CommonAdapter<SpinnerEntity> {

    public SpinnerAdapter(Context mContext) {
        super(mContext, getMenuList(), R.layout.item_spinner);
    }

    @Override
    public void bindItemDatas(ViewHolder holder, SpinnerEntity bean) {
        TextView tv = (TextView) holder.getView(R.id.tv_spinner);
        Drawable icon = mContext.getResources().getDrawable(bean.getIconRes());
        icon.setBounds(0, 0, 36, 36);
        tv.setCompoundDrawables(icon, null, null, null);
        tv.setText(bean.getShowStr());
    }

    private static List<SpinnerEntity> getMenuList() {
        List<SpinnerEntity> spinnerList = new ArrayList<>();
        SpinnerEntity userApp = new SpinnerEntity("用户应用", R.drawable.ic_user);
        spinnerList.add(userApp);

        SpinnerEntity systemApp = new SpinnerEntity("系统应用", R.drawable.ic_system);
        spinnerList.add(systemApp);

        SpinnerEntity allApp = new SpinnerEntity("全部应用", R.drawable.ic_install);
        spinnerList.add(allApp);

        SpinnerEntity canOpenApp = new SpinnerEntity("可打开应用", R.drawable.ic_open);
        spinnerList.add(canOpenApp);

        SpinnerEntity notOpenApp = new SpinnerEntity("不可打开应用", R.drawable.ic_not);
        spinnerList.add(notOpenApp);

        SpinnerEntity showExtra = new SpinnerEntity("查看导出apk", R.drawable.ic_extra_doc);
        spinnerList.add(showExtra);

        SpinnerEntity appSettings = new SpinnerEntity("应用设置", R.drawable.ic_settings);
        spinnerList.add(appSettings);

        return spinnerList;
    }
}
