package com.zebpay.demo.sumeet.chawla.utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sumeet on 18/11/15.
 */
public class Utils {

    //public static String mCurrentPhotoPath;

    public static final int getColor(Context context, int id) {
        //final int version = Build.VERSION.SDK_INT;
        //if (version >= 23) {
        return ContextCompat.getColor(context, id);
        /*} else {
            return context.getResources().getColor(id);
        }*/
    }

    public static final Drawable getDrawable(Context context, int id) {
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        //return context.getResources().getDrawable(id, context.getTheme());
        return ContextCompat.getDrawable(context, id);
        /*} else {
            return context.getResources().getDrawable(id);
        }*/
    }

    /*public static final int setStatusBarColor(Context context, boolean isOverlay) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = ((Activity) context).getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (isOverlay) {
                window.setStatusBarColor(getColor(context, R.color.vipi_light_overlay_statusbar));
                return 0;
            } else {
                window.setStatusBarColor(getColor(context, R.color.vipi_status_bar_blue));
                return 1;
            }
        } else {
            return -1;
        }

    }*/


    public static int GetPixelFromDips(Context context, float pixels) {
        // Get the screen's density scale
        final float scale = context.getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }


    /*public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    public int pxToDp(int px, ) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }*/

    public static void makeShortToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

    public static String getTimeInAgo(String date) {
        long time = getDateInMillis(date);
        return DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS).toString();
    }

    public static long getDateInMillis(String srcDate) {
        SimpleDateFormat desiredFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        long dateInMillis = 0;
        try {
            Date date = desiredFormat.parse(srcDate);
            dateInMillis = date.getTime();
            return dateInMillis;
        } catch (ParseException e) {
            //Log.d("Exception while parsing date. " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    public static void makeLongToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_LONG).show();
    }

    public static void clearPreviousActivities(Context ctx, Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ctx.startActivity(intent);
    }


    public static String saveToInternalStorage(Bitmap bitmapImage, String fileName, Context context) throws IOException {
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/profile_pictures
        File directory = cw.getDir(Constants.PROFILE_PICTURES, Context.MODE_PRIVATE);
        // Create profile_pictures
        File mypath = new File(directory, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 80, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        return directory.getAbsolutePath();
    }


    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}