package com.dollop.syzygy.direction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.view.animation.LinearInterpolator;

import com.dollop.syzygy.R;
import com.dollop.syzygy.fragment.caregiver.CareGiverMainFragment;
import com.dollop.syzygy.fragment.client.ClientMainFragment;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.utility.AppSingleton;
import com.dollop.syzygy.utility.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class GetUserDirectionsData extends AsyncTask<Object, String, String> {

    GoogleMap mMap;
    String url;
    String googleDirectionsData;
    float rotate = 0;

public static float old_rotation = 0;

    String duration, distance;
    LatLng latLng;
    LatLng pickup;
    private ArrayList<Polyline> polylines;

    @Override
    protected String doInBackground(Object... objects)
    {
        try
        {
            mMap = (GoogleMap) objects[0];
            url = (String) objects[1];
            latLng = (LatLng) objects[2];



            if(objects!= null && objects.length > 4)
                pickup = (LatLng) objects[4];

            if(objects!= null && objects.length > 3)
            {
                if( objects[3] != null)
                    rotate = (float) objects[3];
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googleDirectionsData = downloadUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googleDirectionsData;
    }

    @Override
    protected void onPostExecute(String s) {
        String[] directionsList;
        HashMap<String, String> duration;
        DataParser parser = new DataParser();
        directionsList = parser.parseDirections(s);
        displayDirection(directionsList);
        if (AppSingleton.getInstance().getDurationCallback() != null)
        {
            if (s != null && s.length() > 0) {
                duration = parser.parseDuration(s);
                AppSingleton.getInstance().getDurationCallback().onGetDuration(duration.get("distance"), duration.get("duration"));
            }
        }


        try
        {
           // List<Marker> markers = new ArrayList<>();
            Marker carMarkers , eMarker;
            if (CareGiverMainFragment.old_pickupLat != 0 && CareGiverMainFragment.old_pickupLong != 0)
            {


                if (SavedData.getHiredUserType().equals(Constants.HIRE_AMBULANCE))
                {
                    carMarkers = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(CareGiverMainFragment.old_pickupLat, CareGiverMainFragment.old_pickupLat))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_car)));
                    carMarkers.setRotation(CareGiverMainFragment.old_rotation);
                } else {
                    rotate =0;
                    carMarkers = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(CareGiverMainFragment.old_pickupLat, CareGiverMainFragment.old_pickupLat))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.doctor_other_small_size)));
                    carMarkers.setRotation(CareGiverMainFragment.old_rotation);
                }


            }
            else
            {
                if (SavedData.getHiredUserType().equals(Constants.HIRE_AMBULANCE))
                {
                    carMarkers = mMap.addMarker(new MarkerOptions()
                            .position(pickup)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_car)));
                    carMarkers.setRotation(0);
                } else {
                    rotate =0;
                    carMarkers = mMap.addMarker(new MarkerOptions()
                            .position(pickup)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.doctor_other_small_size)));
                    carMarkers.setRotation(0);
                }



            }

            if(ClientMainFragment.is_first_time)
            {
                ClientMainFragment.is_first_time = false;
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pickup, 14));

            }

            eMarker = mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_green)));
            //markers.add(eMarker);
           // markers.add(carMarker);
            if (CareGiverMainFragment.old_pickupLat != 0 && CareGiverMainFragment.old_pickupLong != 0)
            {
                LatLng location2 = new LatLng(CareGiverMainFragment.old_pickupLat, CareGiverMainFragment.old_pickupLong);
                carMarker = carMarkers;
                animateMarkerNew(location2, pickup, carMarker);
            }

            CareGiverMainFragment.old_pickupLat = pickup.latitude;
            CareGiverMainFragment.old_pickupLong = pickup.longitude;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void displayDirection(String[] directionsList) {

        int count = directionsList.length;
        polylines = new ArrayList<>();
        mMap.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < count; i++) {
                PolylineOptions options = new PolylineOptions();
                options.color(Color.BLACK);
                options.width(20);
                options.addAll(PolyUtil.decode(directionsList[i]));
                Polyline line = mMap.addPolyline(options);

                PolylineOptions options1 = new PolylineOptions();
               // options1.color(Color.parseColor("#18addb"));
                options1.color(Color.parseColor("#d1395c"));
                options1.width(14);
                options1.addAll(PolyUtil.decode(directionsList[i]));
                Polyline line1 = mMap.addPolyline(options1);

                polylines.add(line1);
                polylines.add(line);

            }
        } else {
            for (int i = 0; i < count; i++) {
                PolylineOptions options = new PolylineOptions();
                options.color(Color.BLACK);
                options.width(14);
                options.addAll(PolyUtil.decode(directionsList[i]));
                Polyline line = mMap.addPolyline(options);

                PolylineOptions options1 = new PolylineOptions();
                // options1.color(Color.parseColor("#18addb"));
                options1.color(Color.parseColor("#d1395c"));
                options1.width(10);
                options1.addAll(PolyUtil.decode(directionsList[i]));
                Polyline line1 = mMap.addPolyline(options1);

                polylines.add(line1);
                polylines.add(line);
            }
        }
    }

    Marker carMarker;
Handler rotate_handler = new Handler();

    Runnable start_rotation = new Runnable()
    {
        @Override
        public void run()
        {
           try
           {
               rotate_handler.removeCallbacks(start_rotation);
               if(total_rotation < diff)
               {
                   if (is_postive_rotate)
                   {

                       carMarker.setRotation((CareGiverMainFragment.old_rotation + total_rotation)%360);

                   } else
                   {
                       if (CareGiverMainFragment.old_rotation - total_rotation >= 0)
                       {
                           carMarker.setRotation(CareGiverMainFragment.old_rotation - total_rotation);
                       }
                       else
                       {
                           carMarker.setRotation(360- total_rotation);
                       }



                   }



                   total_rotation = deivde + total_rotation;

                   rotate_handler.postDelayed(start_rotation,150);
               }
               else
               {
                   CareGiverMainFragment.old_rotation = rotate;
                   carMarker.setRotation(CareGiverMainFragment.old_rotation );
               }

           }
           catch (Exception e) {
           e.printStackTrace();
           }
        }
    };


float deivde = 0;
float total_rotation = 0;
boolean is_postive_rotate = false;
    float diff = 0;

    private void animateMarkerNew(final LatLng startPosition, final LatLng destination, final Marker marker) {

        if (marker != null)
        {

            deivde =0;
            total_rotation =0;
            diff =0;

          /*  if(rotate >= CareGiverMainFragment.old_rotation)
            {
                diff = rotate - CareGiverMainFragment.old_rotation;
                is_postive_rotate = true;

            }
            else
            {
                diff = CareGiverMainFragment.old_rotation - rotate;
                is_postive_rotate = false;
            }

            deivde = diff/20;*/


            if (rotate >= CareGiverMainFragment.old_rotation)
            {
                diff = rotate - CareGiverMainFragment.old_rotation;
                is_postive_rotate = true;
                if (diff > 180)
                {
                    diff = 360 - diff;
                    is_postive_rotate = false;
                }

            } else {
                diff = CareGiverMainFragment.old_rotation - rotate;
                is_postive_rotate = false;
                if (diff > 180)
                {
                    diff = 360 - diff;
                    is_postive_rotate = true;
                }


            }

            deivde = diff / 10;



            rotate_handler.postDelayed(start_rotation,10);

            final LatLng endPosition = new LatLng(destination.latitude, destination.longitude);


            final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(3000); // duration 3 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
            {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        /*map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                .target(newPosition)
                                .zoom(18f)
                                .build()));*/
                        //float bearing = getBearing(startPosition, new LatLng(newPosition.latitude, newPosition.longitude));

                        /*rotate = rotate  + 10;
                        if(rotate >360)
                            rotate =0;*/





                    } catch (Exception ex) {
                        //I don't care atm..
                    }
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    // if (mMarker != null) {
                    // mMarker.remove();
                    // }
                    // mMarker = googleMap.addMarker(new MarkerOptions().position(endPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));

                }
            });
            valueAnimator.start();
        }
    }


    private interface LatLngInterpolatorNew {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements GetUserDirectionsData.LatLngInterpolatorNew {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }


}

