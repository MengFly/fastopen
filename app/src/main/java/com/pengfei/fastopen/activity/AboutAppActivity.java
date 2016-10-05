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
//        new ReadTextTask().execute("aboutApp.html");
    }

    private class ReadTextTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProDialog(0, null, null);
        }

        @Override
        protected void onPostExecute(String s) {
            hintProDialog();
            if (s != null) {

            }
        }

        @Override
        protected String doInBackground(String... params) {
            AssetManager manager = getAssets();
            InputStream in = null;
            BufferedReader reader = null;
            StringBuilder sb = new StringBuilder();
            try {
                in = manager.open(params[0]);
                reader = new BufferedReader(new InputStreamReader(in));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    sb.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return sb.toString();

        }
    }


}
