package com.zebpay.demo.sumeet.chawla.volley;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

/**
 * Created by sumeet on 16/12/15.
 */
public class VolleyUtils {
    private static final String TAG = VolleyUtils.class.getSimpleName();
    public static final int DEFAULT_TIMEOUT_MS = 20000;
    public static final int DEFAULT_MAX_RETRIES = 0;
    public static final int DEFAULT_NETWORK_THREAD_POOL_SIZE = 10;


    public static void downloadAndSetImage(Context context, ImageView image, String url) {
        if (null != url && !url.isEmpty() && image != null) {
            ImageLoader mImageLoader = MyVolley.getInstance(context).getImageLoader();
            //Instead of using this one we have created our own Customized one!
            //ImageLoader.getImageListener(mImageView, 0, 0)
            //ImageView imageProfilePic = (ImageView) findViewById(R.id.imageProfilePic);
            mImageLoader.get(url, new CustomImageListener(image, context));
        }
    }

    public static void downloadAndSaveImage(Context context, ImageView image, String url, String friendId) {
        if (null != url && !url.isEmpty() && image != null) {
            ImageLoader mImageLoader = MyVolley.getInstance(context).getImageLoader();
            //Instead of using this one we have created our own Customized one!
            //ImageLoader.getImageListener(mImageView, 0, 0)
            //ImageView imageProfilePic = (ImageView) findViewById(R.id.imageProfilePic);
             mImageLoader.get(url, new CustomImageListener(image, context, friendId));
        }
    }

}
