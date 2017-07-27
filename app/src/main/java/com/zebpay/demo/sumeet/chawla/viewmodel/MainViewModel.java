package com.zebpay.demo.sumeet.chawla.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableArrayList;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.volley.Response;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.zebpay.demo.sumeet.chawla.BuildConfig;
import com.zebpay.demo.sumeet.chawla.MainActivity;
import com.zebpay.demo.sumeet.chawla.R;
import com.zebpay.demo.sumeet.chawla.interfaces.MainInterface;
import com.zebpay.demo.sumeet.chawla.model.RequestModel;
import com.zebpay.demo.sumeet.chawla.model.database.SQLManager;
import com.zebpay.demo.sumeet.chawla.pojos.ActivityFeed;
import com.zebpay.demo.sumeet.chawla.pojos.ActivityFeedPojo;
import com.zebpay.demo.sumeet.chawla.pojos.TickerPojo;
import com.zebpay.demo.sumeet.chawla.services.ScheduleJobService;
import com.zebpay.demo.sumeet.chawla.utility.Constants;
import com.zebpay.demo.sumeet.chawla.utility.Utils;
import com.zebpay.demo.sumeet.chawla.utility.ZebpayPreference;
import com.zebpay.demo.sumeet.chawla.volley.CustomErrorListener;
import com.zebpay.demo.sumeet.chawla.volley.DiskBasedCaching.Util;

/**
 * Created by Sumeet on 26-07-2017.
 */

public class MainViewModel implements Response.Listener {

    private Context context;
    public ObservableArrayList<ActivityFeed> activityFeeds = new ObservableArrayList<>();
    private MainInterface mainInterface;
    private static final String TAG = MainViewModel.class.getSimpleName();
    private static FirebaseJobDispatcher dispatcher;

    public MainViewModel(Context context) {
        this.context = context;
        if (context instanceof MainActivity) {
            mainInterface = (MainInterface) context;
        }
    }

    /**
     * This method will fetch ticker data. And it will be giving updated data in 5 mins.
     */
    public void fetchTickerData() {
        getUpdatedTickerData(null);
        scheduleJob(context);
        //updateJob(dispatcher);
    }

    //Dispatcher Code.

    public static void scheduleJob(Context context) {
        //creating new firebase job dispatcher
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        //creating new job and adding it with dispatcher
        Job job = createJob(dispatcher);
        dispatcher.mustSchedule(job);
    }

    public static Job createJob(FirebaseJobDispatcher dispatcher) {
        Job job = dispatcher.newJobBuilder()
                //persist the task across boots
                .setLifetime(Lifetime.FOREVER)
                //.setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                //call this service when the criteria are met.
                .setService(ScheduleJobService.class)
                //unique id of the task
                .setTag(TAG)
                //don't overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                // We are mentioning that the job is periodic.
                //.setRecurring(true)
                // Run between 300 - 360 seconds from now.
                .setTrigger(Trigger.executionWindow(300, 360))
                // retry with exponential backoff
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                //.setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                //Run this job only when the network is available.
                .setConstraints(Constraint.ON_ANY_NETWORK).setRecurring(true)
                .build();
        return job;
    }

    //, Constraint.DEVICE_CHARGING

    /*public static Job updateJob(FirebaseJobDispatcher dispatcher) {
        Job newJob = dispatcher.newJobBuilder()
                //update if any task with the given tag exists.
                .setReplaceCurrent(false)
                //Integrate the job you want to start.
                .setService(ScheduleJobService.class)
                .setTag(TAG)
                // Run between 300 - 600 seconds (5-10 mins) from now.
                .setTrigger(Trigger.executionWindow(300, 600)).setConstraints(Constraint.ON_ANY_NETWORK).setRecurring(false)
                .build();
        return newJob;
    }*/

    public void cancelJob(Context context) {
        //FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        if (dispatcher != null) {
            //Cancel all the jobs for this package
            dispatcher.cancelAll();
            // Cancel the job for this tag
            dispatcher.cancel(TAG);
        }
    }


    /**
     * This method will get data and store in Database.
     * If the app has data then it will collect from database else from network.
     */
    public void fetchFeedData() {
        getFeedDataFromDB();
        if (activityFeeds.isEmpty()) {
            getFeedDataFromNetwork();
        } else {
            if (mainInterface != null) {
                mainInterface.executeBinding();
            }
        }
    }

    /**
     * This method will fetch feed data stored in DB.
     */
    private void getFeedDataFromDB() {
        SQLManager sqlManager = SQLManager.getInstance(context);
        sqlManager.openDatabase();
        activityFeeds = sqlManager.getFeedData();
        sqlManager.closeDatabase();
    }

    /**
     * This method will take feed data from network.
     */
    private void getFeedDataFromNetwork() {
        String url = BuildConfig.ACTIVITY_FEED_SERVER_URL;
        RequestModel.callNetworkRequest(context, TAG, this, false, null, new CustomErrorListener(context), null, url, ActivityFeedPojo.class);
    }

    @Override
    public void onResponse(Object response) {
        if (response instanceof ActivityFeedPojo) {
            ActivityFeedPojo activityFeedPojo = (ActivityFeedPojo) response;
            if (activityFeedPojo.returncode.equals(Constants.SUCCESS)) {
                activityFeeds.clear();
                activityFeeds.addAll(activityFeedPojo.activityFeed);

                if (activityFeeds != null && !activityFeeds.isEmpty()) {
                    storeFeedDataInDB(activityFeeds);
                    mainInterface.executeBinding();
                }
            } else {
                Utils.makeShortToast(context, context.getString(R.string.not_able_to_fetch_data));
            }
        } else if (response instanceof TickerPojo) {
            TickerPojo tickerPojo = (TickerPojo) response;
            mainInterface.setTicker(tickerPojo);

            //Setting the value in shared preference.
            ZebpayPreference.setUserDetails(context, ZebpayPreference.TOTAL_VALUE, tickerPojo.market);
        }
    }

    /**
     * This method will store feed data in database.
     *
     * @param activityFeeds
     */
    private void storeFeedDataInDB(ObservableArrayList<ActivityFeed> activityFeeds) {
        SQLManager sqlManager = SQLManager.getInstance(context);
        sqlManager.openDatabase();
        sqlManager.insertFeedData(activityFeeds);
        sqlManager.closeDatabase();
    }

    public void getUpdatedTickerData(View view) {
        boolean flag = Utils.isNetworkAvailable(context);
        if (!flag) {
            Utils.makeShortToast(context, context.getString(R.string.please_connect_internet));
            return;
        } else {
            if (view != null) {
                Utils.makeShortToast(view.getContext(), context.getString(R.string.getting_updated_data));
            }
        }

        String url = BuildConfig.TICKER_SERVER_URL;
        RequestModel.callNetworkRequest(context, TAG, this, false, ((AppCompatActivity) context).getSupportFragmentManager(), new CustomErrorListener(context), null, url, TickerPojo.class);
    }

}
