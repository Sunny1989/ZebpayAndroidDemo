package com.zebpay.demo.sumeet.chawla.views;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zebpay.demo.sumeet.chawla.R;
import com.zebpay.demo.sumeet.chawla.custom.base.BaseAppcompatActivity;
import com.zebpay.demo.sumeet.chawla.databinding.SettingsBinding;
import com.zebpay.demo.sumeet.chawla.utility.Utils;
import com.zebpay.demo.sumeet.chawla.utility.ZebpayPreference;

/**
 * Created by Sumeet on 27-07-2017.
 */

public class SettingsActivity extends BaseAppcompatActivity {

    private SettingsBinding settingsBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
    }


    /**
     * This method will save the values in shared preferences.
     *
     * @param view
     */
    public void saveInPreferences(View view) {
        String percentage = settingsBinding.etPercentage.getText().toString();
        String differenceValue = settingsBinding.etValue.getText().toString();

        if(Integer.parseInt(percentage) < 0 || Integer.parseInt(percentage) > 100){
            Utils.makeShortToast(this,getString(R.string.enter_valid_percent));
            return;
        }else if(Integer.parseInt(differenceValue) < 0){
            Utils.makeShortToast(this,"Enter Valid");
            return;
        }


        ZebpayPreference.setUserDetails(this, ZebpayPreference.DIFFERENCE_VALUE, differenceValue);
        ZebpayPreference.setUserDetails(this, ZebpayPreference.PERCENTAGE, percentage);

        finish();
    }

}
