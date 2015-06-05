package com.example.dpt.pulltorefreshdemo.types;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.dpt.pulltorefreshdemo.R;

/**
 * Created by dupengtao on 15/6/1.
 */
public class LinearLayoutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linerlayout);
        LinearLayout ll=(LinearLayout)findViewById(R.id.ll);
        for(int i=0 ;i<100;i++){
            TextView tv=(TextView)View.inflate(this,R.layout.header,null);
            tv.setText("I am TextView "+i);
            ll.addView(tv);
        }
    }
}
