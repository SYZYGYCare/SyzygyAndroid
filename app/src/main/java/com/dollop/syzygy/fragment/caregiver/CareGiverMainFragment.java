package com.dollop.syzygy.fragment.caregiver;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.syzygy.Model.CaregiverCurrentStatus;
import com.dollop.syzygy.Model.CurrentStatusResponse;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.ChatActivity;
import com.dollop.syzygy.activity.caregiver.CareGiverMainActivity;
import com.dollop.syzygy.activity.caregiver.CareGiverRatingActivity;
import com.dollop.syzygy.activity.caregiver.CareGiverStopWatchActivity;
import com.dollop.syzygy.activity.caregiver.CareGiverSummeryPageActivity;
import com.dollop.syzygy.activity.client.AmbulanceSummeryPage;
import com.dollop.syzygy.database.datahelper.PathKmHelper;
import com.dollop.syzygy.direction.GetDirectionsData;
import com.dollop.syzygy.notification.Config;
import com.dollop.syzygy.notification.NotificationUtils;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.utility.GPSTrackerNew;
import com.dollop.syzygy.utility.GpsHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class CareGiverMainFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int REQUEST_CODE_AUTOCOMPLETE = 3535;
    Button mainFragmentBtnAccept;
    Button mainFragmentBtnDeny;
    Button mainFragmentBtnStart;
    LinearLayout mainFragmentBtnCancel;
    LinearLayout call_linerlayout;
    LinearLayout chat_linerlayout;
    @BindView(R.id.content_care_giver_main)
    RelativeLayout content_care_giver_main;
    LinearLayout buttonLayout;
    LinearLayout buttonLayoutStart;

    ImageView buttomUserImage;
    ImageView buttomUserImageStart;
    TextView buttomUserName;
    TextView buttomUserNameStart;
    TextView buttomUserGender;
    TextView buttomUserGenderStart;
    String typeOfCaregiver = "";
    GoogleMap googleMap;
    LinearLayout linearLayoutEndId;
    TextView end_ride = null, reset_address = null;
    private GPSTrackerNew gps;
    private double latitude_current_new;
    private double longitude_current_new;
    int PERMISSION_ALL = 1;
    CaregiverCurrentStatus caregiverCurrentStatus = null;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String lng;
    private String lat;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    //    private String client_id;
    private String requestLongitud;
    private String requestLatitude;
    private GoogleApiClient mGoogleApiClient;
    private LatLng origin_new;
    private LatLng dest_new;
    boolean is_destination_done = false;
    private Marker carMarker, eMarker;
    private LatLng carLastLocation, carCurrentLocation;
    private GpsHelper gpsHelper;
    private boolean isFirstTime;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private double pickupLat, pickupLong, destLat, destLong;
    public static double old_pickupLat = 0, old_pickupLong = 0;
    private String FinalSource = "";
    private String FinalDestination = "";
    private String callnumber;
    String complete_status = "0";
    private int RESULT_CANCELED = 1;
    public static float old_rotation = 0;

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


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.content_care_giver_main, container, false);

        ((CareGiverMainActivity) getActivity()).launchFragmentTitle(getString(R.string.app_name));

        this.gps = new GPSTrackerNew(getActivity());
        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        isFirstTime = true;
        String to = SavedData.gettocken_id();
        buttonLayout = (LinearLayout) rootView.findViewById(R.id.buttonLayout);
        buttonLayoutStart = (LinearLayout) rootView.findViewById(R.id.buttonLayoutStart);
        linearLayoutEndId = (LinearLayout) rootView.findViewById(R.id.linearLayoutEndId);

        mainFragmentBtnAccept = (Button) rootView.findViewById(R.id.mainFragmentBtnAccept);
        mainFragmentBtnDeny = (Button) rootView.findViewById(R.id.mainFragmentBtnDeny);
        mainFragmentBtnStart = (Button) rootView.findViewById(R.id.mainFragmentBtnStart);
        mainFragmentBtnCancel = (LinearLayout) rootView.findViewById(R.id.mainFragmentBtnCancel);
        call_linerlayout = (LinearLayout) rootView.findViewById(R.id.call_linerlayout);
        chat_linerlayout = (LinearLayout) rootView.findViewById(R.id.chat_linerlayout);

        buttomUserImage = (ImageView) rootView.findViewById(R.id.buttomUserImage);
        buttomUserImageStart = (ImageView) rootView.findViewById(R.id.buttomUserImageStart);

        buttomUserName = (TextView) rootView.findViewById(R.id.buttomUserName);
        buttomUserNameStart = (TextView) rootView.findViewById(R.id.buttomUserNameStart);
        buttomUserGender = (TextView) rootView.findViewById(R.id.buttomUserGender);
        buttomUserGenderStart = (TextView) rootView.findViewById(R.id.buttomUserGenderStart);
        end_ride = (TextView) rootView.findViewById(R.id.end_ride);
        reset_address = (TextView) rootView.findViewById(R.id.reset_address);

        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.caregiver_main_branch_map);
        mapFragment.getMapAsync(this);

        chat_linerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("id", SavedData.getClientId());
                intent.putExtra("type", "2");
                startActivity(intent);
            }
        });
        call_linerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                if (callnumber.length() == 10) {
                    intent.setData(Uri.parse("tel:" + "+91" + callnumber));
                } else {
                    if (callnumber.contains("+")) {
                        intent.setData(Uri.parse("tel:" + callnumber));
                    } else {
                        intent.setData(Uri.parse("tel:" + "+" + callnumber));
                    }

                }
                startActivity(intent);
            }
        });


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    S.E("call 2");
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    SavedData.saveNotificationRequest("no");
                    String message = intent.getStringExtra("message");

                    try {
                        SavedData.saveNotificationRequest("no");
                        JSONObject jsonObject = new JSONObject(message);
                        S.E("jsonObject ----   " + jsonObject.toString());
                        JSONObject msg = jsonObject.getJSONObject("msg");

                        if (msg.getString("user_type").equals("7")) {
                            googleMap.clear();
                            buttonLayout.setVisibility(View.GONE);
                            buttonLayoutStart.setVisibility(View.GONE);
                            SavedData.saveAcceptLayoutKeep(false);
                            S.T(getActivity(), "Client Canceled your last Appoinment !");
                            S.I_clear(getActivity(), CareGiverMainActivity.class, null);
                        } else if (msg.getString("user_type").equals("11")) {
                            Intent intent1 = new Intent(getActivity(), ChatActivity.class);
                            intent1.putExtra("id", msg.getString("sender_id"));
                            intent1.putExtra("type", "2");
                            startActivity(intent1);
                        } else {

                            is_destination_done = false;
                            requestLatitude = msg.getString("latitude");
                            requestLongitud = msg.getString("longitud");
                            String full_name = msg.getString("full_name");
                            String address = msg.getString("address");
                            String gender = msg.getString("gender");
                            callnumber = msg.getString("phone");
                            String profile_pic = msg.getString("profile_pic");
                            String last_name = msg.getString("last_name");
                            typeOfCaregiver = msg.getString("type");
                            SavedData.saveClientId(msg.getString("client_id"));
                            buttonLayout.setVisibility(View.VISIBLE);
                            buttomUserName.setText(full_name);
                            buttomUserNameStart.setText(full_name);
                            if (gender.equals("null")) {
                                buttomUserGender.setText("No gender detail found");
                                buttomUserGenderStart.setText("No gender detail found");
                            } else {
                                buttomUserGender.setText(gender);
                                buttomUserGenderStart.setText(gender);
                            }
                            lat = requestLatitude;
                            lng = requestLongitud;

                            S.E("SavedData.getLatitude() : " + SavedData.getLatitude());
                            S.E("SavedData.getLongitude() : " + SavedData.getLongitude());
                           /* if (SavedData.getLatitude().equals(""))
                            {

                            } else*/
                            {
                                if (gps.canGetLocation()) {
                                    latitude_current_new = gps.getLatitude();
                                    longitude_current_new = gps.getLongitude();
                                    if (latitude_current_new != 0.0 && longitude_current_new != 0.0) {
                                        origin_new = new LatLng(gps.getLatitude(), gps.getLongitude());
                                        dest_new = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                                    }

                                }
                                //  SavedData.saveAcceptLayoutKeep(true);
                                SavedData.saveAcceptedFullName(msg.getString("full_name"));
                                SavedData.saveAcceptedPhoneNo(msg.getString("phone"));
                                if (gender != null && !gender.equalsIgnoreCase("null") && !gender.equalsIgnoreCase(""))
                                    SavedData.saveAccepteduserGender(msg.getString("gender"));
                                SavedData.saveAcceptedLatitude(requestLatitude);
                                SavedData.saveAcceptedLongitude(requestLongitud);
                                SavedData.saveAcceptedImagePath(msg.getString("profile_pic"));
                                SavedData.saveClientId(msg.getString("client_id"));

                                String Hirecareg = msg.getString("hire_caregiver_id");
                                SavedData.saveHireCareGiverId(Hirecareg);

                                SavedData.saveAcceptedsrcLatitude("" + latitude_current_new);
                                SavedData.saveAcceptedsrcLongitude("" + longitude_current_new);
                                SavedData.saveAcceptCareType(typeOfCaregiver);
                            }
                            if (profile_pic != null) {
                                Picasso.with(getActivity()).load(Const.URL.Image_Url + profile_pic).into(buttomUserImage);
                                Picasso.with(getActivity()).load(Const.URL.Image_Url + profile_pic).into(buttomUserImageStart);
                            }


                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (!SavedData.getAcceptLayout()) {
                                        buttonLayout.setVisibility(View.GONE);
                                        buttonLayoutStart.setVisibility(View.GONE);
                                        googleMap.clear();
                                    }

                                }
                            }, 30000);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        S.E("ErrorMessage" + e);
                    }
                }
            }
        };
        mainFragmentBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptRequest();

                pickupLat = Double.parseDouble(SavedData.getAcceptedLatitude());
                pickupLong = Double.parseDouble(SavedData.getAcceptedLongitude());

                UpdateLocation();

            }
        });
        mainFragmentBtnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonLayout.setVisibility(View.GONE);
                buttonLayoutStart.setVisibility(View.GONE);
                googleMap.clear();
                Update_Deny_status();
                //denyRequest();
            }
        });

        reset_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeOfCaregiver.toLowerCase().equals("caregiver")) {
                    S.I_clear(getActivity(), CareGiverStopWatchActivity.class, null);
                    SavedData.saveAcceptLayoutKeep(false);
                } else {
                    try {
                        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
                        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
                    } catch (GooglePlayServicesRepairableException e) {
                        // TODO: Handle the error.
                    } catch (GooglePlayServicesNotAvailableException e) {
                        // TODO: Handle the error.
                    }
                }
            }
        });

        mainFragmentBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeOfCaregiver.toLowerCase().equals("caregiver")) {
                    S.I_clear(getActivity(), CareGiverStopWatchActivity.class, null);
                    SavedData.saveAcceptLayoutKeep(false);
                } else {
                    try {
                        Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
                        startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
                    } catch (GooglePlayServicesRepairableException e) {
                        // TODO: Handle the error.
                    } catch (GooglePlayServicesNotAvailableException e) {
                        // TODO: Handle the error.
                    }
                }
            }
        });

        end_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    complete_status = "1";

                    SavedData.saveAcceptLayoutKeep(false);
                    if (SavedData.getSRCAmbulanceLatitude() != null && !SavedData.getSRCAmbulanceLatitude().equalsIgnoreCase("")) {
                        FinalSource = getCurrentLocation(Double.parseDouble(SavedData.getSRCAmbulanceLatitude()), Double.parseDouble(SavedData.getSRCAmbulanceLongitude()));
                    } else {
                        SavedData.saveSRCAmbulanceLatitude(caregiverCurrentStatus.getLattitude());
                        SavedData.saveSRCAmbulanceLongitude(caregiverCurrentStatus.getLongitude());
                        FinalSource = getCurrentLocation(Double.parseDouble(caregiverCurrentStatus.getLattitude()), Double.parseDouble(caregiverCurrentStatus.getLongitude()));

                    }
                    if (SavedData.getDestAmbulanceLatitude() != null && !SavedData.getDestAmbulanceLatitude().equalsIgnoreCase("")) {
                        FinalDestination = getCurrentLocation(Double.parseDouble(SavedData.getDestAmbulanceLatitude()), Double.parseDouble(SavedData.getDESTAmbulanceLongitude()));

                    } else {
                        SavedData.saveDestAmbulanceLatitude(caregiverCurrentStatus.getDest_lattitude());
                        SavedData.saveDESTAmbulanceLongitude(caregiverCurrentStatus.getDest_longitude());


                        FinalDestination = getCurrentLocation(Double.parseDouble(caregiverCurrentStatus.getDest_lattitude()), Double.parseDouble(caregiverCurrentStatus.getDest_longitude()));

                    }
                    try {
                        SavedData.saveClientId(caregiverCurrentStatus.getClientId());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    stopServiceForClient2(0.0, FinalSource, FinalDestination);
                    UpdateLocation();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mainFragmentBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelRequestFromClient();
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                LatLng latLng = place.getLatLng();
                if (gps.canGetLocation()) {
                    latitude_current_new = gps.getLatitude();
                    longitude_current_new = gps.getLongitude();
                    if (latitude_current_new != 0.0 && longitude_current_new != 0.0) {
                        origin_new = new LatLng(gps.getLatitude(), gps.getLongitude());
                        dest_new = new LatLng(latLng.latitude, latLng.longitude);
                        is_destination_done = true;
                        googleMap.clear();
                        showMarkersOnMap();

                        SavedData.saveDestAmbulanceLatitude("" + latLng.latitude);
                        SavedData.saveDESTAmbulanceLongitude("" + latLng.longitude);
                        //    SavedData.saveAcceptLayoutKeep(false);
                        stopServiceForClient();
                        UpdateLocation();
                    }

                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    public void showMarkersOnMap() {
        try {
            destLat = dest_new.latitude;
            destLong = dest_new.longitude;

            if (pickupLat == 0 || pickupLong == 0) {
                pickupLat = origin_new.latitude;
                pickupLong = origin_new.longitude;
            }


            googleMap.clear();

            Object dataTransfer[];

            dataTransfer = new Object[5];
            String url = getDirectionsUrl(getActivity(), pickupLat, pickupLong, destLat, destLong);
            GetDirectionsData getDirectionsData = new GetDirectionsData();
            dataTransfer[0] = googleMap;
            dataTransfer[1] = url;
            dataTransfer[2] = new LatLng(destLat, destLong);  // end lat long
            dataTransfer[3] = rotate;  // end lat long
            dataTransfer[4] = new LatLng(pickupLat, pickupLong);  // end lat long
            getDirectionsData.execute(dataTransfer);

            List<Marker> markers = new ArrayList<>();

            if (SavedData.getSaveType().equals("2")) {
                carMarker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(pickupLat, pickupLong))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_car)));
                markers.add(carMarker);
            } else {
                carMarker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(pickupLat, pickupLong))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.doctor_other_small_size)));
                markers.add(carMarker);
            }


            eMarker = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(destLat, destLong))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_green)));
            markers.add(eMarker);
            if (isFirstTime)
                focusMap(googleMap, markers);

            mapUISettings(googleMap);

            //had.postDelayed(update_location,2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            try {
                SavedData.saveAcceptedLatitude("" + destLat);
                SavedData.saveAcceptedLongitude("" + destLong);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {

        }
    }

    public static String getDirectionsUrl(Context context, double pickupLat
            , double pickupLong
            , double destinationLat
            , double destinationLong) {
        StringBuilder googleDirectionsUrl = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        googleDirectionsUrl.append("mode=driving");
        googleDirectionsUrl.append("&transit_routing_preference=less_driving");
        googleDirectionsUrl.append("&origin=" + pickupLat + "," + pickupLong);
        googleDirectionsUrl.append("&destination=" + destinationLat + "," + destinationLong);
        googleDirectionsUrl.append("&key=" + context.getResources().getString(R.string.google_maps_key));

        Log.d("Direction URL", googleDirectionsUrl.toString());
        return googleDirectionsUrl.toString();
    }

    public void mapUISettings(GoogleMap mMap) {
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
    }

    public void focusMap(GoogleMap mMap, List<Marker> markers) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }// offset from edges of the map in pixels

        LatLngBounds bounds = builder.build();// offset from edges of the map in pixels
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().widthPixels;
        int padding = (int) (width * 0.12);
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        mMap.animateCamera(cu);
    }

    public void updateMarkerOnMap() {


        try {
            //googleMap.clear();
            Object dataTransfer[];
            dataTransfer = new Object[5];
            String url = getDirectionsUrl(getActivity(), pickupLat, pickupLong, destLat, destLong);
            GetDirectionsData getDirectionsData = new GetDirectionsData();
            dataTransfer[0] = googleMap;
            dataTransfer[1] = url;
            dataTransfer[2] = new LatLng(destLat, destLong);
            dataTransfer[3] = rotate;
            dataTransfer[4] = new LatLng(pickupLat, pickupLong);

            getDirectionsData.execute(dataTransfer);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*public void animateMarker(GoogleMap mMap, final Marker marker, final LatLng startPosition, final LatLng toPosition, final boolean hideMarker) {

        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();

        final long duration = 1000;
        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * toPosition.longitude + (1 - t) * startPosition.longitude;
                double lat = t * toPosition.latitude + (1 - t) * startPosition.latitude;

                marker.setPosition(new LatLng(lat, lng));
                marker.setRotation((float) bearingBetweenLocations(startPosition, toPosition));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    public static double bearingBetweenLocations(LatLng latLng1, LatLng latLng2) {

        double PI = 3.14159;
        double lat1 = latLng1.latitude * PI / 180;
        double long1 = latLng1.longitude * PI / 180;
        double lat2 = latLng2.latitude * PI / 180;
        double long2 = latLng2.longitude * PI / 180;

        double dLon = (long2 - long1);

        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1)
                * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.atan2(y, x);

        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;

        return brng;
    }*/

    private void cancelRequestFromClient() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.CAREGIVER_CANCEL_REQUEST, 1, getPramsForCancelRequest(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("cancel request response : " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("200")) {
                        buttonLayout.setVisibility(View.GONE);
                        buttonLayoutStart.setVisibility(View.GONE);
                        SavedData.saveAcceptLayoutKeep(false);
                        googleMap.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getPramsForCancelRequest() {
        HashMap<String, String> prams = new HashMap<>();
        prams.put("token", SavedData.gettocken_id());
        prams.put("hire_caregiver_id", SavedData.getHireCareGiverId());
        prams.put("reciever_id", SavedData.getClientId());
        prams.put("status", "cancel");
        prams.put("reciever_type", "client");
        return prams;
    }


    private void stopServiceForClient() {
        S.E("prams -=-=-=-=-==-    " + getPramsForStopHiringTime());

        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.AMUBLANCE_START_STOP, 1, getPramsForStopHiringTime(), new Helper() {
            @Override
            public void backResponse(String response) {

                S.E("prams -=-=-=-=-==-Resopnse    " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getString("status").equals("200")) {
                        buttonLayout.setVisibility(View.GONE);
                        buttonLayoutStart.setVisibility(View.GONE);
                        linearLayoutEndId.setVisibility(View.VISIBLE);
                        try {
                            SavedData.saveSRCAmbulanceLatitude("" + origin_new.latitude);
                            SavedData.saveSRCAmbulanceLongitude("" + origin_new.longitude);
                            SavedData.saveDestAmbulanceLatitude("" + dest_new.latitude);
                            SavedData.saveDESTAmbulanceLongitude("" + dest_new.longitude);
                            SavedData.saveInWOrking("working");
                            SavedData.saveRideStore(true);
                            S.T(getActivity(), "Ride start");
                        } catch (Exception e) {

                        }
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getPramsForStopHiringTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String s = sdf.format(new Date());

        HashMap<String, String> prams = new HashMap<>();
        prams.put("token", SavedData.gettocken_id());
        prams.put("client_id", SavedData.getClientId());
        prams.put("hire_caregiver_id", SavedData.getHireCareGiverId());
        prams.put("total_kilometer", "0");
        prams.put("source_latitude", "" + dest_new.latitude);
        prams.put("source_longitude", "" + dest_new.longitude);
        prams.put("status", "start");
        prams.put("type", "2");
        return prams;
    }

    private void stopServiceForClient2(double distance, String finalSource, String finalDestination) {
        S.E("prams -=-=-=-=-==- 2   " + getPramsForStopHiringTime2(distance, finalSource, finalDestination));
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.AMUBLANCE_START_STOP, 1, getPramsForStopHiringTime2(distance, finalSource, finalDestination), new Helper() {
            @Override
            public void backResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        S.E("LogAboutDatat" + response);
                        S.T(getActivity(), "Ride Complete");
                        linearLayoutEndId.setVisibility(View.GONE);
                        SavedData.saveInWOrking("");
                        SavedData.saveRideStore(false);
                        googleMap.clear();
                        Bundle bundle = new Bundle();
                        bundle.putString("message", response);
                        bundle.putString("ActivityCheck", "Ambulance");
                        SavedData.saveCaregiverMessageForsummery(response);
                        SavedData.savePaymentStatus(true);
                        PathKmHelper.getInstance().deleteAll();
                        Intent intent1 = new Intent(getActivity(), AmbulanceSummeryPage.class);
                        intent1.putExtras(bundle);
                        startActivity(intent1);

                    } else {
                        S.T(getActivity(), "Try Again");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getCurrentLocation(double lat, double lng) {
        List<Address> addresses = new ArrayList<>();
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            return addresses.get(0).getAddressLine(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private Map<String, String> getPramsForStopHiringTime2(double distance, String finalSource, String finalDestination) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String s = sdf.format(new Date());

        HashMap<String, String> prams = new HashMap<>();
        prams.put("token", SavedData.gettocken_id());
        prams.put("client_id", SavedData.getClientId());
        if (SavedData.getHireCareGiverId() == null || SavedData.getHireCareGiverId().equalsIgnoreCase(""))
            prams.put("hire_caregiver_id", caregiverCurrentStatus.getHireCaregiverId());
        else
            prams.put("hire_caregiver_id", SavedData.getHireCareGiverId());
        prams.put("total_kilometer", "" + distance);
        prams.put("source_latitude", SavedData.getSRCAmbulanceLatitude());
        prams.put("source_longitude", SavedData.getSRCAmbulanceLongitude());
        prams.put("destination_latitude", SavedData.getDestAmbulanceLatitude());
        prams.put("destination_longitude", SavedData.getDESTAmbulanceLongitude());
        prams.put("source_location", finalSource);
        prams.put("destination_location", finalDestination);
        prams.put("status", "payment");
        prams.put("type", "2");


        return prams;
    }


    /*this method is never used */
    private void denyRequest() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.CAREGIVER_ACCEPT_REQUEST, 1, getPramsForDenyRequest(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E(response);
            }
        });
    }

    private Map<String, String> getPramsForDenyRequest() {
        HashMap<String, String> prams = new HashMap<>();
        prams.put("token", SavedData.gettocken_id());
        return prams;
    }

    private void acceptRequest() {
        S.E("accept prams ----    " + getPramsForAcceptRequest());
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.CAREGIVER_ACCEPT_REQUEST, 1, getPramsForAcceptRequest(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("Accept Request responce : " + response);
                try {
                    SavedData.saveNotificationTIme("");
                    if (response != null && response.contains("already accepted")) {
                        buttonLayout.setVisibility(View.GONE);
                        buttonLayoutStart.setVisibility(View.GONE);
                        googleMap.clear();
                        Toast.makeText(getActivity(), "This request is already accepted", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equals("200")) {
                            showMarkersOnMap();
                            JSONObject data = jsonObject.getJSONObject("data");
                            String hire_caregiver_id = data.getString("hire_caregiver_id");
                            SavedData.saveHireCareGiverId(hire_caregiver_id);
                            SavedData.saveAcceptLayoutKeep(true);
                            buttonLayout.setVisibility(View.GONE);
                            buttonLayoutStart.setVisibility(View.VISIBLE);


                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    S.E("logprintStackTrace" + e);
                }

            }
        });
    }

    private Map<String, String> getPramsForAcceptRequest() {
        HashMap<String, String> prams = new HashMap<>();

        prams.put("token", SavedData.gettocken_id());
        prams.put("client_id", SavedData.getClientId());
        prams.put("latitude", SavedData.getAcceptedLatitude());
        prams.put("longitud", SavedData.getAcceptedLongitude());
        prams.put("hire_caregiver_id", SavedData.getHireCareGiverId());
        prams.put("status", "accepted");
        if (typeOfCaregiver.toLowerCase().equals("caregiver")) {
            prams.put("type", "1");
        } else {
            prams.put("type", "2");
        }
        return prams;
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(getActivity());
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {


        try {
            this.googleMap = googleMap;
            setUpMap();
            String time = SavedData.getNotificationTime();
            if (time != null && !time.equalsIgnoreCase("")) {
                long notii_time = Long.parseLong(time);
                Date currentTime = Calendar.getInstance().getTime();

                long curretim = currentTime.getTime();

                long diffInMs = curretim - notii_time;

                long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs);
                if (diffInSec > 30) {
                    SavedData.saveNotificationTIme("");
                    GetCurrentStatus();
                } else {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            if (!SavedData.getAcceptLayout()) {
                                buttonLayout.setVisibility(View.GONE);
                                buttonLayoutStart.setVisibility(View.GONE);
                                googleMap.clear();
                            }


                        }
                    }, diffInSec * 1000);

                    showRequest();
                }

            } else {
                GetCurrentStatus();
            }


           /*int second =0;
           int minutes =0;

           String[] min_sec_ar = time.split(":");
            second = Integer.parseInt(min_sec_ar[1]);
            minutes = Integer.parseInt(min_sec_ar[0]);

            Date currentTime = Calendar.getInstance().getTime();
            int min = currentTime.getMinutes();
            int sec = currentTime.getSeconds();
            boolean is_get_status = false;*/


            /*if(min > minutes )
            {
                if(second < 30)
                {
                    is_get_status = true;
                }
                else
                {
                    int diff = min - minutes;
                    if(diff >=2)
                    {
                        is_get_status = true;
                    }
                    else
                    {

                    }
                }

            }
            else  if(min == minutes )
            {
                int diff = sec - second;
                if(second > sec)
                {

                }
                else
                {

                }
            }
            else
            {

            }*/

            //  GetCurrentStatus();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setUpMap() {
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        buildGoogleApiClient();

        mGoogleApiClient.connect();
        //googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        googleMap.getUiSettings().setZoomControlsEnabled(false);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLastLocation != null) {
                List<Address> addresses = new ArrayList<>();
                LatLng UCA = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

                // Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

                SavedData.saveAcceptedLatitude("" + mLastLocation.getLatitude());
                SavedData.saveAcceptedLongitude("" + mLastLocation.getLongitude());

               /* try {
                    addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(UCA, 17));
            }

            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(2000); //5 seconds
            mLocationRequest.setFastestInterval(2000); //3 seconds
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        pickupLat = location.getLatitude();
        pickupLong = location.getLongitude();

        SavedData.saveAcceptedsrcLatitude("" + pickupLat);
        SavedData.saveAcceptedsrcLongitude("" + pickupLong);
        rotate = location.getBearing();


        if (is_processing == false) {

            try {
                updateMarkerOnMap();
                String caregiver_id = SavedData.getHireCareGiverId();
                if (caregiver_id != null && !caregiver_id.equalsIgnoreCase(""))
                {
                    is_processing = true;
                    UpdateLocation();
                } else if (caregiverCurrentStatus != null) {
                    if (caregiverCurrentStatus.getBookingstatus().equalsIgnoreCase("start") || caregiverCurrentStatus.getBookingstatus().equalsIgnoreCase("accepted")) {
                        is_processing = true;
                        UpdateLocation();
                    }
                }
               // Toast.makeText(getActivity(), "rotation : " + rotate, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }




            /*handler_location.postDelayed(new Runnable()
            {
                @Override
                public void run() {

                    try {


                        updateMarkerOnMap();
                        String caregiver_id = SavedData.getHireCareGiverId();
                        if (caregiver_id != null && !caregiver_id.equalsIgnoreCase(""))
                        {
                            UpdateLocation();
                        } else if (caregiverCurrentStatus != null)
                        {
                            if (caregiverCurrentStatus.getBookingstatus().equalsIgnoreCase("start") || caregiverCurrentStatus.getBookingstatus().equalsIgnoreCase("accepted")) {
                                UpdateLocation();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 3000);*/
        }


    }

    Handler handler_location = new Handler();
    boolean is_processing = false;
    float rotate = 0;

    /*private void animateMarkerNew(final LatLng startPosition, final LatLng destination, final Marker marker) {

        if (marker != null) {

            final LatLng endPosition = new LatLng(destination.latitude, destination.longitude);

            final float startRotation = marker.getRotation();
            final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(3000); // duration 3 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        *//*map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                .target(newPosition)
                                .zoom(18f)
                                .build()));*//*
                        //float bearing = getBearing(startPosition, new LatLng(newPosition.latitude, newPosition.longitude));

                        *//*rotate = rotate  + 10;
                        if(rotate >360)
                            rotate =0;*//*
                        marker.setRotation(rotate);
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

        class LinearFixed implements LatLngInterpolatorNew {
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
*/
    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }


    private void Update_Deny_status() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.DENY_REQUEST, 1, getDeny_parameter(), new Helper() {
            public void backResponse(String response) {
                if (response != null) {

                }
            }
        });
    }


    private Map<String, String> getDeny_parameter() {

        String hire_id = SavedData.getHireCareGiverId();
        HashMap<String, String> param = new HashMap<>();
        param.put("hire_caregiver_id", hire_id);
        param.put("token", SavedData.gettocken_id());

        return param;
    }


    private void UpdateLocation() {
        S.E("accept prams ----    " + getUpdateLocationParam());

        Map<String, String> prams = getUpdateLocationParam();
        new JSONParser(getActivity()).parseVollyStringRequestWithoutLoad(Const.URL.CAREGIVER_UPDATE_LOCATION, 1, prams, new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("Accept Request responce : " + response);
                try {
                    is_processing = false;

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    S.E("logprintStackTrace" + e);
                }

            }
        });
    }

    private Map<String, String> getUpdateLocationParam() {
        HashMap<String, String> prams = new HashMap<>();
        try {
            prams.put("hire_caregiver_id", SavedData.getHireCareGiverId());
            prams.put("client_id", SavedData.getClientId());
            prams.put("is_completed", complete_status);
            prams.put("current_position", "" + rotate);
            prams.put("token", SavedData.gettocken_id());
            prams.put("lattitude", "" + pickupLat);
            prams.put("longitude", "" + pickupLong);
            if (is_destination_done == false) {
                prams.put("dest_lattitude", "");
                prams.put("dest_longitude", "");

                //prams.put("dest_lattitude", "" + requestLatitude);
                //  prams.put("dest_longitude", "" + requestLongitud);

            } else {
                prams.put("dest_lattitude", "" + destLat);
                prams.put("dest_longitude", "" + destLong);
            }

            if (typeOfCaregiver.toLowerCase().equals("caregiver")) {
                prams.put("type", "1");
            } else {
                prams.put("type", "2");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prams;
    }

    private Map<String, String> getParamForCurrentStatus() {
        HashMap<String, String> prams = new HashMap<>();
        try {
            prams.put("token", SavedData.gettocken_id());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return prams;
    }

    private void GetCurrentStatus() {


        Map<String, String> prams = getParamForCurrentStatus();
        new JSONParser(getActivity()).parseVollyStringRequestWithoutLoad(Const.URL.GET_BOOKING_STATUS_CAREGIVER, 1, prams, new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("Accept Request responce : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {

                        Gson gson = new Gson();
                        CurrentStatusResponse assignlistModel = gson.fromJson(response, CurrentStatusResponse.class);
                        caregiverCurrentStatus = assignlistModel.getData().get(0);

                        if (caregiverCurrentStatus != null)
                        {
                            SavedData.saveHireCareGiverId(caregiverCurrentStatus.getHireCaregiverId());

                            if (caregiverCurrentStatus.getBookingstatus() != null && caregiverCurrentStatus.getBookingstatus().equalsIgnoreCase("accepted"))
                            {

                                rotate = 0;
                                try {
                                    pickupLat = Double.parseDouble(SavedData.getAcceptedSrcLatitude());
                                    pickupLong = Double.parseDouble(SavedData.getAcceptedSrcLongitude());

                                    destLat = Double.parseDouble(caregiverCurrentStatus.getLattitude());
                                    destLong = Double.parseDouble(caregiverCurrentStatus.getLongitude());
                                } catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                                // destLat = Double.parseDouble(SavedData.getAcceptedLatitude());
                                //  destLong = Double.parseDouble(SavedData.getAcceptedLongitude());


                                callnumber = caregiverCurrentStatus.getPhone();
                                String profile_pic = "" + caregiverCurrentStatus.getProfilePic();
                                String full_name = caregiverCurrentStatus.getFullName();
                                String gender = caregiverCurrentStatus.getGender();

                                typeOfCaregiver = caregiverCurrentStatus.getType();


                                try {
                                    SavedData.saveAcceptedFullName(full_name);
                                    SavedData.saveAcceptedPhoneNo(callnumber);
                                    if (gender != null && !gender.equalsIgnoreCase("null") && !gender.equalsIgnoreCase(""))
                                        SavedData.saveAccepteduserGender(gender);

                                    SavedData.saveAcceptedImagePath(profile_pic);
                                    SavedData.saveClientId(caregiverCurrentStatus.getClientId());

                                    String Hirecareg = caregiverCurrentStatus.getHireCaregiverId();
                                    SavedData.saveHireCareGiverId(Hirecareg);

                                    SavedData.saveAcceptedsrcLatitude("" + pickupLat);
                                    SavedData.saveAcceptedsrcLongitude("" + pickupLong);
                                    if (typeOfCaregiver.equalsIgnoreCase("1")) {
                                        typeOfCaregiver = "caregiver";
                                        SavedData.saveAcceptCareType(typeOfCaregiver);
                                    } else {
                                        typeOfCaregiver = "ambulance";
                                        SavedData.saveAcceptCareType(typeOfCaregiver);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                buttonLayout.setVisibility(View.VISIBLE);
                                buttomUserName.setText(full_name);
                                buttomUserNameStart.setText(full_name);

                                buttonLayoutStart.setVisibility(View.VISIBLE);
                                if (profile_pic != null) {
                                    Picasso.with(getActivity()).load(Const.URL.Image_Url + profile_pic).into(buttomUserImage);
                                    Picasso.with(getActivity()).load(Const.URL.Image_Url + profile_pic).into(buttomUserImageStart);
                                }


                                updateMarkerOnMap();

                            } else if (caregiverCurrentStatus.getBookingstatus() != null && caregiverCurrentStatus.getBookingstatus().equalsIgnoreCase("start")) {

                                typeOfCaregiver = caregiverCurrentStatus.getType();
                                if (typeOfCaregiver.equalsIgnoreCase("2")) {
                                    rotate = 0;
                                    buttonLayout.setVisibility(View.GONE);
                                    buttonLayoutStart.setVisibility(View.GONE);
                                    linearLayoutEndId.setVisibility(View.VISIBLE);
                                    try {
                                        if (SavedData.getAcceptedSrcLatitude() != null && !SavedData.getAcceptedSrcLatitude().equalsIgnoreCase("")) {
                                            pickupLat = Double.parseDouble(SavedData.getAcceptedSrcLatitude());
                                            pickupLong = Double.parseDouble(SavedData.getAcceptedSrcLongitude());

                                        } else {
                                            pickupLat = Double.parseDouble(caregiverCurrentStatus.getLattitude());
                                            pickupLong = Double.parseDouble(caregiverCurrentStatus.getLongitude());

                                        }

                                    } catch (Exception e) {

                                        e.printStackTrace();
                                    }

                                    is_destination_done = true;
                                    try {
                                        destLat = Double.parseDouble(caregiverCurrentStatus.getDest_lattitude());
                                        destLong = Double.parseDouble(caregiverCurrentStatus.getDest_longitude());

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    updateMarkerOnMap();
                                } else {
                                    S.I_clear(getActivity(), CareGiverStopWatchActivity.class, null);
                                    SavedData.saveAcceptLayoutKeep(false);
                                }

                               /* if (SavedData.getRideStart())
                                {

                                }*/


                            } else if (caregiverCurrentStatus.getBookingstatus() != null && caregiverCurrentStatus.getBookingstatus().equalsIgnoreCase("payment")) {

                                try {
                                    typeOfCaregiver = caregiverCurrentStatus.getType();
                                    if (typeOfCaregiver.equalsIgnoreCase("1"))
                                    {
                                        try
                                        {
                                            SavedData.saveAcceptLayoutKeep(false);
                                            if (SavedData.getSRCAmbulanceLatitude() != null && !SavedData.getSRCAmbulanceLatitude().equalsIgnoreCase("")) {
                                                FinalSource = getCurrentLocation(Double.parseDouble(SavedData.getSRCAmbulanceLatitude()), Double.parseDouble(SavedData.getSRCAmbulanceLongitude()));
                                            } else {
                                                SavedData.saveSRCAmbulanceLatitude(caregiverCurrentStatus.getLattitude());
                                                SavedData.saveSRCAmbulanceLongitude(caregiverCurrentStatus.getLongitude());
                                                FinalSource = getCurrentLocation(Double.parseDouble(caregiverCurrentStatus.getLattitude()), Double.parseDouble(caregiverCurrentStatus.getLongitude()));

                                            }
                                            if (SavedData.getDestAmbulanceLatitude() != null && !SavedData.getDestAmbulanceLatitude().equalsIgnoreCase("")) {
                                                FinalDestination = getCurrentLocation(Double.parseDouble(SavedData.getDestAmbulanceLatitude()), Double.parseDouble(SavedData.getDESTAmbulanceLongitude()));

                                            } else {
                                                SavedData.saveDestAmbulanceLatitude(caregiverCurrentStatus.getDest_lattitude());
                                                SavedData.saveDESTAmbulanceLongitude(caregiverCurrentStatus.getDest_longitude());


                                                FinalDestination = getCurrentLocation(Double.parseDouble(caregiverCurrentStatus.getDest_lattitude()), Double.parseDouble(caregiverCurrentStatus.getDest_longitude()));

                                            }
                                            try {
                                                SavedData.saveClientId(caregiverCurrentStatus.getClientId());


                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            stopServiceForClient2(0.0, FinalSource, FinalDestination);
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                        String responsee = SavedData.getCaregiverMessageForSummery();
                                        if (responsee != null && !responsee.equalsIgnoreCase("")) {
                                            Bundle bundle = new Bundle();
                                            bundle.putString("message", responsee);
                                            S.I_clear(getActivity(), CareGiverSummeryPageActivity.class, bundle);
                                        } else {
                                            S.I_clear(getActivity(), CareGiverStopWatchActivity.class, null);
                                            SavedData.saveAcceptLayoutKeep(false);
                                        }


                                    } else {
                                        FinalSource = getCurrentLocation(Double.parseDouble(caregiverCurrentStatus.getLattitude()), Double.parseDouble(caregiverCurrentStatus.getLongitude()));

                                       String client_id = SavedData.getClientId();
                                       if(client_id == null || client_id.equalsIgnoreCase(""))
                                       {
                                           SavedData.saveClientId(caregiverCurrentStatus.getClientId());
                                       }

                                        String sour_lat =  SavedData.getSRCAmbulanceLatitude();

                                        if(sour_lat == null || sour_lat.equalsIgnoreCase(""))
                                        {
                                            SavedData.saveSRCAmbulanceLatitude(caregiverCurrentStatus.getLattitude());
                                        }

                                        String sour_long =  SavedData.getSRCAmbulanceLongitude();

                                        if(sour_long == null || sour_long.equalsIgnoreCase(""))
                                        {
                                            SavedData.saveSRCAmbulanceLongitude(caregiverCurrentStatus.getLongitude());
                                        }

                                        String des_lat =  SavedData.getDestAmbulanceLatitude();

                                        if(des_lat == null || des_lat.equalsIgnoreCase(""))
                                        {
                                            SavedData.saveDestAmbulanceLatitude(caregiverCurrentStatus.getLattitude());
                                        }


                                        String des_long =  SavedData.getDESTAmbulanceLongitude();

                                        if(des_long == null || des_long.equalsIgnoreCase(""))
                                        {
                                            SavedData.saveDESTAmbulanceLongitude(caregiverCurrentStatus.getLongitude());
                                        }


                                       try
                                       {
                                           if (caregiverCurrentStatus.getDest_lattitude() == null || caregiverCurrentStatus.getDest_lattitude().equalsIgnoreCase("")) {
                                               FinalDestination = getCurrentLocation(Double.parseDouble(SavedData.getDestAmbulanceLatitude()), Double.parseDouble(SavedData.getDESTAmbulanceLongitude()));
                                           } else
                                               FinalDestination = getCurrentLocation(Double.parseDouble(caregiverCurrentStatus.getDest_lattitude()), Double.parseDouble(caregiverCurrentStatus.getDest_longitude()));
                                           stopServiceForClient2(0.0, FinalSource, FinalDestination);
                                       }
                                       catch (Exception e)
                                       {

                                           stopServiceForClient2(0.0, FinalSource, FinalSource);
                                           e.printStackTrace();
                                       }
                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                            if (SavedData.getRatingStatus()) {

                                String message = SavedData.getCaregiverMessageForSummery();
                                S.E("message on summery --else if" + message);
                                JSONObject jsonObject1 = new JSONObject(message);
                                String kmpercages = "0.0";
                                JSONObject msg = jsonObject1.getJSONObject("data");
                                String CaregiverId = msg.getString("client_id");
                                String min_charges = "";
                                if (msg.getString("min_charges").equals("null")) {
                                    min_charges = "0.0";
                                } else {

                                    min_charges = msg.getString("min_charges");
                                }

                                if (msg.getString("amount").equals("null")) {

                                } else {
                                    kmpercages = msg.getString("amount");

                                }
                                double totalkm = 0;
                                try {
                                    totalkm = Double.parseDouble((new DecimalFormat("##.##").format(Double.parseDouble(msg.getString("total_kilometer")))));
                                    double amount = Double.parseDouble((new DecimalFormat("##.##").format(Double.parseDouble(kmpercages))));
                                    double totalAmount1 = totalkm * amount;
                                    S.E("totalAmount1" + totalAmount1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Bundle bundle = new Bundle();
                                try {
                                    bundle.putString("FullName", msg.getString("full_name"));
                                    bundle.putString("Type", "ambulance");
                                    bundle.putString("Picture", msg.getString("profile_pic"));

                                    if (totalkm > 1) {
                                        double remaningKm = totalkm - 1;
                                        double extraAmount = remaningKm * (Double.parseDouble(kmpercages));
                                        bundle.putString("Amount", "" + new DecimalFormat("##.##").format(extraAmount + Double.parseDouble(min_charges)));
                                    } else {
                                        bundle.putString("Amount", "" + new DecimalFormat("##.##").format(Double.parseDouble(min_charges)));

                                    }


                                    bundle.putString("StartTime", msg.getString("source_location"));
                                    bundle.putString("StopTime", msg.getString("destination_location"));
                                    bundle.putString("TotalTime", "");
                                    bundle.putString("PaymentMode", "Cash");
                                    bundle.putString("client_id", CaregiverId);
                                    SavedData.saveAmbulance(false);
                                    SavedData.saveMessageForsummery("");
                                    SavedData.saveRatingStatus(true);
                                    Intent intent1 = new Intent(getActivity(), CareGiverRatingActivity.class);
                                    intent1.putExtras(bundle);
                                    startActivity(intent1);


                                } catch (JSONException e) {
                                    S.E("check exception" + e);
                                }
                            }


                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    S.E("logprintStackTrace" + e);
                }


            }
        });
    }

    private void showRequest() {

        try {
            String message = SavedData.getNotificationJson();
            JSONObject jsonObject = new JSONObject(message);
            S.E("jsonObject ----   " + jsonObject.toString());
            JSONObject msg = jsonObject.getJSONObject("msg");


            is_destination_done = false;
            requestLatitude = msg.getString("latitude");
            requestLongitud = msg.getString("longitud");
            String full_name = msg.getString("full_name");
            String address = msg.getString("address");
            String gender = msg.getString("gender");
            callnumber = msg.getString("phone");
            String profile_pic = msg.getString("profile_pic");
            String last_name = msg.getString("last_name");
            typeOfCaregiver = msg.getString("type");
            SavedData.saveClientId(msg.getString("client_id"));
            buttonLayout.setVisibility(View.VISIBLE);
            buttomUserName.setText(full_name);
            buttomUserNameStart.setText(full_name);
            if (gender.equals("null")) {
                buttomUserGender.setText("No gender detail found");
                buttomUserGenderStart.setText("No gender detail found");
            } else {
                buttomUserGender.setText(gender);
                buttomUserGenderStart.setText(gender);
            }
            lat = requestLatitude;
            lng = requestLongitud;

            S.E("SavedData.getLatitude() : " + SavedData.getLatitude());
            S.E("SavedData.getLongitude() : " + SavedData.getLongitude());
                           /* if (SavedData.getLatitude().equals(""))
                            {

                            } else*/
            {
                if (gps.canGetLocation()) {
                    latitude_current_new = gps.getLatitude();
                    longitude_current_new = gps.getLongitude();
                    if (latitude_current_new != 0.0 && longitude_current_new != 0.0) {
                        origin_new = new LatLng(gps.getLatitude(), gps.getLongitude());
                        dest_new = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                    }

                }
                //  SavedData.saveAcceptLayoutKeep(true);
                SavedData.saveAcceptedFullName(msg.getString("full_name"));
                SavedData.saveAcceptedPhoneNo(msg.getString("phone"));
                if (gender != null && !gender.equalsIgnoreCase("null") && !gender.equalsIgnoreCase(""))
                    SavedData.saveAccepteduserGender(msg.getString("gender"));
                SavedData.saveAcceptedLatitude(requestLatitude);
                SavedData.saveAcceptedLongitude(requestLongitud);
                SavedData.saveAcceptedImagePath(msg.getString("profile_pic"));
                SavedData.saveClientId(msg.getString("client_id"));

                String Hirecareg = msg.getString("hire_caregiver_id");
                SavedData.saveHireCareGiverId(Hirecareg);

                SavedData.saveAcceptedsrcLatitude("" + latitude_current_new);
                SavedData.saveAcceptedsrcLongitude("" + longitude_current_new);
                SavedData.saveAcceptCareType(typeOfCaregiver);
            }
            if (profile_pic != null) {
                Picasso.with(getActivity()).load(Const.URL.Image_Url + profile_pic).into(buttomUserImage);
                Picasso.with(getActivity()).load(Const.URL.Image_Url + profile_pic).into(buttomUserImageStart);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}