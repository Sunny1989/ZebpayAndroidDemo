package com.zebpay.demo.sumeet.chawla.volley.DiskBasedCaching;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.zebpay.demo.sumeet.chawla.utility.logs.Logcat;


/**
 * Basic LRU Memory cache.
 *
 * @author Trey Robinson
 */
public class BitmapLruImageCache extends LruCache<String, Bitmap> implements ImageCache {

    private final String TAG = this.getClass().getSimpleName();

    public BitmapLruImageCache(int maxSize) {
        super(maxSize);
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public Bitmap getBitmap(String url) {
        Logcat.showLog(TAG, "Retrieved item from Mem Cache");
        return get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        Logcat.showLog(TAG, "Added item to Mem Cache");
        put(url, bitmap);
    }
}
