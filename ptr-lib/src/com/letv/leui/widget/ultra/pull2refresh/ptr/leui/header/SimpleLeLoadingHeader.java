package com.letv.leui.widget.ultra.pull2refresh.ptr.leui.header;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import com.letv.leui.widget.ultra.pull2refresh.ptr.PtrFrameLayout;
import com.letv.leui.widget.ultra.pull2refresh.ptr.PtrUIHandler;
import com.letv.leui.widget.ultra.pull2refresh.ptr.R;
import com.letv.leui.widget.ultra.pull2refresh.ptr.indicator.PtrIndicator;
import com.letv.leui.widget.ultra.pull2refresh.ptr.leui.util.LogHelper;

import java.util.ArrayList;

/**
 * Created by dupengtao on 15-5-14.
 */
public class SimpleLeLoadingHeader extends RelativeLayout implements PtrUIHandler {

    private static final String LOG_TAG = "SimpleLeLoadingHeader";
    private Context mContext;
    //private PtrTensionIndicator mPtrTensionIndicator;
    private SimpleLeLoadingView mSimpleLeLoadingView;
    private boolean needRest;

    public SimpleLeLoadingHeader(Context context) {
        this(context, null);
    }

    public SimpleLeLoadingHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        //this.setBackgroundColor(Color.parseColor("#ebebeb"));
        LayoutParams rLP = new LayoutParams(LayoutParams.MATCH_PARENT, 210);
        this.setLayoutParams(rLP);
        View rootView = View.inflate(mContext, R.layout.le_ptr_simple_loading_header, this);
        mSimpleLeLoadingView = (SimpleLeLoadingView) rootView.findViewById(R.id.le_head_simple_loading_view);
    }

    public SimpleLeLoadingHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setUp(PtrFrameLayout ptrFrameLayout) {
        //mPtrFrameLayout = ptrFrameLayout;
        //mPtrTensionIndicator = new PtrTensionIndicator();
        //mPtrFrameLayout.setPtrIndicator(mPtrTensionIndicator);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        needRest = false;
        LogHelper.e(LOG_TAG, "onUIReset ");
        if(frame.isAutoRefresh()){
            LogHelper.e(LOG_TAG,"AutoRefresh --- onUIReset");
            //mSimpleLeLoadingView.autoPull2RefreshAnim();
            mSimpleLeLoadingView.cancelAutoPull2RefreshAnim();
        }
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        //mSimpleLeLoadingView.resetOriginals();
        LogHelper.e(LOG_TAG, "onUIRefreshPrepare ");
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        needRest = true;
        LogHelper.e(LOG_TAG, "onUIRefreshBegin --- percent ---");

        if(frame.isAutoRefresh()){
            LogHelper.e(LOG_TAG,"AutoRefresh --- onUIRefreshBegin");
            mSimpleLeLoadingView.autoPull2RefreshAnim();
        }


    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        if (needRest) {
            //byte st = frame.getStatus();
            //if (st == PtrFrameLayout.PTR_STATUS_COMPLETE || st == PtrFrameLayout.PTR_STATUS_LOADING) {
            //    return ;
            //}
            //mSimpleLeLoadingView.completeAnim();
        }
        LogHelper.e(LOG_TAG, "onUIRefreshComplete --- ");
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {

        // start optimize refresh
        byte st = frame.getStatus();
        if (st == PtrFrameLayout.PTR_STATUS_COMPLETE || st == PtrFrameLayout.PTR_STATUS_LOADING) {
            return ;
        }
        // end optimize refresh
        float percent = ptrIndicator.getCurrentPercent();
        long l = Float.valueOf(percent * 1000).longValue();
        if (l > 0) {
            mSimpleLeLoadingView.setPercent(l);
        } else {
            mSimpleLeLoadingView.resetOriginals();
        }
        //Log.e("xxxx","onUIPositionChange --- percent ---"+ percent);
    }

    public void setLastUpdateTimeKey(String key) {

    }

    public void setLastUpdateTimeRelateObject(Object object) {

    }

    public ArrayList<Integer> getDefaultColorList(){
        return mSimpleLeLoadingView.getDefaultColorList();
    }

    public void setEachColor4Balls(int color1,int color2,int color3,int color4,int color5,int color6){
        mSimpleLeLoadingView.setEachColor4Balls(color1, color2, color3, color4, color5, color6);
    }
}
