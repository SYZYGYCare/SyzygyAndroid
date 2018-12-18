package com.dollop.syzygy.activity.caregiver;

import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.syzygy.Model.CaregiverCurrentStatus;
import com.dollop.syzygy.Model.CurrentStatusResponse;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.activity.client.AmbulanceSummeryPage;
import com.dollop.syzygy.activity.client.Client_RatingActivity;
import com.dollop.syzygy.activity.client.SummeryPageActivity;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.sohel.UserAccount;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CareGiverSummeryPageActivity extends BaseActivity {

    TextView caregiverName;
    TextView startTime;
    TextView endTime;
    TextView workingTime;
    TextView amount;
    Button finish;


    Button change_method;
    RelativeLayout payment_process;
    boolean is_start_thread = false;

    RatingBar ratingBar1;
    EditText describtion;
    String caregiver_id = "";
    ImageView ClientImage;
    int screenWidth = 500;
    JSONObject mainobject;
    TextView textWalletBalance;

    LinearLayout byWalletlayoutID;

    LinearLayout layoutRemainID;

    LinearLayout walletId;
    JSONObject msg;
    private String client_id = "";


    private TextView bywalletAmount;
    private TextView remainAmount;

    private String walletBalance;
    private double doubleAmount;
    private double doubleWallet;


    @Override
    protected int getContentResId() {
        return R.layout.activity_summery_page;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleToolbar("Summary");

        ClientImage = (ImageView) findViewById(R.id.Client_image);
        amount = (TextView) findViewById(R.id.amount);
        workingTime = (TextView) findViewById(R.id.working_time);
        endTime = (TextView) findViewById(R.id.end_time);
        startTime = (TextView) findViewById(R.id.start_time);
        bywalletAmount = (TextView) findViewById(R.id.bywalletId);
        remainAmount = (TextView) findViewById(R.id.tvremainId);
        caregiverName = (TextView) findViewById(R.id.caregiver_name);
        textWalletBalance = (TextView) findViewById(R.id.textWalletBalance);
        byWalletlayoutID = (LinearLayout) findViewById(R.id.byWalletlayoutID);
        layoutRemainID = (LinearLayout) findViewById(R.id.layoutRemainID);
        walletId = (LinearLayout) findViewById(R.id.walletId);
        payment_process = (RelativeLayout) findViewById(R.id.payment_process);
        textWalletBalance.setVisibility(View.VISIBLE);

        byWalletlayoutID.setVisibility(View.VISIBLE);
        layoutRemainID.setVisibility(View.VISIBLE);
        walletId.setVisibility(View.GONE);

        finish = (Button) findViewById(R.id.finish);
        change_method = (Button) findViewById(R.id.change_method);
        finish.setText("Hire Amount");


        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        S.E("message on summery -- " + message);
        try {
            mainobject = new JSONObject(message);
            msg = mainobject.getJSONObject("data");
            S.E("mainobject.getString(\"full_name\")-- " + msg.getString("full_name"));
            client_id = msg.getString("client_id");
            endTime.setText(msg.getString("stop_time"));
            caregiverName.setText(msg.getString("full_name"));
            startTime.setText(msg.getString("start_time"));

            workingTime.setText(msg.getString("total_time") + " min");

            if (msg.getString("amount") != null) {

                int total_time_spend = Integer.parseInt(msg.getString("total_time"));

                if (total_time_spend > 60) {
                    int extraTime = total_time_spend - 60;
                    double amountInMinitus = Double.parseDouble(msg.getString("amount")) / 60;
                    double totalExtraAmount = amountInMinitus * extraTime;

                    amount.setText(new DecimalFormat("##.##").format(totalExtraAmount + Double.parseDouble(msg.getString("min_charges"))) + "");
                } else {
                    amount.setText(new DecimalFormat("##.##").format(Double.parseDouble(msg.getString("min_charges"))) + "");
                }

            }


            Picasso.with(CareGiverSummeryPageActivity.this).load(Const.URL.Image_Url + msg.getString("profile_pic")).error(R.drawable.user_profile_pic)
                    .into(ClientImage);
        } catch (Exception e) {
            e.printStackTrace();
            S.E("e.printStackTrace()-- " + e.toString());
        }
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (remainAmount.getText().toString().equals("0"))
                {
                    Payfromwalletpopup();
                }
                else
                {
                    SelectPaymentMethodeForCaregiver();
                }



            }
        });
        is_start_thread = true;
        GetCurrentStatus();
        getClientToken();
        getUserWalletBalance();
        change_method.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                payment_process.setVisibility(View.GONE);
            }
        });
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
                caregiverType="1";
                paymentMode="2";
                PayType = "online";
                transaction_id = "" + System.currentTimeMillis();
                PaymentOnServerForCaregiver();

                //   PaymentOnServer();
            }
        });

        dialog.show();
    }



    String paymentMode, caregiverType, type = "", transaction_id = "", PayType = "";

    private void SelectPaymentMethodeForCaregiver() {
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
                transaction_id = "" +System.currentTimeMillis();
                PayType = "cash";

                caregiverType = "1";


                PaymentOnServerForCaregiver();


            }
        });
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                payment_process.setVisibility(View.VISIBLE);

            }
        });

        dialog.show();
    }


    @Override
    public void finish() {

      /*  if (mainFragment) {
            super.finish();
        } else {*/

        S.I_clear(CareGiverSummeryPageActivity.this, CareGiverMainActivity.class, null);
    }

    private void RatingPopup() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rating_barpopup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        CircleImageView user_pic = (CircleImageView) dialog.findViewById(R.id.user_pic);
        TextView user_name = (TextView) dialog.findViewById(R.id.user_name);
        ratingBar1 = (RatingBar) dialog.findViewById(R.id.ratingBar1);
        describtion = (EditText) dialog.findViewById(R.id.describtion);
        Button submit = (Button) dialog.findViewById(R.id.submit);
        try {
            user_name.setText(mainobject.getString("full_name"));
            Picasso.with(CareGiverSummeryPageActivity.this).load(Const.URL.Image_Url + mainobject.getString("profile_pic")).error(R.drawable.user_profile_pic)
                    .into(user_pic);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserAccount.isEmpty(describtion) || ratingBar1.equals("0.0")) {
                    dialog.dismiss();

                    AddFinalRatting();

                }
            }
        });

        dialog.show();
    }

    private void AddFinalRatting() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.RATTING, 1, getParam(), new Helper() {
            @Override
            public void backResponse(String response) {
                Log.e("response", "getParams" + getParam());
                Log.e("Anitest", "response" + response);

                try {
                    JSONObject mainobject = new JSONObject(response);

                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");

                    if (status == 200) {
                        Toast.makeText(CareGiverSummeryPageActivity.this, "sucessfully", Toast.LENGTH_SHORT).show();

                        finish();
                    } else {
                        Toast.makeText(CareGiverSummeryPageActivity.this, "Rating Not Submited!Try Again", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParam() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("token", SavedData.gettocken_id());
        postParams.put("caregiver_id", caregiver_id);
        postParams.put("rating", String.valueOf(ratingBar1.getRating()));
        postParams.put("feedback", describtion.getText().toString());

        return postParams;
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
    String payment_status = "";
    String CaregiverId = "";
    String hire_caregiver_id = "";

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
                        caregiver_id = caregiverCurrentStatus.getCaregiverId();
                        hire_caregiver_id = caregiverCurrentStatus.getHireCaregiverId();


                        SavedData.saveHireCareGiverId(caregiverCurrentStatus.getHireCaregiverId());


                        if (payment_status.equalsIgnoreCase("complete")) {


                            try {
                                is_start_thread = false;

                                Bundle bundle = new Bundle();
                                SavedData.saveSummerypage(false);

                                // SavedData.saveMessageForsummery("");
                                try {
                                    bundle.putString("FullName", caregiverName.getText().toString());
                                    bundle.putString("Type", "caregiver");
                                    bundle.putString("Picture", msg.getString("profile_pic"));
                                    bundle.putString("Amount", amount.getText().toString());
                                    bundle.putString("StartTime", msg.getString("start_time"));
                                    bundle.putString("StopTime", msg.getString("stop_time"));
                                    bundle.putString("TotalTime", msg.getString("total_time"));
                                    bundle.putString("client_id", client_id);

                                    S.I_clear(CareGiverSummeryPageActivity.this, CareGiverRatingActivity.class, bundle);

                                } catch (Exception e) {
                                    S.E("check exception" + e);
                                }
                            } catch (Exception e) {
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


    private Map<String, String> paymentOnServerParam() {


        HashMap<String, String> postParams = new HashMap<>();
        try {
            postParams.put("token", user_token);
            postParams.put("caregiver_id", caregiver_id);
            postParams.put("amount", amount.getText().toString());
            postParams.put("payment_mode", paymentMode);
            postParams.put("transaction_id", transaction_id);
            postParams.put("type", "1");
            postParams.put("hire_caregiver_id", hire_caregiver_id);


            postParams.put("source", "");
            postParams.put("destination", "");
            postParams.put("start_time", msg.getString("start_time"));
            postParams.put("end_time", msg.getString("stop_time"));
            postParams.put("total_time", msg.getString("total_time"));
            postParams.put("total_kilometer", "");
        } catch (Exception e) {
            e.printStackTrace();
        }


        return postParams;
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

                    if (status == 200) {
                        if (doubleAmount >= doubleWallet) {
                            double doubleRemain = doubleAmount - doubleWallet;

                            remainAmount.setText("" + new DecimalFormat("##.##").format(doubleRemain));
                            bywalletAmount.setText(doubleWallet + "");
                            S.E("checkvalues if" + doubleAmount + ":amount" + doubleWallet + ":wallet" + walletBalance);

                            upDateUserWallet(String.valueOf("0"));
                        } else {
                            double doubleRemain = doubleWallet - doubleAmount;
                            remainAmount.setText("0");
                            bywalletAmount.setText(doubleAmount + "");
                            upDateUserWallet(String.valueOf(doubleRemain));

                        }

                        UpdateCompletestatus();
                        is_start_thread = false;

                        Bundle bundle = new Bundle();
                        SavedData.saveSummerypage(false);

                        //SavedData.saveMessageForsummery("");
                        try {
                            bundle.putString("FullName", caregiverName.getText().toString());
                            bundle.putString("Type", "caregiver");
                            bundle.putString("Picture", msg.getString("profile_pic"));
                            bundle.putString("Amount", amount.getText().toString());
                            bundle.putString("StartTime", msg.getString("start_time"));
                            bundle.putString("StopTime", msg.getString("stop_time"));
                            bundle.putString("TotalTime", msg.getString("total_time"));
                            bundle.putString("client_id", client_id);

                            S.I_clear(CareGiverSummeryPageActivity.this, CareGiverRatingActivity.class, bundle);

                        } catch (Exception e) {
                            S.E("check exception" + e);
                        }
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


    private void getUserWalletBalance() {
        new JSONParser(CareGiverSummeryPageActivity.this).parseVollyStringRequest(Const.URL.GET_USER_WALLET_BALANCE, 1, getParamsForUserWallet(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("refer code : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {

                        textWalletBalance.setText(getString(R.string.rupyee_symbol) + " " + jsonObject.getString("wallet"));

                        walletBalance = jsonObject.getString("wallet");

                        String amountTotal = amount.getText().toString();

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
                        S.T(CareGiverSummeryPageActivity.this, jsonObject.getString("message"));
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


    private void upDateUserWallet(String updateWalletBalance) {
        new JSONParser(CareGiverSummeryPageActivity.this).parseVollyStringRequest(Const.URL.Update_User_Wallet, 1, getParamsForUpdateUserwallet(updateWalletBalance), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("refer code :ipdate " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if (message.equals("success")) {


                    } else if (jsonObject.getString("status").equals("1")) {
                        S.T(CareGiverSummeryPageActivity.this, jsonObject.getString("message"));
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

String user_token ;

    private void getClientToken() {
        new JSONParser(CareGiverSummeryPageActivity.this).parseVollyStringRequest(Const.URL.GET_USER_TOKEN, 1, getParamsForClient_token(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("refer code : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("true")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");


                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                        user_token = jsonObject1.getString("token");
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



}
