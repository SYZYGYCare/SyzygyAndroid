package com.dollop.syzygy.utility;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AlertDialog;

public class GpsHelper {

    private Context context;
    private Boolean connected;

    public GpsHelper(Context context) {
        this.context = context;
    }

    public boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    public void gpsEnableDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Please enable GPS to use your current location. Press Setting to enable High Accuracy GPS")
                .setCancelable(false)
                .setTitle("Enable GPS")
                .setPositiveButton("Setting",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                context.startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
