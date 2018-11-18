package com.dollop.syzygy.activity.client;

import android.Manifest;
import android.content.Context;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sohel on 9/26/2017.
 */

public class ClientSignUpActivity extends BaseActivity {
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
                        if (strPassword.length() >5 ||strPassword.equals(strConfirmPassword))
                        {
                            ClinetrRegistration();

                        } else {

                            Toast.makeText(ClientSignUpActivity.this,"Please enter at least 6 digits password.",Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        Toast.makeText(ClientSignUpActivity.this,"Please enter valid email.",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    UserAccount.EditTextPointer.setError("This can't be empty!");
                    UserAccount.EditTextPointer.requestFocus();
                }
            }
        });

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        devideID = telephonyManager.getDeviceId();

    }

    private void ClinetrRegistration() {

        new JSONParser(this).parseVollyStringRequest(Const.URL.Registration_Url, 1, getParam(), new Helper() {
            @Override
            public void backResponse(String response) {

                Log.e("response", "check" + response);
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {

                        JSONObject data = mainobject.getJSONObject("data");
                        String clienttocken = data.getString("token");

                        String type = data.getString("user_type");

                        SavedData.saveTocken(clienttocken);
                        SavedData.saveTockenUserType(type);
                        SavedData.saveUserPhone(Contectnumber);

                        SavedData.saveClientPhone("");

                        S.I_clear(ClientSignUpActivity.this, ClientMainActivity.class, null);
                        S.T(ClientSignUpActivity.this, "Success");
                    } else {
/*
                        Toast.makeText(ClientSignUpActivity.this, "Sorry Registration not Submitted!", Toast.LENGTH_SHORT).show();
*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        });

    }

    private Map<String, String> getParam() {
        HashMap<String, String> postOarameters = new HashMap<>();
        postOarameters.put("authority_id", "1");//client for 1 ;
        postOarameters.put("full_name", editName.getText().toString());
        postOarameters.put("password", editConfirmPassword.getText().toString());
        postOarameters.put("email_id", editEmail.getText().toString());
        postOarameters.put("contact_no", Contectnumber);
        postOarameters.put("user_device_id", devideID);


        Log.e("number", "number" + Contectnumber);
        Log.e("", "postOarameters" + postOarameters);
        return postOarameters;
    }

}
