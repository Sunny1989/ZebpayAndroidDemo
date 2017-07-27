package com.zebpay.demo.sumeet.chawla.volley;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zebpay.demo.sumeet.chawla.R;
import com.zebpay.demo.sumeet.chawla.utility.dialog.ProgressBarDialog;
import com.zebpay.demo.sumeet.chawla.utility.logs.Logcat;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by sumeet on 8/12/15.
 */
public class GsonRequest<T> extends Request<T> {
    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private Map<String, String> headers;
    private final Response.Listener<T> listener;
    private final Context ctxt;
    private final Map<String, String> params;
    public static final String TAG = GsonRequest.class.getSimpleName();

    //Adding CustomErrorListener, so that we will not be required to mention in all activities!
    private CustomErrorListener customErrorListener;
    private ProgressBarDialog progressBarDialog;

    private ProgressDialog progressDialog;
    private boolean showDialog;

    /**
     * Make a GET request and return a parsed object from JSON.
     *
     * @param url     URL of the request to make
     * @param clazz   Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     */
    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers,
                       Response.Listener<T> listener, Response.ErrorListener errorListener, Map<String, String> params, Context ctxt, int methodType, boolean showDialog) {
        super(methodType, url, errorListener);
        setRetryPolicy(new DefaultRetryPolicy(VolleyUtils.DEFAULT_TIMEOUT_MS,
                VolleyUtils.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        this.params = params;
        this.ctxt = ctxt;
        this.showDialog = showDialog;
        if (errorListener instanceof CustomErrorListener) {
            customErrorListener = (CustomErrorListener) errorListener;
        }
        Logcat.showLog(TAG, url);

        //From now we will call dialog only when request is hit (addRequestQueue in MyVolley)!
        /*if (showDialog == true) {
            //showProgressBarDialog();
            showProgressDialog();
        }*/

    }

    public void showProgressBarDialog() {
        if (progressBarDialog == null) {
            progressBarDialog = ProgressBarDialog.getInstance(ctxt);

            if (null != customErrorListener) {
                //customErrorListener.setProgressBarDialog(progressBarDialog);
            }
        }
        progressBarDialog.showProgressDialog();
    }


    public void closeProgressDialog() {
        if (null != progressBarDialog && progressBarDialog.isShowing()) {
            progressBarDialog.dismissDialog();
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (headers != null) {
            return headers;
        } else {
            headers = VolleyGlobal.getInstance(ctxt).getHeaders();
            Logcat.showLog(TAG, headers.values().toString());
            return headers;
        }

    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }


    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
        //closeProgressDialog();
        dismissProgressDialog();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Logcat.showLog(TAG, json);
            T myPojo = gson.fromJson(json, clazz);
            return Response.success(myPojo, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } /*catch (IOException e) {
            return Response.error(new ParseError(e));
        }*/ catch (Exception e) {
            return Response.error(new ParseError(e));
        }
    }


    public void showProgressDialog() {
        if (showDialog) {
            progressDialog = ProgressDialog.show(ctxt, ctxt.getString(R.string.please_wait), ctxt.getString(R.string.processing), true, false);
            if (customErrorListener != null) {
                customErrorListener.setProgressBarDialog(progressDialog);
            }
        }
    }

    public void dismissProgressDialog() {
        if (null != progressDialog) {
            progressDialog.dismiss();
        }
    }

}