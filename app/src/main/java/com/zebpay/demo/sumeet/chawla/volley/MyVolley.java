package com.zebpay.demo.sumeet.chawla.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.zebpay.demo.sumeet.chawla.R;
import com.zebpay.demo.sumeet.chawla.utility.Utils;
import com.zebpay.demo.sumeet.chawla.volley.DiskBasedCaching.BitmapLruImageCache;

/**
 * Created by sumeet on 8/12/15.
 */
public class MyVolley {

    private static MyVolley mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;
    public static final String TAG = MyVolley.class.getSimpleName();
    private ImageLoader.ImageCache mImageCache;

    //For Disk Based Caching !
    private int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 10;
    private Bitmap.CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    private int DISK_IMAGECACHE_QUALITY = 100;
    //PNG is lossless so quality is ignored but must be provided
    //But Sunny have used JPEG!

    private MyVolley(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        //Sumeet : We are keeping both the options!
        //i) LRU cache and ii) Disk-Based Cache.
        //So that we can use any of the two!

        //Disk!
        //We were using this but it slows down loading of image in listview!
        //Especially when we scroll up and down!
        /*String uniqueName = mCtx.getPackageCodePath();
        mImageCache = new DiskLruImageCache(context, uniqueName, DISK_IMAGECACHE_SIZE, DISK_IMAGECACHE_COMPRESS_FORMAT, DISK_IMAGECACHE_QUALITY);*/

        //Lru!
        //By using this, its preventing from flicking of image when we scroll up and down in listview(as it recycles views).
        mImageCache = new BitmapLruImageCache(DISK_IMAGECACHE_SIZE);

        mImageLoader = new ImageLoader(mRequestQueue, mImageCache);

        //LRU cache! My Previous Implementation!
        /*new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap>
                    cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        };*/

        //Added to make image upload and download faster!
        //mImageLoader.setBatchedResponseDelay(0);
    }

    public static synchronized MyVolley getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyVolley(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {

            //this is default Queue provided by Volley!
            //So why to implement our own Sunny ?
            mRequestQueue = Volley.newRequestQueue(mCtx);

            //Therefore, Commenting the below Code :

            /*// getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            Cache cache = new DiskBasedCache(mCtx.getCacheDir(), 10 * 1024 * 1024);
            Network network = new BasicNetwork(new HurlStack());

            //Changed over here, changed default pool size!
            //This worked out for making it work fast in older devices also!
            //Still not working (fastly in previous devices)
            mRequestQueue = new RequestQueue(cache, network, VolleyUtils.DEFAULT_NETWORK_THREAD_POOL_SIZE);

            // Don't forget to start the volley request queue
            mRequestQueue.start();
            //mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());*/

        }
        return mRequestQueue;
    }

    public <T> boolean addToRequestQueue(Request<T> req) {
        if (!checkForNetwork()) {
            return false;
        }
        getRequestQueue().add(req);
        return true;
    }

    public <T> boolean addToRequestQueue(Request<T> req, String tag) {
        if (!checkForNetwork()) {
            return false;
        }
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
        //From now we will show dialog only when request is hit!
        if (req instanceof GsonRequest) {
            ((GsonRequest) req).showProgressDialog();
        }
        return true;
    }

    public boolean checkForNetwork() {
        boolean flag = Utils.isNetworkAvailable(mCtx);
        if (!flag) {
            //Utils.makeShortToast(mCtx, mCtx.getString(R.string.please_connect_internet));
            return false;
        }
        return true;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }


}
