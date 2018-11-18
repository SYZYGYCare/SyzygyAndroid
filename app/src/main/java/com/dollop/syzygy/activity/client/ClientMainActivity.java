package com.dollop.syzygy.activity.client;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.AboutUsActivity;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.activity.PrivacyPolicyActivity;
import com.dollop.syzygy.activity.SupportDetailActivity;
import com.dollop.syzygy.activity.TermAndConditionActivity;
import com.dollop.syzygy.activity.WelcomeActivity;
import com.dollop.syzygy.data.BaseItem;
import com.dollop.syzygy.data.ClientCustomDataProvider;
import com.dollop.syzygy.fragment.client.ClientChangepassword;
import com.dollop.syzygy.fragment.client.ClientEmergencyContact;
import com.dollop.syzygy.fragment.client.ClientMainFragment;
import com.dollop.syzygy.fragment.client.ReferAndEarnFragment;
import com.dollop.syzygy.fragment.client.SupportClaimFragment;
import com.dollop.syzygy.fragment.client.Your_Hires;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.views.LevelBeamView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.openrnd.multilevellistview.ItemInfo;
import pl.openrnd.multilevellistview.MultiLevelListAdapter;
import pl.openrnd.multilevellistview.MultiLevelListView;
import pl.openrnd.multilevellistview.OnItemClickListener;

public class ClientMainActivity extends BaseActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, DrawerLocker, OnMenuItemClickListener, OnMenuItemLongClickListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static DrawerLayout drawer;
    public static ActionBarDrawerToggle toggle;
    ////////////////////////////////////////////
    LatLng latLng;
    int currentFragment = 0;
    TextView clientUserName, clientMobileNo;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    ImageView clientUserImage;
    Marker mCurrLocationMarker, marker1;
    double currentlat;
    double currentlong;
    Marker marker;
    SupportMapFragment mapFragment;
    boolean mainFragment = true;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private MultiLevelListView multiLevelListView;
    private GoogleMap mMap;
    private String Hire_CareGiver_id;

    public void onLaunchingFragment() {
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.rlMapLayout, new ClientMainFragment()).commit();
        drawer.closeDrawer(GravityCompat.START);
        mainFragment = true;

    }

    private ContextMenuDialogFragment mMenuDialogFragment;
    private FragmentManager fragmentManager1;
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        private void showItemDescription(Object object, ItemInfo itemInfo) {
            StringBuilder builder = new StringBuilder("\"");
            builder.append(((BaseItem) object).getName());
            builder.append("\" clicked!\n");
            builder.append(getItemInfoDsc(itemInfo));

            /*  Toast.makeText(CareGiverMainActivity.this, builder.toString(), Toast.LENGTH_SHORT).show();*/
        }

        @Override
        public void onItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);

            if (((BaseItem) item).getName().equals("Logout")) {
                logoutDialog();
            } else if (((BaseItem) item).getName().equals("About")) {

                S.I(ClientMainActivity.this, AboutUsActivity.class, null);
                drawer.closeDrawer(GravityCompat.START);
            } else if (((BaseItem) item).getName().equals("Support")) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.rlMapLayout, new SupportClaimFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                mainFragment = false;
            } else if (((BaseItem) item).getName().equals("Your Hires")) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.rlMapLayout, new Your_Hires()).commit();
                drawer.closeDrawer(GravityCompat.START);
                mainFragment = false;
            } else if (((BaseItem) item).getName().equals("Emergency Contact")) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.rlMapLayout, new ClientEmergencyContact()).commit();
                drawer.closeDrawer(GravityCompat.START);
                mainFragment = false;
            } else if (((BaseItem) item).getName().equals("Add Someone")) {
               /* mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.rlMapLayout, new ClientAddSenior()).commit();
                drawer.closeDrawer(GravityCompat.START);*/
                Intent intent = new Intent(ClientMainActivity.this, ClientSeniorListActivity.class);
                intent.putExtra("whereKey", "Drawer");
                startActivity(intent);
                mainFragment = false;
            } else if (((BaseItem) item).getName().equals("Hire Your Caregiver")) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.rlMapLayout, new ClientMainFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                mainFragment = true;
            } else if (((BaseItem) item).getName().equals("Privacy Policy")) {
                S.I(ClientMainActivity.this, PrivacyPolicyActivity.class, null);
                drawer.closeDrawer(GravityCompat.START);
            } else if (((BaseItem) item).getName().equals("Terms & Conditions")) {
                S.I(ClientMainActivity.this, TermAndConditionActivity.class, null);
                drawer.closeDrawer(GravityCompat.START);
            } else if (((BaseItem) item).getName().equals("Change Password")) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.rlMapLayout, new ClientChangepassword()).commit();
                drawer.closeDrawer(GravityCompat.START);
                mainFragment = false;
            } else if (((BaseItem) item).getName().equals("Refer & Earn")) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.rlMapLayout, new ReferAndEarnFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                mainFragment = false;
            }
        }

        @Override
        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }
    };

    @Override
    protected int getContentResId() {
        return R.layout.activity_client_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton(getString(R.string.app_name));

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.rlMapLayout, new ClientMainFragment()).commit();
        confMenu();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        currentFragment = 13;
        SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

      /*  ClientMainFragment clientMainFragment = new ClientMainFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.rlMapLayout, clientMainFragment);

        fragmentTransaction.commit();*/

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        /*navigationView.setNavigationItemSelectedListener(this);*/
        Menu menu = navigationView.getMenu();
        View headerview = navigationView.getHeaderView(0);
        clientUserImage = (ImageView) findViewById(R.id.clientUserImage);
        clientUserName = (TextView) findViewById(R.id.clientUserName);
        clientMobileNo = (TextView) findViewById(R.id.clientMobileNo);
        clientMobileNo.setText(SavedData.getUserPhone());
        RelativeLayout clientHeaderView = (RelativeLayout) findViewById(R.id.clientHeaderView);

        clientHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.I(ClientMainActivity.this, ClientUpdateProfile.class, null);
            }
        });

        getClientProfile();

        fragmentManager1 = getSupportFragmentManager();
        initMenuFragment();

    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.dimen_50dp));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(this);
        mMenuDialogFragment.setItemLongClickListener(this);
    }

    private List<MenuObject> getMenuObjects() {
        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.ic_cross_color);

        MenuObject send = new MenuObject("Health Tips");
        send.setResource(R.drawable.ic_bell);

        MenuObject like = new MenuObject("ToDo List");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icon_todo_new);
        like.setBitmap(b);

        MenuObject remainder = new MenuObject("Reminder");
        Bitmap br = BitmapFactory.decodeResource(getResources(), R.drawable.ic_clock_red);
        remainder.setBitmap(br);

        /*MenuObject addFr = new MenuObject("Tips Notification");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_message));
        addFr.setDrawable(bd);*/

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(remainder);
//        menuObjects.add(addFr);
        return menuObjects;
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
//                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng latLng = new LatLng(22.4959, 75.1545);
        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(latLng);
        markerOptions1.title("Dr Robort");
        markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_caregiver_home));
        marker = mMap.addMarker(markerOptions1);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));


        LatLng latLng1 = new LatLng(22.432, 75.5464);
        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(latLng1);
        markerOptions2.title("Dr Maya");
        markerOptions2.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_caregiver_non_active));
        marker = mMap.addMarker(markerOptions2);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        LatLng latLng2 = new LatLng(22.3122, 75.3264);
        MarkerOptions markerOptions3 = new MarkerOptions();
        markerOptions3.position(latLng2);
        markerOptions3.title("ambu");
        markerOptions3.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ambulance_home));
        marker = mMap.addMarker(markerOptions3);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        LatLng latLng3 = new LatLng(22.5512, 75.9434);
        MarkerOptions markerOptions4 = new MarkerOptions();
        markerOptions4.position(latLng3);
        markerOptions4.title("Ambulance Wala");
        markerOptions4.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_ambulance_home));
        marker = mMap.addMarker(markerOptions4);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    private void getClientProfile() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.GET_PROFILE, 1, getParams(), new Helper() {

            @Override
            public void backResponse(String response) {
                S.E(response);
                try {
                    JSONObject mainobject = new JSONObject(response);

                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        /*JSONObject jsonObject1 = mainobject.getJSONObject("data");

                        clientUserName.setText(jsonObject1.getString("full_name"));*/


                        JSONObject jsonObject1 = mainobject.getJSONObject("data");

                        if (jsonObject1.has("full_name"))
                        {
                            clientUserName.setText(jsonObject1.getString("full_name"));
                            SavedData.saveUserName(jsonObject1.getString("full_name"));
                        }

                        if (jsonObject1.has("email_id"))
                        {

                            SavedData.saveUserEmail(jsonObject1.getString("email_id"));
                        }


                        if (jsonObject1.has("user_id"))
                        {
                           ;
                            SavedData.saveUserId(jsonObject1.getString("user_id"));
                        }


                        Picasso.with(ClientMainActivity.this).load(Const.URL.Image_Url + jsonObject1.getString("profile_pic")).error(R.drawable.user_profile_pic).into(clientUserImage);

                        //    clientMobileNo.setText(jsonObject1.getString("phone"));
                    } else {
//                        Toast.makeText(context, "\"No HireGiver\"", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("authority_id", "1");
        return params;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        currentlat = location.getLatitude();
        currentlong = location.getLongitude();
        if (currentlat == 0.0) {


        } else {
            //   getCaregiverAcrordingLocation();

        }


        Log.e("getLongitude", "" + currentlat);
        Log.e("getLongitude", "" + currentlong);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));


        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
      /*  Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
             addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            S.E("address -=-=-=-=-=-=-=     " + addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            e.printStackTrace();
            S.E("address -=-=-=-=-=-=-=     " + e);

        }
*/

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
        }
    }

/*
    private void getCaregiverAcrordingLocation() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.GET_CAREGIVER_LOC, 1, getParams2(), new Helper() {

            @Override
            public void backResponse(String response) {
                S.E("CheckAni" + response);
                try {
                    JSONObject mainobject = new JSONObject(response);

                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        JSONObject jsonObject1 = mainobject.getJSONObject("data");


                    } else {
//                        Toast.makeText(context, "\"No HireGiver\"", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
*/

    public Map<String, String> getParams2() {
        HashMap<String, String> postParameters = new HashMap<>();
        postParameters.put("token", SavedData.gettocken_id());
        postParameters.put("latitude", " 22.7571");
        postParameters.put("longitude", "75.8822");
//        M.E("param.." + postParameters);
        return postParameters;
    }

    @Override
    public void finish() {

        if (mainFragment) {
            super.finish();
        } else {
            Hire_CareGiver_id = SavedData.getHireCareGiverId();
            Hiring_Cancel();

            ClientMainFragment clientMainFragment = new ClientMainFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.rlMapLayout, clientMainFragment);
            fragmentTransaction.commit();
            mainFragment = true;
            return;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void confMenu() {
        multiLevelListView = (MultiLevelListView) findViewById(R.id.ClientmultiLevelMenu);

        // custom ListAdapter
        ClientMainActivity.ListAdapter listAdapter = new ClientMainActivity.ListAdapter();

        multiLevelListView.setAdapter(listAdapter);
        multiLevelListView.setOnItemClickListener(mOnItemClickListener);

        listAdapter.setDataItems(ClientCustomDataProvider.getInitialItems());
    }

    private String getItemInfoDsc(ItemInfo itemInfo) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("level[%d], idx in level[%d/%d]",
                itemInfo.getLevel() + 1, /*Indexing starts from 0*/
                itemInfo.getIdxInLevel() + 1 /*Indexing starts from 0*/,
                itemInfo.getLevelSize()));

        if (itemInfo.isExpandable()) {
            builder.append(String.format(", expanded[%b]", itemInfo.isExpanded()));
        }
        return builder.toString();
    }

    public void getCurrentStatus(boolean n) {

        mainFragment = n;

    }

    public void launchFragmentTitle(String fragemntName) {

        getSupportActionBar().setTitle(fragemntName);/*
        setToolbarWithBackButton(fragemntName);*/
    }

    @Override
    public void setDrawerEnabled(boolean enabled) {
        int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
        drawer.setDrawerLockMode(lockMode);
        toggle.setDrawerIndicatorEnabled(enabled);
    }

    private void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ClientMainActivity.this);
        builder.setTitle("Confirmation!");
        builder.setMessage("Are you sure? You want to Logout.");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                getLogoutId();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.item_bell, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_bell:
                if (fragmentManager1.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager1, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return true;
    }

    private void getLogoutId() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.LogOut, 1, getParams5(), new Helper() {

            @Override
            public void backResponse(String response) {
                S.E("responseLogout" + response);
                try {
                    JSONObject mainobject = new JSONObject(response);

                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        SavedData.getInstance();
                        SavedData.saveTocken("Logout");
                        SavedData.clear();
                        S.I_clear(ClientMainActivity.this, WelcomeActivity.class, null);
                    } else {
//                        Toast.makeText(context, "\"No HireGiver\"", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    S.E("logout" + e);
                }
            }
        });
    }

    private Map<String, String> getParams5() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("type", "2");

        return params;
    }

    @Override
    public void onMenuItemClick(View clickedView, int position) {
        if (position == 0) {
           // Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
        }
        if (position == 1) {
            Intent intent = new Intent(ClientMainActivity.this, HealthTipsActivity.class);
            startActivity(intent);
        }
        if (position == 2) {
            Intent intent = new Intent(ClientMainActivity.this, CheckListActivity.class);
            startActivity(intent);
        }
        if (position == 3) {
            S.I(ClientMainActivity.this, ClientReminderActivity.class, null);
        }
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {

    }

    private class ListAdapter extends MultiLevelListAdapter {

        @Override
        public List<?> getSubObjects(Object object) {
            // DIEKSEKUSI SAAT KLIK PADA GROUP-ITEM
            return ClientCustomDataProvider.getSubItems((BaseItem) object);
        }

        @Override
        public boolean isExpandable(Object object) {
            return ClientCustomDataProvider.isExpandable((BaseItem) object);
        }

        @Override
        public View getViewForObject(final Object object, View convertView, ItemInfo itemInfo) {
            ClientMainActivity.ListAdapter.ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ClientMainActivity.ListAdapter.ViewHolder();
                convertView = LayoutInflater.from(ClientMainActivity.this).inflate(R.layout.data_item, null);
                //viewHolder.infoView = (TextView) convertView.findViewById(R.id.dataItemInfo);
                viewHolder.nameView = (TextView) convertView.findViewById(R.id.dataItemName);
                viewHolder.arrowView = (ImageView) convertView.findViewById(R.id.dataItemArrow);
                viewHolder.levelBeamView = (LevelBeamView) convertView.findViewById(R.id.dataItemLevelBeam);
                viewHolder.dataItemImage = (ImageView) convertView.findViewById(R.id.dataItemImage);
                viewHolder.mainLayout = (LinearLayout) convertView.findViewById(R.id.mainLayout);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ClientMainActivity.ListAdapter.ViewHolder) convertView.getTag();
            }
            viewHolder.dataItemImage.setImageResource(((BaseItem) object).getIcon());
            viewHolder.nameView.setText(((BaseItem) object).getName());
            //viewHolder.infoView.setText(getItemInfoDsc(itemInfo));
            viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });
            viewHolder.nameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (((BaseItem) object).getName().equals("Logout")) {
                        logoutDialog();
                    } else if (((BaseItem) object).getName().equals("Your Hires")) {
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.rlMapLayout, new Your_Hires()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        mainFragment = false;
                    } else if (((BaseItem) object).getName().equals("Emergency Contact")) {
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.rlMapLayout, new ClientEmergencyContact()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        mainFragment = false;
                    } else if (((BaseItem) object).getName().equals("Add Senior")) {
               /* mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.rlMapLayout, new ClientAddSenior()).commit();
                drawer.closeDrawer(GravityCompat.START);*/
                        Intent intent = new Intent(ClientMainActivity.this, ClientSeniorListActivity.class);
                        intent.putExtra("whereKey", "Drawer");
                        startActivity(intent);
                        mainFragment = false;

                    } else if (((BaseItem) object).getName().equals("Privacy Policy")) {
                        S.I(ClientMainActivity.this, PrivacyPolicyActivity.class, null);
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (((BaseItem) object).getName().equals("Terms & Conditions")) {
                        S.I(ClientMainActivity.this, TermAndConditionActivity.class, null);
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (((BaseItem) object).getName().equals("Hire Your Caregiver")) {
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.rlMapLayout, new ClientMainFragment()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        mainFragment = false;
                    } else if (((BaseItem) object).getName().equals("Change Password")) {
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.rlMapLayout, new ClientChangepassword()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        mainFragment = false;
                    } else if (((BaseItem) object).getName().equals("Refer & Earn")) {
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.rlMapLayout, new ReferAndEarnFragment()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        mainFragment = false;
                    } else if (((BaseItem) object).getName().equals("About")) {
                        S.I(ClientMainActivity.this, AboutUsActivity.class, null);
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (((BaseItem) object).getName().equals("Support")) {
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.rlMapLayout, new SupportClaimFragment()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        mainFragment = false;
                    }
                }
            });
            if (itemInfo.isExpandable()) {
                viewHolder.arrowView.setVisibility(View.VISIBLE);
                viewHolder.arrowView.setImageResource(itemInfo.isExpanded() ?
                        R.drawable.ic_expand_less : R.drawable.ic_expand_more);
            } else {
                viewHolder.arrowView.setVisibility(View.GONE);
            }

            viewHolder.levelBeamView.setLevel(itemInfo.getLevel());

            return convertView;
        }

        private class ViewHolder {
            TextView nameView;
            TextView infoView;
            ImageView arrowView, dataItemImage;
            LevelBeamView levelBeamView;
            LinearLayout mainLayout;
        }
    }

    private void Hiring_Cancel() {

        new JSONParser(ClientMainActivity.this).parseVollyStringRequest(Const.URL.CAREGIVER_CANCEL_REQUEST, 1, getParamsHiring_cnacel(), new Helper() {
            public void backResponse(String response) {
                try {
                    //careGiverSpecializationslistwithService.clear();
                    S.E("Hiring_Cancel" + response);
                    S.E("Hiring_CancelParam" + getParamsHiring_cnacel());
                    JSONObject object = new JSONObject(response);
                    if (object.getString("status").equals("200")) {
                        SavedData.saveAcceptLayoutKeep(false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                S.I_clear(ClientMainActivity.this, ClientMainActivity.class, null);
                                finish();
                            }
                        }, 2000);

                    }
//                    linearLayoutAmbulancenCareId
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParamsHiring_cnacel() {
        HashMap<String, String> param = new HashMap<>();
        param.put("token", SavedData.gettocken_id());
        param.put("reciever_id", Hire_CareGiver_id);
        param.put("reciever_type", "caregiver");
        S.E(" token..." + SavedData.gettocken_id());
        return param;
    }
}