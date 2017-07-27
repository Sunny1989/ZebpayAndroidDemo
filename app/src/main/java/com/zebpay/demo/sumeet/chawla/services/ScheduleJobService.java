package com.zebpay.demo.sumeet.chawla.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.android.volley.Response;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.zebpay.demo.sumeet.chawla.BuildConfig;
import com.zebpay.demo.sumeet.chawla.MainActivity;
import com.zebpay.demo.sumeet.chawla.R;
import com.zebpay.demo.sumeet.chawla.model.RequestModel;
import com.zebpay.demo.sumeet.chawla.pojos.ActivityFeedPojo;
import com.zebpay.demo.sumeet.chawla.pojos.TickerPojo;
import com.zebpay.demo.sumeet.chawla.utility.Constants;
import com.zebpay.demo.sumeet.chawla.utility.ZebpayPreference;
import com.zebpay.demo.sumeet.chawla.volley.CustomErrorListener;

/**
 * Created by Sumeet on 26-07-2017.
 */

public class ScheduleJobService extends JobService implements Response.Listener {

    private static final String TAG = ScheduleJobService.class.getSimpleName();
    private JobParameters parameters;

    @Override
    public boolean onStartJob(final JobParameters params) {
        //Offloading work to a new thread.
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                codeYouWantToRun(params);
            }
        }).start();*/

        parameters = params;
        String url = BuildConfig.TICKER_SERVER_URL;
        RequestModel.callNetworkRequest(this, TAG, this, false, null, new CustomErrorListener(this), null, url, TickerPojo.class);

        //This flag represents whether to off this service or not.
        //If you do have some task to do in another thread (any big task) then make it True.
        //Else make it False, if you have to off the service immediately.
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        //This flag represents if for some reason transaction did not happen..
        //So whether to start or not.
        //True -> Again resend.
        //False=-> Don't send
        return true;
    }

  /*  public void codeYouWantToRun(final JobParameters parameters) {
        try {

            Log.d(TAG, "completeJob: " + "jobStarted");
            //This task takes 2 seconds to complete.
            Thread.sleep(2000);

            Log.d(TAG, "completeJob: " + "jobFinished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //Tell the framework that the job has completed and doesnot needs to be reschedule
            jobFinished(parameters, true);
        }
    }*/

    @Override
    public void onResponse(Object response) {
        if (response instanceof TickerPojo) {
            TickerPojo tickerPojo = (TickerPojo) response;
            //For refreshing data and getting updated one!
            Intent intent = new Intent(Constants.REFRESH_DATA_BROADCAST);
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.TICKER_OBJ, tickerPojo);
            intent.putExtras(bundle);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            //Build Notification, if it satisfies condition.

            String totalValue = ZebpayPreference.getUserDetails(this, ZebpayPreference.TOTAL_VALUE);
            String diffValue = ZebpayPreference.getUserDetails(this, ZebpayPreference.DIFFERENCE_VALUE);
            String percentage = ZebpayPreference.getUserDetails(this, ZebpayPreference.PERCENTAGE);

            if (diffValue != null && !diffValue.isEmpty()) {
                //If its less than or greater than the values specified, then show it to user.
                int total = Integer.parseInt(totalValue);
                int market = Integer.parseInt(tickerPojo.market);
                int diff = Integer.parseInt(diffValue);
                if (total - market >= diff || total - market <= diff) {
                    buildNotification(tickerPojo);
                }
            }

            if (percentage != null && !percentage.isEmpty()) {
                ////If its greater or equals to the percentage specified, then show it to user.
                int market = Integer.parseInt(tickerPojo.market);
                int total = Integer.parseInt(totalValue);

                int changeValue = market - total;
                //% increase = Increase ÷ Original Number × 100.

                int percent = (changeValue / total) * 100;

                if (percent >= Integer.parseInt(percentage)) {
                    buildNotification(tickerPojo);
                }
            }

            if (parameters != null) {
                jobFinished(parameters, true);
            }
        }
    }


    private void buildNotification(TickerPojo tickerPojo) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_notifications);
        mBuilder.setContentTitle("Market : " + tickerPojo.market);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        mBuilder.setAutoCancel(true);

        /*            .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("My notification")
                .setContentText("Hello World!");*/
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        //stackBuilder.addParentStack(ResultActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // mNotificationId is a unique integer your app uses to identify the
        // notification. For example, to cancel the notification, you can pass its ID
        // number to NotificationManager.cancel().
        mNotificationManager.notify(0, mBuilder.build());
    }

}
