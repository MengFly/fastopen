package com.pengfei.fastopen.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.Toast;

import com.pengfei.fastopen.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 所有Activity的基类。提供了一些简便的方法来方便调用 在使用这个类的时候，请注意Activity的Theme应该是AppCompat这一系列的主题
 *
 * @author mengfei
 */
public abstract class BaseActivity extends AppCompatActivity {

    private ProgressDialog mPro;
    //是否点击两下退出当前Activity
    private boolean isPress2Exit = false;

    protected Toolbar toolbar;

    /**
     * Activity本身的对象,省的在内部类里面每次都要通过**Activity.this来获取了
     */
    protected Context mContext;

    protected String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        TAG = mContext.getClass().getSimpleName();
        initView(savedInstanceState);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        BaseApplication.getInstance().addActivity(this);
    }

    public void setPress2Exit(boolean press2Exit) {
        isPress2Exit = press2Exit;
    }

    /**
     * 初始化视图的方法<br>
     * 所有的绑定Activity视图的方法都应该在这个方法里面实现
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                           shut down then this Bundle contains the data it most recently
     *                           supplied in {@link #onSaveInstanceState}. <b><i>Note:
     *                           Otherwise it is null.</i></b>
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 显示一个Toast
     *
     * @param toastStr Toast要显示的内容
     */
    public void showToast(String toastStr) {
        Toast.makeText(mContext, toastStr, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        BaseApplication.getInstance().removeActivity(this);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (isPress2Exit)
                    exit2PressBack();
                else
                    super.onKeyDown(keyCode, event);
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

    /**
     * 获取TAG
     */
    protected String getTAG() {
        return this.getClass().getSimpleName();
    }

    /**
     * 显示一个提示等待框
     *
     * @param logoRes 等待框要显示的logo，如果为0那么就不显示
     */
    public void showProDialog(int logoRes, String title, String message) {
        if (mPro == null) {
            mPro = ProgressDialog.show(mContext, title, message);
            mPro.setCancelable(true);
        }
        if (logoRes != 0) {
            mPro.setIcon(logoRes);
        }
        if (title != null) {
            mPro.setTitle(title);
        }
        if (message != null) {
            mPro.setMessage(message);
        }
        if (!isFinishing() && !mPro.isShowing()) {
            mPro.show();
        }
    }

    /**
     * 隐藏显示的ProDialog
     */
    public void hintProDialog() {
        if (mPro != null && mPro.isShowing() && !isFinishing()) {
            mPro.dismiss();
        }
    }

}
