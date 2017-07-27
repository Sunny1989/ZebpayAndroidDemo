package com.zebpay.demo.sumeet.chawla;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zebpay.demo.sumeet.chawla.custom.base.BaseAppcompatActivity;

/**
 * Created by Sumeet on 27-07-2017.
 */

public class HomeActivity extends BaseAppcompatActivity {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_test);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_openRight) {
            drawer.openDrawer(GravityCompat.END);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
