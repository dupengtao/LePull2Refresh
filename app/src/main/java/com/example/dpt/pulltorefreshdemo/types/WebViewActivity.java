package com.example.dpt.pulltorefreshdemo.types;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.dpt.pulltorefreshdemo.R;
import com.demo.widget.ptr.PtrClassicFrameLayout;
import com.demo.widget.ptr.PtrDefaultHandler;
import com.demo.widget.ptr.PtrFrameLayout;
import com.demo.widget.ptr.PtrHandler;

/**
 * Created by dupengtao on 15/6/2.
 */
public class WebViewActivity extends Activity{
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_wv);

        final PtrClassicFrameLayout mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.ptr_demo_list);
        mWebView = (WebView) findViewById(R.id.wv_sample);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mPtrFrame.refreshComplete();
            }
        });

        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mWebView, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadData();
            }
        });

        //自动下拉刷新
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 200);
    }

    private void loadData() {
        mWebView.loadUrl("https://www.baidu.com/");
    }
}
