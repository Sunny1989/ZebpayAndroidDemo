package com.zebpay.demo.sumeet.chawla.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.zebpay.demo.sumeet.chawla.R;
import com.zebpay.demo.sumeet.chawla.custom.base.BaseAppcompatActivity;
import com.zebpay.demo.sumeet.chawla.databinding.DetailActivityBinding;
import com.zebpay.demo.sumeet.chawla.pojos.ActivityFeed;
import com.zebpay.demo.sumeet.chawla.utility.Constants;

/**
 * Created by Sumeet on 27-07-2017.
 */

public class DetailActivity extends BaseAppcompatActivity {

    private DetailActivityBinding detailActivityBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ActivityFeed activityFeed = bundle.getParcelable(Constants.FEED_OBJ);
            if (activityFeed != null) {
                detailActivityBinding.setActivityFeed(activityFeed);
            }
        }

    }
}
