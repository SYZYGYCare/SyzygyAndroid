package com.dollop.syzygy.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.dollop.syzygy.database.model.PathKmModel;
import com.dollop.syzygy.database.model.TymModel;
import com.dollop.syzygy.database.model.UserModel;

public class DataManager extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "syzygy";

    public DataManager(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TymModel.creteTable(db);
        UserModel.creteTable(db);
        PathKmModel.creteTable(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int paramInt1, int paramInt2) {
        TymModel.dropTable(db);
        UserModel.dropTable(db);
        PathKmModel.dropTable(db);
        onCreate(db);
    }
}