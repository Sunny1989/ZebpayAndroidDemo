package com.zebpay.demo.sumeet.chawla.utility.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by sumeet on 26/11/15.
 */
public class NoticeDialogFragment extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
    * implement this interface in order to receive event callbacks.
    * Each method passes the DialogFragment in case the host needs to query it. */
    /*public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);

        public void onDialogNegativeClick(DialogFragment dialog);
    }*/

    // Use this instance of the interface to deliver action events
    //NoticeDialogListener mListener;

    private String title;
    private String message;
    private String okLbl;
    private String cancelLbl;
    private DialogInterface.OnClickListener okClickListener, cancelClickListener;

    public NoticeDialogFragment() {
        super();
    }


    @SuppressLint("ValidFragment")
    public NoticeDialogFragment(String title, String message, String okLbl, String cancelLbl, DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        this.title = title;
        this.message = message;
        this.okLbl = okLbl;
        this.cancelLbl = cancelLbl;
        this.okClickListener = okListener;
        this.cancelClickListener = cancelListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message)
                .setPositiveButton(okLbl, okClickListener)
                .setNegativeButton(cancelLbl, cancelClickListener);
        return builder.create();
    }
}
