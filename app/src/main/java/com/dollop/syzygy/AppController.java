package com.dollop.syzygy;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.dollop.syzygy.database.datahelper.PathKmHelper;
import com.dollop.syzygy.database.datahelper.TymDataHelper;
import com.dollop.syzygy.database.datahelper.UserDataHelper;

/**
 * Created by androidsys1 on 3/31/2017.
 */

public class AppController extends MultiDexApplication {

    private static AppController mInstance;
    public static int invoiceId = 0;
    private RequestQueue mRequestQueue;
    public static final String TAG = AppController.class.getSimpleName();

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        mInstance = this;
        new TymDataHelper(this);
        new UserDataHelper(this);
        new PathKmHelper(this);
    }
}

