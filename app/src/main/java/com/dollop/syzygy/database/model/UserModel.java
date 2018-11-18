package com.dollop.syzygy.database.model;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by CRUD-PC on 10/5/2016.
 */
public class UserModel {
    public static final String TABLE_NAME = "UserModel";
    public static final String KEY_ID = "_id";
    public static String KEY_NAME = "userName";
    public static String KEY_EMAIL = "userEmail";
    public static String KEY_MOBILE = "userMobile";
    public static String KEY_GENDER = "userGender";
    public static String KEY_ADDRESS = "address";
    public static String KEY_ADHAR_NUMBER = "adharNumber";
    public static String KEY_USER_IMAGE = "userImage";
    public static String KEY_USER_TYPE = "user_type";

    public static void creteTable(SQLiteDatabase db) {
        String CREATE_CLIENTTABLE = "create table " + TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_NAME + " text, " +
                KEY_EMAIL + " text, " +
                KEY_MOBILE + " text, " +
                KEY_GENDER + " text, " +
                KEY_ADDRESS + " text, " +
                KEY_ADHAR_NUMBER + " text, " +
                KEY_USER_IMAGE + " text, " +
                KEY_USER_TYPE + " text " +
                ")";
        db.execSQL(CREATE_CLIENTTABLE);
    }

    public static void dropTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    String userName, userEmail, userMobile, userGender, address, adharNumber, userImage, user_type;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdharNumber() {
        return adharNumber;
    }

    public void setAdharNumber(String adharNumber) {
        this.adharNumber = adharNumber;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
