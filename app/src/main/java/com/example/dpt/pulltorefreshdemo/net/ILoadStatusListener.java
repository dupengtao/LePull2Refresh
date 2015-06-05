package com.example.dpt.pulltorefreshdemo.net;

/**
 * Callback interface for load Method
 * <p/>
 * @see com.letv.leui.common.recommend.widget.LeRecommendViewGroup#load(String, ILoadStatusListener)
 *
 * Created by dupengtao on 15-1-8.
 */
public interface ILoadStatusListener {

    int ERROR_OTHERS = 1;
    int ERROR_ID_NULL = 2;
    int ERROR_URL = 3;
    int ERROR_NO_INTERNET = 4;
    int SUCCESS_DEFAULT = 20;
    int SUCCESS_DATA_EMPTY = 21;


    void onStart();

    void onError(int statusId, Exception e);

    void onSuccess(int statusId);

    void onFinish(int statusId);
}
