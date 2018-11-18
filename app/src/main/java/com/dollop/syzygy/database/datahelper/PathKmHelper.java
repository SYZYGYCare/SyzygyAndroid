package com.dollop.syzygy.database.datahelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dollop.syzygy.database.DataManager;
import com.dollop.syzygy.database.model.PathKmModel;
import com.dollop.syzygy.database.model.PathKmModel;
import com.dollop.syzygy.sohel.S;

import java.util.ArrayList;

/**
 * Created by CRUD-PC on 10/7/2016.
 */
public class PathKmHelper {
    private static PathKmHelper instance;
    Context cx;
    private SQLiteDatabase db;
    private DataManager dm;

    public PathKmHelper(Context cx) {
        instance = this;
        this.cx = cx;
        dm = new DataManager(cx, DataManager.DATABASE_NAME, null, DataManager.DATABASE_VERSION);
    }

    public static PathKmHelper getInstance() {
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
        db.delete(PathKmModel.TABLE_NAME, null, null);
        S.E("data delete success!");
        close();
    }

    public void insertData(PathKmModel cartModel) {
        open();
        ContentValues values = new ContentValues();
        values.put(PathKmModel.KEY_LATITUDE, cartModel.getLatitude());
        values.put(PathKmModel.KEY_LONGITUDE, cartModel.getLongitude());
        db.insert(PathKmModel.TABLE_NAME, null, values);
        close();
    }

    public ArrayList<PathKmModel> getList() {
        ArrayList<PathKmModel> arrayList = new ArrayList<PathKmModel>();
        read();
        Cursor cursor = db.rawQuery("select * from " + PathKmModel.TABLE_NAME, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            do {
                PathKmModel cartModel = new PathKmModel();
                cartModel.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(PathKmModel.KEY_LATITUDE))));
                cartModel.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(PathKmModel.KEY_LONGITUDE))));

                arrayList.add(cartModel);
            } while ((cursor.moveToPrevious()));
            cursor.close();
        }
        close();
        return arrayList;
    }
}