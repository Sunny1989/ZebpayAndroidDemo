package com.zebpay.demo.sumeet.chawla.model.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.databinding.ObservableArrayList;

import com.zebpay.demo.sumeet.chawla.pojos.ActivityFeed;

import java.util.List;

/**
 * Created by sumeet on 1/8/16.
 */
public class SQLManager {

    private static SQLManager instance;
    private static SQLHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    private SQLManager(Context context) {
        mContext = context;
    }

    public static synchronized SQLManager getInstance(Context context) {
        if (instance == null) {
            instance = new SQLManager(context);
        }

        if (mDatabaseHelper == null) {
            mDatabaseHelper = SQLHelper.getInstance(context.getApplicationContext());
        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase() {
        // Opening new database
        if (mDatabase == null || !mDatabase.isOpen()) {
            mDatabase = mDatabaseHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
        }
    }


    public ObservableArrayList<ActivityFeed> getFeedData() {
        ObservableArrayList<ActivityFeed> activityFeeds = new ObservableArrayList<>();
        try {
            String query = "select * from " + SQLHelper.TABLE_USER_ACTIVITY;
            Cursor cursor = mDatabase.rawQuery(query, null);
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    ActivityFeed activityFeed = new ActivityFeed();
                    activityFeed.AcitvityType = String.valueOf(cursor.getInt(cursor.getColumnIndex(SQLHelper.ACTIVITY_TYPE)));
                    activityFeed.SourceImageUrl = cursor.getString(cursor.getColumnIndex(SQLHelper.SOURCE_IMAGE_URL));
                    activityFeed.Title = cursor.getString(cursor.getColumnIndex(SQLHelper.TITLE));
                    activityFeed.Description = cursor.getString(cursor.getColumnIndex(SQLHelper.DESCRIPTION));
                    activityFeed.Time = cursor.getString(cursor.getColumnIndex(SQLHelper.TIME));
                    activityFeed.RefNumber = cursor.getString(cursor.getColumnIndex(SQLHelper.REF_NUMBER));
                    activityFeed.Name = cursor.getString(cursor.getColumnIndex(SQLHelper.NAME));
                    activityFeed.RefGuid = cursor.getString(cursor.getColumnIndex(SQLHelper.REF_GUID));
                    activityFeed.PaymentTypeId = cursor.getString(cursor.getColumnIndex(SQLHelper.PAYMENT_TYPE_ID));
                    activityFeed.PaymentTypeGuid = cursor.getString(cursor.getColumnIndex(SQLHelper.PAYMENT_TYPE_GUID));
                    activityFeeds.add(activityFeed);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activityFeeds;
    }

    /**
     * This method will insert multiple record in one transaction.
     *
     * @param activityFeeds
     */
    public void insertFeedData(List<ActivityFeed> activityFeeds) {
        String sql = "INSERT INTO " + SQLHelper.TABLE_USER_ACTIVITY + " (" + SQLHelper.ACTIVITY_TYPE + SQLHelper.COMMA_SEP + SQLHelper.SOURCE_IMAGE_URL + SQLHelper.COMMA_SEP +
                SQLHelper.TITLE + SQLHelper.COMMA_SEP + SQLHelper.DESCRIPTION + SQLHelper.COMMA_SEP + SQLHelper.TIME + SQLHelper.COMMA_SEP +
                SQLHelper.REF_NUMBER + SQLHelper.COMMA_SEP + SQLHelper.NAME + SQLHelper.COMMA_SEP +
                SQLHelper.REF_GUID + SQLHelper.COMMA_SEP + SQLHelper.PAYMENT_TYPE_ID + SQLHelper.COMMA_SEP +
                SQLHelper.PAYMENT_TYPE_GUID + ") VALUES (?,?,?,?,?,?,?,?,?,?);";
        SQLiteStatement statement = mDatabase.compileStatement(sql);

        try {
            mDatabase.beginTransaction();
            for (ActivityFeed activityFeed : activityFeeds) {
                statement.clearBindings();
                statement.bindLong(1, Integer.parseInt(activityFeed.AcitvityType));
                statement.bindString(2, activityFeed.SourceImageUrl == null ? "" : activityFeed.SourceImageUrl);
                statement.bindString(3, activityFeed.Title == null ? "" : activityFeed.Title);
                statement.bindString(4, activityFeed.Description == null ? "" : activityFeed.Description);
                statement.bindString(5, activityFeed.Time == null ? "" : activityFeed.Time);
                statement.bindString(6, activityFeed.RefNumber == null ? "" : activityFeed.RefNumber);
                statement.bindString(7, activityFeed.Name == null ? "" : activityFeed.Name);
                statement.bindString(8, activityFeed.RefGuid == null ? "" : activityFeed.RefGuid);
                statement.bindString(9, activityFeed.PaymentTypeId == null ? "" : activityFeed.PaymentTypeId);
                statement.bindString(10, activityFeed.PaymentTypeGuid == null ? "" : activityFeed.PaymentTypeGuid);
                statement.execute();
            }
            mDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mDatabase.endTransaction();
        }
    }


}

