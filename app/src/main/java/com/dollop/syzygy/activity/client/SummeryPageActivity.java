package com.dollop.syzygy.activity.client;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.syzygy.Model.CaregiverCurrentStatus;
import com.dollop.syzygy.Model.CurrentStatusResponse;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.utility.Constants;
import com.google.gson.Gson;
import com.payumoney.core.PayUmoneySdkInitializer;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class SummeryPageActivity extends BaseActivity {

    TextView caregiverName;
    TextView startTime;
    TextView endTime;
    TextView workingTime;
    TextView amount;
    TextView remainAmount;
    TextView bywalletAmount;
    Button finish;
    RatingBar ratingBar1;
    EditText describtion;
    ImageView ClientImage;
    int screenWidth = 500;
    JSONObject mainobject;
    JSONObject msg;
    String transaction_id = "";
    String FinalSource;
    String FinalDestination;
    String FinalAmount;
    String paymentMode;
    String caregiverType;
    String type;
    String Start_time;
    String source_location;
    String destination_location;
    String caregiver_id;
    String stop_time;
    String total_time;
    String hire_caregiver_id;
    String Amount;
    String total_kilometer;
    String PayType;
    int rupees;
    private TextView textWalletBalance;
    private String walletBalance="0";
    private double doubleWallet;
    private  double totalPrice;

    Button change_method;
    RelativeLayout payment_process;
    boolean is_start_thread = false;

    @Override
    protected int getContentResId() {
        return R.layout.activity_summery_page;
    }

    private double doubleAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleToolbar("Summary");

        ClientImage = (ImageView) findViewById(R.id.Client_image);
        amount = (TextView) findViewById(R.id.amount);
        workingTime = (TextView) findViewById(R.id.working_time);
        bywalletAmount = (TextView) findViewById(R.id.bywalletId);
        remainAmount = (TextView) findViewById(R.id.tvremainId);
        endTime = (TextView) findViewById(R.id.end_time);
        startTime = (TextView) findViewById(R.id.start_time);
        caregiverName = (TextView) findViewById(R.id.caregiver_name);
        textWalletBalance = (TextView) findViewById(R.id.textWalletBalance);
        payment_process = (RelativeLayout) findViewById(R.id.payment_process);
        finish = (Button) findViewById(R.id.finish);
        change_method = (Button) findViewById(R.id.change_method);
        /*finish = (Button) findViewById(R.id.finish);*/

        String message = SavedData.getMessageForSummery();
        S.E("message on summery -- " + message);
        try {
            mainobject = new JSONObject(message);
            msg = mainobject.getJSONObject("msg");
            S.E("mainobject.getString(\"full_name\")-- " + msg.getString("full_name"));
            caregiver_id = msg.getString("caregiver_id");
            endTime.setText(msg.getString("stop_time"));
            caregiverName.setText(msg.getString("full_name"));
            startTime.setText(msg.getString("start_time"));


            workingTime.setText(msg.getString("total_time")+" min");

            if ((String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(msg.getString("amount"))))) != null) {

                int total_time_spend = Integer.parseInt(msg.getString("total_time"));

                if(total_time_spend>60){
                    int extraTime = total_time_spend-60;
                    double amountInMinitus = Double.parseDouble(msg.getString("amount")) / 60;
                    double totalExtraAmount = amountInMinitus*extraTime;

                    amount.setText(new DecimalFormat("##.##").format(totalExtraAmount + Double.parseDouble(msg.getString("min_charges"))) + "");
                }else {
                    amount.setText(new DecimalFormat("##.##").format(Double.parseDouble(msg.getString("min_charges"))) + "");
                }

    /*            double amountInMinitus = Double.parseDouble(msg.getString("amount")) / 60;

                totalPrice = amountInMinitus * Double.parseDouble(msg.getString("total_time"));
                amount.setText(new DecimalFormat("##.##").format(totalPrice + Double.parseDouble(msg.getString("min_charges"))) + "");*/
            }

            Picasso.with(SummeryPageActivity.this).load(Const.URL.Image_Url + msg.getString("profile_pic")).error(R.drawable.user_profile_pic)
                    .into(ClientImage);
            type = msg.getString("type");
            if (type.equals("caregiver")) {
                Start_time = msg.getString("start_time");

                stop_time = msg.getString("stop_time");
                total_time = msg.getString("total_time");
            } else {
                source_location = msg.getString("source_location");
                destination_location = msg.getString("destination_location");
                total_kilometer = msg.getString("total_kilometer");
            }
            caregiver_id = msg.getString("caregiver_id");
            hire_caregiver_id = msg.getString("hire_caregiver_id");
            Amount = amount.getText().toString();
            rupees = Integer.parseInt(new DecimalFormat("##.##").format(Double.parseDouble(msg.getString("amount"))));
            finish.setText("Proceed to Pay");

        } catch (JSONException e) {
            e.printStackTrace();
            S.E("e.printStackTrace()-- " + e.toString());
        }

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountTotal = amount.getText().toString();
              /*  String walletBalance = walletBalance;
                double doubleAmount = Double.parseDouble(amountTotal);
                double doubleWallet = Double.parseDouble(walletBalance);*/
                if (remainAmount.getText().toString().equals("0")) {
                    caregiverType="1";
                    paymentMode="2";
                    PayType = "online";
                    payment_process.setVisibility(View.VISIBLE);
                //    PaymentOnServer();
                } else {
                  SelectPaymentMethode(remainAmount.getText().toString());
                }


            }
        });

        getWalletBalance();
        is_start_thread = true;
        GetClientCurrentStatus();

        change_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                payment_process.setVisibility(View.GONE);
            }
        });

    }

    private void getWalletBalance() {
        new JSONParser(SummeryPageActivity.this).parseVollyStringRequest(Const.URL.GET_WALLET_BALANCE, 1, getParamsForWallet(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("refer code : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {

                        textWalletBalance.setText(getString(R.string.rupyee_symbol) + " " + jsonObject.getString("wallet"));

                     walletBalance=   jsonObject.getString("wallet");

                        String amountTotal = amount.getText().toString();

                        doubleAmount = Double.parseDouble(amountTotal);
                         doubleWallet = Double.parseDouble( jsonObject.getString("wallet"));
                        if (doubleAmount >= doubleWallet) {
                            double doubleRemain = doubleAmount - doubleWallet;

                            remainAmount.setText("" + new DecimalFormat("##.##").format(doubleRemain));
                            bywalletAmount.setText(doubleWallet + "");
                            S.E("checkvalues if"+doubleAmount+":amount"+doubleWallet+":wallet"+walletBalance);
                            //SelectPaymentMethode(remainAmount.getText().toString());
                        //    upDateWallet(String.valueOf("0"));
                        } else {
                            double doubleRemain = doubleWallet - doubleAmount;
                            remainAmount.setText("0");
                            bywalletAmount.setText(doubleAmount + "");

                            caregiverType="1";
                            paymentMode="2";
                            PayType = "online";

                            //  SelectPaymentMethode(String.valueOf(doubleAmount));
                          //  upDateWallet(String.valueOf(doubleRemain));
                            paymentOnServerParam();
                            S.E("checkvalues if else"+doubleAmount+":amount"+doubleWallet+":wallet"+walletBalance);

                        }
                    } else if (jsonObject.getString("status").equals("1")) {
                        S.T(SummeryPageActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParamsForWallet() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());

        return params;
    }

    private void upDateWallet(String updateWalletBalance) {
        new JSONParser(SummeryPageActivity.this).parseVollyStringRequest(Const.URL.Update_Wallet, 1, getParamsForUpdate(updateWalletBalance), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("refer code :ipdate " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("success")) {


                    } else if (jsonObject.getString("status").equals("1")) {
                        S.T(SummeryPageActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParamsForUpdate(String updateWalletBalance) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("amount", updateWalletBalance);
     /*   update_wallet
                token*/

        return params;
    }


    private void SelectPaymentMethode(final String s) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.selectpaymentpop_uplayout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        Button cash = (Button) dialog.findViewById(R.id.cash_Btn);
        Button online = (Button) dialog.findViewById(R.id.online_Btn);
        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*RatingPopup();*/
                dialog.dismiss();
                transaction_id = "";
                S.E("if is runnning AmmbulanceActivity");
               /* PathKmHelper.getInstance().deleteAll();*/
                paymentMode = "1";
                PayType = "cash";

                    caregiverType = "1";

                payment_process.setVisibility(View.VISIBLE);
              //  PaymentOnServer();
            }
        });
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PayType = "online";
                paymentMode = "2";
                caregiverType="1";


                if (rupees > 0) {
                    makePayment();
                   /* Intent intent = new Intent(SummeryPageActivity.this, InitialScreenActivity.class);
                    intent.putExtra("amount",  s);
                    startActivityForResult(intent, 101);*/
                } else {
                    S.T(SummeryPageActivity.this, "Invalid Amount");
                }
            }
        });
        dialog.show();
    }

    private void PaymentOnServer() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.PAYMENT_TO_SERVER, 1, paymentOnServerParam(), new Helper() {
            @Override
            public void backResponse(String response) {
                Log.e("response", "getParams" + paymentOnServerParam());
                Log.e("Anitest", "response" + response);

                try {
                    JSONObject mainobject1 = new JSONObject(response);
                    int status = mainobject1.getInt("status");
                    String message = mainobject1.getString("message");

                    if (status == 200) {
                        if (doubleAmount >= doubleWallet) {
                            double doubleRemain = doubleAmount - doubleWallet;

                            remainAmount.setText("" + new DecimalFormat("##.##").format(doubleRemain));
                            bywalletAmount.setText(doubleWallet + "");
                            S.E("checkvalues if"+doubleAmount+":amount"+doubleWallet+":wallet"+walletBalance);
                            //SelectPaymentMethode(remainAmount.getText().toString());
                              upDateWallet(String.valueOf("0"));
                        } else {
                            double doubleRemain = doubleWallet - doubleAmount;
                            remainAmount.setText("0");
                            bywalletAmount.setText(doubleAmount + "");
                            //  SelectPaymentMethode(String.valueOf(doubleAmount));
                              upDateWallet(String.valueOf(doubleRemain));
                         /*   paymentOnServerParam();*/
                          //  S.E("checkvalues if else"+doubleAmount+":amount"+doubleWallet+":wallet"+walletBalance);

                        }
/*
                        Toast.makeText(SummeryPageActivity.this, "sucessfully", Toast.LENGTH_SHORT).show();
*/

                        Bundle bundle = new Bundle();
                        SavedData.saveSummerypage(false);

                        SavedData.saveMessageForsummery("");

                        bundle.putString("FullName", caregiverName.getText().toString());
                        bundle.putString("Type", msg.getString("type"));
                        bundle.putString("Picture", msg.getString("profile_pic"));
                        bundle.putString("Amount", amount.getText().toString());
                        bundle.putString("StartTime", msg.getString("start_time"));
                        bundle.putString("StopTime", msg.getString("stop_time"));
                        bundle.putString("TotalTime", msg.getString("total_time"));
                        bundle.putString("PaymentMode", PayType);
                        bundle.putString("caregiver_id", caregiver_id);
                        S.I_clear(SummeryPageActivity.this, Client_RatingActivity.class, bundle);
                        /*finish();*/
                    } else {
/*
                        Toast.makeText(SummeryPageActivity.this, "Rating Not Submited!Try Again", Toast.LENGTH_SHORT).show();
*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> paymentOnServerParam() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("token", SavedData.gettocken_id());
        postParams.put("caregiver_id", caregiver_id);
        postParams.put("amount", Amount);
        postParams.put("payment_mode", paymentMode);
        postParams.put("transaction_id", transaction_id);
        postParams.put("type", "1");
        postParams.put("hire_caregiver_id", hire_caregiver_id);

        if (type.equals("ambulance")) {
            postParams.put("source", source_location);
            postParams.put("destination", destination_location);

            postParams.put("start_tme", "");
            postParams.put("end_time", "");
            postParams.put("total_time", "");
            postParams.put("total_kilometer", total_kilometer);
        } else {
            postParams.put("source", "");
            postParams.put("destination", "");
            postParams.put("start_time", Start_time);
            postParams.put("end_time", stop_time);
            postParams.put("total_time", total_time);
            postParams.put("total_kilometer", "");
        }
        return postParams;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null)
        {
            TransactionResponse transactionResponse = data.getParcelableExtra( PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE );

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null)
            {

                if(transactionResponse.getTransactionStatus().equals( TransactionResponse.TransactionStatus.SUCCESSFUL ))
                {

                    UpdateCompletestatus();
                    caregiverType = "1";
                    PaymentOnServer();
                    //Success Transaction
                } else{
                    Toast.makeText(SummeryPageActivity.this,"Trannsaction failed, Please try again or pay cash.",Toast.LENGTH_SHORT).show();
                }


               /* String payuResponse = transactionResponse.getPayuResponse();
                String merchantResponse = transactionResponse.getTransactionDetails();*/

            } /* else if (resultModel != null && resultModel.getError() != null)
            {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else
                {
                Log.d(TAG, "Both objects are null!");
            }*/
        }
      /*  else if (resultCode == 101)
        {

            is_start_thread = false;
            UpdateCompletestatus();
            caregiverType = "1";
            PaymentOnServer();
        }*/
    }


    private void UpdateCompletestatus() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.UPDATE_CANCEL_REQUEST, 1, getCancel_parameter(), new Helper() {
            public void backResponse(String response) {
                if (response != null) {

                }
            }
        });
    }


    private Map<String, String> getCancel_parameter() {

        String hire_id = SavedData.getHireCareGiverId();
        HashMap<String, String> param = new HashMap<>();
        param.put("hire_caregiver_id", hire_id);
        param.put("status", "complete");
        S.E(" token..." + SavedData.gettocken_id());
        return param;
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
String payment_status = "";
String CaregiverId = "";
    CaregiverCurrentStatus caregiverCurrentStatus = null;

    private void GetClientCurrentStatus()
    {


        Map<String, String> prams = getParamForCurrentStatus();
        new JSONParser(this).parseVollyStringRequestWithoutLoad(Const.URL.GET_BOOKING_STATUS_USER, 1, prams, new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("Accept Request responce : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        Gson gson = new Gson();
                        CurrentStatusResponse assignlistModel = gson.fromJson(response, CurrentStatusResponse.class);
                        caregiverCurrentStatus = assignlistModel.getData().get(0);
                        if (caregiverCurrentStatus != null) {
                            payment_status = caregiverCurrentStatus.getBookingstatus();
                        }
                        CaregiverId = caregiverCurrentStatus.getCaregiverId();
                        if (payment_status.equalsIgnoreCase("complete"))
                        {

                            try {

                                caregiverType="1";
                                paymentMode="1";
                                PayType = "cash";
                                is_start_thread = false;
                                Bundle bundle = new Bundle();
                                SavedData.saveSummerypage(false);

                                SavedData.saveMessageForsummery("");

                                bundle.putString("FullName", caregiverName.getText().toString());
                                bundle.putString("Type", msg.getString("type"));
                                bundle.putString("Picture", msg.getString("profile_pic"));
                                bundle.putString("Amount", amount.getText().toString());
                                bundle.putString("StartTime", msg.getString("start_time"));
                                bundle.putString("StopTime", msg.getString("stop_time"));
                                bundle.putString("TotalTime", msg.getString("total_time"));
                                bundle.putString("PaymentMode", PayType);
                                bundle.putString("caregiver_id", caregiver_id);
                                S.I_clear(SummeryPageActivity.this, Client_RatingActivity.class, bundle);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    S.E("logprintStackTrace" + e);
                }

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(is_start_thread )
                            GetClientCurrentStatus();
                    }
                }, 3000);
            }
        });
    }



//PayuMoney Integration


    public static final String TAG = "PayUMoneySDK Sample";

    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


   /* private double getAmount() {


        Double amount = 10.0;

        if (isDouble(amt.getText().toString())) {
            amount = Double.parseDouble(amt.getText().toString());
            return amount;
        } else {
            Toast.makeText(getApplicationContext(), "Paying Default Amount ₹10", Toast.LENGTH_LONG).show();
            return amount;
        }
    }*/


    public void makePayment()
    {
        String phone = SavedData.getUserPhone();
        try
        {
            if (phone != null && !phone.equalsIgnoreCase("") && phone.length() > 10) {
                int start_index = phone.length() - 10;
                phone = phone.substring(start_index, phone.length());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        String productName = "syzygy";
        String firstName = SavedData.getUserName();
        transaction_id = "0nf7" + System.currentTimeMillis();
        String email = SavedData.getUserEmail();
        String sUrl = "https://payumoney.com/mobileapp/payumoney/success.php";
        String fUrl = "https://payumoney.com/mobileapp/payumoney/failure.php";
        String udf1 = "";
        String udf2 = "";
        String udf3 = "";
        String udf4 = "";
        String udf5 = "";

        String salt =  Constants.PRODCUTION_SLAT;
        String   key = Constants.PRODUCTION_MERCHANT_KEY;
        String   merchantId = Constants.PRODUCTION_MERCHANT_ID;
        PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
        builder.setAmount(Double.parseDouble(amount.getText().toString()))
       // builder.setAmount(Double.parseDouble("10"))
                .setTxnId(transaction_id)
                .setPhone(phone)
                .setProductName(productName)
                .setFirstName(firstName)
                .setEmail(email)
                .setsUrl(sUrl)
                .setfUrl(fUrl)
                .setUdf1(udf1)
                .setUdf2(udf2)
                .setUdf3(udf3)
                .setUdf4(udf4)
                .setUdf5(udf5)
                .setUdf6("")
                .setUdf7("")
                .setUdf8("")
                .setUdf9("")
                .setUdf10("")
                .setIsDebug(false)                              // Integration environment - true (Debug)/ false(Production)
                .setKey(key)                        // Merchant key
                .setMerchantId(merchantId);


     //   String hashSequence = key+"|"+transaction_id+"|"+Double.parseDouble("10")+"|"+productName+"|"+firstName+"|"+email+"|"+udf1+"|"+udf2+"|"+udf3+"|"+udf4+"|"+udf5+"|"+"|"+"|"+"|"+"|"+"|"+salt;
        String hashSequence = key+"|"+transaction_id+"|"+Double.parseDouble(amount.getText().toString())+"|"+productName+"|"+firstName+"|"+email+"|"+udf1+"|"+udf2+"|"+udf3+"|"+udf4+"|"+udf5+"|"+"|"+"|"+"|"+"|"+"|"+salt;
        String serverCalculatedHash= hashCal( hashSequence);

        try
        {
            PayUmoneySdkInitializer.PaymentParam paymentParam = builder.build();
            paymentParam.setMerchantHash(serverCalculatedHash);
            PayUmoneyFlowManager.startPayUMoneyFlow(
                    paymentParam,
                    SummeryPageActivity.this,
                    R.style.AppTheme_default,
                    true);

        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public static String hashCal( String hashString) {
        StringBuilder hash = new StringBuilder();
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
            messageDigest.update(hashString.getBytes());
            byte[] mdbytes = messageDigest.digest();
            for (byte hashByte : mdbytes) {
                hash.append(Integer.toString((hashByte & 0xff) + 0x100, 16).substring(1));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }


    private void showDialogMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(TAG);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        builder.show();

    }




}
