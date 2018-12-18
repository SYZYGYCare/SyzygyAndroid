package com.dollop.syzygy.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.caregiver.CareGiverMainActivity;
import com.dollop.syzygy.activity.caregiver.CareGiverStopWatchActivity;
import com.dollop.syzygy.activity.client.ClientMainActivity;
import com.dollop.syzygy.servics.LocationService;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;


public class SplashActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 42;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 43;
    final static int REQUEST_LOCATION = 199;
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private GoogleApiClient googleApiClient;
    private int STORAGE_PERMISSION_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        S.E("on create");
        noLocation();
    }

    @Override
    public void onConnected(Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                //Displaying another toast if permission is not granted
                setResult(Activity.RESULT_CANCELED);
                finish();
            }

        }
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    noLocation();
                } else {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            }

        }
    }

    // check whether gps is enabled
    public boolean noLocation() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        S.E("on loc");

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //  buildAlertMessageNoGps();
            S.E("on loc if");

            enableLoc();
            return true;
        } else {
            // code for login

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run()
                {
                    startService(new Intent(SplashActivity.this, LocationService.class));
                    if (SavedData.gettocken_id() .equals("")) {   Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();}else {

                        if (SavedData.gettocken_id().equals("Logout")) {

                            Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            if (SavedData.getStartTimer() != null) {
                                if (SavedData.getStartTimer().equals("yes")) {
                                    if (SavedData.gettockenUserType().toLowerCase().toLowerCase().equals("client")) {
                                        S.I_clear(SplashActivity.this, ClientMainActivity.class, null);
                                    } else {
                                        S.I_clear(SplashActivity.this, CareGiverStopWatchActivity.class, null);
                                    }
                                } else {
                                    if (SavedData.gettockenUserType().toLowerCase().equals("client")) {
                                        Intent intent = new Intent(SplashActivity.this, ClientMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Intent intent = new Intent(SplashActivity.this, CareGiverMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            } else {
                                if (SavedData.gettockenUserType().toLowerCase().equals("client")) {
                                    Intent intent = new Intent(SplashActivity.this, ClientMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(SplashActivity.this, CareGiverMainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }

                    }
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
        return false;
    }


    private void enableLoc() {

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(SplashActivity.this)
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {


                        }
                    }).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(
                                        (Activity) SplashActivity.this, REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_LOCATION:
                if (resultCode == Activity.RESULT_CANCELED) {
                    finish();
                } else if (resultCode == Activity.RESULT_OK) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            if (SavedData.gettocken_id().equals("")){
                                Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                                startActivity(intent);
                                finish();
                            } else{
                                if (SavedData.gettocken_id().equals("Logout")) {
                                    Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    if (SavedData.getStartTimer() != null) {
                                        if (SavedData.getStartTimer().equals("yes")) {
                                            S.I(SplashActivity.this, CareGiverStopWatchActivity.class, null);
                                        } else {
                                            if (SavedData.gettockenUserType().toLowerCase().equals("client")) {
                                                Intent intent = new Intent(SplashActivity.this, ClientMainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Intent intent = new Intent(SplashActivity.this, CareGiverMainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    } else {
                                        if (SavedData.gettockenUserType().toLowerCase().equals("client")) {
                                            Intent intent = new Intent(SplashActivity.this, ClientMainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent = new Intent(SplashActivity.this, CareGiverMainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }

                            }
                        }
                    }, SPLASH_DISPLAY_LENGTH);
                }
            default: {

                break;
            }
        }

    }



}











/*public class SplashActivity extends BaseActivity {
    Button caregiversplashId, clientId;
    SharedPreferences sharedPreferences;

    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    int PERMISSION_ALL = 1;

    @Override
    protected int getContentResId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("AniCheck" + SavedData.gettocken_id(), "check");

     *//*   Intent intent=new Intent(SplashActivity.this, LocationService.class);
        startService(intent);*//*

        LocationManager mlocManager = (LocationManager) SplashActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!enabled) {
            showDialogGPS();
        }

        if (!hasPermissions(SplashActivity.this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(SplashActivity.this, PERMISSIONS, PERMISSION_ALL);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (SavedData.gettocken_id() != null) {

                    if (SavedData.gettocken_id().equals("Logout")) {

                        Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        if (SavedData.gettockenUserType().toLowerCase().equals("CLIENT")) {
                            Intent intent = new Intent(SplashActivity.this, ClientMainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {

                            Intent intent = new Intent(SplashActivity.this, CareGiverMainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else {

                    Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 2000);
    }

    private void showDialogGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Enable GPS");
        builder.setMessage("Please enable GPS");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setNegativeButton("Ignore", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}*/
