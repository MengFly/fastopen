package com.pengfei.fastopen.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.pengfei.fastopen.R;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * 用于显示app信息的ListAdapter
 * Created by mengfei on 2017/7/21.
 */
public class ShowAppInfoAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<ParentItem> parentItems;

    public ShowAppInfoAdapter(Context context, List<ParentItem> parentItems) {
        this.context = context;
        this.parentItems = parentItems;
    }

    @Override
    public int getGroupCount() {
        return parentItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (parentItems.get(groupPosition).childs == null) {
            return 0;
        } else {
            return parentItems.get(groupPosition).childs.size();
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentItems.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return parentItems.get(groupPosition).childs.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentItem item = parentItems.get(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_app_info_parent, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_app_parent);
        tv.setText(item.showMsg);
        Drawable drawable = context.getResources().getDrawable(item.msgIcon);
        drawable.setBounds(0, 0, 32, 32);
        tv.setCompoundDrawables(drawable, null, null, null);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_app_child, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.tv_app_child);
        tv.setText(parentItems.get(groupPosition).childs.get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class ParentItem {

        public ParentItem(String showMsg, int msgIcon, List<String> childs) {
            this.childs = childs;
            this.showMsg = showMsg + (childs == null ? "" : "(" + childs.size() + ")");
            this.msgIcon = msgIcon;
        }

        //要显示的信息
        private String showMsg;
        //要显示的信息的icon
        private int msgIcon;
        //要进行显示的子类
        private List<String> childs;
    }
}
