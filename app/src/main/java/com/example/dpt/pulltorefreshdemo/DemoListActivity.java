package com.example.dpt.pulltorefreshdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import com.example.dpt.pulltorefreshdemo.recyclerview.MarginDecoration;
import com.example.dpt.pulltorefreshdemo.recyclerview.TextViewHolder;
import com.example.dpt.pulltorefreshdemo.types.*;
import com.letv.leui.widget.ultra.pull2refresh.ptr.PtrClassicFrameLayout;
import com.letv.leui.widget.ultra.pull2refresh.ptr.PtrDefaultHandler;
import com.letv.leui.widget.ultra.pull2refresh.ptr.PtrFrameLayout;


public class DemoListActivity extends Activity {

    private PtrClassicFrameLayout mPtrFrame;
    private LinearLayoutManager llm;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_list);
        Demo[] demos = {
                new Demo(this, TextViewActivity.class, R.string.a_text_view),
                new Demo(this, ListViewActivity.class, R.string.a_list_view),
                new Demo(this, WebViewActivity.class, R.string.a_web_view),
                new Demo(this, PhotosActivity.class, R.string.a_photo_set),
                new Demo(this, ChangeHeaderColorActivity.class, R.string.change_header_color),
                new Demo(this, TestActivity.class, R.string.change_header_color),
                new Demo(this, ScrollViewActivity.class, R.string.a_scroll_view),

                //new Demo(this, GridLayoutActivity.class, R.string.grid_layout),
                //new Demo(this,
                //        GridLayoutVariableSpanSizeActivity.class, R.string.grid_layout_variable_span_size),
                //new Demo(this, GridLayoutHeaderActivity.class, R.string.grid_layout_header),
                //new Demo(this, GridLayoutAutoFitActivity.class, R.string.grid_layout_auto_fit),
                //new Demo(this, GridLayoutAutoFitHeaderActivity.class, R.string.grid_layout_auto_fit_header),
                //new Demo(this, GridLayoutAddRemoveActivity.class, R.string.grid_layout_add_remove)
        };


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.addItemDecoration(new MarginDecoration(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new DemoHeaderAdapter(demos));
        llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);



        //pull to refresh
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
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerView, header);
            }
        });

        //适配recyclerView
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mPtrFrame.setEnabled(llm.findFirstCompletelyVisibleItemPosition() == 0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_demo_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private static class DemoHeaderAdapter extends RecyclerView.Adapter<TextViewHolder> {
        private static final int ITEM_VIEW_TYPE_HEADER = 0;
        private static final int ITEM_VIEW_TYPE_ITEM = 1;
        private final Demo[] demos;
        private View headerView;

        public DemoHeaderAdapter(Demo[] demos) {
            this.demos = demos;
        }

        @Override
        public TextViewHolder onCreateViewHolder(ViewGroup parent, int position) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            return new TextViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final TextViewHolder holder, final int position) {
            final Demo demo = demos[position];
            holder.textView.setText(demo.title);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = holder.textView.getContext();
                    context.startActivity(new Intent(context, demo.activityClass));
                }
            });
        }

        @Override
        public int getItemCount() {
            return demos.length;
        }

    }

    public static class Demo {
        public final Class<?> activityClass;
        public final String title;

        public Demo(Context context, Class<?> activityClass, int titleId) {
            this.activityClass = activityClass;
            this.title = context.getString(titleId);
        }
    }
}
