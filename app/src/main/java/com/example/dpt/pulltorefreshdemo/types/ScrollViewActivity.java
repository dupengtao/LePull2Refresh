package com.example.dpt.pulltorefreshdemo.types;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import com.example.dpt.pulltorefreshdemo.R;
import com.demo.widget.ptr.PtrClassicFrameLayout;
import com.demo.widget.ptr.PtrDefaultHandler;
import com.demo.widget.ptr.PtrFrameLayout;
import com.demo.widget.ptr.PtrHandler;

/**
 * Created by dupengtao on 15/6/2.
 */
public class ScrollViewActivity extends Activity{
    private ScrollView sv;
    private PtrClassicFrameLayout mPtrFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_sv);

        mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.ptr_demo_list);
        sv = (ScrollView) findViewById(R.id.sv);

        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, sv, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                loadData();
            }
        });
    }

    private void loadData() {
        //DEMO
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.refreshComplete();
            }
        }, 800);
    }
}
