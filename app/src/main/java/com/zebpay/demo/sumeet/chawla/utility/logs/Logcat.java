package com.zebpay.demo.sumeet.chawla.utility.logs;

import android.util.Log;

import com.zebpay.demo.sumeet.chawla.BuildConfig;

/**
 * Created by sumeet on 17/12/15.
 */
public class Logcat {
    public static void showLog(String tag, String msg) {
        if (BuildConfig.showLog) {
            if (msg == null) {
                msg = "null";
            }
            Log.d(tag, msg);
        }
    }
}
