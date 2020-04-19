package com.rjx.common.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.rjx.common.R;

/**
 * @author Jianxiong Rao
 * @description : Test
 */
public class WebViewActivity extends AppCompatActivity {
    public static final String INTENT_TAG_TITLE = "title";
    public static final String INTENT_TAG_URL = "url";
    private String url;
    WebView mWebView;

    public static void startCommonWeb(Context context, String title, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(INTENT_TAG_TITLE, title);
        intent.putExtra(INTENT_TAG_URL, url);
        if (context instanceof Service) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mWebView = findViewById(R.id.web_view);
        url = getIntent().getStringExtra(INTENT_TAG_URL);
        if(!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        }
    }
}
