package com.pengfei.fastopen.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.pengfei.fastopen.R;
import com.pengfei.fastopen.activity.BaseActivity;
import com.pengfei.fastopen.db.SaveAppDBManager;
import com.pengfei.fastopen.entity.LicenseEntity;
import com.pengfei.fastopen.entity.SaveAppEntity;
import com.pengfei.fastopen.utils.AppFileManager;
import com.pengfei.fastopen.utils.DialogUtils;
import com.pengfei.fastopen.utils.FileUtils;
import com.pengfei.fastopen.utils.ImageUtils;
import com.pengfei.fastopen.utils.IntentUtils;

import java.io.File;
import java.util.List;

/**
 * 设置界面的Adapter，用于展示设置界面的数据
 * Created by mengfei on 2017/7/23.
 */
public class SettingAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<SettingItem> list;

    public SettingAdapter(Context context, List<SettingItem> lists) {
        this.context = context;
        this.list = lists;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return list.get(groupPosition).childItems == null ? 0 : list.get(groupPosition).childItems.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return list.get(groupPosition).childItems.get(childPosition);
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
        SettingItem item = list.get(groupPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dialog_item, parent, false);
        }
        TextView itemTv = (TextView) convertView.findViewById(R.id.tv_item);
        Drawable itemIcon = context.getResources().getDrawable(item.itemIconRes);
        itemIcon.setBounds(0, 0, 48, 40);
        itemTv.setCompoundDrawables(itemIcon, null, null, null);
        itemTv.setText(item.itemName);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Object child = list.get(groupPosition).childItems.get(childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_dialog_item_child, parent, false);
        }
        final TextView item = (TextView) convertView.findViewById(R.id.tv_item_child);
        item.setPadding(30, 10, 10, 10);
        if (child instanceof String && child.toString().endsWith(".apk")) {
            childExtraApk(convertView, child, item, groupPosition, childPosition);
        } else if (child instanceof SaveAppEntity) {
            childUninstallApp(convertView, groupPosition, childPosition, (SaveAppEntity) child, item);
        } else if (child instanceof LicenseEntity) {
            childLicense(convertView, (LicenseEntity) child, item);
        } else {
            item.setText(child.toString());
        }
        return convertView;
    }

    //当child是License的时候
    private void childLicense(View convertView, final LicenseEntity child, TextView item) {
        item.setText(child.title);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(child.title);
                builder.setMessage(child.desc);
                builder.setPositiveButton("关闭", null);
                builder.setNegativeButton("查看", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IntentUtils.openBrowser(context, child.url);
                    }
                });
                builder.create().show();
            }
        });
    }

    // 当选项是道出的APK的是偶的操作
    private void childExtraApk(View convertView, final Object child, TextView item, final int groupPosition, final int childPosition) {
        item.setText(child.toString());
        Drawable itemIcon = context.getResources().getDrawable(R.drawable.ic_apk);
        itemIcon.setBounds(0, 0, 36, 36);
        item.setCompoundDrawables(itemIcon, null, null, null);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] items = new String[]{"安装", "删除"};
                DialogUtils.getItemsDialog(child.toString(), context, items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                IntentUtils.installApp(context, new File(AppFileManager.getExtraAppDir(), child.toString()));
                                break;
                            case 1:
                                if (!FileUtils.deleteFile(new File(AppFileManager.getExtraAppDir(), child.toString()))) {
                                    ((BaseActivity) context).showToast("删除失败");
                                } else {
                                    list.get(groupPosition).childItems.remove(childPosition);
                                    list.get(groupPosition).updateName();
                                    notifyDataSetChanged();
                                    ((BaseActivity) context).showToast("文件已删除");
                                }
                                break;
                        }
                    }
                }).create().show();
            }
        });
    }

    //当显示的是已经卸载的应用的时候的操作
    private void childUninstallApp(View convertView, final int groupPosition, final int childPosition, final SaveAppEntity child, TextView item) {
        item.setText(child.getName());
        byte[] icon = child.getIcon();
        if (icon != null) {
            Bitmap bitmap = ImageUtils.changeByteToBitmap(icon);
            Drawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
            drawable.setBounds(0, 0, 36, 36);
            item.setCompoundDrawables(drawable, null, null, null);
        }
        final String[] items = new String[]{"去应用商店中查看", "删除记录"};
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.getItemsDialog(child.getName(), context, items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                IntentUtils.openApplicationMarket(context, child.getPackageName());
                                break;
                            case 1:
                                SaveAppDBManager.deleteApp(child.getPackageName());
                                list.get(groupPosition).childItems.remove(childPosition);
                                notifyDataSetChanged();
                                list.get(groupPosition).updateName();
                                Toast.makeText(context, "记录删除成功", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).create().show();
            }
        });
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public static class SettingItem<T> {
        public String itemName;
        int itemIconRes;
        List<T> childItems;

        public SettingItem(String itemName, int itemIconRes, List<T> childItems, boolean isShowChildCount) {
            if (isShowChildCount) {
                this.itemName = itemName + " (" + childItems.size() + ")";
            } else {
                this.itemName = itemName;
            }
            this.itemIconRes = itemIconRes;
            this.childItems = childItems;
        }

        void updateName() {
            itemName = itemName.substring(0, itemName.indexOf("(")) + "(" + childItems.size() + ")";
        }
    }
}
