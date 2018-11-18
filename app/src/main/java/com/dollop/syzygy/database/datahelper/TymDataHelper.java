package com.dollop.syzygy.database.datahelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dollop.syzygy.database.DataManager;
import com.dollop.syzygy.database.model.TymModel;

import java.util.ArrayList;

/**
 * Created by CRUD-PC on 10/7/2016.
 */
public class TymDataHelper {
    private static TymDataHelper instance;
    private SQLiteDatabase db;
    private DataManager dm;
    Context cx;

    public TymDataHelper(Context cx) {
        instance = this;
        this.cx = cx;
        dm = new DataManager(cx, DataManager.DATABASE_NAME, null, DataManager.DATABASE_VERSION);
    }

    public static TymDataHelper getInstance() {
        return instance;
    }

    public void open() {
        db = dm.getWritableDatabase();
    }

    public void close() {
        db.close();
    }

    public void read() {
        db = dm.getReadableDatabase();
    }

    public void deleteAll() {
        open();
        db.delete(TymModel.TABLE_NAME, null, null);
        close();
    }

    public void insertData(TymModel cartModel) {
        open();
        ContentValues values = new ContentValues();
        values.put(TymModel.KEY_STOP_TYM, cartModel.getStopTym());
        db.insert(TymModel.TABLE_NAME, null, values);
        close();
    }

    public ArrayList<TymModel> getList() {
        ArrayList<TymModel> arrayList = new ArrayList<TymModel>();
        read();
        Cursor cursor = db.rawQuery("select * from " + TymModel.TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            do {
                TymModel cartModel = new TymModel();
                cartModel.setStopTym(cursor.getString(cursor.getColumnIndex(TymModel.KEY_STOP_TYM)));

                arrayList.add(cartModel);
            } while ((cursor.moveToPrevious()));
            cursor.close();
        }
        close();
        return arrayList;
    }
}