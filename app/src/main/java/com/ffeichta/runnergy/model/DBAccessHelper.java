package com.ffeichta.runnergy.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Fabian on 19.11.2015.
 */
public class DBAccessHelper extends SQLiteOpenHelper {

    private static final String TAG = com.ffeichta.runnergy.model.DBAccessHelper.class
            .getSimpleName();

    // Name of the file, which contains the database
    private static final String DATABASE_NAME = "runnergy.db";
    // If version changes, the database will be renewed
    private static final int DATABASE_VERSION = 7;

    private static String CREATE_ACTIVITIES = "CREATE TABLE activities(" +
            "aid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "atype TEXT NOT NULL, " +
            "adate TEXT NOT NULL, " +
            "aduration INTEGER NOT NULL, " +
            "tid INTEGER NOT NULL, " +
            "FOREIGN KEY (tid) REFERENCES tracks(tid) " +
            "ON DELETE CASCADE ON UPDATE CASCADE" +
            ");";

    private static String CREATE_COORDINATES = "CREATE TABLE coordinates(" +
            "cid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "clongitude REAL NOT NULL, " +
            "clatitude REAL NOT NULL, " +
            "cisstart INTEGER NOT NULL DEFAULT 0," +
            "cisend INTEGER NOT NULL DEFAULT 0," +
            "ctimefromstart INTEGER NOT NULL," +
            "aid INTEGER NOT NULL, " +
            "FOREIGN KEY (aid) REFERENCES activities(aid) " +
            "ON DELETE CASCADE ON UPDATE CASCADE" +
            "); ";

    private static String CREATE_TRACKS = "CREATE TABLE tracks(" +
            "tid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "tname TEXT NOT NULL UNIQUE, " +
            "tdistance REAL NOT NULL" +
            "); ";

    private static String CREATE_SETTINGS = "CREATE TABLE settings(" +
            "skey TEXT NOT NULL PRIMARY KEY, " +
            "svalue TEXT NOT NULL" +
            "); ";

    private static DBAccessHelper instance = null;

    /**
     * Private, because of Singleton-Pattern
     *
     * @param context
     */
    private DBAccessHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Returns an instance of this class
     *
     * @param context
     * @return
     */
    public static com.ffeichta.runnergy.model.DBAccessHelper getInstance(
            Context context) {
        if (instance == null)
            instance = new com.ffeichta.runnergy.model.DBAccessHelper(
                    context);
        return instance;
    }

    /**
     * Called if there is no database
     *
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TRACKS);
        sqLiteDatabase.execSQL(CREATE_ACTIVITIES);
        sqLiteDatabase.execSQL(CREATE_COORDINATES);
        sqLiteDatabase.execSQL(CREATE_SETTINGS);
        Log.d(TAG, "DB created");

    }

    /**
     * Called when the version of the database changes
     *
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(CREATE_TRACKS);
        sqLiteDatabase.execSQL(CREATE_ACTIVITIES);
        sqLiteDatabase.execSQL(CREATE_COORDINATES);
        sqLiteDatabase.execSQL(CREATE_SETTINGS);
        Log.d(TAG, "DB created after upgrade");
    }
}