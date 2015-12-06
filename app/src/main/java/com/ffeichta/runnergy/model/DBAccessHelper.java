package com.ffeichta.runnergy.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

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
    private static String DROP_ACTIVITIES = "DROP TABLE IF EXISTS activities;";

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
    private static String DROP_COORDINATES = "DROP TABLE IF EXISTS coordinates;";

    private static String CREATE_TRACKS = "CREATE TABLE tracks(" +
            "tid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "tname TEXT NOT NULL UNIQUE, " +
            "tdistance REAL NOT NULL" +
            "); ";
    private static String DROP_TRACKS = "DROP TABLE IF EXISTS tracks;";

    private static String CREATE_SETTINGS = "CREATE TABLE settings(" +
            "skey TEXT NOT NULL PRIMARY KEY, " +
            "svalue TEXT NOT NULL" +
            "); ";
    private static String DROP_SETTINGS = "DROP TABLE IF EXISTS settings;";

    private static String INSERT_TRACK1 = "INSERT INTO tracks(tid, tname, tdistance) "
            + "  VALUES(1, \"Ahornach-Rein\", 12529);";
    private static String INSERT_TRACK2 = "INSERT INTO tracks(tid, tname, tdistance) "
            + "  VALUES(2, \"Ahornach-Knutton\", 16981);";

    private static String INSERT_ACTIVITY1 = "INSERT INTO activities(aid, atype, adate, aduration, tid) "
            + "  VALUES(1, \"RUNNING\", \"2015-11-10 17:11:59.651\", 660, 1);";
    private static String INSERT_ACTIVITY2 = "INSERT INTO activities(aid, atype, adate, aduration, tid) "
            + "  VALUES(2, \"CYCLING\", \"2015-12-29 11:51:33.974\", 5108, 2);";

    private static String INSERT_COORDINATE1 = "INSERT INTO coordinates(cid, clongitude, clatitude, cisstart, cisend, ctimefromstart, aid) "
            + "  VALUES(1, ..., ..., 1, 0, 0, 1);";
    private static String INSERT_COORDINATE2 = "INSERT INTO coordinates(cid, clongitude, clatitude, cisstart, cisend, ctimefromstart, aid) "
            + "  VALUES(2, ..., ..., 0, 0, 5, 1);";
    private static String INSERT_COORDINATE3 = "INSERT INTO coordinates(cid, clongitude, clatitude, cisstart, cisend, ctimefromstart, aid) "
            + "  VALUES(3, ..., ..., 0, 0, 10, 1);";
    private static String INSERT_COORDINATE4 = "INSERT INTO coordinates(cid, clongitude, clatitude, cisstart, cisend, ctimefromstart, aid) "
            + "  VALUES(4, ..., ..., 0, 1, 15, 1);";

    private static String INSERT_SETTING1 = "INSERT INTO settings(skey, svalue) "
            + "  VALUES(\"unit_of_length\", \"km\");";

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
        insertDefaultValues(sqLiteDatabase);
        Log.d(TAG, "Default values inserted");
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
        sqLiteDatabase.execSQL(DROP_TRACKS);
        sqLiteDatabase.execSQL(DROP_ACTIVITIES);
        sqLiteDatabase.execSQL(DROP_COORDINATES);
        sqLiteDatabase.execSQL(DROP_SETTINGS);
        onCreate(sqLiteDatabase);
    }

    private void insertDefaultValues(SQLiteDatabase sqlLiteDatabase) {
        ContentValues values = new ContentValues(5);
        values.put("skey", "unit_of_length");
        values.put("svalue", "km");
        sqlLiteDatabase.insertOrThrow("settings", null, values);
    }

    private void insertTestData(SQLiteDatabase sqlLiteDatabase) {
        sqlLiteDatabase.execSQL(INSERT_TRACK1);
        sqlLiteDatabase.execSQL(INSERT_TRACK2);
        sqlLiteDatabase.execSQL(INSERT_ACTIVITY1);
        sqlLiteDatabase.execSQL(INSERT_ACTIVITY2);
       /* sqlLiteDatabase.execSQL(INSERT_COORDINATE1);
        sqlLiteDatabase.execSQL(INSERT_COORDINATE2);
        sqlLiteDatabase.execSQL(INSERT_COORDINATE3);
        sqlLiteDatabase.execSQL(INSERT_COORDINATE4);*/
    }

    /**
     * Methods that select something from the database
     */

    /**
     * @return
     */
    public ArrayList<Track> getTracks() {
        ArrayList<Track> ret = null;
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = getWritableDatabase();
            c = db.rawQuery("SELECT tid, tname, tdistance, (SELECT COUNT(*) FROM activities WHERE tracks.tid=activities.tid)" + "  FROM tracks ORDER BY 4 DESC;", null);
            while (c.moveToNext()) {
                if (ret == null)
                    ret = new ArrayList<Track>();
                ret.add(new Track(c.getInt(0), c.getString(1), c.getDouble(2)));
            }
            if (ret != null) {
                // If activities are found, then they are inserted
                for (int i = 0; i < ret.size(); i++)
                    ret.get(i).setActivities(getActivities(ret.get(i)));
            }
        } catch (SQLiteException e) {
            Log.d(TAG, "Error in getTracks(): " + e.getMessage());
        } finally {
            try {
                c.close();
            } catch (Exception e) {
            }
            try {
                db.close();
            } catch (Exception e) {
            }
        }
        if (ret != null) {
            Log.d(TAG, "getTracks() was successful");
        }
        return ret;
    }

    /**
     * @param t
     * @return
     */
    public ArrayList<Activity> getActivities(Track t) {
        ArrayList<Activity> ret = null;
        if (t != null) {
            SQLiteDatabase db = null;
            Cursor c = null;
            try {
                db = getWritableDatabase();
                c = db.rawQuery("SELECT * " + "  FROM activities "
                                + "  WHERE tid = ? " + "ORDER BY adate;",
                        new String[]{String.valueOf(t.getId())});
                while (c.moveToNext()) {
                    if (ret == null)
                        ret = new ArrayList<Activity>();
                    ret.add(new Activity(c.getInt(0), Activity.Type.valueOf(c.getString(1)), c.getLong(2), c.getInt(3), t));
                }
            } catch (SQLiteException e) {
                Log.d(TAG, "Error in getActivities(): " + e.getMessage());
            } finally {
                try {
                    c.close();
                } catch (Exception e) {
                }
                try {
                    db.close();
                } catch (Exception e) {
                }
            }
        }
        if (ret != null) {
            Log.d(TAG, "getActivities() was successful");
        }
        return ret;
    }

    /**
     * @param id
     * @return
     */
    public Activity getActivity(int id) {
        Activity ret = null;
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = getWritableDatabase();
            c = db.rawQuery("SELECT * " + "  FROM activities "
                            + "  WHERE aid = ?;",
                    new String[]{String.valueOf(id)});
            if (c.moveToFirst()) {
                ret = new Activity(c.getInt(0), Activity.Type.valueOf(c.getString(1)), c.getLong(2), c.getInt(3));
            }
        } catch (SQLiteException e) {
            Log.d(TAG, "Error in getActivity(): " + e.getMessage());
        } finally {
            try {
                c.close();
            } catch (Exception e) {
            }
            try {
                db.close();
            } catch (Exception e) {
            }
        }
        if (ret != null)
            Log.d(TAG, "getActivity() was successful");
        return ret;
    }

    /**
     * @param a
     * @return
     */
    public ArrayList<Coordinate> getCoordinates(Activity a) {
        ArrayList<Coordinate> ret = null;
        if (a != null) {
            SQLiteDatabase db = null;
            Cursor c = null;
            try {
                db = getWritableDatabase();
                c = db.rawQuery("SELECT * " + "  FROM coordinates "
                                + "  WHERE aid = ? " + ";",
                        new String[]{String.valueOf(a.getId())});
                while (c.moveToNext()) {
                    if (ret == null)
                        ret = new ArrayList<Coordinate>();
                    ret.add(new Coordinate(c.getInt(0), c.getDouble(1), c.getDouble(2), c.getInt(3) > 0, c.getInt(4) > 0, c.getInt(5), a));
                }
            } catch (SQLiteException e) {
                Log.d(TAG, "Error in getCoordinates(): " + e.getMessage());
            } finally {
                try {
                    c.close();
                } catch (Exception e) {
                }
                try {
                    db.close();
                } catch (Exception e) {
                }
            }
        }
        if (ret != null) {
            Log.d(TAG, "getCoordinates() was successful");
        }
        return ret;
    }

    /**
     * @param id
     * @return
     */
    public Coordinate getCoordinate(int id) {
        Coordinate ret = null;
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = getWritableDatabase();
            c = db.rawQuery("SELECT * " + "  FROM coordinates "
                            + "  WHERE cid = ?;",
                    new String[]{String.valueOf(id)});
            if (c.moveToFirst()) {
                ret = new Coordinate(c.getInt(0), c.getDouble(1), c.getDouble(2), c.getInt(3) > 0, c.getInt(4) > 0, c.getInt(5));
            }
        } catch (SQLiteException e) {
            Log.d(TAG, "Error in getCoordinate(): " + e.getMessage());
        } finally {
            try {
                c.close();
            } catch (Exception e) {
            }
            try {
                db.close();
            } catch (Exception e) {
            }
        }
        if (ret != null)
            Log.d(TAG, "getCoordinate() was successful");
        return ret;
    }

    /**
     * @param longitude
     * @param latitude
     * @param id
     * @return
     */
    public int getIDOfClosestCoordinateInActivity(double longitude, double latitude, int id) {
        int ret = 0;
        return ret;
    }

    /**
     * @return
     */
    public ArrayList<Setting> getSettings() {
        ArrayList<Setting> ret = null;
        SQLiteDatabase db = null;
        Cursor c = null;
        try {
            db = getWritableDatabase();
            c = db.rawQuery("SELECT * " + "  FROM setings;", null);
            while (c.moveToNext()) {
                if (ret == null)
                    ret = new ArrayList<Setting>();
                ret.add(new Setting(c.getString(0), c.getString(1)));
            }
        } catch (SQLiteException e) {
            Log.d(TAG, "Error in getSettings(): " + e.getMessage());
        } finally {
            try {
                c.close();
            } catch (Exception e) {
            }
            try {
                db.close();
            } catch (Exception e) {
            }
        }
        if (ret != null) {
            Log.d(TAG, "getSettings() was successful");
        }
        return ret;
    }

    /**
     * Methods that insert something in the database
     */

    /**
     * @param t
     * @return
     */
    public int insertTrack(Track t) {
        int ret = 0;
        return ret;
    }

    /**
     * @param a
     * @return
     */
    public int insertActivity(Activity a) {
        int ret = 0;
        return ret;
    }

    /**
     * @param c
     * @return
     */
    public int insertCoordinate(Coordinate c) {
        int ret = 0;
        return ret;
    }

    /**
     * @param s
     * @return
     */
    public int insertSetting(Setting s) {
        int ret = 0;
        return ret;
    }

    /**
     * Methods that update something in the database
     */

    /**
     * @param t
     * @return
     */
    public int updateTrack(Track t) {
        int ret = 0;
        return ret;
    }

    /**
     * @param s
     * @return
     */
    public int updateSetting(Setting s) {
        int ret = 0;
        return ret;
    }

    /**
     * Methods that delete something from the database
     */

    /**
     * @param t
     * @return
     */
    public int deleteTrack(Track t) {
        int ret = 0;
        return ret;
    }

    /**
     * @param a
     * @return
     */
    public int deleteActivity(Activity a) {
        int ret = 0;
        return ret;
    }
}