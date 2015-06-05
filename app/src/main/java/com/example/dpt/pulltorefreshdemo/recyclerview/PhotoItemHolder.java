package com.example.dpt.pulltorefreshdemo.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.example.dpt.pulltorefreshdemo.R;

/**
 * Created by dupengtao on 15/6/2.
 */
public class PhotoItemHolder extends RecyclerView.ViewHolder {
    public TextView tv;
    public ImageView iv;
    public ImageLoader.ImageContainer imageContainer;
    public PhotoItemHolder(View itemView) {
        super(itemView);
        iv=(ImageView)itemView.findViewById(R.id.iv_photo);
        tv=(TextView)itemView.findViewById(R.id.tv_photo);
    }
}
