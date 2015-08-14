package com.example.dpt.pulltorefreshdemo.types;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.example.dpt.pulltorefreshdemo.R;
import com.letv.leui.widget.ultra.pull2refresh.ptr.PtrClassicFrameLayout;
import com.letv.leui.widget.ultra.pull2refresh.ptr.PtrDefaultHandler;
import com.letv.leui.widget.ultra.pull2refresh.ptr.PtrFrameLayout;

/**
 * Created by dupengtao on 15/6/2.
 */
public class TextViewActivity extends Activity {

    private PtrClassicFrameLayout mPtrFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_tv);

        View tv = findViewById(R.id.text);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TextViewActivity.this,"hello world",Toast.LENGTH_SHORT).show();
            }
        });

        mPtrFrame =(PtrClassicFrameLayout)findViewById(R.id.ptr_demo_list);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //模拟业务逻辑，0.8秒后完成刷新
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                }, 800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }
        });

    }
}
