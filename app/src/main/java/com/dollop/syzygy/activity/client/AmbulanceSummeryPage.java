package com.dollop.syzygy.activity.client;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.syzygy.Model.CaregiverCurrentStatus;
import com.dollop.syzygy.Model.CurrentStatusResponse;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.activity.caregiver.CareGiverRatingActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AmbulanceSummeryPage extends BaseActivity {

    @BindView(R.id.ambulance_image)
    CircleImageView ambulanceImage;


    @BindView(R.id.ambulance_name)
    TextView ambulanceName;

    @BindView(R.id.back_button)
    ImageView back_button;

    @BindView(R.id.total_km)
    TextView totalKm;
    @BindView(R.id.km_per_charges)
    TextView kmPerCharges;
    @BindView(R.id.total_amount)
    TextView totalAmount;
    @BindView(R.id.byWalletlayoutID)
    LinearLayout byWalletlayoutID;
    @BindView(R.id.layoutRemainID)
    LinearLayout layoutRemainID;
    @BindView(R.id.walletId)
    LinearLayout walletId;


    @BindView(R.id.finish)
    Button finish;

    @BindView(R.id.change_method)
    Button change_method;
    @BindView(R.id.payment_process)
    RelativeLayout payment_process;

    JSONObject mainobject;
    String ActivityCheck;
    String CaregiverId;
    String client_id;
    String user_token;
    EditText describtion;
    RatingBar ratingBar1;
    JSONObject msg;
    String transaction_id = "";
    String FinalSource, FinalDestination, FinalAmount, paymentMode, caregiverType, type = "", Start_time, source_location, destination_location, caregiver_id, stop_time, total_time, hire_caregiver_id, total_kilometer;
    String Amount, PayType = "cash";
    int rupees;
    private String min_charges = "0.0";
    private TextView bywalletAmount;
    private TextView remainAmount;
    private TextView textWalletBalance;
    private String walletBalance;
    private double doubleAmount;
    private double doubleWallet;
    String payment_status = "";
    boolean is_start_thread = false;

    /*{"start_time":0,"amount":0,"full_name":"aniambulance","stop_time":"12:13","user_type":6,"user_id":"269","agency_id":null,"profile_pic":null,"caregiver_id":"84","total_time":"0.0","type":"ambulance"}*/
    @Override
    protected int getContentResId() {
        return R.layout.activity_ambulance_summery_page;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setToolbarWithBackButton("Summary");
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        bywalletAmount = (TextView) findViewById(R.id.bywalletId);
        remainAmount = (TextView) findViewById(R.id.tvremainId);
        textWalletBalance = (TextView) findViewById(R.id.textWalletBalance);
        ActivityCheck = bundle.getString("ActivityCheck");

        byWalletlayoutID.setVisibility(View.VISIBLE);
        layoutRemainID.setVisibility(View.VISIBLE);
        walletId.setVisibility(View.VISIBLE);




        if (ActivityCheck.equals("Client")) {
            String message = SavedData.getNotificationJson();
           // String message = SavedData.getCaregiverMessageForSummery();
            S.E("message on summery -- " + message);
            try {
                String kmPerChargestr = "0.0";
                mainobject = new JSONObject(message);

                msg = mainobject.getJSONObject("msg");
              //  S.E("mainobject.getString(\"full_name\")-- " + msg.getString("full_name"));
                CaregiverId = msg.getString("caregiver_id");
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
                //totalKm.setText(new DecimalFormat("##.##").format(Double.parseDouble(msg.getString("total_kilometer"))));
                totalKm.setText(msg.getString("total_kilometer"));
                ambulanceName.setText(msg.getString("full_name"));
                /* String s=new DecimalFormat("##.##").format(Double.parseDouble(msg.getString("amount")));*/
                if (msg.getString("total_kilometer").contains(",")) {
                    String val = msg.getString("total_kilometer").replace(",", "");
                    msg.put("total_kilometer", val);
                }
                kmPerCharges.setText(new DecimalFormat("##.##").format(Double.parseDouble(kmPerChargestr)));
                double totalkm = Double.parseDouble((msg.getString("total_kilometer")));
                double amount = Double.parseDouble(kmPerChargestr);
                Amount = String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(kmPerChargestr)));
                rupees = Integer.parseInt(new DecimalFormat("##.##").format(Double.parseDouble(kmPerChargestr)));
                double totalAmount1 = totalkm * amount;

                if (totalkm > 1) {
                    double remaningKm = totalkm - 1;
                    double extraAmount = remaningKm * (Double.parseDouble(kmPerChargestr));

                    totalAmount.setText("" + new DecimalFormat("##.##").format(extraAmount + Double.parseDouble(min_charges)));
                } else {
                    totalAmount.setText("" + new DecimalFormat("##.##").format(Double.parseDouble(min_charges)));
                }
                S.E("totalAmount1" + totalAmount1);

                type = msg.getString("type");
                if (type.equals("caregiver")) {
                    Start_time = msg.getString("start_time");
                    stop_time = msg.getString("stop_time");
                    total_time = msg.getString("total_time");
                } else {
                    source_location = msg.getString("source_location");
                    destination_location = msg.getString("destination_location");
                }
                caregiver_id = msg.getString("caregiver_id");
                hire_caregiver_id = msg.getString("hire_caregiver_id");
                total_kilometer = msg.getString("total_kilometer");
          /*  startTime.setText(msg.getString("start_time"));

            workingTime.setText(msg.getString("total_time"));*/

                finish.setText("Proceed to Pay");
                Picasso.with(AmbulanceSummeryPage.this).load(Const.URL.Image_Url + msg.getString("profile_pic")).error(R.drawable.user_profile_pic)
                        .into(ambulanceImage);
            } catch (Exception e) {
                e.printStackTrace();
                S.E("e.printStackTrace()-- " + e.toString());
            }
        } else if (ActivityCheck.equals("Ambulance")) {
            //String message = bundle.getString("message");
            String message = SavedData.getCaregiverMessageForSummery();


            S.E("message on summery --else if" + message);
            //  {"status":200,"message":"rides Stop","data":{"client_id":"91","user_id":"248","full_name":"Aniruddha client ","profile_pic":"55e89eb7f17a00bf4c270b81a66e8b59_1.png","source_latitude":"75.86413941","source_longitude":"75.86413941","destination_latitude":"22.6900344","destination_longitude":"75.8640888","total_kilometer":"0.3756685406652714","type":"ambulance","amount":0}}
            try {
              /*  byWalletlayoutID.setVisibility(View.GONE);
                layoutRemainID.setVisibility(View.GONE);
                textWalletBalance.setVisibility(View.GONE);
               */
                walletId.setVisibility(View.GONE);
                JSONObject jsonObject = new JSONObject(message);
                String kmpercages = "0.0";

                msg = jsonObject.getJSONObject("data");

                try {
                    String kmPerChargestr = "0.0";

                    CaregiverId = SavedData.getCareGiverId();
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
                    //totalKm.setText(new DecimalFormat("##.##").format(Double.parseDouble(msg.getString("total_kilometer"))));
                    totalKm.setText(msg.getString("total_kilometer"));
                    ambulanceName.setText(msg.getString("full_name"));
                    /* String s=new DecimalFormat("##.##").format(Double.parseDouble(msg.getString("amount")));*/
                    kmPerCharges.setText(new DecimalFormat("##.##").format(Double.parseDouble(kmPerChargestr)));
                    if (msg.getString("total_kilometer").contains(",")) {
                        String val = msg.getString("total_kilometer").replace(",", "");
                        msg.put("total_kilometer", val);
                    }

                    double totalkm = Double.parseDouble((msg.getString("total_kilometer")));
                    double amount = Double.parseDouble(kmPerChargestr);
                    Amount = String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(kmPerChargestr)));
                    rupees = Integer.parseInt(new DecimalFormat("##.##").format(Double.parseDouble(kmPerChargestr)));
                    double totalAmount1 = totalkm * amount;


                    source_location = msg.getString("source_location");
                    destination_location = msg.getString("destination_location");

                    //   caregiver_id = msg.getString("caregiver_id");
                    // hire_caregiver_id = msg.getString("hire_caregiver_id");

                    hire_caregiver_id = SavedData.getHireCareGiverId();
                    total_kilometer = msg.getString("total_kilometer");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                client_id = msg.getString("client_id");

                if (msg.getString("min_charges").equals("null")) {
                    min_charges = "0.0";
                } else {

                    min_charges = msg.getString("min_charges");
                }
                type = msg.getString("type");
                //totalKm.setText((new DecimalFormat("##.##").format(Double.parseDouble(msg.getString("total_kilometer")))));
                totalKm.setText(msg.getString("total_kilometer"));
                ambulanceName.setText(msg.getString("full_name"));
                total_kilometer = msg.getString("total_kilometer");
                if (msg.getString("amount").equals("null")) {

                } else {
                    kmpercages = msg.getString("amount");

                }
                kmPerCharges.setText(kmpercages);
                double totalkm = Double.parseDouble((new DecimalFormat("##.##").format(Double.parseDouble(msg.getString("total_kilometer")))));
                double amount = Double.parseDouble((new DecimalFormat("##.##").format(Double.parseDouble(kmpercages))));
                double totalAmount1 = totalkm * amount;
                S.E("totalAmount1" + totalAmount1);

                if (totalkm > 1) {
                    double remaningKm = totalkm - 1;
                    double extraAmount = remaningKm * (Double.parseDouble(kmpercages));

                    totalAmount.setText("" + new DecimalFormat("##.##").format(extraAmount + Double.parseDouble(min_charges)));
                } else {
                    totalAmount.setText("" + new DecimalFormat("##.##").format(Double.parseDouble(min_charges)));
                }


                finish.setText("Hire Amount");
                Picasso.with(AmbulanceSummeryPage.this).load(Const.URL.Image_Url + msg.getString("profile_pic")).error(R.drawable.user_profile_pic)
                        .into(ambulanceImage);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        is_start_thread = true;
        if (ActivityCheck.equals("Client")) {
            GetClientCurrentStatus();
        } else {
            GetCurrentStatus();
        }


        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ActivityCheck.equals("Client"))
                {
                    /* RatingPopup();*/
                    if (remainAmount.getText().toString().equals("0"))
                    {
                      /*  caregiverType = "2";
                        PayType = "online";
                        paymentMode = "2";
                        payment_process.setVisibility(View.VISIBLE);*/
                        Payfromwalletpopup();

                    } else {
                        SelectPaymentMethode(remainAmount.getText().toString());

                    }
                } else if (ActivityCheck.equals("Ambulance")) {


                    if (remainAmount.getText().toString().equals("0")) {

                       /* transaction_id = "";
                        PayType = "online";
                        paymentMode = "2";

                        if (type.equals("ambulance")) {
                            caregiverType = "2";
                        } else {
                            caregiverType = "1";
                        }
                        PaymentOnServerForCaregiver();*/

                        Payfromwalletpopup();



                    } else {
                        SelectPaymentMethodeForCaregiver(remainAmount.getText().toString());
                    }


                }
            }
        });

        change_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                payment_process.setVisibility(View.GONE);
            }
        });

        if (ActivityCheck.equals("Client")) {
            getWalletBalance();
        } else {
            getClientToken();
            getUserWalletBalance();
        }
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


    private void Payfromwalletpopup()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.payment_from_wallet);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        Button cash = (Button) dialog.findViewById(R.id.contiune);
        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (ActivityCheck.equals("Client"))
                {

                        caregiverType = "2";
                        PayType = "online";
                        paymentMode = "2";
                        payment_process.setVisibility(View.VISIBLE);


                } else if (ActivityCheck.equals("Ambulance")) {


                        PayType = "online";
                        paymentMode = "2";

                    transaction_id = ""  +System.currentTimeMillis();
                        if (type.equals("ambulance")) {
                            caregiverType = "2";
                        } else {
                            caregiverType = "1";
                        }
                        PaymentOnServerForCaregiver();



                }


                //   PaymentOnServer();
            }
        });

        dialog.show();
    }




    private void SelectPaymentMethode(final String amount)
    {
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
                dialog.dismiss();
                paymentMode = "1";
                transaction_id = "";
                PayType = "cash";
                if (type.equals("ambulance")) {
                    caregiverType = "2";
                } else {
                    caregiverType = "1";
                }
                payment_process.setVisibility(View.VISIBLE);


                //   PaymentOnServer();
            }
        });
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                PayType = "online";
                paymentMode = "2";
                if (rupees > 0) {
                    /*Intent intent = new Intent(AmbulanceSummeryPage.this, InitialScreenActivity.class);
                    intent.putExtra("amount", amount);
                    startActivityForResult(intent, 101);*/
                    makePayment();
                } else {
                    S.T(AmbulanceSummeryPage.this, "Invalid Amount");
                }
            }
        });

        dialog.show();
    }

    private void getWalletBalance() {
        new JSONParser(AmbulanceSummeryPage.this).parseVollyStringRequest(Const.URL.GET_WALLET_BALANCE, 1, getParamsForWallet(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("refer code : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {

                        textWalletBalance.setText(getString(R.string.rupyee_symbol) + " " + jsonObject.getString("wallet"));

                        walletBalance = jsonObject.getString("wallet");

                        String amountTotal = totalAmount.getText().toString();

                        doubleAmount = Double.parseDouble(amountTotal);
                        doubleWallet = Double.parseDouble(jsonObject.getString("wallet"));
                        if (doubleAmount >= doubleWallet) {
                            double doubleRemain = doubleAmount - doubleWallet;

                            //totalAmount.setText();
                            remainAmount.setText("" + new DecimalFormat("##.##").format(doubleRemain));
                            bywalletAmount.setText(doubleWallet + "");
                            S.E("checkvalues if" + doubleAmount + ":amount" + doubleWallet + ":wallet" + walletBalance);
                            //SelectPaymentMethode(remainAmount.getText().toString());
                            // upDateWallet(String.valueOf("0"));
                        } else {
                            double doubleRemain = doubleWallet - doubleAmount;
                            remainAmount.setText("0");
                            bywalletAmount.setText(doubleAmount + "");
                            caregiverType = "2";
                            PayType = "online";
                            paymentMode = "2";
                            //  SelectPaymentMethode(String.valueOf(doubleAmount));
                            //   upDateWallet(String.valueOf(doubleRemain));
                            //    paymentOnServerParam();
                            S.E("checkvalues if else" + doubleAmount + ":amount" + doubleWallet + ":wallet" + walletBalance);

                        }
                    } else if (jsonObject.getString("status").equals("1")) {
                        S.T(AmbulanceSummeryPage.this, jsonObject.getString("message"));
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


    private void getUserWalletBalance() {
        new JSONParser(AmbulanceSummeryPage.this).parseVollyStringRequest(Const.URL.GET_USER_WALLET_BALANCE, 1, getParamsForUserWallet(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("refer code : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {

                        textWalletBalance.setText(getString(R.string.rupyee_symbol) + " " + jsonObject.getString("wallet"));

                        walletBalance = jsonObject.getString("wallet");

                        String amountTotal = totalAmount.getText().toString();

                        doubleAmount = Double.parseDouble(amountTotal);
                        doubleWallet = Double.parseDouble(jsonObject.getString("wallet"));
                        if (doubleAmount >= doubleWallet) {
                            double doubleRemain = doubleAmount - doubleWallet;

                            remainAmount.setText("" + new DecimalFormat("##.##").format(doubleRemain));
                            bywalletAmount.setText(doubleWallet + "");
                            S.E("checkvalues if" + doubleAmount + ":amount" + doubleWallet + ":wallet" + walletBalance);
                            //SelectPaymentMethode(remainAmount.getText().toString());
                            // upDateWallet(String.valueOf("0"));
                        } else {
                            double doubleRemain = doubleWallet - doubleAmount;
                            remainAmount.setText("0");
                            bywalletAmount.setText(doubleAmount + "");
                            //  SelectPaymentMethode(String.valueOf(doubleAmount));
                            //   upDateWallet(String.valueOf(doubleRemain));
                            //    paymentOnServerParam();
                            S.E("checkvalues if else" + doubleAmount + ":amount" + doubleWallet + ":wallet" + walletBalance);

                        }
                    } else if (jsonObject.getString("status").equals("1")) {
                        S.T(AmbulanceSummeryPage.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParamsForUserWallet() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", client_id);

        return params;
    }


    private void upDateWallet(String updateWalletBalance) {
        new JSONParser(AmbulanceSummeryPage.this).parseVollyStringRequest(Const.URL.Update_Wallet, 1, getParamsForUpdate(updateWalletBalance), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("refer code :ipdate " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("success")) {


                    } else if (jsonObject.getString("status").equals("1")) {
                        S.T(AmbulanceSummeryPage.this, jsonObject.getString("message"));
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


    private void upDateUserWallet(String updateWalletBalance) {
        new JSONParser(AmbulanceSummeryPage.this).parseVollyStringRequest(Const.URL.Update_User_Wallet, 1, getParamsForUpdateUserwallet(updateWalletBalance), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("refer code :ipdate " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("success")) {


                    } else if (jsonObject.getString("status").equals("1")) {
                        S.T(AmbulanceSummeryPage.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParamsForUpdateUserwallet(String updateWalletBalance) {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", client_id);
        params.put("amount", updateWalletBalance);
     /*   update_wallet
                token*/

        return params;
    }


    private void PaymentOnServer() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.PAYMENT_TO_SERVER, 1, paymentOnServerParamForClient(), new Helper() {
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
                            S.E("checkvalues if" + doubleAmount + ":amount" + doubleWallet + ":wallet" + walletBalance);
                            //SelectPaymentMethode(remainAmount.getText().toString());
                            upDateWallet(String.valueOf("0"));
                        } else {
                            double doubleRemain = doubleWallet - doubleAmount;
                            remainAmount.setText("0");
                            bywalletAmount.setText(doubleAmount + "");
                            //  SelectPaymentMethode(String.valueOf(doubleAmount));
                            upDateWallet(String.valueOf(doubleRemain));
                            //  paymentOnServerParam();
                            S.E("checkvalues if else" + doubleAmount + ":amount" + doubleWallet + ":wallet" + walletBalance);

                        }
                        is_start_thread = false;
                        Bundle bundle = new Bundle();
                        bundle.putString("FullName", msg.getString("full_name"));
                        bundle.putString("Type", msg.getString("type"));
                        bundle.putString("Picture", msg.getString("profile_pic"));
                        bundle.putString("Amount", totalAmount.getText().toString());
                        bundle.putString("StartTime", msg.getString("source_location"));
                        bundle.putString("StopTime", msg.getString("destination_location"));
                        bundle.putString("TotalTime", "");
                        bundle.putString("PaymentMode", PayType);
                        bundle.putString("caregiver_id", caregiver_id);
                        SavedData.saveAmbulance(false);
                        SavedData.savePayType(PayType);
                        SavedData.saveRatingStatus(true);
                        // SavedData.saveMessageForsummery("");
                        S.I_clear(AmbulanceSummeryPage.this, Client_RatingActivity.class, bundle);

                    } else {
/*
                        Toast.makeText(AmbulanceSummeryPage.this, "Rating Not Submited!Try Again", Toast.LENGTH_SHORT).show();
*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private Map<String, String> paymentOnServerParamForClient() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("token", SavedData.gettocken_id());
        postParams.put("caregiver_id", CaregiverId);
        postParams.put("amount", totalAmount.getText().toString());
        postParams.put("payment_mode", paymentMode);
        postParams.put("transaction_id", transaction_id);
        postParams.put("type", caregiverType);
        postParams.put("hire_caregiver_id", hire_caregiver_id);
        if (type.equals("ambulance")) {
            postParams.put("source", source_location);
            postParams.put("destination", destination_location);

            postParams.put("start_time", "");
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


    private Map<String, String> paymentOnServerParam() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("token", user_token);
        if (CaregiverId == null || CaregiverId.equalsIgnoreCase(""))
            postParams.put("caregiver_id", "27");
        else
            postParams.put("caregiver_id", CaregiverId);
        postParams.put("amount", totalAmount.getText().toString());
        postParams.put("payment_mode", paymentMode);
        postParams.put("transaction_id", transaction_id);
        postParams.put("type", caregiverType);
        postParams.put("hire_caregiver_id", hire_caregiver_id);
        if (type.equals("ambulance")) {
            postParams.put("source", source_location);
            postParams.put("destination", destination_location);

            postParams.put("start_time", "");
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
     /*   if (resultCode == 101) {
            PaymentOnServer();
        }*/


        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {

                    UpdateCompletestatus();
                    caregiverType = "2";
                    PaymentOnServer();
                    //Success Transaction
                } else {
                    Toast.makeText(AmbulanceSummeryPage.this, "Trannsaction failed, Please try again or pay cash.", Toast.LENGTH_SHORT).show();
                }



           /*     String payuResponse = transactionResponse.getPayuResponse();
                String merchantResponse = transactionResponse.getTransactionDetails();*/

            } /* else if (resultModel != null && resultModel.getError() != null)
            {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else
                {
                Log.d(TAG, "Both objects are null!");
            }*/
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finishAffinity();
        }
        return super.onKeyDown(keyCode, event);
    }


    private void SelectPaymentMethodeForCaregiver(final String amount) {
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
                dialog.dismiss();
                /*RatingPopup();*/
                S.E("if is runnning AmmbulanceActivity");
                /* PathKmHelper.getInstance().deleteAll();*/
                paymentMode = "1";


                transaction_id = ""+ System.currentTimeMillis();

                PayType = "cash";
                if (type.equals("ambulance")) {
                    caregiverType = "2";
                } else {
                    caregiverType = "1";
                }

                PaymentOnServerForCaregiver();

                UpdateCompletestatus();
                is_start_thread = false;



            }
        });
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                paymentMode = "2";
                PayType = "online";
                payment_process.setVisibility(View.VISIBLE);


                /*
                PayType = "online";
                paymentMode = "2";
                if (rupees > 0) {
                    Intent intent = new Intent(AmbulanceSummeryPage.this, InitialScreenActivity.class);
                    intent.putExtra("amount", amount);
                    startActivityForResult(intent, 101);
                } else {
                    S.T(AmbulanceSummeryPage.this, "Invalid Amount");
                }*/
            }
        });

        dialog.show();
    }


    private void PaymentOnServerForCaregiver() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.PAYMENT_TO_SERVER, 1, paymentOnServerParam(), new Helper() {
            @Override
            public void backResponse(String response) {
                Log.e("response", "getParams" + paymentOnServerParam());
                Log.e("Anitest", "response" + response);

                try {
                    JSONObject mainobject1 = new JSONObject(response);
                    int status = mainobject1.getInt("status");
                    String message = mainobject1.getString("message");

                    if (status == 200)
                    {
                        if (doubleAmount >= doubleWallet) {
                            double doubleRemain = doubleAmount - doubleWallet;

                            remainAmount.setText("" + new DecimalFormat("##.##").format(doubleRemain));
                            bywalletAmount.setText(doubleWallet + "");
                            S.E("checkvalues if" + doubleAmount + ":amount" + doubleWallet + ":wallet" + walletBalance);
                            //SelectPaymentMethode(remainAmount.getText().toString());
                            upDateUserWallet(String.valueOf("0"));
                        } else {
                            double doubleRemain = doubleWallet - doubleAmount;
                            remainAmount.setText("0");
                            bywalletAmount.setText(doubleAmount + "");
                            //  SelectPaymentMethode(String.valueOf(doubleAmount));
                            upDateUserWallet(String.valueOf(doubleRemain));
                           // paymentOnServerParam();
                            S.E("checkvalues if else" + doubleAmount + ":amount" + doubleWallet + ":wallet" + walletBalance);

                        }
                        Bundle bundle = new Bundle();
                        try {
                            bundle.putString("FullName", msg.getString("full_name"));
                            bundle.putString("Type", "ambulance");
                            bundle.putString("Picture", msg.getString("profile_pic"));
                            bundle.putString("Amount", totalAmount.getText().toString());
                            bundle.putString("StartTime", msg.getString("source_location"));
                            bundle.putString("StopTime", msg.getString("destination_location"));
                            bundle.putString("TotalTime", "");
                            bundle.putString("PaymentMode", PayType);
                            bundle.putString("client_id", client_id);
                            SavedData.saveAmbulance(false);
                            SavedData.saveMessageForsummery("");
                            SavedData.savePaymentStatus(false);
                            SavedData.saveRatingStatus(true);


                            S.I_clear(AmbulanceSummeryPage.this, CareGiverRatingActivity.class, bundle);

                        } catch (JSONException e) {
                            S.E("check exception" + e);
                        }

                    } else {
/*
                        Toast.makeText(AmbulanceSummeryPage.this, "Rating Not Submited!Try Again", Toast.LENGTH_SHORT).show();
*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

    CaregiverCurrentStatus caregiverCurrentStatus = null;

    private void GetCurrentStatus() {


        Map<String, String> prams = getParamForCurrentStatus();
        new JSONParser(this).parseVollyStringRequestWithoutLoad(Const.URL.GET_BOOKING_STATUS_CAREGIVER, 1, prams, new Helper() {
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
                        hire_caregiver_id = caregiverCurrentStatus.getHireCaregiverId();
                        SavedData.saveHireCareGiverId(caregiverCurrentStatus.getHireCaregiverId());
                        if (payment_status.equalsIgnoreCase("complete")) {
                            try {

                                PayType = "online";


                                paymentMode = "2";

                                is_start_thread = false;
                                Bundle bundle = new Bundle();
                                bundle.putString("FullName", msg.getString("full_name"));
                                bundle.putString("Type", "ambulance");
                                bundle.putString("Picture", msg.getString("profile_pic"));
                                bundle.putString("Amount", totalAmount.getText().toString());
                                bundle.putString("StartTime", msg.getString("source_location"));
                                bundle.putString("StopTime", msg.getString("destination_location"));
                                bundle.putString("TotalTime", "");
                                bundle.putString("PaymentMode", PayType);
                                bundle.putString("client_id", client_id);
                                SavedData.saveAmbulance(false);
                                SavedData.saveMessageForsummery("");
                                SavedData.savePaymentStatus(false);
                                SavedData.saveRatingStatus(true);
                                S.I_clear(AmbulanceSummeryPage.this, CareGiverRatingActivity.class, bundle);

                            } catch (JSONException e) {
                                S.E("check exception" + e);
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
                        if (is_start_thread)
                            GetCurrentStatus();
                    }
                }, 3000);
            }
        });
    }


    private void GetClientCurrentStatus() {


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
                        hire_caregiver_id = caregiverCurrentStatus.getHireCaregiverId();
                        SavedData.saveHireCareGiverId(caregiverCurrentStatus.getHireCaregiverId());


                        if (payment_status.equalsIgnoreCase("complete")) {

                            try {
                                caregiverType = "2";
                                if (remainAmount.getText().toString().equals("0"))
                                PayType = "online";
                                else
                                    PayType = "cash";

                                paymentMode = "1";

                                is_start_thread = false;
                                Bundle bundle = new Bundle();
                                bundle.putString("FullName", msg.getString("full_name"));
                                bundle.putString("Type", msg.getString("type"));
                                bundle.putString("Picture", msg.getString("profile_pic"));
                                bundle.putString("Amount", totalAmount.getText().toString());
                                bundle.putString("StartTime", msg.getString("source_location"));
                                bundle.putString("StopTime", msg.getString("destination_location"));
                                bundle.putString("TotalTime", "");
                                bundle.putString("PaymentMode", PayType);
                                bundle.putString("caregiver_id", caregiver_id);
                                SavedData.saveAmbulance(false);
                                SavedData.savePayType(PayType);
                                SavedData.saveRatingStatus(true);
                                // SavedData.saveMessageForsummery("");
                                S.I_clear(AmbulanceSummeryPage.this, Client_RatingActivity.class, bundle);
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
                        if (is_start_thread)
                            GetClientCurrentStatus();
                    }
                }, 3000);
            }
        });
    }


    private void getClientToken() {
        new JSONParser(AmbulanceSummeryPage.this).parseVollyStringRequest(Const.URL.GET_USER_TOKEN, 1, getParamsForClient_token(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("refer code : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");


                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                        user_token = jsonObject1.getString("token");

                    } else if (jsonObject.getString("status").equals("1")) {
                        S.T(AmbulanceSummeryPage.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParamsForClient_token() {
        HashMap<String, String> params = new HashMap<>();
        params.put("client_id", client_id);

        return params;
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


    public void makePayment() {


        try {

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
            transaction_id = ""+System.currentTimeMillis();
            String email = SavedData.getUserEmail();
            String sUrl = "https://payumoney.com/mobileapp/payumoney/success.php";
            String fUrl = "https://payumoney.com/mobileapp/payumoney/failure.php";
            String udf1 = "";
            String udf2 = "";
            String udf3 = "";
            String udf4 = "";
            String udf5 = "";

            String salt = Constants.PRODCUTION_SLAT;
            String key = Constants.PRODUCTION_MERCHANT_KEY;
            String merchantId = Constants.PRODUCTION_MERCHANT_ID;
            PayUmoneySdkInitializer.PaymentParam.Builder builder = new PayUmoneySdkInitializer.PaymentParam.Builder();
              builder.setAmount(Double.parseDouble(remainAmount.getText().toString()))
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
             String hashSequence = key+"|"+transaction_id+"|"+Double.parseDouble(totalAmount.getText().toString())+"|"+productName+"|"+firstName+"|"+email+"|"+udf1+"|"+udf2+"|"+udf3+"|"+udf4+"|"+udf5+"|"+"|"+"|"+"|"+"|"+"|"+salt;
           // String hashSequence = key + "|" + transaction_id + "|" + Double.parseDouble("10") + "|" + productName + "|" + firstName + "|" + email + "|" + udf1 + "|" + udf2 + "|" + udf3 + "|" + udf4 + "|" + udf5 + "|" + "|" + "|" + "|" + "|" + "|" + salt;
            String serverCalculatedHash = hashCal(hashSequence);
            try {
                PayUmoneySdkInitializer.PaymentParam paymentParam = builder.build();
                paymentParam.setMerchantHash(serverCalculatedHash);
                PayUmoneyFlowManager.startPayUMoneyFlow(
                        paymentParam,
                        AmbulanceSummeryPage.this,
                        R.style.AppTheme_default,
                        true);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public static String hashCal(String hashString) {
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

    /*public static String hashCal(String str) {
        byte[] hashseq = str.getBytes();
        StringBuilder hexString = new StringBuilder();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("SHA-512");
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException ignored) {
        }
        return hexString.toString();
    }*/


}
