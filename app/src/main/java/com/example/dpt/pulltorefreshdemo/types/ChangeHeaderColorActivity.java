package com.example.dpt.pulltorefreshdemo.types;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.dpt.pulltorefreshdemo.R;
import com.demo.widget.ptr.PtrClassicFrameLayout;
import com.demo.widget.ptr.PtrDefaultHandler;
import com.demo.widget.ptr.PtrFrameLayout;
import com.demo.widget.ptr.leui.header.SimpleLeLoadingHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dupengtao on 15/6/2.
 */
public class ChangeHeaderColorActivity extends Activity {

    private PtrClassicFrameLayout mPtrFrame;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_lv);
        mListView = (ListView) findViewById(R.id.lv_local);
        List<String> list = new ArrayList<String>(500);
        for(int i=0;i<500;i++){
            list.add("item -- "+i);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.item_lv,R.id.text,list);
        mListView.setAdapter(arrayAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ChangeHeaderColorActivity.this,"item "+position+" item click",Toast.LENGTH_SHORT).show();
            }
        });


        mPtrFrame =(PtrClassicFrameLayout)findViewById(R.id.ptr_demo_list);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                frame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPtrFrame.refreshComplete();
                    }
                }, 800);
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
            }
        });

        //改变头部小球颜色
        SimpleLeLoadingHeader defaultHeader = mPtrFrame.getDefaultHeader();
        int headerColor = Color.parseColor("#8bc34a");
        defaultHeader.setEachColor4Balls(headerColor,headerColor,headerColor,headerColor,headerColor,headerColor);
    }
}
