package com.dollop.syzygy.servics;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;

import com.dollop.syzygy.database.datahelper.PathKmHelper;
import com.dollop.syzygy.database.model.PathKmModel;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sohel on 10/7/2017.
 */

public class LocationService extends Service implements LocationListener {

    public static final int notify = 5000;
    protected LocationManager locationManager;
    String lat, lng;
    String mprovider;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   //recreate new
        mTimer.scheduleAtFixedRate(new TimeDisplay(), 20000, notify);   //Schedule task
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();    //For Cancel Timer
/*
        Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show();
*/
    }

    public boolean isUsable(Location location) {
        return location != null && location.hasAccuracy() && location.getAccuracy() < 800 && location.getSpeed() > 0;
    }

    private void getLocation() {
        try {

            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 10, this);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 20000, 100, this);

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 20000, 100, this);


            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            mprovider = locationManager.getBestProvider(criteria, false);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            Location location = locationManager.getLastKnownLocation(mprovider);

            if (location != null)
                onLocationChanged(location);
        } catch (Exception e) {
            e.printStackTrace();
            S.E("location : " + e.toString());
        }
    }

    boolean is_processing = false;
    @Override
    public void onLocationChanged(Location location) {

        if(is_processing == false)
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    is_processing = false;
                }
            },10000);

            is_processing = true;
            lat = String.valueOf(location.getLatitude());
            lng = String.valueOf(location.getLongitude());
            SavedData.saveLatitude(lat);
            SavedData.saveLONGITUDE(lng);
            if (isUsable(location))
            {

                S.E("saveLatitude" + lat);
                S.E("saveLONGITUDE" + lng);
                if (SavedData.getRideStart())
                {
                    PathKmModel pathKmModel = new PathKmModel();
                    pathKmModel.setLatitude(location.getLatitude());
                    pathKmModel.setLongitude(location.getLongitude());
                    PathKmHelper.getInstance().insertData(pathKmModel);
                }
            }
            new JSONParser(this).parseVollyStringRequestWithautProgressBar(Const.URL.UPDATE_CAREGIVER_LOCATION, 1, getPrams(), new Helper() {
                @Override
                public void backResponse(String response) {
                }
            });
        }

       /* Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private void getFcmID() {
        updateFCM();
    }

    private void updateFCM() {
        new JSONParser(this).parseVollyStringRequestWithautProgressBar(Const.URL.UPDATE_FCM_ID, 1, getPramsForFCM(), new Helper() {

            @Override
            public void backResponse(String response) {
                S.E("checkUPDATE_FCM_ID" + getPramsForFCM());
                S.E("checkUPDATE_FCM_ID" + response);
            }
        });
    }

    private Map<String, String> getPramsForFCM() {
        HashMap<String, String> prams = new HashMap<>();
        prams.put("token", SavedData.gettocken_id());
        prams.put("fcm_id", FirebaseInstanceId.getInstance().getToken());
        return prams;
    }

    @Override
    public void onProviderDisabled(String provider) {
      /*  S.T(LocationService.this, "Please Enable GPS and Internet");*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    private Map<String, String> getPrams() {
        HashMap<String, String> prams = new HashMap<>();
        String type =  SavedData.gettockenUserType();
        prams.put("token", SavedData.gettocken_id());
        prams.put("latitude", lat);
        prams.put("longitud", lng);
        prams.put("type", type);
        return prams;
    }

    //class TimeDisplay for handling task
    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (SavedData.gettocken_id() != null)
                        getFcmID();

                    getLocation();
                }
            });
        }
    }

}