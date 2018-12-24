package com.dollop.syzygy.fragment.client;


import android.Manifest;
import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.syzygy.Model.CareGiverServiceModel;
import com.dollop.syzygy.Model.CareGiverSpecialization;
import com.dollop.syzygy.Model.LocationResponse;
import com.dollop.syzygy.Model.LocationUpdate;
import com.dollop.syzygy.Model.UserCurrentStatus;
import com.dollop.syzygy.Model.UserCurrentStatusResponse;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.ChatActivity;
import com.dollop.syzygy.activity.SplashActivity;
import com.dollop.syzygy.activity.client.AmbulanceSummeryPage;
import com.dollop.syzygy.activity.client.ClientMainActivity;
import com.dollop.syzygy.activity.client.ClientSeniorListActivity;
import com.dollop.syzygy.activity.client.Client_RatingActivity;
import com.dollop.syzygy.activity.client.SummeryPageActivity;
import com.dollop.syzygy.adapter.AmbulanceTypeAdapter;
import com.dollop.syzygy.adapter.ClientGetServiceAdapter;
import com.dollop.syzygy.adapter.SpecializationAdapter;
import com.dollop.syzygy.direction.GetUserDirectionsData;
import com.dollop.syzygy.listeners.RecyclerItemClickListener;
import com.dollop.syzygy.notification.Config;
import com.dollop.syzygy.notification.NotificationUtils;
import com.dollop.syzygy.servics.LocationService;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.NetworkUtil;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.utility.Constants;
import com.dollop.syzygy.utility.GPSTrackerNew;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.graphics.Color.parseColor;

/**
 * Created by sohel on 9/27/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class ClientMainFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    //    for stop watch
    private final static int MSG_UPDATE_TIME = 0;
    public static int REQuestCode = 22;
    private final Handler mUpdateTimeHandler = new ClientUIUpdateHandler(this);
    LinearLayout header_layout, login_header, notlogin_header;
    TextView clientUserName, clientMobileNo;
    ArrayList<LatLng> latlongDr = new ArrayList<LatLng>();
    ArrayList<LatLng> latLngsAmbulance = new ArrayList<LatLng>();
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Dialog demoDialog;
    boolean popup;
    int currentFragment = 0;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    Marker mCurrLocationMarker, marker1;
    double currentlat;
    double currentlong;
    boolean is_from_other = false;
    public static boolean is_first_time = false;
    public static boolean is_on_map = false;
    boolean is_start_ride = false;
    String phoneNumber = "0123456789";
    String reason, fromdate, todate;
    Marker marker;
    Polyline line;
    LatLng latLng;
    String callnumber;
    ArrayList<CareGiverSpecialization> careGiverSpecializationslist = new ArrayList<>();
    ArrayList<CareGiverSpecialization> careGiverSpecializationslistwithService = new ArrayList<>();
    ArrayList<CareGiverServiceModel> serviceCareList = new ArrayList<>();
    ClientGetServiceAdapter clientGetServiceAdapter;
    AmbulanceTypeAdapter ambulanceTypeAdapter;
    CareGiverSpecialization careGiverSpecialization;
    String seniorIdstr = "";
    ImageView CaregiverBtnDrawer, HireNowBtDrawer;
    Button ConfirmHiringBtDrawer;
    TextView ClientProfileNameId, editProfile, minimumChargeID;
    LinearLayout ClientProfileHeaderId;
    LinearLayout tvAmbulanceId, tvNurseId, tvDoctorId, tvDeitisimId, tvPhysicianId, LinearTypeAmbulanceId;
    LinearLayout linearHireDrawer, linearConfirmHiringDrawer, linearLayoutAmbulancenCareId, LinearHireestimate, LinearHiringconfirm, LinearHireConfirm;
    CircleImageView ClientProfileImageId;
    LinearLayout horizontalScrollView1;
    Button BtnAmulanceClickId, BtnCareGiverClickId;
    String typeAmbulanceCareGiver = "";
    String hire_id_for_cancel = "";
    EditText searchLocation;
    ImageView buttonRightArrow;
    String CareGiver_id = "";
    String Hire_CareGiver_id = "";
    int i = 0;
    TextView tvCaregiverId, tvRatingId, textViewVehichleNumber;
    ImageView ivCaregiverPicId;
    //    SupportMapFragment mapFragment;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    boolean isNetwork;
    SupportPlaceAutocompleteFragment autocompleteFragment;
    @BindView(R.id.Ambulance_recycleview)
    RecyclerView AmbulanceRecycleview;
    @BindView(R.id.caregiver_recycleview)
    RecyclerView caregiverRecycleview;
    Unbinder unbinder;
    LinearLayout callLinerlayout;
    LinearLayout chat_linerlayout;
    LinearLayout cancelLinerlayout;
    RelativeLayout clockLayout, mainLayout;
    LinearLayout linearProgressIn;
    TextView careGiverStopWatchText;
    ArrayList<Long> listForTym = new ArrayList<>();
    long tym = 0;
    int flag = 0;
    LinearLayout linearLayoutId;
    String timeForPauseORreSume = "";
    @BindView(R.id.caregiver_specilization_recycleview)
    RecyclerView caregiverSpecilizationRecycleview;
    @BindView(R.id.specializationLayout)
    LinearLayout specializationLayout;
    @BindView(R.id.ridingStart)
    LinearLayout ridingStart;
    private GoogleMap mMap;
    private String otherlongitude;
    private String getlongitude;
    private String getlatitude;
    private String otherlatitude;
    private LocationManager locationManager;
    private boolean isGPS;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String requestLatitude;
    private String requestLongitud;
    private String full_name;
    private ClientTimerService timerService = new ClientTimerService();
    private boolean serviceBound;
    private BroadcastReceiver mstopRegistrationBroadcastReceiver;
    private boolean isCareGiverAvaliable = false;
    private boolean is_from_ambulance = false;
    private String mSelectedDateTime = "";
    private int day, month, year, hour, minute;
    private String currentDate = "";
    private String mSelectedDate = "";
    private String mSelectedTime = "";
    boolean isAnyOptionSelected = false;
    boolean isEditTextChangeListerWork = false;
    private String reasonForCancel = "";
    private AlertDialog alertDailog;
    private Handler handlerContactRefershApi;
    private Runnable runnableContactRefershApi;
    private boolean isFirstTime;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private double pickupLat, pickupLong, destLat, destLong;
    private LatLng origin_new;
    private LatLng dest_new;
    private Marker carMarker, eMarker;
    private LatLng carLastLocation, carCurrentLocation;
    private GPSTrackerNew gps;
    private double latitude_current_new;
    private double longitude_current_new;
    boolean is_from_mapready = false;
    UserCurrentStatus userCurrentStatus = null;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 3535;

    boolean is_running = false;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            S.E("Service bound");
            ClientTimerService.RunServiceBinder binder = (ClientTimerService.RunServiceBinder) service;
            timerService = binder.getService();
            serviceBound = true;
            // Ensure the service is not in the foreground when bound
            timerService.background();
            // Update the UI if the service is already running the timer
            if (timerService.isTimerRunning()) {
                ClientupdateUIStartRun();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            S.E("Service disconnect");
            serviceBound = false;
        }
    };
    boolean TimerRunning = false;
    private String timeInMinut;
    private long timeInM = 0;
    private static long save_time = 0;
    private String serviceId = "";
    private String specializationId = "";
    private LatLng dest;
    private LatLng origin;
    private ImageView hireLaterId;
    private String datestr1 = "";
    private String timestr1 = "";

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

    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    /**
     * Updates the timer readout in the UI; the service must be bound
     */

    private void ClientupdateUITimer() {
        if (serviceBound) {
//            careGiverStopWatchText.setText(timerService.elapsedTime() + " seconds");
//            timeInM += timerService.elapsedTime();
            if (TimerRunning) {
                save_time = timeInM + timerService.elapsedTime();
                if (save_time == 0) {
                    try {
                        String time = SavedData.getTimerTime();
                        if (time != null && !time.equalsIgnoreCase(""))
                            timeInM = Long.parseLong(SavedData.getTimerTime());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                long sec = save_time % 60;
                long min = (save_time / 60) % 60;
                long hours = (save_time / 60) / 60;

                String time = "";

                if (hours < 10)
                    time = "0" + hours;
                else
                    time = "" + hours;

                if (min < 10)
                    time = time + ":0" + min;
                else
                    time = time + ":" + min;


                if (sec < 10)
                    time = time + ":0" + sec;
                else
                    time = time + ":" + sec;

                careGiverStopWatchText.setText(time);

            }


        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.content_client_main, container, false);
        this.gps = new GPSTrackerNew(getActivity());
        isFirstTime = true;
        isCareGiverAvaliable = false;
        autocompleteFragment = (SupportPlaceAutocompleteFragment) getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);



     /*   else if (SavedData.getAmbulanceSummery() == true)
        {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Bundle bundle = new Bundle();
                    bundle.putString("ActivityCheck", "Client");
                    Intent intent1 = new Intent(getActivity(), AmbulanceSummeryPage.class);
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                }
            }, 500);


        }*/


        is_running = true;
        locationManager = (LocationManager) getActivity().getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        autocompleteFragment.setHint("Current Location");

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.e("Tages", "Place: " + place.getLatLng());
                getlatitude = String.valueOf(place.getLatLng().latitude);
                getlongitude = String.valueOf(place.getLatLng().longitude);

                mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));

                if (NetworkUtil.isNetworkAvailable(getActivity()))
                {
                    caregiverSpecilizationRecycleview.setVisibility(View.GONE);
                    specializationLayout.setVisibility(View.GONE);
                    getCaregiverAcrordingLocation();
                } else {
                    /*  noInternetDialog(getString(R.string.no_internet));*/
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.e("Tages", "An error occurred: " + status);
            }
        });


        if (!hasPermissions(getActivity(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.client_main_mapId);
        mapFragment.getMapAsync(this);


        clockLayout = (RelativeLayout) rootView.findViewById(R.id.clockLayout);
        mainLayout = (RelativeLayout) rootView.findViewById(R.id.mainLayout);

        careGiverStopWatchText = (TextView) rootView.findViewById(R.id.careGiverStopWatchText);

        linearHireDrawer = (LinearLayout) rootView.findViewById(R.id.linearHireDrawer);
        LinearHireConfirm = (LinearLayout) rootView.findViewById(R.id.LinearHireConfirm);
        linearConfirmHiringDrawer = (LinearLayout) rootView.findViewById(R.id.linearConfirmHiringDrawer);
        LinearHireestimate = (LinearLayout) rootView.findViewById(R.id.LinearHireestimate);
        linearLayoutAmbulancenCareId = (LinearLayout) rootView.findViewById(R.id.linearLayoutAmbulancenCareId);
        LinearHiringconfirm = (LinearLayout) rootView.findViewById(R.id.LinearHiringconfirm);
        linearProgressIn = (LinearLayout) rootView.findViewById(R.id.linearProgressIn);
        tvCaregiverId = (TextView) rootView.findViewById(R.id.tvCaregiverId);
        tvRatingId = (TextView) rootView.findViewById(R.id.tvRatingId);
        textViewVehichleNumber = (TextView) rootView.findViewById(R.id.textViewVehichleNumber);

        callLinerlayout = (LinearLayout) rootView.findViewById(R.id.call_linerlayout);
        chat_linerlayout = (LinearLayout) rootView.findViewById(R.id.chat_linerlayout);
        cancelLinerlayout = (LinearLayout) rootView.findViewById(R.id.cancel_Linerlayout);
        ivCaregiverPicId = (ImageView) rootView.findViewById(R.id.ivCaregiverPicId);
        hireLaterId = (ImageView) rootView.findViewById(R.id.hireLaterId);


        horizontalScrollView1 = (LinearLayout) rootView.findViewById(R.id.horizontalScrollView1);

        LinearTypeAmbulanceId = (LinearLayout) rootView.findViewById(R.id.LinearTypeAmbulanceId);
        BtnAmulanceClickId = (Button) rootView.findViewById(R.id.BtnAmulanceClickId);
        BtnCareGiverClickId = (Button) rootView.findViewById(R.id.BtnCareGiverClickId);

        if (SavedData.getStartTimer() != null) {
            if (SavedData.getStartTimer().equals("yes")) {
                mainLayout.setVisibility(View.GONE);
                clockLayout.setVisibility(View.VISIBLE);
            } else {
                mainLayout.setVisibility(View.VISIBLE);
                clockLayout.setVisibility(View.GONE);
            }
        }

        horizontalScrollView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpecializationDialog();
            }
        });
        BtnAmulanceClickId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ClientMainActivity) getActivity()).getCurrentStatus(false);
                mMap.clear();
                BtnAmulanceClickId.setTextColor(parseColor("#FFFFFF"));
                BtnAmulanceClickId.setBackgroundColor(getActivity().getResources().getColor(R.color.colorbutton));
                BtnCareGiverClickId.setTextColor(getActivity().getResources().getColor(R.color.colorbutton));
                BtnCareGiverClickId.setBackgroundColor(parseColor("#FFFFFF"));
                SavedData.saveHiredUserType(Constants.HIRE_AMBULANCE);
                typeAmbulanceCareGiver = "2";
                serviceId = "";
                specializationId = "";
                is_from_ambulance = true;
                getCaregiverAcrordingLocation();
                LinearTypeAmbulanceId.setVisibility(View.VISIBLE);
                horizontalScrollView1.setVisibility(View.GONE);
                specializationLayout.setVisibility(View.GONE);

                getAmbulanceType();
                if (latLngsAmbulance.size() > 0) {


                } else {
                }
            }
        });

        hireLaterId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog();
            }
        });
        BtnCareGiverClickId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BtnCareGiverClickId.setTextColor(parseColor("#FFFFFF"));
                BtnCareGiverClickId.setBackgroundColor(getActivity().getResources().getColor(R.color.colorbutton));
                BtnAmulanceClickId.setTextColor(getActivity().getResources().getColor(R.color.colorbutton));
                BtnAmulanceClickId.setBackgroundColor(parseColor("#FFFFFF"));
                ((ClientMainActivity) getActivity()).getCurrentStatus(false);
                caregiverSpecilizationRecycleview.setVisibility(View.GONE);
                mMap.clear();
                typeAmbulanceCareGiver = "1";
                serviceId = "";
                specializationId = "";
                is_from_ambulance = false;
                getCaregiverAcrordingLocation();
                LinearTypeAmbulanceId.setVisibility(View.GONE);
                horizontalScrollView1.setVisibility(View.VISIBLE);
                SavedData.saveHiredUserType(Constants.HIRE_CARE_GIVER);
                getServices();
                ((ClientMainActivity) getActivity()).getCurrentStatus(false);
                if (latlongDr.size() > 0) {


                } else {

                }
            }
        });

        HireNowBtDrawer = (ImageView) rootView.findViewById(R.id.HireNowBtDrawer);
        minimumChargeID = (TextView) rootView.findViewById(R.id.minimumChargeID);
        ConfirmHiringBtDrawer = (Button) rootView.findViewById(R.id.ConfirmHiringBtDrawer);
        ConfirmHiringBtDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHiring();
            }
        });
        HireNowBtDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearConfirmHiringDrawer.setVisibility(View.VISIBLE);
                linearHireDrawer.setVisibility(View.GONE);
            }
        });
        callLinerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        chat_linerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("id", CareGiver_id);
                intent.putExtra("type", "1");
                startActivity(intent);
            }
        });
        cancelLinerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCancelReasonDailog();
            }
        });


        //notificationHandelar();
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                S.E("call 1");
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    S.E("call 2");
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    S.E("call 3");
                    String message = intent.getStringExtra("message");
                    S.E("checked By Ani Unik" + message);
                    try {
                        SavedData.saveNotificationRequest("no");
                        JSONObject jsonObject = new JSONObject(message);
                        JSONObject msg = jsonObject.getJSONObject("msg");
                        S.E("sohel - requestLatitude " + jsonObject.getJSONObject("msg"));

                        if (msg.getString("user_type").equals("7")) {
                            S.T(getActivity(), "Care Giver canceled you request !");
                            Intent refresh = new Intent(getActivity(), ClientMainActivity.class);
                            refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(refresh);
                            SavedData.saveAcceptLayoutKeep(false);
                            getActivity().finish();
//                                backToHome();

                        } else if (msg.getString("user_type").equals("11")) {
                            Intent intent1 = new Intent(getActivity(), ChatActivity.class);
                            intent1.putExtra("id", msg.getString("sender_id"));
                            intent1.putExtra("type", "1");
                            startActivity(intent1);
                        } else if (msg.getString("user_type").equals(Constants.ACCEPT_USER_AMBULANCE)) {


                            try {
                                if (null != handlerContactRefershApi) {
                                    handlerContactRefershApi.removeCallbacks(runnableContactRefershApi);
                                }
                                S.E("user 3= " + msg.getString("user_type"));
                                requestLatitude = msg.getString("latitude");
                                requestLongitud = msg.getString("longitud");
                                LinearHireConfirm.setVisibility(View.GONE);


                                CareGiver_id = msg.getString("caregiver_id");
                                Hire_CareGiver_id = msg.getString("hire_caregiver_id");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                            GetUpdateLocation();
                            tvCaregiverId.setText(msg.getString("full_name"));
                            try {
                                if (!"".equals(msg.getString("vehical_registration_no"))) {
                                    textViewVehichleNumber.setVisibility(View.VISIBLE);
                                    textViewVehichleNumber.setText("Vehicle Number : " + msg.getString("vehical_registration_no"));
                                } else {
                                    textViewVehichleNumber.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            callnumber = msg.getString("phone");

                            tvRatingId.setText(new DecimalFormat("##.#").format(Double.parseDouble(msg.getString("ratingReview"))));
                            Picasso.with(getActivity()).load(Const.URL.Image_Url + msg.getString("profile_pic")).error(R.drawable.user_profile_pic)
                                    .into(ivCaregiverPicId);
                            S.E("sohel - requestLatitude " + requestLatitude);
                            S.E("sohel - requestLongitud " + requestLongitud);
                            S.E("callnumber" + callnumber);
                            S.E("Hire_CareGiver_id" + Hire_CareGiver_id);

                            S.E("sohel - getlatitude " + getlatitude);
                            S.E("sohel - getlongitude " + getlongitude);

                            LinearHiringconfirm.setVisibility(View.VISIBLE);
                            linearProgressIn.setVisibility(View.GONE);
                            linearLayoutAmbulancenCareId.setVisibility(View.GONE);
                            origin = new LatLng(Double.parseDouble(getlatitude), Double.parseDouble(getlongitude));
                            dest = new LatLng(Double.parseDouble(requestLatitude), Double.parseDouble(requestLongitud));

                         /*   MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(origin);
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_green));
                            marker = mMap.addMarker(markerOptions);

                            MarkerOptions markerOptions1 = new MarkerOptions();
                            markerOptions1.position(dest);
                            markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_green));
                            marker = mMap.addMarker(markerOptions1);

                            // Getting URL to the Google Directions API
                            String url = getDirectionsUrl(origin, dest);
                            DownloadTask downloadTask = new DownloadTask();
                            // Start downloading json data from Google Directions API
                            downloadTask.execute(url);*/
                            SavedData.saveAcceptLayoutKeep(true);

                            SavedData.saveAcceptedFullName(msg.getString("full_name"));
                            SavedData.saveAcceptedPhoneNo(msg.getString("phone"));
                            SavedData.saveAcceptedLatitude(requestLatitude);
                            SavedData.saveAcceptedLongitude(requestLongitud);
                            SavedData.saveAcceptedRating(msg.getString("ratingReview"));
                            SavedData.saveAcceptedImagePath(msg.getString("profile_pic"));
                            SavedData.saveCareGiverId(CareGiver_id);
                            SavedData.saveHireCareGiverId(Hire_CareGiver_id);
                        } else if (msg.getString("user_type").equals("5")) {
                            if (msg.getString("type").toLowerCase().equals("caregiver")) {
                                ((ClientMainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);

                                ClientMainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                ClientMainActivity.toggle.setDrawerIndicatorEnabled(false);
                                TimerRunning = true;
                                Intent i = new Intent(getActivity(), ClientTimerService.class);
                                getActivity().startService(i);

                                mainLayout.setVisibility(View.GONE);
                                clockLayout.setVisibility(View.VISIBLE);

                                SavedData.saveStartTimer("yes");

                                timerService.startTimer();
                                ClientupdateUIStartRun();
                                SavedData.saveAcceptLayoutKeep(false);
                            } else
                                {

                                is_start_ride = true;
                                LinearHiringconfirm.setVisibility(View.GONE);
                                ridingStart.setVisibility(View.VISIBLE);


                                try {
                                    if (gps.canGetLocation()) {
                                        latitude_current_new = gps.getLatitude();
                                        longitude_current_new = gps.getLongitude();
                                        if (latitude_current_new != 0.0 && longitude_current_new != 0.0) {
                                            origin_new = new LatLng(gps.getLatitude(), gps.getLongitude());
                                            dest_new = new LatLng(Double.parseDouble(msg.getString("source_latitude")), Double.parseDouble(msg.getString("source_longitude")));
                                            mMap.clear();
                                            showMarkersOnMap();
                                        }

                                    }

                                    SavedData.saveSRCAmbulanceLatitude("" + origin.latitude);
                                    SavedData.saveSRCAmbulanceLongitude("" + origin.longitude);
                                    SavedData.saveDestAmbulanceLatitude("" + dest.latitude);
                                    SavedData.saveDESTAmbulanceLongitude("" + dest.longitude);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                SavedData.saveInWOrking("working");
                                SavedData.saveAcceptLayoutKeep(false);
                            }


                        } else if (msg.getString("user_type").equals("6")) {
                            if (msg.getString("type").toLowerCase().equals("caregiver")) {

                                try {
                                    mainLayout.setVisibility(View.VISIBLE);
                                    clockLayout.setVisibility(View.GONE);

                                    ClientMainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                                    ClientMainActivity.toggle.setDrawerIndicatorEnabled(true);

                                    SavedData.saveStartTimer("no");

                                    Intent intent2 = new Intent(getActivity(), ClientTimerService.class);
                                    getActivity().stopService(intent2);


                                    Bundle bundle = new Bundle();
                                    bundle.putString("message", message);
                                    SavedData.saveSummerypage(true);
                                    SavedData.saveMessageForsummery(SavedData.getNotificationJson());


                                    S.I_clear(getActivity(), SummeryPageActivity.class, null);


                                    linearLayoutAmbulancenCareId.setVisibility(View.VISIBLE);
                                    LinearHiringconfirm.setVisibility(View.GONE);
                                } catch (Exception e) {

                                }
                            } else if (msg.getString("type").toLowerCase().equals("ambulance")) {

                                SavedData.saveInWOrking("");
                                ridingStart.setVisibility(View.GONE);
                                try {
                                    mainLayout.setVisibility(View.VISIBLE);
                                    clockLayout.setVisibility(View.GONE);

                                    ClientMainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                                    ClientMainActivity.toggle.setDrawerIndicatorEnabled(true);

                                    SavedData.saveStartTimer("no");
                                    Bundle bundle = new Bundle();
                                    bundle.putString("message", message);
                                    bundle.putString("ActivityCheck", "Client");
                                    SavedData.saveAmbulance(true);
                                    SavedData.saveMessageForsummery(SavedData.getNotificationJson());

                                    S.I_clear(getActivity(), AmbulanceSummeryPage.class, bundle);
                                    linearLayoutAmbulancenCareId.setVisibility(View.VISIBLE);
                                    LinearHiringconfirm.setVisibility(View.GONE);
                                } catch (Exception e) {

                                }
                            }
                        } else if (msg.getString("user_type").equals("9")) {

                            timeInMinut = msg.getString("time");

                            timeInM = Long.parseLong(timeInMinut);
                            long sec = timeInM % 60;
                            long min = (timeInM / 60) % 60;
                            long hours = (timeInM / 60) / 60;


                            String time = "";

                            if (hours < 10)
                                time = "0" + hours;
                            else
                                time = "" + hours;

                            if (min < 10)
                                time = time + ":0" + min;
                            else
                                time = time + ":" + min;


                            if (sec < 10)
                                time = time + ":0" + sec;
                            else
                                time = time + ":" + sec;

                            careGiverStopWatchText.setText(time);


                            timerService.stopTimer();
                            TimerRunning = false;

                        } else if (msg.getString("user_type").equals("8")) {
                            TimerRunning = true;
                            timeInMinut = msg.getString("time");
                            if (timeInMinut.equals("")) {
                                timeInM = 0;
                            } else {
                                timeInM = Long.parseLong(timeInMinut);
                            }
                            timerService.startTimer();


                        } else if (msg.getString("user_type").equals("15")) {
                            getlatitude = msg.getString("latitude");
                            getlongitude = msg.getString("longitude");
                            specializationId = msg.getString("caregiver_specialization_id");
                            serviceId = msg.getString("service_id");
                            typeAmbulanceCareGiver = msg.getString("type");

                            newRerminderLaterDialog(getActivity(), "Reminder For Book Caregiver and Ambulance!Book Your appointment");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Checked", "Again" + e);
                    }
                }
            }
        };

        unbinder = ButterKnife.bind(this, rootView);
        caregiverRecycleview.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), caregiverRecycleview, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (isCareGiverAvaliable)
                        {
                            S.E("position for product " + position);


                            serviceId = serviceCareList.get(position).getService_id();
                            if ("Doctor".equals(serviceCareList.get(position).getService_name())) {
                                Constants.SPECILIZATION_ROLE_DOCTOR = true;
                            } else {
                                Constants.SPECILIZATION_ROLE_DOCTOR = false;
                            }
                            getSpecialiazation(serviceCareList.get(position).getService_id());
                            getCaregiverAcrordingLocation();
                            if (latlongDr.size() > 0) {

                            } else {

                            }
                        } else {
                            Toast.makeText(getActivity(), "No Caregiver Found", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        AmbulanceRecycleview.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), AmbulanceRecycleview, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (isCareGiverAvaliable) {
                    serviceId = careGiverSpecializationslist.get(position).getCaregiver_specialization_id();
                    S.E("CheckPAramsPAssing" + serviceId);
                    getCaregiverAcrordingLocation();
                    LinearHireestimate.setVisibility(View.VISIBLE);
                    linearHireDrawer.setVisibility(View.VISIBLE);

                    linearLayoutAmbulancenCareId.setVisibility(View.GONE);
                    AmbulanceRecycleview.setVisibility(View.GONE);
                    if (latLngsAmbulance.size() > 0) {

                    } else {

                    }
                } else {
                    Toast.makeText(getActivity(), "No Ambulance Found", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        return rootView;
    }


    public void showMarkersOnMap() {
        destLat = dest_new.latitude;
        destLong = dest_new.longitude;

        pickupLat = origin_new.latitude;
        pickupLong = origin_new.longitude;

        mMap.clear();
        Object dataTransfer[];


        dataTransfer = new Object[5];
        String url = getDirectionsUrl(getActivity(), pickupLat, pickupLong, destLat, destLong);
        GetUserDirectionsData getDirectionsData = new GetUserDirectionsData();
        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        dataTransfer[2] = new LatLng(destLat, destLong);  // end lat long
        dataTransfer[3] = rotate;  // end lat long
        dataTransfer[4] = new LatLng(pickupLat, pickupLong);  // end lat long

        getDirectionsData.execute(dataTransfer);

        List<Marker> markers = new ArrayList<>();


        if (SavedData.getHiredUserType().equals(Constants.HIRE_AMBULANCE)) {
            carMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(pickupLat, pickupLong))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_car)));
            markers.add(carMarker);
        } else {

            carMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(pickupLat, pickupLong))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.doctor_other_small_size)));
            markers.add(carMarker);
        }


        eMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(destLat, destLong))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_green)));
        markers.add(eMarker);
        if (isFirstTime)
            focusMap(mMap, markers);
        mapUISettings(mMap);
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

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
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

    public void updateLocalMap() {
        List<Marker> markers = new ArrayList<>();

        if (SavedData.getHiredUserType().equals(Constants.HIRE_AMBULANCE)) {
            carMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(pickupLat, pickupLong))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ambulance_home)));
            markers.add(carMarker);
        } else {

            carMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(pickupLat, pickupLong))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.doctor_other_small_size)));
            markers.add(carMarker);
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(pickupLat, pickupLong), 17));


      /*  carCurrentLocation = new LatLng(pickupLat, pickupLong);
        if (carLastLocation != null) {
            animateMarker(mMap, carMarker, carCurrentLocation, carLastLocation, false);
        } else {
            animateMarker(mMap, carMarker, carCurrentLocation, carCurrentLocation, false);
        }
        carLastLocation = new LatLng(pickupLat, pickupLong);*/
        mapUISettings(mMap);
    }

    public void updateMarkerOnMap() {
        if (carMarker != null) {
            carMarker.remove();
        }

        try {
            if (locationUpdates != null) {
                Object dataTransfer[];
                dataTransfer = new Object[5];
                String url = getDirectionsUrl(getActivity(), Double.parseDouble(locationUpdates.getLattitude()), Double.parseDouble(locationUpdates.getLongitude()), Double.parseDouble(locationUpdates.getDest_lattitude()), Double.parseDouble(locationUpdates.getDest_longitude()));
                GetUserDirectionsData getDirectionsData = new GetUserDirectionsData();
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                dataTransfer[2] = new LatLng(Double.parseDouble(locationUpdates.getDest_lattitude()), Double.parseDouble(locationUpdates.getDest_longitude()));
                dataTransfer[3] = Float.parseFloat(locationUpdates.getCurrentPosition());
                dataTransfer[4] = new LatLng(Double.parseDouble(locationUpdates.getLattitude()), Double.parseDouble(locationUpdates.getLongitude()));
                getDirectionsData.execute(dataTransfer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        /*List<Marker> markers = new ArrayList<>();
        carMarker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(pickupLat, pickupLong))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_car)));
        markers.add(carMarker);

        carCurrentLocation = new LatLng(pickupLat, pickupLong);
        if (carLastLocation != null) {
            animateMarker(mMap, carMarker, carCurrentLocation, carLastLocation, false);
        } else {
            animateMarker(mMap, carMarker, carCurrentLocation, carCurrentLocation, false);
        }
        carLastLocation = new LatLng(pickupLat, pickupLong);
        mapUISettings(mMap);*/
    }


    public void animateMarker(GoogleMap mMap, final Marker marker, final LatLng startPosition, final LatLng toPosition, final boolean hideMarker) {

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
                // marker.setRotation((float) bearingBetweenLocations(startPosition, toPosition));
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
    }


    public void newRerminderLaterDialog(final Context cx, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(cx);
        adb.setTitle("Reminder");
        adb.setMessage(message);
        adb.setPositiveButton("Hire Now", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                getHiring();

            }
        });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }

    /*****************
     * Method to select date from date picker
     ***********************/
    public void dateDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                mSelectedDate = Constants.dateFormatForDisplayForThisAppOnly(year, monthOfYear + 1, dayOfMonth);
                timeDialog();

            }
        }, year, month, day);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(new Date());
        currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        dpDialog.setMinDate(calendar1);
        dpDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");

    }


    /*****************
     * Method to select time from time picker
     ***********************/
    public void timeDialog() {
        TimePickerDialog tpDialog = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                mSelectedTime = String.valueOf(hourOfDay + ":" + minute + ":00");

                schedualDate(mSelectedDate, mSelectedTime);
            }
        }, hour, minute, false);
        tpDialog.show(getActivity().getFragmentManager(), "TimePickerDialog");

    }

    Handler handler = new Handler();

    @Override
    public void onResume() {
        super.onResume();
        try {
            is_on_map = true;
            is_running = true;
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.REGISTRATION_COMPLETE));
            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));
            NotificationUtils.clearNotifications(getActivity());

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (is_from_mapready) {
                        is_from_mapready = false;
                    } else {
                        GetCurrentStatus();
                    }

                }
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //notificationHandelar();

    }

    @Override
    public void onPause() {
        is_on_map = false;
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    private void SpecializationDialog() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.specialization_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        final RecyclerView SpcializationrecyclerView = (RecyclerView) dialog.findViewById(R.id.Specilization_recycleview);
        final ImageView specialozationDialogBtnCoess = (ImageView) dialog.findViewById(R.id.specialozationDialogBtnCoess);
        SpcializationrecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), SpcializationrecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        S.E("position for product " + position);

                        ForSeniorORCurrent();
                        specializationId = careGiverSpecializationslistwithService.get(position).getCaregiver_specialization_id();
                        careGiverSpecializationslistwithService.get(position);
                        dialog.dismiss();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

        specialozationDialogBtnCoess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        SpecializationAdapter specializationAdapter = new SpecializationAdapter(getActivity(), careGiverSpecializationslistwithService);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity());
        SpcializationrecyclerView.setLayoutManager(layoutManager);
        SpcializationrecyclerView.setItemAnimator(new DefaultItemAnimator());
        SpcializationrecyclerView.setAdapter(specializationAdapter);
        dialog.show();
    }

    private void ForSeniorORCurrent() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addseniorcurrentdialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        final TextView tvCurrentLocId = (TextView) dialog.findViewById(R.id.tvCurrentLocId);
        final TextView tvForSeniorId = (TextView) dialog.findViewById(R.id.tvForSeniorId);
        final LinearLayout linearLayoutIdSenior = (LinearLayout) dialog.findViewById(R.id.linearLayoutIdSenior);
        final LinearLayout linearLayoutIdcurrent = (LinearLayout) dialog.findViewById(R.id.linearLayoutIdcurrent);
        final LinearLayout linearotherlocation = (LinearLayout) dialog.findViewById(R.id.linearotherlocation);
        final ImageView addSeniorDialogBtnCoess = (ImageView) dialog.findViewById(R.id.addSeniorDialogBtnCoess);
        caregiverSpecilizationRecycleview.setVisibility(View.GONE);
        specializationLayout.setVisibility(View.GONE);
        addSeniorDialogBtnCoess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        linearLayoutIdSenior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                LinearHireestimate.setVisibility(View.VISIBLE);
                linearHireDrawer.setVisibility(View.VISIBLE);
                linearLayoutAmbulancenCareId.setVisibility(View.GONE);
                horizontalScrollView1.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), ClientSeniorListActivity.class);
                intent.putExtra("whereKey", "Main");
                startActivityForResult(intent, REQuestCode);
            }
        });
        linearLayoutIdcurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                LinearHireestimate.setVisibility(View.VISIBLE);
                linearHireDrawer.setVisibility(View.VISIBLE);
                linearLayoutAmbulancenCareId.setVisibility(View.GONE);
                horizontalScrollView1.setVisibility(View.GONE);
            }
        });

        linearotherlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialog.dismiss();
                    LinearHireestimate.setVisibility(View.VISIBLE);
                    linearHireDrawer.setVisibility(View.VISIBLE);
                    linearLayoutAmbulancenCareId.setVisibility(View.GONE);
                    horizontalScrollView1.setVisibility(View.GONE);

                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(getActivity());
                    startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        dialog.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        setUpMap();
        is_first_time = true;
        is_from_mapready = true;
        GetCurrentStatus();
        /*if (SavedData.getAcceptLayout())
        {
            LinearHireConfirm.setVisibility(View.GONE);
            LinearHiringconfirm.setVisibility(View.VISIBLE);
            linearProgressIn.setVisibility(View.GONE);
            linearLayoutAmbulancenCareId.setVisibility(View.GONE);

            CareGiver_id = SavedData.getCareGiverId();
            Hire_CareGiver_id = SavedData.getHireCareGiverId();
            tvCaregiverId.setText(SavedData.getAcceptedFullName());
            callnumber = SavedData.getAcceptedPhoneNo();

            GetUpdateLocation();
            if (SavedData.getAcceptedRating().equals("")) {

            } else {
                tvRatingId.setText(new DecimalFormat("##.#").format(Double.parseDouble(SavedData.getAcceptedRating())));
            }
            Picasso.with(getActivity()).load(Const.URL.Image_Url + SavedData.getAcceptedImagePath()).error(R.drawable.user_profile_pic)
                    .into(ivCaregiverPicId);

            LinearHiringconfirm.setVisibility(View.VISIBLE);
            linearProgressIn.setVisibility(View.GONE);
            linearLayoutAmbulancenCareId.setVisibility(View.GONE);

        } else*/


    }


    private void setUpMap() {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(false);

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

            List<Address> addresses = new ArrayList<>();
            LatLng UCA;

            UCA = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            getlatitude = String.valueOf(mLastLocation.getLatitude());
            getlongitude = String.valueOf(mLastLocation.getLongitude());

            if (SavedData.getLatitude().equals("")) {
                UCA = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                getlatitude = String.valueOf(mLastLocation.getLatitude());
                getlongitude = String.valueOf(mLastLocation.getLongitude());

            } else {


                UCA = new LatLng(Double.parseDouble(SavedData.getLatitude()), Double.parseDouble(SavedData.getLongitude()));
                getlatitude = String.valueOf(SavedData.getLatitude());
                getlongitude = String.valueOf(SavedData.getLongitude());
            }


            try {
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                addresses = geocoder.getFromLocation(Double.parseDouble(getlatitude), Double.parseDouble(getlongitude), 1);
                autocompleteFragment.setText(addresses.get(0).getAddressLine(0));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(UCA, 17));

            LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(5000); //5 seconds
            mLocationRequest.setFastestInterval(3000); //3 seconds
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            if (NetworkUtil.isNetworkAvailable(getActivity())) {
                try {
                    getCaregiverAcrordingLocation();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }
        } catch (Exception e) {

        }
    }


    private ArrayList<LatLng> getCaregiverAcrordingLocation() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GET_CAREGIVER_LOC, 1, getParams2(), new Helper() {

            @Override
            public void backResponse(String response) {
                mMap.clear();
                S.E("CheckAni" + response);
                S.E("CheckAniPAram" + getParams2());
                try {
                    JSONObject mainobject = new JSONObject(response);

                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        isCareGiverAvaliable = true;
                        final LatLngBounds.Builder bld = new LatLngBounds.Builder();
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        float minimumPrevious = 0;
                        float minimumPrevious2 = 0;
                        float maximumValue = 0;
                        float minimumValue = 0;
                        latLngsAmbulance.clear();
                        latlongDr.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            S.E("CheckAniSize" + jsonArray.length());
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String full_name = jsonObject.getString("full_name");
                            String latitude = jsonObject.getString("latitude");
                            String longitud = jsonObject.getString("longitud");
                            String service_name = jsonObject.getString("service_name");
                            String min_charges = jsonObject.getString("min_charges");

                            S.E("min_charges" + min_charges);

                            float minimumChargesCurrent = Float.parseFloat(min_charges);

                            if (minimumChargesCurrent > minimumPrevious) {
                                maximumValue = minimumChargesCurrent;
                                minimumPrevious = minimumChargesCurrent;
                            } else {

                                maximumValue = minimumPrevious;

                            }

                            if (i == 0) {
                                minimumPrevious2 = minimumChargesCurrent;
                            }

                            if (minimumChargesCurrent < minimumPrevious2) {
                                minimumValue = minimumChargesCurrent;
                                minimumPrevious2 = minimumChargesCurrent;
                            } else {

                                minimumValue = minimumPrevious2;

                            }


                            S.E("check all methods Minimum Charges:" + minimumValue + "-" + maximumValue);
                            if (minimumValue == maximumValue) {
                                minimumChargeID.setText("Minimum Charges:" + min_charges);
                            } else {
                                minimumChargeID.setText("Minimum Charges:" + minimumValue + "-" + maximumValue);
                            }

                            String type = jsonObject.getString("type");
                            MarkerOptions markerOptions4 = new MarkerOptions();

                            double doublelatitude = Double.parseDouble(latitude);
                            double doublelongitude = Double.parseDouble(longitud);

                            LatLng ll = new LatLng(doublelatitude, doublelongitude);

                            bld.include(ll);
                            if (type.equals("2")) {
                                latLngsAmbulance.add(ll);
                                markerOptions4.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ambulance_home));
                            } else {
                                if (service_name.equals("Doctor")) {
                                    markerOptions4.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_caregiver_home));
                                } else {
                                    markerOptions4.icon(BitmapDescriptorFactory.fromResource(R.drawable.doctor_other_small_size));


                                }

                                latlongDr.add(ll);
                            }

                            List<Address> addresses = null;
                            LatLng UCA = new LatLng(doublelatitude, doublelongitude);
                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                            try {
                                addresses = geocoder.getFromLocation(doublelatitude, doublelongitude, 1);
                                S.E("address -=-=-=-=-=-=-=     " + addresses.get(0).getAddressLine(0));
                                markerOptions4.title(full_name + "(" + service_name + ")");

                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("check error", "Checked bu ani" + e);
                            }


                            markerOptions4.position(ll);
                            marker = mMap.addMarker(markerOptions4);
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
                            //  mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                        }
                        // mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                    } else {
                        isCareGiverAvaliable = false;
                        if(is_from_ambulance)
                        {
                            noInternetDialog("No Ambulance found");
                        }
                        else
                        {
                            noInternetDialog("No Caregiver found");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                    Log.e("checked by ani", "" + e);
                }

                handler_get_upad.postDelayed(get_updated_user, 10000);

            }
        });
        return null;
    }

    Handler handler_get_upad = new Handler();

    public Map<String, String> getParams2() {

        String cityName = "";
        try
        {



                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(getlatitude),Double.parseDouble(getlongitude) , 1);

                cityName = addresses.get(0).getLocality();




      /*      String stateName = addresses.get(0).getAddressLine(1);
            String countryName = addresses.get(0).getAddressLine(2);*/
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        HashMap<String, String> postParameters = new HashMap<>();
        postParameters.put("token", SavedData.gettocken_id());
        postParameters.put("latitude", getlatitude);
        postParameters.put("longitud", getlongitude);
        postParameters.put("city_name", cityName);
        postParameters.put("type", typeAmbulanceCareGiver);
        postParameters.put("service_id", serviceId);
        postParameters.put("specialization", specializationId);



        return postParameters;

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            if (SavedData.getLatitude().equalsIgnoreCase("")) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 17));
            }
            SavedData.saveLatitude("" + location.getLatitude());
            SavedData.saveLONGITUDE("" + location.getLongitude());
        }
        /*if (userCurrentStatus != null)
        {

            String status = userCurrentStatus.getBookingstatus();
            if(status == null || status.equalsIgnoreCase("completed") || status.equalsIgnoreCase("cancel"))
            {
                getlatitude = String.valueOf(location.getLatitude());
                getlongitude = String.valueOf(location.getLongitude());

                pickupLat = location.getLatitude();
                pickupLong = location.getLongitude();

                updateLocalMap();
            }
        }
         else   if (!SavedData.getInWorking().equals("working") && !SavedData.getAcceptLayout())
        {
            getlatitude = String.valueOf(location.getLatitude());
            getlongitude = String.valueOf(location.getLongitude());

            pickupLat = location.getLatitude();
            pickupLong = location.getLongitude();

            updateLocalMap();

        }
*/
    }


    private void getHiring() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.HiringConfirmation_Notification_Client, 1, getParams3(), new Helper() {
            @Override
            public void backResponse(String response) {

                S.E("Hiring request" + response);
                S.E("Hiring PArams" + getParams3());
                try {
                    is_from_other = false;
                    JSONObject mainobject = new JSONObject(response);
                    String status = mainobject.getString("status");
                    /*   int data = mainobject.getInt("1");*/
                    if (status.equals("success")) {
                        JSONObject content1 = mainobject.getJSONObject("data");
                        if (content1.has("hire_caregiver_id"))
                            hire_id_for_cancel = content1.getString("hire_caregiver_id");
                        //   Toast.makeText(getActivity(), "Notification Sent", Toast.LENGTH_SHORT).show();
                        linearConfirmHiringDrawer.setVisibility(View.GONE);
                        LinearHireestimate.setVisibility(View.GONE);
                        LinearHireConfirm.setVisibility(View.GONE);
                        linearProgressIn.setVisibility(View.VISIBLE);
                        startRunnableThreadWaitingForAccept();
                    } else {
                        Toast.makeText(getActivity(), "Not Sent...Try Again", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("LinearHiringconfirm error" + e);
                }
            }
        });
    }


    public void startRunnableThreadWaitingForAccept() {
        handlerContactRefershApi = new Handler();
        runnableContactRefershApi = new Runnable() {
            public void run() {
                try {
                    if (null != handlerContactRefershApi) {


                        handlerContactRefershApi.removeCallbacks(runnableContactRefershApi);


                    }

                    Update_cancel_status();

                    handlerContactRefershApi.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                getActivity().onBackPressed();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, 1000);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handlerContactRefershApi.postDelayed(runnableContactRefershApi, 30000);
    }

    private Map<String, String> getParams3()
    {
        HashMap<String, String> hashMap = new HashMap<>();


        if (is_from_other) {
            hashMap.put("latitude", otherlatitude);
            hashMap.put("longitud", otherlongitude);
        } else {
            hashMap.put("latitude", getlatitude);
            hashMap.put("longitud", getlongitude);
        }
        hashMap.put("token", SavedData.gettocken_id());
        hashMap.put("type", typeAmbulanceCareGiver);
        hashMap.put("service_id", serviceId);
        hashMap.put("specialization", specializationId);
        hashMap.put("senior_id", seniorIdstr);
        hashMap.put("status", "booking");

        return hashMap;
    }

    private void schedualDate(final String dateStr, final String timeStr) {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GETSCHEDUAL_TIMEDATE, 1, getSchedual(dateStr, timeStr), new Helper() {
            @Override
            public void backResponse(String response) {

                S.E("Hiring reques GETSCHEDUAL_TIMEDATEt" + response);
                S.E("Hiring PArams GETSCHEDUAL_TIMEDATE" + getSchedual(dateStr, timeStr));
                try {
                    JSONObject mainobject = new JSONObject(response);
                    if (mainobject.getString("status").equals("200")) {
                        S.I_clear(getActivity(), ClientMainActivity.class, null);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("LinearHiringconfirm error" + e);
                }
            }
        });
    }

    private Map<String, String> getSchedual(String dates, String times) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SavedData.gettocken_id());
        hashMap.put("time", times);
        hashMap.put("date", dates);
        hashMap.put("service_id", serviceId);
        hashMap.put("caregiver_specialization_id", specializationId);

        hashMap.put("type", typeAmbulanceCareGiver);
        hashMap.put("latitude", getlatitude);
        hashMap.put("longitude", getlongitude);

        return hashMap;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        is_running = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQuestCode && data != null) {
/*
            S.T(getActivity(), "ITs Come" + data.getStringExtra("id"));
*/
            seniorIdstr = data.getStringExtra("id");
            autocompleteFragment.setHint("Select Location For Senior");

        } else if (requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                LatLng latLng = place.getLatLng();
                if (gps.canGetLocation()) {
                    otherlatitude = "" + latLng.latitude;
                    otherlongitude = "" + latLng.longitude;
                    is_from_other = true;
                    //   dest_new = new LatLng(latLng.latitude, latLng.longitude);
                    /*if (latitude_current_new != 0.0 && longitude_current_new != 0.0)
                    {
                        origin_new = new LatLng(gps.getLatitude(), gps.getLongitude());
                        dest_new = new LatLng(latLng.latitude, latLng.longitude);


                        hashMap.put("longitud", getlongitude);

                    }*/

                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else {
            ((ClientMainActivity) getActivity()).onLaunchingFragment();

        }


    }

    private void getServices() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GetService_Url, 1, getParamService(), new Helper() {
            public void backResponse(String response) {
                try {
                    serviceCareList.clear();
                    S.E("Service ka response" + response);
                    JSONObject object1 = new JSONObject(response);
                    if (object1.getString("status").equals("200")) {
                        JSONArray content1 = object1.getJSONArray("data");
                        for (int j = 0; j < content1.length(); j++) {
                            JSONObject jsonObject1 = content1.getJSONObject(j);
                            CareGiverServiceModel careGiverServiceModel = new CareGiverServiceModel();
                            careGiverServiceModel.setService_id(jsonObject1.getString("service_id"));
                            careGiverServiceModel.setService_name(jsonObject1.getString("service_name"));
                            careGiverServiceModel.setServiceCreatedDate(jsonObject1.getString("service_created_date"));
                            serviceCareList.add(careGiverServiceModel);
                        }
                        clientGetServiceAdapter = new ClientGetServiceAdapter(getActivity(), serviceCareList);
                        LinearLayoutManager layoutManager
                                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        caregiverRecycleview.setLayoutManager(layoutManager);
                        caregiverRecycleview.setItemAnimator(new DefaultItemAnimator());
                        caregiverRecycleview.setAdapter(clientGetServiceAdapter);
                        caregiverRecycleview.setVisibility(View.VISIBLE);
                        S.E("size " + serviceCareList.size());
                    } else {
//                        avi.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParamService() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("type", "1");
        S.E("getService ki request" + params);
        return params;

    }

    private void getAmbulanceType() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GET_AMBULANCE_TYPE, 1, getParamAmbulance(), new Helper() {
            public void backResponse(String response) {
                try {
                    careGiverSpecializationslist.clear();
                    S.E("Ambulance  ka response" + response);
                    JSONObject object1 = new JSONObject(response);
                    if (object1.getString("status").equals("200")) {
                        JSONArray content1 = object1.getJSONArray("data");
                        for (int j = 0; j < content1.length(); j++) {
                            JSONObject jsonObject = content1.getJSONObject(j);
                            careGiverSpecialization = new CareGiverSpecialization();
                            careGiverSpecialization.setSpecialization(jsonObject.getString("specialization"));
                            careGiverSpecialization.setCaregiver_specialization_id(jsonObject.getString("caregiver_specialization_id"));
                            careGiverSpecializationslist.add(careGiverSpecialization);
                        }
                        ambulanceTypeAdapter = new AmbulanceTypeAdapter(getActivity(), careGiverSpecializationslist);
                        LinearLayoutManager layoutManager
                                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        AmbulanceRecycleview.setLayoutManager(layoutManager);
                        AmbulanceRecycleview.setItemAnimator(new DefaultItemAnimator());
                        AmbulanceRecycleview.setAdapter(ambulanceTypeAdapter);
                    } else {
//                        avi.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParamAmbulance() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("type", "2");
        S.E("getService ki request" + params);
        return params;

    }

    private void getSpecialiazation(String service_id) {

        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GETSpecializationList_Url, 1, getParams(service_id), new Helper() {
            public void backResponse(String response) {
                try {
                    careGiverSpecializationslistwithService.clear();
                    S.E("getSpecialiazation" + response);
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("200")) {
                        JSONArray content = object.getJSONArray("data");
                        for (int i = 0; i < content.length(); i++) {
                            JSONObject jsonObject = content.getJSONObject(i);
                            careGiverSpecialization = new CareGiverSpecialization();
//                            careGiverServiceModel.setService_id(jsonObject.getString("service_id"));
                            careGiverSpecialization.setSpecialization(jsonObject.getString("specialization"));
                            careGiverSpecialization.setCaregiver_specialization_id(jsonObject.getString("caregiver_specialization_id"));
                            careGiverSpecializationslistwithService.add(careGiverSpecialization);
                        }
                        /*SpecializationDialog();*/
                        S.E("careGiverSpecializationslistwithService" + careGiverSpecializationslistwithService.size());
                        caregiverRecycleview.setVisibility(View.GONE);
                        horizontalScrollView1.setVisibility(View.GONE);

                        caregiverSpecilizationRecycleview.setVisibility(View.VISIBLE);
                        specializationLayout.setVisibility(View.VISIBLE);
                        SpecializationAdapter specializationAdapter = new SpecializationAdapter(getActivity(), careGiverSpecializationslistwithService);
                        LinearLayoutManager layoutManager
                                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
                        caregiverSpecilizationRecycleview.setLayoutManager(layoutManager);
                        caregiverSpecilizationRecycleview.setItemAnimator(new DefaultItemAnimator());
                        caregiverSpecilizationRecycleview.setAdapter(specializationAdapter);
                        caregiverSpecilizationRecycleview.addOnItemTouchListener(
                                new RecyclerItemClickListener(getActivity(), caregiverSpecilizationRecycleview, new RecyclerItemClickListener.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        if (isCareGiverAvaliable) {
                                            S.E("position for product " + position);
                                            caregiverSpecilizationRecycleview.setVisibility(View.GONE);
                                            ForSeniorORCurrent();
                                            specializationId = careGiverSpecializationslistwithService.get(position).getCaregiver_specialization_id();
                                            careGiverSpecializationslistwithService.get(position);
                                        } else {
                                            Toast.makeText(getActivity(), "No Caregiver Found", Toast.LENGTH_SHORT).show();
                                        }


                                    }

                                    @Override
                                    public void onLongItemClick(View view, int position) {
                                        // do whatever
                                    }
                                })
                        );

                    } else {
//                        avi.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParams(String service_id) {
        HashMap<String, String> param = new HashMap<>();
        param.put("token", SavedData.gettocken_id());
        param.put("service_id", service_id);
        S.E(" token..." + SavedData.gettocken_id());
        return param;
    }

    public void showCancelReasonDailog() {
        isAnyOptionSelected = false;
        isEditTextChangeListerWork = false;
        reasonForCancel = "";

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        LinearLayout linearLayoutCaregiverDelayedButton, linearLayoutAmbulanceDelayedButton, linearLayoutChangeMyMindButton, linearLayoutUnableToContactCaregiverButton, linearLayoutUnableToContactAmbulanceButton, linearLayoutCaregiverDeniedDutyButton, linearLayoutAmbulanceDeniedDuty;

        final ImageView imageViewCareGiverDelayed, imageViewAmbulanceDelayed, imageViewChangeMyMind, iamgeViewUnableToContactCaregiver, imageViewUnableToContactAmbulance, imageViewCaregiverDeniedDuty, imageViewAmbulanceDeniedDuty;

        final EditText editTextAnyReason;

        final TextView textViewDontCancel, textViewCancelHire;

        linearLayoutCaregiverDelayedButton = (LinearLayout) dialogView.findViewById(R.id.linearLayoutCaregiverDelayedButton);
        linearLayoutAmbulanceDelayedButton = (LinearLayout) dialogView.findViewById(R.id.linearLayoutAmbulanceDelayedButton);
        linearLayoutChangeMyMindButton = (LinearLayout) dialogView.findViewById(R.id.linearLayoutChangeMyMindButton);
        linearLayoutUnableToContactCaregiverButton = (LinearLayout) dialogView.findViewById(R.id.linearLayoutUnableToContactCaregiverButton);
        linearLayoutUnableToContactAmbulanceButton = (LinearLayout) dialogView.findViewById(R.id.linearLayoutUnableToContactAmbulanceButton);
        linearLayoutCaregiverDeniedDutyButton = (LinearLayout) dialogView.findViewById(R.id.linearLayoutCaregiverDeniedDutyButton);
        linearLayoutAmbulanceDeniedDuty = (LinearLayout) dialogView.findViewById(R.id.linearLayoutAmbulanceDeniedDuty);

        imageViewCareGiverDelayed = (ImageView) dialogView.findViewById(R.id.imageViewCareGiverDelayed);
        imageViewAmbulanceDelayed = (ImageView) dialogView.findViewById(R.id.imageViewAmbulanceDelayed);
        imageViewChangeMyMind = (ImageView) dialogView.findViewById(R.id.imageViewChangeMyMind);
        iamgeViewUnableToContactCaregiver = (ImageView) dialogView.findViewById(R.id.iamgeViewUnableToContactCaregiver);
        imageViewUnableToContactAmbulance = (ImageView) dialogView.findViewById(R.id.imageViewUnableToContactAmbulance);
        imageViewCaregiverDeniedDuty = (ImageView) dialogView.findViewById(R.id.imageViewCaregiverDeniedDuty);
        imageViewAmbulanceDeniedDuty = (ImageView) dialogView.findViewById(R.id.imageViewAmbulanceDeniedDuty);

        editTextAnyReason = (EditText) dialogView.findViewById(R.id.editTextAnyReason);

        textViewDontCancel = (TextView) dialogView.findViewById(R.id.textViewDontCancel);
        textViewCancelHire = (TextView) dialogView.findViewById(R.id.textViewCancelHire);


        linearLayoutCaregiverDelayedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditTextChangeListerWork) {

                    imageViewCareGiverDelayed.setVisibility(View.VISIBLE);
                    imageViewAmbulanceDelayed.setVisibility(View.GONE);
                    imageViewChangeMyMind.setVisibility(View.GONE);
                    iamgeViewUnableToContactCaregiver.setVisibility(View.GONE);
                    imageViewUnableToContactAmbulance.setVisibility(View.GONE);
                    imageViewCaregiverDeniedDuty.setVisibility(View.GONE);
                    imageViewAmbulanceDeniedDuty.setVisibility(View.GONE);
                    textViewCancelHire.setTextColor(getActivity().getResources().getColor(R.color.dot_dark_screen2));
                    reasonForCancel = "";
                    reasonForCancel = "Caregiver Delayed";
                    isAnyOptionSelected = true;
                    editTextAnyReason.setEnabled(false);
                }
            }
        });


        linearLayoutAmbulanceDelayedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditTextChangeListerWork) {

                    imageViewCareGiverDelayed.setVisibility(View.GONE);
                    imageViewAmbulanceDelayed.setVisibility(View.VISIBLE);
                    imageViewChangeMyMind.setVisibility(View.GONE);
                    iamgeViewUnableToContactCaregiver.setVisibility(View.GONE);
                    imageViewUnableToContactAmbulance.setVisibility(View.GONE);
                    imageViewCaregiverDeniedDuty.setVisibility(View.GONE);
                    imageViewAmbulanceDeniedDuty.setVisibility(View.GONE);
                    textViewCancelHire.setTextColor(getActivity().getResources().getColor(R.color.dot_dark_screen2));
                    reasonForCancel = "";
                    reasonForCancel = "Ambulance Delayed";
                    isAnyOptionSelected = true;
                    editTextAnyReason.setEnabled(false);
                }
            }
        });

        linearLayoutChangeMyMindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditTextChangeListerWork) {

                    imageViewCareGiverDelayed.setVisibility(View.GONE);
                    imageViewAmbulanceDelayed.setVisibility(View.GONE);
                    imageViewChangeMyMind.setVisibility(View.VISIBLE);
                    iamgeViewUnableToContactCaregiver.setVisibility(View.GONE);
                    imageViewUnableToContactAmbulance.setVisibility(View.GONE);
                    imageViewCaregiverDeniedDuty.setVisibility(View.GONE);
                    imageViewAmbulanceDeniedDuty.setVisibility(View.GONE);
                    reasonForCancel = "";
                    reasonForCancel = "Changed my mind";
                    textViewCancelHire.setTextColor(getActivity().getResources().getColor(R.color.dot_dark_screen2));
                    isAnyOptionSelected = true;
                    editTextAnyReason.setEnabled(false);
                }
            }
        });


        linearLayoutUnableToContactCaregiverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditTextChangeListerWork) {

                    imageViewCareGiverDelayed.setVisibility(View.GONE);
                    imageViewAmbulanceDelayed.setVisibility(View.GONE);
                    imageViewChangeMyMind.setVisibility(View.GONE);
                    iamgeViewUnableToContactCaregiver.setVisibility(View.VISIBLE);
                    imageViewUnableToContactAmbulance.setVisibility(View.GONE);
                    imageViewCaregiverDeniedDuty.setVisibility(View.GONE);
                    imageViewAmbulanceDeniedDuty.setVisibility(View.GONE);
                    reasonForCancel = "";
                    reasonForCancel = "Unable to contact caregiver";
                    textViewCancelHire.setTextColor(getActivity().getResources().getColor(R.color.dot_dark_screen2));
                    isAnyOptionSelected = true;
                    editTextAnyReason.setEnabled(false);
                }
            }
        });

        linearLayoutUnableToContactAmbulanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditTextChangeListerWork) {

                    imageViewCareGiverDelayed.setVisibility(View.GONE);
                    imageViewAmbulanceDelayed.setVisibility(View.GONE);
                    imageViewChangeMyMind.setVisibility(View.GONE);
                    iamgeViewUnableToContactCaregiver.setVisibility(View.GONE);
                    imageViewUnableToContactAmbulance.setVisibility(View.VISIBLE);
                    imageViewCaregiverDeniedDuty.setVisibility(View.GONE);
                    imageViewAmbulanceDeniedDuty.setVisibility(View.GONE);
                    reasonForCancel = "";
                    reasonForCancel = "Unable to contact ambulance";
                    textViewCancelHire.setTextColor(getActivity().getResources().getColor(R.color.dot_dark_screen2));
                    isAnyOptionSelected = true;
                    editTextAnyReason.setEnabled(false);
                }
            }
        });

        linearLayoutCaregiverDeniedDutyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditTextChangeListerWork) {

                    imageViewCareGiverDelayed.setVisibility(View.GONE);
                    imageViewAmbulanceDelayed.setVisibility(View.GONE);
                    imageViewChangeMyMind.setVisibility(View.GONE);
                    iamgeViewUnableToContactCaregiver.setVisibility(View.GONE);
                    imageViewUnableToContactAmbulance.setVisibility(View.GONE);
                    imageViewCaregiverDeniedDuty.setVisibility(View.VISIBLE);
                    imageViewAmbulanceDeniedDuty.setVisibility(View.GONE);
                    reasonForCancel = "";
                    reasonForCancel = "Caregiver denied duty";
                    textViewCancelHire.setTextColor(getActivity().getResources().getColor(R.color.dot_dark_screen2));
                    isAnyOptionSelected = true;
                    editTextAnyReason.setEnabled(false);
                }
            }
        });

        linearLayoutAmbulanceDeniedDuty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEditTextChangeListerWork) {
                    imageViewCareGiverDelayed.setVisibility(View.GONE);
                    imageViewAmbulanceDelayed.setVisibility(View.GONE);
                    imageViewChangeMyMind.setVisibility(View.GONE);
                    iamgeViewUnableToContactCaregiver.setVisibility(View.GONE);
                    imageViewUnableToContactAmbulance.setVisibility(View.GONE);
                    imageViewCaregiverDeniedDuty.setVisibility(View.GONE);
                    imageViewAmbulanceDeniedDuty.setVisibility(View.VISIBLE);
                    reasonForCancel = "";
                    reasonForCancel = "Ambulance denied duty";
                    textViewCancelHire.setTextColor(getActivity().getResources().getColor(R.color.dot_dark_screen2));
                    isAnyOptionSelected = true;
                    editTextAnyReason.setEnabled(false);
                }
            }
        });


        editTextAnyReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    isEditTextChangeListerWork = true;
                    textViewCancelHire.setTextColor(getActivity().getResources().getColor(R.color.dot_dark_screen2));
                } else {
                    isEditTextChangeListerWork = false;
                    textViewCancelHire.setTextColor(getActivity().getResources().getColor(R.color.text_secondary));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textViewDontCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditTextChangeListerWork = false;
                isAnyOptionSelected = false;
                reasonForCancel = "";
                alertDailog.dismiss();
            }
        });

        textViewCancelHire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnyOptionSelected) {
                    alertDailog.dismiss();
                    Hiring_Cancel();
                } else if (isEditTextChangeListerWork) {
                    if (editTextAnyReason.getText().toString().trim().length() != 0) {
                        reasonForCancel = editTextAnyReason.getText().toString().trim();
                        alertDailog.dismiss();
                        Hiring_Cancel();
                    } else {
                        Toast.makeText(getActivity(), "Please enter reason or select!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Select any reason or type here", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertDailog = dialogBuilder.create();
        alertDailog.show();
    }


    private void Hiring_Cancel() {
        // Update_cancel_status();
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.CAREGIVER_CANCEL_REQUEST, 1, getParamsHiring_cnacel(), new Helper() {
            public void backResponse(String response) {
                try {
                    careGiverSpecializationslistwithService.clear();
                    S.E("Hiring_Cancel" + response);
                    S.E("Hiring_CancelParam" + getParamsHiring_cnacel());
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("200")) {
                        SavedData.saveAcceptLayoutKeep(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                S.I_clear(getActivity(), ClientMainActivity.class, null);
                                getActivity().finish();
                            }
                        }, 2000);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParamsHiring_cnacel() {

        String hire_id = SavedData.getHireCareGiverId();
        HashMap<String, String> param = new HashMap<>();
        param.put("token", SavedData.gettocken_id());
        param.put("reciever_id", CareGiver_id);
        param.put("reciever_type", "caregiver");
        param.put("cancel_reason", reasonForCancel);
        if (hire_id == null || hire_id.equalsIgnoreCase(""))
            param.put("hire_caregiver_id", Hire_CareGiver_id);
        else
            param.put("hire_caregiver_id", hire_id);
        param.put("status", "cancel");
        S.E(" token..." + SavedData.gettocken_id());
        return param;
    }


    private void Update_cancel_status() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.UPDATE_CANCEL_REQUEST, 1, getCancel_parameter(), new Helper() {
            public void backResponse(String response) {
                if (response != null) {

                }
            }
        });
    }


    private Map<String, String> getCancel_parameter() {

        String hire_id = SavedData.getHireCareGiverId();
        HashMap<String, String> param = new HashMap<>();
        param.put("hire_caregiver_id", hire_id_for_cancel);
        param.put("status", "cancel");
        S.E(" token..." + SavedData.gettocken_id());
        return param;
    }

    private void noInternetDialog(String msg) {
        try {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.no_internet_dialog_box);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            final Button codPopUpBtn = (Button) dialog.findViewById(R.id.dialogOkBtn);
            final TextView textNoInternet = (TextView) dialog.findViewById(R.id.textNoInternet);

            textNoInternet.setText(msg);

            codPopUpBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dialog.dismiss();
                    //   getCaregiverAcrordingLocation();
                }
            });
            dialog.show();
        } catch (Exception e) {
            Log.e("Cec", "TryCatch" + e);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        S.E("Starting and binding service");
        Intent i = new Intent(getActivity(), ClientTimerService.class);
        getActivity().startService(i);
        getActivity().bindService(i, mConnection, 0);
    }
//    for stop watch

    @Override
    public void onStop() {
        super.onStop();
        ClientupdateUIStopRun();
        if (serviceBound) {
            timerService.foreground();
            getActivity().unbindService(mConnection);
            serviceBound = false;
        }
        SavedData.saveTimerTime("" + save_time);
    }

    /**
     * Updates the UI when a run starts
     */
    private void ClientupdateUIStartRun() {
        mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
    }

    /**
     * Updates the UI when a run stops
     */
    private void ClientupdateUIStopRun() {
        mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
    }

    /**
     * Timer service tracks the start and end time of timer; service can be placed into the
     * foreground to prevent it being killed when the activity goes away
     */
    public static class ClientTimerService extends Service {

        private static final String TAG = ClientTimerService.class.getSimpleName();
        // Foreground notification id
        private static final int NOTIFICATION_ID = 1;
        // Service binder
        private final IBinder serviceBinder = new RunServiceBinder();
        // Start and end times in milliseconds
        private long startTime, endTime;
        // Is the service tracking time?
        private boolean isTimerRunning;

        @Override
        public void onCreate() {
            S.E("Creating service");
            startTime = 0;
            endTime = 0;
            isTimerRunning = false;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            S.E("Starting service");
            return Service.START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            S.E("Binding service");
            return serviceBinder;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            S.E("Destroying service");


        }

        /**
         * Starts the timer
         */
        public void startTimer() {
            if (!isTimerRunning) {
                startTime = System.currentTimeMillis();
                isTimerRunning = true;
            } else {
                S.E("startTimer request for an already running timer");
            }
        }

        /**
         * Stops the timer
         */
        public void stopTimer() {
            if (isTimerRunning) {
                endTime = System.currentTimeMillis();
                isTimerRunning = false;
            } else {
                S.E("stopTimer request for a timer that isn't running");
            }
        }

        /**
         * @return whether the timer is running
         */
        public boolean isTimerRunning() {
            return isTimerRunning;
        }

        /**
         * Returns the  elapsed time
         *
         * @return the elapsed time in seconds
         */
        public long elapsedTime() {
            // If the timer is running, the end time will be zero
            return endTime > startTime ?
                    (endTime - startTime) / 1000 :
                    (System.currentTimeMillis() - startTime) / 1000;
        }

        /**
         * Place the service into the foreground
         */
        public void foreground() {
//            startForeground(NOTIFICATION_ID, createNotification());
        }

        /**
         * Return the service to the background
         */
        public void background() {
            stopForeground(true);
        }

        /**
         * Creates a notification for placing the service into the foreground
         *
         * @return a notification for interacting with the service when in the foreground
         */

        public class RunServiceBinder extends Binder {
            public ClientTimerService getService() {
                return ClientTimerService.this;
            }
        }
    }

    /**
     * When the timer is running, use this handler to update
     * the UI every second to show timer progress
     */
    private class ClientUIUpdateHandler extends Handler {

        private final static int UPDATE_RATE_MS = 1000;
        private final WeakReference<ClientMainFragment> activity;

        ClientUIUpdateHandler(ClientMainFragment activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message message) {
            if (MSG_UPDATE_TIME == message.what) {
                S.E("updating time");
                ClientupdateUITimer();
                sendEmptyMessageDelayed(MSG_UPDATE_TIME, UPDATE_RATE_MS);
            }
        }
    }

    Handler handler_loc = new Handler();
    LocationUpdate locationUpdates;

    private void GetUpdateLocation() {
        S.E("accept prams ----    " + getUpdateLocationParam());

        Map<String, String> prams = getUpdateLocationParam();
        new JSONParser(getActivity()).parseVollyStringRequestWithoutLoad(Const.URL.CAREGIVER_GET_LOCATION, 1, prams, new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("Accept Request responce : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {

                        Gson gson = new Gson();
                        LocationResponse assignlistModel = gson.fromJson(response, LocationResponse.class);
                        locationUpdates = assignlistModel.getData().get(0);


                        if (is_start_ride)
                        {
                            LinearHiringconfirm.setVisibility(View.GONE);
                            ridingStart.setVisibility(View.VISIBLE);
                            SavedData.saveInWOrking("working");
                            SavedData.saveAcceptLayoutKeep(false);
                        } else {
                            if (pickupLat != 0 && pickupLat > 0) {
                                locationUpdates.setDest_lattitude("" + pickupLat);
                                locationUpdates.setDest_longitude("" + pickupLong);
                            } else {
                                try {
                                    pickupLat = Double.parseDouble(SavedData.getLatitude());
                                    pickupLong = Double.parseDouble(SavedData.getLongitude());

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                locationUpdates.setDest_lattitude("" + pickupLat);
                                locationUpdates.setDest_longitude("" + pickupLong);

                            }


                        }

                        updateMarkerOnMap();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    S.E("logprintStackTrace" + e);
                }


                handler_loc.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GetUpdateLocation();
                    }
                }, 5000);


            }
        });
    }


    private Map<String, String> getParamForCurrentStatus() {
        HashMap<String, String> prams = new HashMap<>();
        try {
            prams.put("token", SavedData.gettocken_id());
            // hashMap.put("token", SavedData.gettocken_id());
            //  prams.put("token", SavedData.gettocken_id());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return prams;
    }


    private Map<String, String> getUpdateLocationParam() {
        HashMap<String, String> prams = new HashMap<>();
        try {
            if (CareGiver_id == null || CareGiver_id.equalsIgnoreCase(""))
                CareGiver_id = SavedData.getCareGiverId();
            if (Hire_CareGiver_id == null || Hire_CareGiver_id.equalsIgnoreCase(""))
                Hire_CareGiver_id = SavedData.getHireCareGiverId();

            prams.put("hire_caregiver_id", Hire_CareGiver_id);
            prams.put("caregiver_id", CareGiver_id);
            // hashMap.put("token", SavedData.gettocken_id());
            //  prams.put("token", SavedData.gettocken_id());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return prams;
    }

    float rotate = 0;

    Runnable get_updated_user = new Runnable() {
        @Override
        public void run() {

            if (!SavedData.getAcceptLayout() && is_running && !SavedData.getInWorking().equals("working")) {

                getCaregiverAcrordingLocationwithoutload();
            }
            handler_get_upad.removeCallbacks(get_updated_user);


        }
    };

    private ArrayList<LatLng> getCaregiverAcrordingLocationwithoutload() {
        new JSONParser(getActivity()).parseVollyStringRequestWithoutLoad(Const.URL.GET_CAREGIVER_LOC, 1, getParams2(), new Helper() {

            @Override
            public void backResponse(String response) {
                mMap.clear();
                S.E("CheckAni" + response);
                S.E("CheckAniPAram" + getParams2());
                try {
                    JSONObject mainobject = new JSONObject(response);

                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        isCareGiverAvaliable = true;
                        final LatLngBounds.Builder bld = new LatLngBounds.Builder();
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        float minimumPrevious = 0;
                        float minimumPrevious2 = 0;
                        float maximumValue = 0;
                        float minimumValue = 0;
                        latLngsAmbulance.clear();
                        latlongDr.clear();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            S.E("CheckAniSize" + jsonArray.length());
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String full_name = jsonObject.getString("full_name");
                            String latitude = jsonObject.getString("latitude");
                            String longitud = jsonObject.getString("longitud");
                            String service_name = jsonObject.getString("service_name");
                            String min_charges = jsonObject.getString("min_charges");

                            S.E("min_charges" + min_charges);

                            float minimumChargesCurrent = Float.parseFloat(min_charges);

                            if (minimumChargesCurrent > minimumPrevious) {
                                maximumValue = minimumChargesCurrent;
                                minimumPrevious = minimumChargesCurrent;
                            } else {

                                maximumValue = minimumPrevious;

                            }

                            if (i == 0) {
                                minimumPrevious2 = minimumChargesCurrent;
                            }

                            if (minimumChargesCurrent < minimumPrevious2) {
                                minimumValue = minimumChargesCurrent;
                                minimumPrevious2 = minimumChargesCurrent;
                            } else {

                                minimumValue = minimumPrevious2;

                            }


                            S.E("check all methods Minimum Charges:" + minimumValue + "-" + maximumValue);
                            if (minimumValue == maximumValue) {
                                minimumChargeID.setText("Minimum Charges:" + min_charges);
                            } else {
                                minimumChargeID.setText("Minimum Charges:" + minimumValue + "-" + maximumValue);
                            }

                            String type = jsonObject.getString("type");
                            MarkerOptions markerOptions4 = new MarkerOptions();

                            double doublelatitude = Double.parseDouble(latitude);
                            double doublelongitude = Double.parseDouble(longitud);

                            LatLng ll = new LatLng(doublelatitude, doublelongitude);

                            bld.include(ll);
                            if (type.equals("2")) {
                                latLngsAmbulance.add(ll);
                                markerOptions4.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ambulance_home));
                            } else {
                                if (service_name.equals("Doctor")) {
                                    markerOptions4.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_caregiver_home));
                                } else {
                                    markerOptions4.icon(BitmapDescriptorFactory.fromResource(R.drawable.doctor_other_small_size));


                                }

                                latlongDr.add(ll);
                            }

                            List<Address> addresses = null;
                            LatLng UCA = new LatLng(doublelatitude, doublelongitude);
                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                            try {
                                addresses = geocoder.getFromLocation(doublelatitude, doublelongitude, 1);
                                S.E("address -=-=-=-=-=-=-=     " + addresses.get(0).getAddressLine(0));
                                markerOptions4.title(full_name + "(" + service_name + ")");

                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("check error", "Checked bu ani" + e);
                            }


                            markerOptions4.position(ll);
                            marker = mMap.addMarker(markerOptions4);
                            // mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
                            //  mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                        }
                        // mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                    } else {
                        //  isCareGiverAvaliable = false;
                        //  noInternetDialog(mainobject.getString("message"));
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                    Log.e("checked by ani", "" + e);
                }

                handler_get_upad.postDelayed(get_updated_user, 10000);

            }
        });
        return null;
    }


    private void GetCurrentStatus() {
        //  S.E("accept prams ----    " + getUpdateLocationParam());

        Map<String, String> prams = getParamForCurrentStatus();
        new JSONParser(getActivity()).parseVollyStringRequestWithoutLoad(Const.URL.GET_BOOKING_STATUS_USER, 1, prams, new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("Accept Request responce : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {

                        Gson gson = new Gson();
                        UserCurrentStatusResponse assignlistModel = gson.fromJson(response, UserCurrentStatusResponse.class);
                        userCurrentStatus = assignlistModel.getData().get(0);

                        if (userCurrentStatus != null) {

                            String status = userCurrentStatus.getBookingstatus();

                            Hire_CareGiver_id = userCurrentStatus.getHireCaregiverId();

                            SavedData.saveHireCareGiverId(Hire_CareGiver_id);

                            if (CareGiver_id == null || CareGiver_id.equalsIgnoreCase(""))
                                SavedData.saveCareGiverId(userCurrentStatus.getCaregiverId());

                            if (status != null && status.equalsIgnoreCase("accepted")) {
                                LinearHireConfirm.setVisibility(View.GONE);
                                LinearHiringconfirm.setVisibility(View.VISIBLE);
                                linearProgressIn.setVisibility(View.GONE);
                                linearLayoutAmbulancenCareId.setVisibility(View.GONE);

                                CareGiver_id = userCurrentStatus.getCaregiverId();
                                Hire_CareGiver_id = userCurrentStatus.getHireCaregiverId();
                                tvCaregiverId.setText(userCurrentStatus.getFullName());
                                callnumber = userCurrentStatus.getPhone();
                                SavedData.saveHireCareGiverId(Hire_CareGiver_id);

                                SavedData.saveAcceptedFullName(userCurrentStatus.getFullName());
                                SavedData.saveAcceptedPhoneNo(callnumber);
                                SavedData.saveCareGiverId(CareGiver_id);


                                //     tvRatingId.setText(new DecimalFormat("##.#").format(Double.parseDouble(userCurrentStatus.ratin)));


                                try {
                                    String type = userCurrentStatus.getType();
                                    if (type != null && type.equalsIgnoreCase("2")) {
                                        textViewVehichleNumber.setVisibility(View.VISIBLE);
                                        textViewVehichleNumber.setText("Vehicle Number : " + userCurrentStatus.getVehicalRegistrationNo());
                                    } else {
                                        textViewVehichleNumber.setVisibility(View.GONE);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                GetUpdateLocation();
                                /*if (SavedData.getAcceptedRating().equals("")) {

                                } else {
                                    tvRatingId.setText(new DecimalFormat("##.#").format(Double.parseDouble(SavedData.getAcceptedRating())));
                                }*/
                                Picasso.with(getActivity()).load(Const.URL.Image_Url + SavedData.getAcceptedImagePath()).error(R.drawable.user_profile_pic)
                                        .into(ivCaregiverPicId);

                                LinearHiringconfirm.setVisibility(View.VISIBLE);
                                linearProgressIn.setVisibility(View.GONE);
                                linearLayoutAmbulancenCareId.setVisibility(View.GONE);
                            } else if (status != null && status.equalsIgnoreCase("start")) {

                                String typeOfCaregiver = userCurrentStatus.getType();

                                is_start_ride = true;
                                if (typeOfCaregiver.equalsIgnoreCase("1")) {
                                    ((ClientMainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);

                                    ClientMainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                    ClientMainActivity.toggle.setDrawerIndicatorEnabled(false);
                                    TimerRunning = true;
                                    Intent i = new Intent(getActivity(), ClientTimerService.class);
                                    getActivity().startService(i);

                                    mainLayout.setVisibility(View.GONE);
                                    clockLayout.setVisibility(View.VISIBLE);

                                    SavedData.saveStartTimer("yes");

                                    timerService.startTimer();
                                    ClientupdateUIStartRun();
                                    SavedData.saveAcceptLayoutKeep(false);
                                } else {
                                    linearLayoutAmbulancenCareId.setVisibility(View.GONE);
                                    LinearHiringconfirm.setVisibility(View.GONE);
                                    ridingStart.setVisibility(View.VISIBLE);
                                    SavedData.saveInWOrking("working");
                                    SavedData.saveAcceptLayoutKeep(false);
                                    GetUpdateLocation();
                                }


                            } else if (status != null && status.equalsIgnoreCase("payment")) {
                                if (userCurrentStatus.getType().equalsIgnoreCase("1")) {
                                    //if (SavedData.getSummeryPage() == true)
                                    // {
                                    String message = SavedData.getMessageForSummery();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("message", message);
                                    SavedData.saveSummerypage(true);


                                    S.I_clear(getActivity(), SummeryPageActivity.class, null);
                                    // }
                                } else {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("ActivityCheck", "Client");
                                    Intent intent1 = new Intent(getActivity(), AmbulanceSummeryPage.class);
                                    intent1.putExtras(bundle);
                                    startActivity(intent1);
                                }


                            } /*else if (status != null && status.equalsIgnoreCase("rating"))
                            {


                            }*/


                            try {
                                if (SavedData.getRatingStatus()) {
                                    String message = SavedData.getMessageForSummery();

                                    String kmPerChargestr = "0.0";
                                    JSONObject mainobject = new JSONObject(message);

                                    JSONObject msg = mainobject.getJSONObject("msg");

                                    String min_charges;
                                    if (msg.getString("amount").equals("null")) {
                                        kmPerChargestr = "0.0";

                                    } else {
                                        kmPerChargestr = msg.getString("amount");
                                    }

                                    if (msg.getString("min_charges").equals("null")) {
                                        min_charges = "0.0";
                                    } else {

                                        min_charges = msg.getString("min_charges");
                                    }
                                    String Amount, PayType;
                                    int rupees;


                                    double totalkm = Double.parseDouble((msg.getString("total_kilometer")));
                                    double amount = Double.parseDouble(kmPerChargestr);

                                    Amount = String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(kmPerChargestr)));
                                    rupees = Integer.parseInt(new DecimalFormat("##.##").format(Double.parseDouble(kmPerChargestr)));
                                    double totalAmount1 = totalkm * amount;

                                    String caregiver_id = msg.getString("caregiver_id");

                                    PayType = SavedData.getPayType();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("FullName", msg.getString("full_name"));
                                    bundle.putString("Type", msg.getString("type"));
                                    bundle.putString("Picture", msg.getString("profile_pic"));

                                    if (totalkm > 1) {
                                        double remaningKm = totalkm - 1;
                                        double extraAmount = remaningKm * (Double.parseDouble(kmPerChargestr));
                                        bundle.putString("Amount", "" + new DecimalFormat("##.##").format(extraAmount + Double.parseDouble(min_charges)));
                                        //totalAmount.setText("" + new DecimalFormat("##.##").format(extraAmount + Double.parseDouble(min_charges)));
                                    } else {
                                        //  totalAmount.setText("" + new DecimalFormat("##.##").format(Double.parseDouble(min_charges)));
                                        bundle.putString("Amount", "" + new DecimalFormat("##.##").format(Double.parseDouble(min_charges)));
                                    }


                                    bundle.putString("StartTime", msg.getString("source_location"));
                                    bundle.putString("StopTime", msg.getString("destination_location"));
                                    bundle.putString("TotalTime", "");
                                    bundle.putString("PaymentMode", PayType);
                                    bundle.putString("caregiver_id", caregiver_id);
                                    SavedData.saveAmbulance(false);
                                    SavedData.savePayType(PayType);
                                    SavedData.saveRatingStatus(true);
                                    // SavedData.saveMessageForsummery("");
                                    //     S.I_clear(AmbulanceSummeryPage.this, Client_RatingActivity.class, bundle);


                                    Intent intent1 = new Intent(getActivity(), Client_RatingActivity.class);
                                    intent1.putExtras(bundle);
                                    startActivity(intent1);
                                } else {

                                    String message = SavedData.getHireLaterMessage();
                                    if (message != null && !message.equalsIgnoreCase("")) {
                                        JSONObject jsonObject1 = new JSONObject(message);
                                        JSONObject msg = jsonObject1.getJSONObject("msg");

                                        getlatitude = msg.getString("latitude");
                                        getlongitude = msg.getString("longitude");
                                        specializationId = msg.getString("caregiver_specialization_id");
                                        serviceId = msg.getString("service_id");
                                        typeAmbulanceCareGiver = msg.getString("type");

                                        newRerminderLaterDialog(getActivity(), "Reminder For Book Caregiver and Ambulance!Book Your appointment");

                                        SavedData.saveHireLaterMessage("");
                                    }

                                }


                            } catch (JSONException e) {
                                S.E("check exception" + e);
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


}