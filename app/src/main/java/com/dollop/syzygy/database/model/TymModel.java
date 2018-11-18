package com.dollop.syzygy.database.model;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by CRUD-PC on 10/5/2016.
 */
public class TymModel {
    public static final String TABLE_NAME = "TymModel";
    public static final String KEY_ID = "_id";
    public static String KEY_STOP_TYM = "stopTym";

    public static void creteTable(SQLiteDatabase db) {
        String CREATE_CLIENTTABLE = "create table " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_STOP_TYM + " text " +
                ")";
        db.execSQL(CREATE_CLIENTTABLE);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    String stopTym;

    public String getStopTym() {
        return stopTym;
    }

    public void setStopTym(String stopTym) {
        this.stopTym = stopTym;
    }
}
