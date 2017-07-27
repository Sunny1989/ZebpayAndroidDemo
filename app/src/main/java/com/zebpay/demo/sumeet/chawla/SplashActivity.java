package com.zebpay.demo.sumeet.chawla;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zebpay.demo.sumeet.chawla.custom.base.BaseAppcompatActivity;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Sumeet on 27-07-2017.
 */

public class SplashActivity extends BaseAppcompatActivity {

    private final int SPLASH_TIME_OUT = 2000;
    private android.os.Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        DataBindingUtil.setContentView(this, R.layout.activity_splash);
        initializeRunnable();
    }


    public void initializeRunnable() {
        handler = new android.os.Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                startAnotherActivity();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != handler) {
            executeRunnable();
        }
    }

    public void executeRunnable() {
        handler.postDelayed(runnable, SPLASH_TIME_OUT);
    }

    public void startAnotherActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != handler) {
            removeRunnable();
        }
    }

    public void removeRunnable() {
        handler.removeCallbacks(runnable);
    }

}
