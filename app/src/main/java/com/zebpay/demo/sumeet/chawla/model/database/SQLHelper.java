package com.zebpay.demo.sumeet.chawla.model.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sumeet on 30/7/16.
 */
public class SQLHelper extends SQLiteOpenHelper {

    //public static final String TABLE_LISTS = "tb_lists";
    private static SQLHelper instance;
    //Its gonna change when we will put upgrade condition!
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "zebpay.db";

    public static final String TABLE_USER_ACTIVITY = "tb_user_activity";

    public static final String COMMA_SEP = ",";

    public static final String USER_ID = "userId";

    public static final String ACTIVITY_TYPE = "activitytype";
    public static final String SOURCE_IMAGE_URL = "sourceimageurl";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String TIME = "time";
    public static final String REF_NUMBER = "refnumber";
    public static final String NAME = "name";
    public static final String REF_GUID = "refguid";
    public static final String PAYMENT_TYPE_ID = "paymenttypeid";
    public static final String PAYMENT_TYPE_GUID = "paymenttypeguid";


    private String SQL_CREATE_TABLE_USER_ACTIVITY = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_ACTIVITY + " ("
            + ACTIVITY_TYPE + " INTEGER" + COMMA_SEP
            + SOURCE_IMAGE_URL + " TEXT" + COMMA_SEP + TITLE + " TEXT" + COMMA_SEP + DESCRIPTION + " TEXT" + COMMA_SEP +
            TIME + " TEXT" + COMMA_SEP + REF_NUMBER + " TEXT" + COMMA_SEP +
            NAME + " TEXT" + COMMA_SEP + REF_GUID + " TEXT" + COMMA_SEP + PAYMENT_TYPE_ID + " TEXT" + COMMA_SEP + PAYMENT_TYPE_GUID + " TEXT" + ");";


    public static synchronized SQLHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SQLHelper(context);
        }
        return instance;
    }

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Settings we are storing in preference!
        //db.execSQL(SQL_CREATE_TABLE_USER_SETTINGS);
        db.execSQL(SQL_CREATE_TABLE_USER_ACTIVITY);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //I think we might require this for db to get upgrade!
        //when user migrate from previous version to new version!
        //We have to fetch old data from old tables and send it into new tables!
        //That which we get from sync!
    }

}
