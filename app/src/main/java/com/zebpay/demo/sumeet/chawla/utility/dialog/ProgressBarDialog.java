package com.zebpay.demo.sumeet.chawla.utility.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;

import com.zebpay.demo.sumeet.chawla.R;
import com.zebpay.demo.sumeet.chawla.utility.Utils;

/**
 * Created by sumeet on 15/12/15.
 */
public class ProgressBarDialog extends Dialog implements View.OnClickListener {

    private ProgressBar progressBar;
    private Context ctxt;
    //private TextView txtCancel;
    private static ProgressBarDialog mInstance;
    private boolean isDismissed = false;

   /* public ProgressBarDialog(Context context) {
        super(context);
        ctxt = context;
    }*/

    private ProgressBarDialog(Context context, int themeResId) {
        super(context, themeResId);
        ctxt = context;
    }

    public static synchronized ProgressBarDialog getInstance(Context context) {
        //if (mInstance == null) {
        mInstance = new ProgressBarDialog(context, R.style.LoadingDialogThemeWithOverlay);
        //}
        return mInstance;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_progress_dialog);
        setCancelable(false);
        progressBar = (ProgressBar) findViewById(R.id.loadingProgress);
        progressBar.getIndeterminateDrawable().setColorFilter(Utils.getColor(ctxt, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        progressBar.setIndeterminate(true);

       /* txtCancel = (TextView) findViewById(R.id.txtCancel);
        txtCancel.setOnClickListener(this);*/
    }

    public void showProgressDialog() {
        show();
        if (null != progressBar) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    public void dismissDialog() {
        if (null != progressBar) {
            progressBar.setVisibility(View.GONE);
        }
        this.dismiss();
        if (isDismissed == false) {
            isDismissed = true;
            this.dismiss();
        } else {
            return;
        }

        //mInstance = null;
    }

    @Override
    public void onClick(View v) {
        /*if (v.getId() == R.id.txtCancel) {
            dismissDialog();
        }*/
    }
}
