package com.dollop.syzygy.activity.client;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.activity.UserAccount;
import com.dollop.syzygy.servics.SmsListener;
import com.dollop.syzygy.servics.SmsReceiver;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ClientForgotPasswordActivity extends BaseActivity {
    EditText EtClientMobileNumberId, EtClientOtpId, EtClientNewPassId, EtClientConPassId;
    Button BtnChangeSubmitPassId;
    String token;

    @Override
    protected int getContentResId() {
        return R.layout.activity_change_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("Forgot Password");

        BtnChangeSubmitPassId = (Button) findViewById(R.id.BtnChangeSubmitPassId);
        EtClientMobileNumberId = (EditText) findViewById(R.id.EtClientMobileNumberId);
        EtClientMobileNumberId.setText(getIntent().getStringExtra("mobileno"));
        EtClientMobileNumberId.setEnabled(false);
        EtClientOtpId = (EditText) findViewById(R.id.EtClientOtpId);
        EtClientConPassId = (EditText) findViewById(R.id.EtClientConPassId);
        EtClientNewPassId = (EditText) findViewById(R.id.EtClientNewPassId);
        BtnChangeSubmitPassId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserAccount.isEmpty(EtClientMobileNumberId)) {
                    if (BtnChangeSubmitPassId.getText().equals("Get Otp")) {
                        getOtp();

                    } else if (BtnChangeSubmitPassId.getText().toString().equals("Save"))
                    {
                        if (UserAccount.isEmpty(EtClientNewPassId, EtClientConPassId))
                        {
                            if(EtClientNewPassId.length()>5)
                            {
                                if (EtClientNewPassId.getText().toString().equals(EtClientConPassId.getText().toString()))
                                {
                                    getChangePass();
                                } else {
                                Toast.makeText(ClientForgotPasswordActivity.this, "Password Missmatch.", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(ClientForgotPasswordActivity.this, "Please enter Minimum 6 digits password.", Toast.LENGTH_SHORT).show();

                            }

                        } else {
                            UserAccount.EditTextPointer.setError("Enter Password!");
                            UserAccount.EditTextPointer.requestFocus();
                        }

                    } else if (BtnChangeSubmitPassId.getText().equals("Submit")) {
                        if (UserAccount.isEmpty(EtClientOtpId)) {
                            getMatchOtp();

                            EtClientOtpId.setText(EtClientOtpId.getText().toString());


                        } else {
                            UserAccount.EditTextPointer.setError("Enter Otp!");
                            UserAccount.EditTextPointer.requestFocus();
                        }
                    } else {
                        UserAccount.EditTextPointer.setError("Fields Can't be Empty!");
                        UserAccount.EditTextPointer.requestFocus();

                    }


                }
            }
        });

        try {
            SmsReceiver.bindListener(new SmsListener() {
                @Override
                public void messageReceived(String messageText) {
                    String[] messageOtp = messageText.split(" ");
                    Log.d("+++++++++++", messageOtp[0]);

                    EtClientOtpId.setText(messageOtp[0]);

                }
            });
        } catch (Exception e) {

            Log.e("Exception", "check" + e);
        }
    }


    private void getChangePass() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.Client_ChangePasword_Url, 1, getParams1(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    Log.e("response", "response" + response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                 /*   String token = mainobject.getString("token");*/
                    if (status == 200) {
/*
                        Toast.makeText(ClientForgotPasswordActivity.this, " Change Password sucessfully", Toast.LENGTH_SHORT).show();
*/

                        S.I_clear(ClientForgotPasswordActivity.this, ClientEnterMobileActivity.class, null);

                    } else {
/*
                        Toast.makeText(ClientForgotPasswordActivity.this, "Password Not Match..", Toast.LENGTH_SHORT).show();
*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                EtClientMobileNumberId.setText("");
                EtClientOtpId.setText("");
                EtClientConPassId.setText("");
                EtClientNewPassId.setText("");
            }

        });
    }

    private Map<String, String> getParams1() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("contact_no", EtClientMobileNumberId.getText().toString());

        postParams.put("new_password", EtClientNewPassId.getText().toString());
        postParams.put("token", token);
        return postParams;
    }

    private void getMatchOtp() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.Client_MatchOtp, 1, getParam(), new Helper() {


            @Override
            public void backResponse(String response) {
                S.E("Checked By Ani Tocken" + response);

                try {
                    JSONObject mainobject = new JSONObject(response);
                    Log.e("response", "response" + response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    token = mainobject.getString("token");
                    if (status == 200) {
/*
                        Toast.makeText(ClientForgotPasswordActivity.this, "sucessfully", Toast.LENGTH_SHORT).show();
*/
                        EtClientOtpId.setVisibility(View.GONE);
                        BtnChangeSubmitPassId.setText("Save");
                        EtClientNewPassId.setVisibility(View.VISIBLE);
                        EtClientConPassId.setVisibility(View.VISIBLE);
                    } else {
/*
                        Toast.makeText(ClientForgotPasswordActivity.this, "Otp Not Match..", Toast.LENGTH_SHORT).show();
*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParam() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("contact_no", EtClientMobileNumberId.getText().toString());
        postParams.put("otp", EtClientOtpId.getText().toString());
        return postParams;
    }


    private void getOtp() {

        new JSONParser(this).parseVollyStringRequest(Const.URL.Client_ForgotPassword_Url, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {

                try {
                    JSONObject mainobject = new JSONObject(response);
                    Log.e("response", "response" + response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {

                        EtClientMobileNumberId.setText(EtClientMobileNumberId.getText().toString());
                        EtClientMobileNumberId.setEnabled(false);
                        EtClientOtpId.setVisibility(View.VISIBLE);

                        BtnChangeSubmitPassId.setText("Submit");

                    } else {
/*
                        Toast.makeText(ClientForgotPasswordActivity.this, "Not Genrated Otp", Toast.LENGTH_SHORT).show();
*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParams() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("contact_no", EtClientMobileNumberId.getText().toString());
        return postParams;
    }


}

