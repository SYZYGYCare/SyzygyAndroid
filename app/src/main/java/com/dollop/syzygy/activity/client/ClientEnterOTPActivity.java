package com.dollop.syzygy.activity.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.servics.SmsListener;
import com.dollop.syzygy.servics.SmsReceiver;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.SavedData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sohel on 9/26/2017.
 */

public class ClientEnterOTPActivity extends BaseActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.signInBtnNext)
    FloatingActionButton signInBtnNext;
    String strNumber = "";
    String strOtp = "";

    @BindView(R.id.editOTP)
    EditText editOTP;

    @Override
    protected int getContentResId() {
        return R.layout.activity_enter_otp;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolbarWithBackButton("Verify User Number");

        strNumber = getIntent().getStringExtra("contact_no");
        signInBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*S.I(ClientEnterOTPActivity.this, ClientSignUpActivity.class, null);*/
                strOtp = editOTP.getText().toString();
                if (strOtp.equals("") || strOtp.length() <6) {
                    editOTP.setError("Please enter valid OTP.");

                } else {

                    getMatchOtp();
                }
            }
        });

        try {
            SmsReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {
                    String[] messageOtp = messageText.split(" ");
                    Log.d("+++++++++++", messageOtp[0]);

                    editOTP.setText(messageOtp[0]);

                }
            });

        } catch (Exception e) {
            Log.e("Exception", "Check" + e);

        }
    }


    private void getMatchOtp() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.MatchOtp, 1, getParam(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    Log.e("response", "response" + response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {
                        Intent intent = new Intent(ClientEnterOTPActivity.this, ClientSignUpActivity.class);
                        intent.putExtra("contact_no", strNumber);
                        SavedData.saveClientPhone(strNumber);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ClientEnterOTPActivity.this,"Please enter valid OTP.",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParam() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("contact_no", strNumber);
        postParams.put("otp", editOTP.getText().toString());

        return postParams;
    }
}