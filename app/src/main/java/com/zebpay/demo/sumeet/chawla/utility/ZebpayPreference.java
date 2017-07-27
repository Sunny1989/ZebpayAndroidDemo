package com.zebpay.demo.sumeet.chawla.utility;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by sumeet on 23/7/16.
 */
public class ZebpayPreference<T> {
    private static android.content.SharedPreferences pref;
    private static android.content.SharedPreferences.Editor editor;

    private static final String PREF_NAME = "TickerPreference";
    public static final String PERCENTAGE = "percentage";
    public static final String DIFFERENCE_VALUE = "value";
    public static final String TOTAL_VALUE = "total_value";

    public static void setValueInDefaultPreference(Context context, String label, String value) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
        editor.putString(label, value);
        editor.apply();
    }


    public static String getValueFromDefaultPreference(Context context, String label, String defaulValue) {
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        String data = pref.getString(label, defaulValue);
        return data;
    }

    public static void setUserDetails(Context context, String label, String value) {
        pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString(label, value);
        editor.apply();
    }

    public static String getUserDetails(Context context, String label) {
        pref = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        //editor = pref.edit();
        String value = pref.getString(label, null);
        return value;
    }
}
