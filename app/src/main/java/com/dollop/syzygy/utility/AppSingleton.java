package com.dollop.syzygy.utility;


import com.dollop.syzygy.direction.DurationCallback;

public class AppSingleton {
    private boolean mLocationPermissionGranted;
    private DurationCallback durationCallback;

    private static final AppSingleton ourInstance = new AppSingleton();

    public static AppSingleton getInstance() {
        return ourInstance;
    }

    private AppSingleton() {
    }

    public boolean ismLocationPermissionGranted() {
        return mLocationPermissionGranted;
    }

    public void setmLocationPermissionGranted(boolean mLocationPermissionGranted) {
        this.mLocationPermissionGranted = mLocationPermissionGranted;
    }

    public DurationCallback getDurationCallback() {
        return durationCallback;
    }

    public void setDurationCallback(DurationCallback durationCallback) {
        this.durationCallback = durationCallback;
    }
}
