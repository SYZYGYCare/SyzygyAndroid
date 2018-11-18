package com.dollop.syzygy.database.model;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by CRUD-PC on 10/5/2016.
 */
public class PathKmModel {
    public static final String TABLE_NAME = "PathKmModel";
    public static final String KEY_ID = "_id";
    public static String KEY_LATITUDE = "latitude";
    public static String KEY_LONGITUDE = "longitude";

    public static void creteTable(SQLiteDatabase db) {
        String CREATE_CLIENTTABLE = "create table " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_LATITUDE + " text ," +
                KEY_LONGITUDE + " text " +
                ")";
        db.execSQL(CREATE_CLIENTTABLE);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

  double latitude,longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
