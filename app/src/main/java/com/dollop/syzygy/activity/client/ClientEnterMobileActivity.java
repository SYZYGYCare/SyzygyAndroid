package com.dollop.syzygy.activity.client;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.rilixtech.CountryCodePicker;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sohel on 9/26/2017.
 */

public class ClientEnterMobileActivity extends BaseActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.signInBtnNext)
    FloatingActionButton signInBtnNext;
    @BindView(R.id.editMobile)
    EditText editMobile;

    String strMobilleNo = "";
    private CountryCodePicker ccp;
    private String countryCode="";
    private String mobileNo="";

    @Override
    protected int getContentResId() {
        return R.layout.activity_enter_mobile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initTitleToolbar("Enter Mobile Number");
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        signInBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  S.I(ClientEnterMobileActivity.this, ClientEnterOTPActivity.class, null);*/
                strMobilleNo = editMobile.getText().toString();
                if (strMobilleNo.equals("") || strMobilleNo.length()<10)
                {
                    editMobile.setError("Please enter valid phone number.");
                } else {
                    countryCode=  ccp.getSelectedCountryCode().toString();
                    mobileNo=countryCode+editMobile.getText().toString();
                    GetOtp();
                }

            }
        });

        if (isSmsPermissionGranted()) {


        } else {

            requestReadAndSendSmsPermission();

        }
    }


    private void GetOtp() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.GetOtp_Url, 1, getParms(), new Helper() {
            @Override
            public void backResponse(String response) {
                /*
                gifTextView.setVisibility(View.GONE);*/
                S.E("getOtp" + getParms());
                S.E("getOtpResponse" + response);
                try {
                    JSONObject mainobject = new JSONObject(response);
                    Log.e("response", "" + response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");


                    if (message.equals("user already registered")) {
                       // SavedData.savePhone("");
                        SavedData.saveClientPhone("");
                        Intent intent = new Intent(ClientEnterMobileActivity.this, ClientEnterPasswordActivity.class);
                        intent.putExtra("contact_no", mobileNo);
                        startActivity(intent);

                    } else if (message.equals("failed")) {
                        S.T(ClientEnterMobileActivity.this, "Invalid Mobile No");

                    } else if (message.equals("success")) {

                        Intent intent = new Intent(ClientEnterMobileActivity.this, ClientEnterOTPActivity.class);
                        intent.putExtra("contact_no", mobileNo);

                        startActivity(intent);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParms() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("contact_no", mobileNo);
        SavedData.saveMOBILE(mobileNo);
        return postParams;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }


    public boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request runtime SMS permission
     */
    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_SMS)) {
            // You may display a non-blocking explanation here, read more in the documentation:
            // https://developer.android.com/training/permissions/requesting.html
        }
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_SMS}, 12);
    }
}