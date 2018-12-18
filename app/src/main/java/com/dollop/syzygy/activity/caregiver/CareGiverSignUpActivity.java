package com.dollop.syzygy.activity.caregiver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.activity.UserAccount;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sohel on 9/26/2017.
 */

public class CareGiverSignUpActivity extends BaseActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolBar;

    @BindView(R.id.editName)
    EditText editName;
    @BindView(R.id.editConfirmPassword)
    EditText editConfirmPassword;
    @BindView(R.id.editPassword)
    EditText editPassword;
    @BindView(R.id.editEmail)
    EditText editEmail;
    @BindView(R.id.btnSignUp)
    Button btnSignUp;

    String strEmail, strName, strConfirmPassword, strPassword, Contectnumber;
    private String devideID;


    @Override
    protected int getContentResId() {
        return R.layout.activity_signup;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolbarWithBackButton("Sign Up");
        Contectnumber = getIntent().getStringExtra("contact_no");
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strEmail = editEmail.getText().toString();
                strName = editName.getText().toString();
                strPassword = editPassword.getText().toString();
                strConfirmPassword = editConfirmPassword.getText().toString();
                if (UserAccount.isEmpty(editName, editEmail, editPassword, editConfirmPassword))
                {
                    if (UserAccount.isEmailValid(editEmail))
                    {
                        if (strPassword.equals(strConfirmPassword)) {
                            CaregiverRegistration();
                        } else {
                            S.T(CareGiverSignUpActivity.this, "Password Not Match");
                        }
                    } else {
                        UserAccount.EditTextPointer.setError("Email Invalid!");
                        UserAccount.EditTextPointer.requestFocus();
                    }
                } else {
                    UserAccount.EditTextPointer.setError("This can't be empty!");
                    UserAccount.EditTextPointer.requestFocus();
                }
            }
        });

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        devideID = telephonyManager.getDeviceId();

    }

    private void CaregiverRegistration() {

        new JSONParser(this).parseVollyStringRequest(Const.URL.Registration_Url, 1, getParam(), new Helper() {
            @Override
            public void backResponse(String response) {

                Log.e("response", "check" + response);
                try {
                    JSONObject ClientToken = null;
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {

                       // if(mainobject.has("data"))
                       //  ClientToken = mainobject.getJSONObject("data");
                      //  String clienttocken = ClientToken.getString("token");

                     //   String type = ClientToken.getString("user_type");
        /*                SavedData.saveTocken(clienttocken);
                        SavedData.saveTockenUserType("");

                        SavedData.saveUserPhone(Contectnumber);*/
                        Intent intent = new Intent(CareGiverSignUpActivity.this, CareGiverEnterMobileActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CareGiverSignUpActivity.this, "Sorry Registration not Submitted!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private Map<String, String> getParam() {
        S.E("fcm_id ==================    " + FirebaseInstanceId.getInstance().getToken());
        HashMap<String, String> postOarameters = new HashMap<>();
        postOarameters.put("authority_id", "2");//client for 1 ;
        postOarameters.put("full_name", editName.getText().toString());
        postOarameters.put("password", editConfirmPassword.getText().toString());
        postOarameters.put("email_id", editEmail.getText().toString());
        postOarameters.put("contact_no", Contectnumber);
        postOarameters.put("user_device_id", devideID);
        if (FirebaseInstanceId.getInstance().getToken() != null)
            postOarameters.put("fcm_id", FirebaseInstanceId.getInstance().getToken());
        Log.e("number", "number" + Contectnumber);
        Log.e("", "postOarameters" + postOarameters);
        return postOarameters;
    }

}