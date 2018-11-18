package com.dollop.syzygy.sohel;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.dollop.syzygy.fragment.client.ClientMainFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by CRUD Technology on 11/25/2015.
 */
public class Utility {
    public static String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
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

    public static void hideSoftKeyboard(final Activity activity, View view) {
        //Set up touch listener for non-text box views to hide keyboard.

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });

        //If a layout container, iterate over children
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                hideSoftKeyboard(activity, innerView);
            }
        }
    }

    public static int getRandomNumber() {
        Random r = new Random();
        int Low = 0;
        int High = 9999;
        return r.nextInt(High - Low) + Low;
    }

    public static String getCurrentDate() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public static String strToDate(String start_dt) {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(start_dt);
            Log.e("date", date.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("dd/MM/yyyy");
        String finalString = newFormat.format(date);
        return finalString;
    }


}
