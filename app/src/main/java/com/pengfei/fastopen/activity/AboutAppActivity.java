package com.pengfei.fastopen.activity;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;

import com.pengfei.fastopen.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by mengfei on 2016/8/14.
 */
public class AboutAppActivity extends BaseActivity {


    WebView aboutAppWV;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.act_about_app);
        aboutAppWV = (WebView) findViewById(R.id.act_about_app_tv);
        aboutAppWV.loadUrl("file:///android_asset/aboutApp.html");
    }


}
