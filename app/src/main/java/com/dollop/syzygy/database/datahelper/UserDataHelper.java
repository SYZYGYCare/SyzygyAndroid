package com.dollop.syzygy.database.datahelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dollop.syzygy.database.DataManager;
import com.dollop.syzygy.database.model.UserModel;
import com.dollop.syzygy.sohel.S;

import java.util.ArrayList;

/**
 * Created by CRUD-PC on 10/7/2016.
 */
public class UserDataHelper {
    private static UserDataHelper instance;
    private SQLiteDatabase db;
    private DataManager dm;
    Context cx;

    public UserDataHelper(Context cx) {
        instance = this;
        this.cx = cx;
        dm = new DataManager(cx, DataManager.DATABASE_NAME, null, DataManager.DATABASE_VERSION);
    }

    public static UserDataHelper getInstance() {
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

    public void delete(int companyId) {
        open();
        db.delete(UserModel.TABLE_NAME, UserModel.KEY_ID + " = '" + companyId + "'", null);
        close();
    }

    public void deleteAll() {
        open();
        db.delete(UserModel.TABLE_NAME, null, null);
        close();
    }

    /*public boolean isExist(String id) {
        Cursor clientCur = db.rawQuery("SELECT * FROM " + UserModel.TABLE_NAME + " WHERE userMobile = '" + id + "'", null);
        boolean exist = (clientCur.getCount() > 0);
        clientCur.close();
        return exist;
    }*/

    private boolean isExist(UserModel userModel) {
        read();
        Cursor cur = db.rawQuery("select * from " + UserModel.TABLE_NAME + " where " + UserModel.KEY_MOBILE + "='" + userModel.getUserMobile() + "'", null);
        if (cur.moveToFirst()) {
            return true;
        }
        return false;
    }

    public void insertData(UserModel userModel) {
        open();
        ContentValues values = new ContentValues();
        values.put(UserModel.KEY_NAME, userModel.getUserName());
        values.put(UserModel.KEY_EMAIL, userModel.getUserEmail());
        values.put(UserModel.KEY_MOBILE, userModel.getUserMobile());
        values.put(UserModel.KEY_GENDER, userModel.getUserGender());
        values.put(UserModel.KEY_ADDRESS, userModel.getAddress());
        values.put(UserModel.KEY_ADHAR_NUMBER, userModel.getAdharNumber());
        values.put(UserModel.KEY_USER_IMAGE, userModel.getUserImage());
        values.put(UserModel.KEY_USER_TYPE, userModel.getUser_type());
        if (!isExist(userModel)) {
            S.E("insert successfully");
            db.insert(UserModel.TABLE_NAME, null, values);
        } else {
            S.E("update successfully" + userModel.getUserMobile());
            db.update(UserModel.TABLE_NAME, values, UserModel.KEY_MOBILE + "=" + userModel.getUserMobile(), null);
        }
        close();
    }

    public ArrayList<UserModel> getList() {
        ArrayList<UserModel> userItem = new ArrayList<UserModel>();
        read();
        Cursor cursor = db.rawQuery("select * from UserModel", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            do {
                UserModel taxiModel = new UserModel();
                taxiModel.setUserName(cursor.getString(cursor.getColumnIndex(UserModel.KEY_NAME)));
                taxiModel.setUserEmail(cursor.getString(cursor.getColumnIndex(UserModel.KEY_EMAIL)));
                taxiModel.setUserMobile(cursor.getString(cursor.getColumnIndex(UserModel.KEY_MOBILE)));
                taxiModel.setUserGender(cursor.getString(cursor.getColumnIndex(UserModel.KEY_GENDER)));
                taxiModel.setAddress(cursor.getString(cursor.getColumnIndex(UserModel.KEY_ADDRESS)));
                taxiModel.setAdharNumber(cursor.getString(cursor.getColumnIndex(UserModel.KEY_ADHAR_NUMBER)));
                taxiModel.setUserImage(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USER_IMAGE)));
                taxiModel.setUser_type(cursor.getString(cursor.getColumnIndex(UserModel.KEY_USER_TYPE)));
                userItem.add(taxiModel);
            } while ((cursor.moveToPrevious()));
            cursor.close();
        }
        close();
        return userItem;
    }
}