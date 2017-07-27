package com.zebpay.demo.sumeet.chawla.model;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.zebpay.demo.sumeet.chawla.R;
import com.zebpay.demo.sumeet.chawla.utility.dialog.NoticeDialogFragment;
import com.zebpay.demo.sumeet.chawla.volley.CustomErrorListener;
import com.zebpay.demo.sumeet.chawla.volley.GsonRequest;
import com.zebpay.demo.sumeet.chawla.volley.MyVolley;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by sumeet on 23/7/16.
 */
public class RequestModel {

    public static void showDialog(final Context context, final String methodName, final FragmentManager fm, final String TAG, final Response.Listener listener, final boolean showDialog, final CustomErrorListener customErrorListener, final Map<String, String> requestParams, final String url, final Class classType) {
        //As we cannot cast Class to variable that is why we have to fragmentManager as variable!
        DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    Class[] params = new Class[9];
                    params[0] = Context.class;
                    params[1] = String.class;
                    params[2] = Response.Listener.class;
                    params[3] = Boolean.TYPE;
                    params[4] = android.support.v4.app.FragmentManager.class;
                    params[5] = CustomErrorListener.class;
                    params[6] = Map.class;
                    params[7] = String.class;
                    params[8] = classType.getClass();

                    Method method = RequestModel.class.getMethod(methodName, params);
                    //We should pass null, if method is static else we should pass that class object!
                    //http://tutorials.jenkov.com/java-reflection/index.html
                    method.invoke(null, context, TAG, listener, showDialog, fm, customErrorListener, requestParams, url, classType);
                    dialogInterface.dismiss();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        };
        DialogInterface.OnClickListener cancelClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((Activity) context).finish();
            }
        };

        NoticeDialogFragment noticeDialogFragment = new NoticeDialogFragment(context.getString(R.string.no_network_connection), context.getString(R.string.please_connect_internet_connect), context.getString(R.string.lbl_continue), context.getString(R.string.cancel), okClickListener, cancelClickListener);
        //((SplashActivity) context).getSupportFragmentManager()
        noticeDialogFragment.show(fm, "dialog");
        noticeDialogFragment.setCancelable(false);
    }

    public static void callNetworkRequest(Context context, String TAG, Response.Listener listener, boolean showDialog, FragmentManager fm, CustomErrorListener customErrorListener, Map<String, String> params, String url, Class classType) {
        try {
            GsonRequest gsonRequest = new GsonRequest(url, classType, null, listener, customErrorListener, params, context, Request.Method.GET, showDialog);
            boolean requestStatus = MyVolley.getInstance(context).addToRequestQueue(gsonRequest, TAG);
            if (!requestStatus) {
                //showDialog(context, "callNetworkRequest", fm, TAG, listener, showDialog, customErrorListener, params, url, classType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
