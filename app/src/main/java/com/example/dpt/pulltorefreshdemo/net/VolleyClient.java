package com.example.dpt.pulltorefreshdemo.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import com.android.volley.*;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * Created by dupengtao on 14-12-3.
 */
public class VolleyClient {
    //image
    public static ImageLoader.ImageContainer loadImage(VolleyController vc, String url, ImageView imageView, int loadingResId, int errorResId) {
        return loadImage(vc, url, imageView, loadingResId, errorResId, 0, 0);
    }

    public static ImageLoader.ImageContainer loadImage(VolleyController vc, String url, ImageView imageView, int loadingResId, int errorResId, int maxWidth, int maxHeight) {
        ImageLoader imageLoader = vc.getImageLoader();
        return imageLoader.get(url, ImageLoader.getImageListener(imageView, loadingResId, errorResId), maxWidth, maxHeight);
    }

    public static ImageLoader.ImageContainer loadImage(VolleyController vc, String url, ImageLoader.ImageListener imageListener) {
        ImageLoader imageLoader = vc.getImageLoader();
        return imageLoader.get(url, imageListener);
    }


    /**
     * if do not use xml anim , you should use{@link com.letv.leui.common.recommend.volley.toolbox.ImageLoader#get(String, com.letv.leui.common.recommend.volley.toolbox.ImageLoader.ImageListener)}
     * eg.
     *
     * @param loadingResId if loadingResId is 0 ,ImageView will not loading image
     * @param animResId    anim in xml
     * @see {@link AbAnimImageListener}
     */
    public static void loadImageWithAnim(VolleyController vc, Context context, String url, ImageView imageView, int loadingResId, int errorResId, final int animResId) {
        vc.getImageLoader().get(url, new AbAnimImageListener(context, imageView, errorResId, loadingResId) {
            @Override
            public int getAnimResId() {
                if (animResId < 1) {
                    return 0;
                }
                return animResId;
            }
        });
    }

    /**
     * @param isShouldCache if false will not in cache
     */
    public static ImageRequest makeImageRequest(VolleyController vc, String url, final ImageView imageView, int loadingResId, final int errorResId, int maxWidth, int maxHeight, boolean isShouldCache, Bitmap.Config decodeConfig, String tag) {
        ImageRequest imgRequest = getImageRequest(vc, url, imageView, loadingResId, errorResId, maxWidth, maxHeight, isShouldCache, decodeConfig);
        vc.addToRequestQueue(imgRequest, tag);
        return imgRequest;
    }

    private static ImageRequest getImageRequest(final VolleyController vc, final String url, final ImageView imageView, int loadingResId, final int errorResId, int maxWidth, int maxHeight, final boolean isShouldCache, Bitmap.Config decodeConfig) {
        if (loadingResId > 0) {
            imageView.setImageResource(loadingResId);
        }
        if (maxWidth < 1) {
            maxWidth = 0;
        }
        if (maxHeight < 1) {
            maxHeight = 0;
        }
        final int finalMaxHeight = maxHeight;
        final int finalMaxWidth = maxWidth;
        ImageRequest imgRequest = new ImageRequest(url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
                if (!isShouldCache) {
                    vc.getLruBitmapCache().putBitmap(getCacheKey(url, finalMaxWidth, finalMaxHeight), response);
                }
            }
        }, maxWidth, maxHeight, decodeConfig, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setImageResource(errorResId);
            }
        });
        if (!isShouldCache) {
            setNoCache(imgRequest);
        }
        return imgRequest;
    }

    public static ImageRequest makeImageRequest(VolleyController vc, String url, ImageView imageView, boolean isShouldCache, int loadingResId, int errorResId) {
        return makeImageRequest(vc, url, imageView, loadingResId, errorResId, 0, 0, isShouldCache, Bitmap.Config.RGB_565, null);
    }

    public static Bitmap loadImageInCache(VolleyController vc, String url, int maxWidth, int maxHeight) {
        Cache.Entry entry = vc.getRequestQueue().getCache().get(getCacheKey(url, maxHeight, maxWidth));
        if (entry.data.length != 0) {
            return BitmapFactory.decodeByteArray(entry.data, 0, entry.data.length);
        } else {
            return null;
        }
    }

    public static String loadInCache(VolleyController vc, String url) {
        Cache.Entry entry = vc.getRequestQueue().getCache().get(url);
        String data = null;
        if (entry == null) {
            return data;
        }
        try {
            data = new String(entry.data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void cacheRemove(VolleyController vc, String url) {
        vc.getRequestQueue().getCache().remove(url);
    }

    public static void cacheClear(VolleyController vc) {
        vc.getRequestQueue().getCache().clear();
    }

    public static void cancelSingleRequest(VolleyController vc, String reqTag) {
        vc.getRequestQueue().cancelAll(reqTag);
    }

    public static void cancelAllRequests(VolleyController vc) {
        vc.getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    private static Request setNoCache(Request request) {
        request.setShouldCache(false);
        return request;
    }


    /**
     * count cache size
     */
    public static long getCacheSize(Context context) {
        File cacheDir = new File(context.getCacheDir(), "volley");
        return cacheDir.length();
    }

    private static String getCacheKey(String url, int maxWidth, int maxHeight) {
        return new StringBuilder(url.length() + 12).append("#W").append(maxWidth)
                .append("#H").append(maxHeight).append(url).toString();
    }
}
