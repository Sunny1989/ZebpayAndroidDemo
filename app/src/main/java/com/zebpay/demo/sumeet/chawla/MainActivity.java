package com.zebpay.demo.sumeet.chawla;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zebpay.demo.sumeet.chawla.custom.base.BaseAppcompatActivity;
import com.zebpay.demo.sumeet.chawla.databinding.MainActivityBinding;
import com.zebpay.demo.sumeet.chawla.interfaces.MainInterface;
import com.zebpay.demo.sumeet.chawla.pojos.TickerPojo;
import com.zebpay.demo.sumeet.chawla.utility.Constants;
import com.zebpay.demo.sumeet.chawla.utility.Utils;
import com.zebpay.demo.sumeet.chawla.viewmodel.MainViewModel;
import com.zebpay.demo.sumeet.chawla.views.SettingsActivity;

public class MainActivity extends BaseAppcompatActivity implements MainInterface, NavigationView.OnNavigationItemSelectedListener {

    private MainActivityBinding mainActivityBinding;
    private MainViewModel mainViewModel;
    private TickerPojo tickerPojo;
    private DrawerLayout drawer;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            // Get extra data included in the Intent
            //String message = intent.getStringExtra("message");
            //Log.d("receiver", "Got message: " + message);
            String action = intent.getAction();
            if (action.equals(Constants.REFRESH_DATA_BROADCAST)) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    tickerPojo = bundle.getParcelable(Constants.TICKER_OBJ);
                    if (mainActivityBinding != null) {
                        setTicker(tickerPojo);
                        Utils.makeShortToast(MainActivity.this, getString(R.string.ticker_updated_mins));
                    }
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (mainViewModel == null) {
            mainViewModel = new MainViewModel(this);
            mainActivityBinding.appBar.content.setMainViewModel(mainViewModel);
            mainActivityBinding.appBar.content.setTicker(tickerPojo);
            mainViewModel.fetchTickerData();
            mainViewModel.fetchFeedData();
        }

        mainActivityBinding.appBar.content.rcvFeeds.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mainActivityBinding.appBar.toolbar.setNavigationIcon(null);
        setSupportActionBar(mainActivityBinding.appBar.toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, mainActivityBinding.appBar.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainActivityBinding.navRightView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.REFRESH_DATA_BROADCAST);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, intentFilter);
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

    @Override
    public void executeBinding() {
        mainActivityBinding.executePendingBindings();
    }


    @Override
    public void setTicker(TickerPojo tickerPojo) {
        this.tickerPojo = tickerPojo;
        mainActivityBinding.appBar.content.setTicker(tickerPojo);
        executeBinding();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

        //If we have to discontinue jobs..
        /*if (mainViewModel != null) {
            mainViewModel.cancelJob(this);
        }*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        String text = "";
        if (id == R.id.nav_settings) {
            text = "Settings";
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            text = "gallery";
        } else if (id == R.id.nav_slideshow) {
            text = "slideshow";
        } else if (id == R.id.nav_manage) {
            text = "tools";
        }
        Utils.makeShortToast(this, "You have chosen " + text);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //drawer.closeDrawer(GravityCompat.START);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }
}
