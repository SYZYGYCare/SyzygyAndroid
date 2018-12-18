package com.dollop.syzygy.activity.caregiver;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.AboutUsActivity;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.activity.PrivacyPolicyActivity;
import com.dollop.syzygy.activity.TermAndConditionActivity;
import com.dollop.syzygy.activity.WelcomeActivity;
import com.dollop.syzygy.activity.client.ClientMainActivity;
import com.dollop.syzygy.data.BaseItem;
import com.dollop.syzygy.data.CustomDataProvider;
import com.dollop.syzygy.fragment.caregiver.AddAmbulance;
import com.dollop.syzygy.fragment.caregiver.AddBankDetailFragment;
import com.dollop.syzygy.fragment.caregiver.AddCareGiver;
import com.dollop.syzygy.fragment.caregiver.CareGiverMainFragment;
import com.dollop.syzygy.fragment.caregiver.CareGiverYourHiresFragment;
import com.dollop.syzygy.fragment.caregiver.ChangePassword;
import com.dollop.syzygy.fragment.caregiver.TrustBadges;
import com.dollop.syzygy.servics.LocationService;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.views.LevelBeamView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.openrnd.multilevellistview.ItemInfo;
import pl.openrnd.multilevellistview.MultiLevelListAdapter;
import pl.openrnd.multilevellistview.MultiLevelListView;
import pl.openrnd.multilevellistview.OnItemClickListener;

public class CareGiverMainActivity extends BaseActivity {

    LinearLayout header_layout, login_header, notlogin_header;
    DrawerLayout drawer;
    ImageView caregiverUserImage;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Dialog demoDialog;
    boolean popup;
    int currentFragment = 0;
    TextView caregiverUserName;
    int screenWidth = 500;
    String phoneNumber = "0123456789";
    String reason, fromdate, todate;
    String disable_registration = "";
    private MultiLevelListView multiLevelListView;

    @Override
    protected int getContentResId() {
        return R.layout.activity_care_giver_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton(getString(R.string.app_name));

        currentFragment = 13;
        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.act_main_content, new CareGiverMainFragment()).commit();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        View headerview = navigationView.getHeaderView(0);
        caregiverUserImage = (ImageView) findViewById(R.id.caregiverUserImage);
        caregiverUserName = (TextView) findViewById(R.id.caregiverUserName);
        RelativeLayout caregiverHeaderView = (RelativeLayout) findViewById(R.id.caregiverHeaderView);

        caregiverHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.I(CareGiverMainActivity.this, CareGiverUpdateProfile.class, null);
            }
        });
        confMenu();


        if(!isMyServiceRunning(LocationService.class))
        {
            startService(new Intent(CareGiverMainActivity.this, LocationService.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getClientProfile();
    }

    private void getClientProfile() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.GET_PROFILE, 1, getParams(), new Helper() {

            @Override
            public void backResponse(String response) {
                S.E("printStackTrace" + response);

                try {
                    JSONObject mainobject = new JSONObject(response);

                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        JSONObject jsonObject1 = mainobject.getJSONObject("data");
                        Picasso.with(CareGiverMainActivity.this).load(Const.URL.Image_Url + jsonObject1.getString("profile_pic")).error(R.drawable.user_profile_pic)
                                .into(caregiverUserImage);
                        caregiverUserName.setText(jsonObject1.getString("full_name"));

                        String ac_holder_namestr = jsonObject1.getString("ac_holder_name");

                        String bank_account_no = jsonObject1.getString("bank_account_no");
                        String bank_ifsc_code = jsonObject1.getString("bank_ifsc_code");
                        if(jsonObject1.has("disable_registration"))
                        disable_registration = jsonObject1.getString("disable_registration");


                        if (bank_account_no.equals("null") || bank_ifsc_code.equals("null") || ac_holder_namestr.equals("null")) {


                        } else {

                            SavedData.saveAccountHolderName(ac_holder_namestr);
                            SavedData.saveIFCCODE(bank_ifsc_code);
                            SavedData.saveAccountNo(bank_account_no);
                        }


//                        Toast.makeText(CareGiverMainActivity.this, "\"No HireGiver\"" + jsonObject1.getString("full_name"), Toast.LENGTH_SHORT).show();
                    } else {
//                        Toast.makeText(context, "\"No HireGiver\"", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("Check", "printStackTrace" + e);
                }
            }
        });
    }

    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("authority_id", "2");
        return params;
    }


    @Override
    public void onBackPressed() {
        drawer.closeDrawer(GravityCompat.START);
        if (currentFragment != 13) {
            finish();
        } else {
            System.exit(2);
           // super.onBackPressed();
           // finishAffinity();
        }
        super.onBackPressed();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } else {
            super.onBackPressed();
            finish();
        }
    }

    public void launchFragmentTitle(String fragemntName) {

        getSupportActionBar().setTitle(fragemntName);/*
        setToolbarWithBackButton(fragemntName);*/
    }

    @Override
    public void finish() {

        if (currentFragment != 13) {
            currentFragment = 13;
            mFragmentManager = getSupportFragmentManager();
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.act_main_content, new CareGiverMainFragment()).commit();
            return;

        } else {

        }
    }

    private void confMenu() {
        multiLevelListView = (MultiLevelListView) findViewById(R.id.multiLevelMenu);

        // custom ListAdapter
        ListAdapter listAdapter = new ListAdapter();

        multiLevelListView.setAdapter(listAdapter);
        multiLevelListView.setOnItemClickListener(mOnItemClickListener);

        listAdapter.setDataItems(CustomDataProvider.getInitialItems());
    }

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
                /*SavedData.getInstance();
                SavedData.saveTocken("Logout");
                finish();
                Intent intent = new Intent(CareGiverMainActivity.this, WelcomeActivity.class);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                currentFragment = 1;*/
                logoutDialog();
            } else if (((BaseItem) item).getName().equals("Care Giver")) {
                if (SavedData.getSaveType().equals("")) {
                    mFragmentManager = getSupportFragmentManager();
                    mFragmentTransaction = mFragmentManager.beginTransaction();
                    mFragmentTransaction.replace(R.id.act_main_content, new AddCareGiver()).commit();
                    drawer.closeDrawer(GravityCompat.START);
                    currentFragment = 2;
                } else {
                    S.E("checkthis" + SavedData.getSaveType());
                    if (SavedData.getSaveType().equals("1")) {
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.act_main_content, new AddCareGiver()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        currentFragment = 2;
                    } else {
                      //  S.T(CareGiverMainActivity.this, "Your already register in to Ambulance");

                    }
                }

            } else if (((BaseItem) item).getName().equals("Ambulance"))
            {
                if(disable_registration != null && disable_registration.equalsIgnoreCase("0"))
                {
                    if (SavedData.getSaveType().equals(""))
                    {
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.act_main_content, new AddAmbulance()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        currentFragment = 3;
                    } else {
                        S.E("checkthis" + SavedData.getSaveType());
                        if (SavedData.getSaveType().equals("2"))
                        {
                            mFragmentManager = getSupportFragmentManager();
                            mFragmentTransaction = mFragmentManager.beginTransaction();
                            mFragmentTransaction.replace(R.id.act_main_content, new AddAmbulance()).commit();
                            drawer.closeDrawer(GravityCompat.START);
                            currentFragment = 3;
                        }
                    }
                } else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CareGiverMainActivity.this);
                    builder.setTitle("Auth!");
                    builder.setMessage("Your account is disable to change profile, Please contact to Admin.");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();

                        }
                    });

                    builder.show();
                }

            } /*else if (((BaseItem) item).getName().equals("Emergency Contact")) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.act_main_content, new EmergencyContactNumber()).commit();
                drawer.closeDrawer(GravityCompat.START);
                currentFragment = 4;
            }*/ else if (((BaseItem) item).getName().equals("Change Password")) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.act_main_content, new ChangePassword()).commit();
                drawer.closeDrawer(GravityCompat.START);
                currentFragment = 5;
/*
                S.T(CareGiverMainActivity.this, "check");
*/
            } else if (((BaseItem) item).getName().equals("Trust Badges")) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.act_main_content, new TrustBadges()).commit();
                drawer.closeDrawer(GravityCompat.START);
                currentFragment = 7;
/*
                S.T(CareGiverMainActivity.this, "check");
*/
            } else if (((BaseItem) item).getName().equals("Home")) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.act_main_content, new CareGiverMainFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
/*
                S.T(CareGiverMainActivity.this, "check");
*/
                currentFragment = 6;
            } else if (((BaseItem) item).getName().equals("Your Hires")) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.act_main_content, new CareGiverYourHiresFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
/*
                S.T(CareGiverMainActivity.this, "check");
*/
                currentFragment = 6;
            } else if (((BaseItem) item).getName().equals("Account Detail")) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.act_main_content, new AddBankDetailFragment()).commit();
                drawer.closeDrawer(GravityCompat.START);
                currentFragment = 6;
            } else if (((BaseItem) item).getName().equals("About")) {
                S.I(CareGiverMainActivity.this, AboutUsActivity.class, null);
                drawer.closeDrawer(GravityCompat.START);
            } else if (((BaseItem) item).getName().equals("Privacy Policy")) {
                S.I(CareGiverMainActivity.this, PrivacyPolicyActivity.class, null);
                drawer.closeDrawer(GravityCompat.START);
            } else if (((BaseItem) item).getName().equals("Terms & Conditions")) {
                S.I(CareGiverMainActivity.this, TermAndConditionActivity.class, null);
                drawer.closeDrawer(GravityCompat.START);
            }
        }

        @Override
        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }
    };

    public class ListAdapter extends MultiLevelListAdapter {

        private class ViewHolder {
            TextView nameView;
            TextView infoView;
            ImageView arrowView, dataItemImage;
            LevelBeamView levelBeamView;
            LinearLayout mainLayout;
        }

        @Override
        public List<?> getSubObjects(Object object) {
            // DIEKSEKUSI SAAT KLIK PADA GROUP-ITEM
            return CustomDataProvider.getSubItems((BaseItem) object);
        }

        @Override
        public boolean isExpandable(Object object) {
            return CustomDataProvider.isExpandable((BaseItem) object);
        }

        @Override
        public View getViewForObject(final Object object, View convertView, final ItemInfo itemInfo) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(CareGiverMainActivity.this).inflate(R.layout.data_item, null);
                //viewHolder.infoView = (TextView) convertView.findViewById(R.id.dataItemInfo);
                viewHolder.nameView = (TextView) convertView.findViewById(R.id.dataItemName);
                viewHolder.arrowView = (ImageView) convertView.findViewById(R.id.dataItemArrow);
                viewHolder.levelBeamView = (LevelBeamView) convertView.findViewById(R.id.dataItemLevelBeam);
                viewHolder.dataItemImage = (ImageView) convertView.findViewById(R.id.dataItemImage);
                viewHolder.mainLayout = (LinearLayout) convertView.findViewById(R.id.mainLayout);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.dataItemImage.setImageResource(((BaseItem) object).getIcon());
            viewHolder.nameView.setText(((BaseItem) object).getName());

            viewHolder.nameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((BaseItem) object).getName().equals("Logout")) {
                        /*SavedData.getInstance();
                        SavedData.saveTocken("Logout");
                       *//* finish();
                        Intent intent = new Intent(CareGiverMainActivity.this, WelcomeActivity.class);
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));*//*
                        S.I(CareGiverMainActivity.this, WelcomeActivity.class, null);
                        currentFragment = 1;*/
                        logoutDialog();
                    } else if (((BaseItem) object).getName().equals("Care Giver"))
                    {
                        S.E("checkthis" + SavedData.getSaveType());

                        if(disable_registration != null && disable_registration.equalsIgnoreCase("0"))
                        {
                            if (SavedData.getSaveType().equals("")) {
                                mFragmentManager = getSupportFragmentManager();
                                mFragmentTransaction = mFragmentManager.beginTransaction();
                                mFragmentTransaction.replace(R.id.act_main_content, new AddCareGiver()).commit();
                                drawer.closeDrawer(GravityCompat.START);
                                currentFragment = 2;
                            } else {
                                S.E("checkthis" + SavedData.getSaveType());
                                if (SavedData.getSaveType().equals("1")) {
                                    mFragmentManager = getSupportFragmentManager();
                                    mFragmentTransaction = mFragmentManager.beginTransaction();
                                    mFragmentTransaction.replace(R.id.act_main_content, new AddCareGiver()).commit();
                                    drawer.closeDrawer(GravityCompat.START);
                                    currentFragment = 2;
                                } else {
                                    //  S.T(CareGiverMainActivity.this, "Your already register in to Ambulance");

                                }
                            }
                        } else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CareGiverMainActivity.this);
                            builder.setTitle("Auth!");
                            builder.setMessage("Your account is disable to change profile, Please contact to Admin.");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();

                                }
                            });

                            builder.show();
                        }

                    } else if (((BaseItem) object).getName().equals("Ambulance"))
                    {
                        S.E("checkthis" + SavedData.getSaveType());


                        if(disable_registration != null && disable_registration.equalsIgnoreCase("0"))
                            {
                            if (SavedData.getSaveType().equals("")) {
                                mFragmentManager = getSupportFragmentManager();
                                mFragmentTransaction = mFragmentManager.beginTransaction();
                                mFragmentTransaction.replace(R.id.act_main_content, new AddAmbulance()).commit();
                                drawer.closeDrawer(GravityCompat.START);
                                currentFragment = 3;
                            } else {
                                S.E("checkthis" + SavedData.getSaveType());
                                if (SavedData.getSaveType().equals("2")) {
                                    mFragmentManager = getSupportFragmentManager();
                                    mFragmentTransaction = mFragmentManager.beginTransaction();
                                    mFragmentTransaction.replace(R.id.act_main_content, new AddAmbulance()).commit();
                                    drawer.closeDrawer(GravityCompat.START);
                                    currentFragment = 3;
                                } else {
                                    //S.T(CareGiverMainActivity.this, "Your already register in to Caregiver");
                                }
                            }
                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(CareGiverMainActivity.this);
                            builder.setTitle("Auth!");
                            builder.setMessage("Your account is disable to change profile, Please contact to Admin.");
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();

                                }
                            });

                            builder.show();
                        }

                    } /*else if (((BaseItem) item).getName().equals("Emergency Contact")) {
                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.act_main_content, new EmergencyContactNumber()).commit();
                drawer.closeDrawer(GravityCompat.START);
                currentFragment = 4;
            }*/ else if (((BaseItem) object).getName().equals("Change Password")) {
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.act_main_content, new ChangePassword()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        currentFragment = 5;
/*
                        S.T(CareGiverMainActivity.this, "check");
*/
                    } else if (((BaseItem) object).getName().equals("Trust Badges")) {
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.act_main_content, new TrustBadges()).commit();
                        drawer.closeDrawer(GravityCompat.START);
                        currentFragment = 7;
/*
                        S.T(CareGiverMainActivity.this, "check");
*/
                    } else if (((BaseItem) object).getName().equals("Home")) {
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.act_main_content, new CareGiverMainFragment()).commit();
                        drawer.closeDrawer(GravityCompat.START);
/*
                        S.T(CareGiverMainActivity.this, "check");
*/
                        currentFragment = 6;
                    } else if (((BaseItem) object).getName().equals("Your Hires")) {
                        mFragmentManager = getSupportFragmentManager();
                        mFragmentTransaction = mFragmentManager.beginTransaction();
                        mFragmentTransaction.replace(R.id.act_main_content, new CareGiverYourHiresFragment()).commit();
                        drawer.closeDrawer(GravityCompat.START);
/*
                        S.T(CareGiverMainActivity.this, "check");
*/
                        currentFragment = 6;
                    } else if (((BaseItem) object).getName().equals("Register For")) {
                        S.E("register text click");
                    } else if (((BaseItem) object).getName().equals("About")) {
                        S.I(CareGiverMainActivity.this, AboutUsActivity.class, null);
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (((BaseItem) object).getName().equals("Privacy Policy")) {
                        S.I(CareGiverMainActivity.this, PrivacyPolicyActivity.class, null);
                        drawer.closeDrawer(GravityCompat.START);
                    } else if (((BaseItem) object).getName().equals("Terms & Conditions")) {
                        S.I(CareGiverMainActivity.this, TermAndConditionActivity.class, null);
                        drawer.closeDrawer(GravityCompat.START);
                    }
                }
            });

            //viewHolder.infoView.setText(getItemInfoDsc(itemInfo));
            viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
    }

    private void logoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CareGiverMainActivity.this);
        builder.setTitle("Confirmation!");
        builder.setMessage("Are you sure? You want to Logout.");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                getLogoutId();

                currentFragment = 1;
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


    private void getLogoutId() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.LogOut, 1, getParams5(), new Helper() {

            @Override
            public void backResponse(String response) {
                S.E(response);
                try {
                    JSONObject mainobject = new JSONObject(response);

                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        SavedData.getInstance();
                        SavedData.saveTocken("Logout");
                        S.T(CareGiverMainActivity.this, "Logout Successfully!");
                        S.I_clear(CareGiverMainActivity.this, WelcomeActivity.class, null);
                        SavedData.clear();
                    } else {
//                        Toast.makeText(context, "\"No HireGiver\"", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParams5() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("type", "1");

        return params;
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


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


}
