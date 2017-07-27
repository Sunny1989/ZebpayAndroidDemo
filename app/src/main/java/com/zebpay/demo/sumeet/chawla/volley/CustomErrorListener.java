package com.zebpay.demo.sumeet.chawla.volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.zebpay.demo.sumeet.chawla.R;
import com.zebpay.demo.sumeet.chawla.utility.Utils;
import com.zebpay.demo.sumeet.chawla.utility.logs.Logcat;


/**
 * Created by sumeet on 17/12/15.
 */
public class CustomErrorListener implements ErrorListener {

    private Context context;
    private ProgressDialog progressBarDialog;
    private final String TAG = CustomErrorListener.class.getSimpleName();

    private ProgressBar progressBar;

    public CustomErrorListener(Context context) {
        this.context = context;

        //We have to keep it if because any error is thrown so it will catch it!
        //And over here we have to close dialog!
        //this.progressBarDialog = ProgressBarDialog.getInstance(context);
    }

    public CustomErrorListener(Context context, ProgressBar progressBar) {
        this.context = context;
        this.progressBar = progressBar;
    }

    public void setProgressBarDialog(ProgressDialog progressBarDialog) {
        this.progressBarDialog = progressBarDialog;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (null != progressBar) {
            //For Contacts Screen!
            progressBar.setVisibility(View.GONE);
        }

        if (null != progressBarDialog && progressBarDialog.isShowing()) {
            progressBarDialog.dismiss();
        }
        if (null == error.getMessage()) {
            Logcat.showLog(TAG, error.getClass().toString());
        } else {
            Logcat.showLog(TAG, error.getMessage());
        }
        String errorText = null;
       /* if (error instanceof BasicPojo) {
            //To show custom error from Server!
            BasicPojo basicPojo = (BasicPojo) error;
            String errorTxt = basicPojo.error.errorLongDesc;
            //errorText = error.getMessage();
            errorText = errorTxt;
        } else*/
        if (error instanceof ServerError) {
            errorText = context.getString(R.string.server_not_responding);
        } else if (error instanceof AuthFailureError) {
            //errorText = context.getString(R.string.authentication_failed);
            errorText = context.getString(R.string.authentication_failed);
            //callLoginScreen();
        } else if (error instanceof ParseError) {
            errorText = context.getString(R.string.could_not_parse_data_from_server);
        } else if (error instanceof NoConnectionError) {
            String msg = error.getCause().getMessage();
            if (msg.equalsIgnoreCase("No Authentication challenges found")) {
                errorText = context.getString(R.string.authentication_failed);
                //callLoginScreen();
            } else {
                errorText = context.getString(R.string.not_able_to_connect);
            }
        } else if (error instanceof TimeoutError) {
            errorText = context.getString(R.string.connection_timed_out);
        } else if (error instanceof NetworkError) {
            errorText = context.getResources().getString(R.string.network_error);
        }
        if (null != errorText) {
            //Hiding when its not connected to internet!
            //Client requirement!
            if (!errorText.equals(context.getString(R.string.not_able_to_connect))) {
                Utils.makeShortToast(context, errorText);
            }
        } else {
            //context.getString(R.string.undefined_error)
            Utils.makeShortToast(context, context.getString(R.string.network_error));
        }
    }

}
