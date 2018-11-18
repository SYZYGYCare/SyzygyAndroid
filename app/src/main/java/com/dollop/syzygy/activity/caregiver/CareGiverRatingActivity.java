package com.dollop.syzygy.activity.caregiver;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.activity.client.ClientMainActivity;
import com.dollop.syzygy.activity.client.Client_RatingActivity;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CareGiverRatingActivity extends BaseActivity {
    TextView user_name, amount, type_Tv, start_time, end_time, total_time, sourceLocation, destinationLocation, skip;
    CircleImageView user_pic;
    ImageView typeimg, SheetImageview;
    String Type, PaymentMode, caregiver_id;
    LinearLayout describtionLayout, TimeLayout;
    Button submit;
    RatingBar ratingBar1;
    EditText describtion;

    @Override
    protected int getContentResId() {
        return R.layout.activity_client__rating;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_name = (TextView) findViewById(R.id.user_name);
        amount = (TextView) findViewById(R.id.finalamount);
        type_Tv = (TextView) findViewById(R.id.type_Tv);
        start_time = (TextView) findViewById(R.id.start_time);
        end_time = (TextView) findViewById(R.id.end_time);
        total_time = (TextView) findViewById(R.id.total_time);
        skip = (TextView) findViewById(R.id.skip);
        sourceLocation = (TextView) findViewById(R.id.sourceLocation);
        submit = (Button) findViewById(R.id.submit);
        destinationLocation = (TextView) findViewById(R.id.destinationLocation);
        typeimg = (ImageView) findViewById(R.id.typeimg);
        SheetImageview = (ImageView) findViewById(R.id.SheetImageview);
        describtionLayout = (LinearLayout) findViewById(R.id.describtionLayout);
        TimeLayout = (LinearLayout) findViewById(R.id.TimeLayout);
        user_pic = (CircleImageView) findViewById(R.id.user_pic);
        ratingBar1 = (RatingBar) findViewById(R.id.ratingBar1);
        describtion = (EditText) findViewById(R.id.describtion);

        Bundle bundle = getIntent().getExtras();
        caregiver_id = bundle.getString("client_id");
        user_name.setText(bundle.getString("FullName"));
        Picasso.with(CareGiverRatingActivity.this).load(Const.URL.Image_Url + bundle.getString("Picture")).error(R.drawable.user_profile_pic)
                .into(user_pic);
        amount.setText(bundle.getString("Amount"));
        Type = bundle.getString("Type");
        PaymentMode = bundle.getString("PaymentMode");
      /*  if (PaymentMode.equals("cash")) {
            SheetImageview.setImageResource(R.drawable.cash);
        } else {
            SheetImageview.setImageResource(R.drawable.bank);
        }*/
        if (Type.equals("caregiver")) {
            type_Tv.setText(Type);
            typeimg.setImageResource(R.drawable.ic_doctor);
            describtionLayout.setVisibility(View.GONE);
            TimeLayout.setVisibility(View.VISIBLE);
            start_time.setText(bundle.getString("StartTime"));
            end_time.setText(bundle.getString("StopTime"));
            total_time.setText(bundle.getString("TotalTime")+" min");
        } else {
            type_Tv.setText(Type);
            typeimg.setImageResource(R.drawable.ic_ambulance_home);
            describtionLayout.setVisibility(View.VISIBLE);
            TimeLayout.setVisibility(View.GONE);
            sourceLocation.setText(bundle.getString("StartTime"));
            destinationLocation.setText(bundle.getString("StopTime"));
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ratingBar1.getRating() == 0.0) {
                    S.T(CareGiverRatingActivity.this, "Please Give a Rating");
                } else {

                    S.E("getParams"+getParam());
                    Update_cancel_status();
                    AddFinalRatting();

                    SavedData.saveRatingStatus(false);
                    /*S.I_clear(CareGiverRatingActivity.this, CareGiverMainActivity.class, null);*/

                }
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update_cancel_status();
                SavedData.saveRatingStatus(false);
                S.I_clear(CareGiverRatingActivity.this, CareGiverMainActivity.class, null);

            }
        });
    }


    private void Update_cancel_status() {
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
        param.put("status", "completed");
        S.E(" token..." + SavedData.gettocken_id());
        return param;
    }

    private void AddFinalRatting() {
        Toast.makeText(CareGiverRatingActivity.this, "sucessfully", Toast.LENGTH_SHORT).show();
        S.I_clear(CareGiverRatingActivity.this, CareGiverMainActivity.class, null);
        new JSONParser(this).parseVollyStringRequest(Const.URL.Caregiver_rating, 1, getParam(), new Helper() {

            @Override
            public void backResponse(String response) {
                Log.e("response", "getParams" + getParam());
                Log.e("Anitest", "response" + response);

                try {
                    JSONObject mainobject = new JSONObject(response);

                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");

                    if (status == 200) {
                        Toast.makeText(CareGiverRatingActivity.this, "sucessfully", Toast.LENGTH_SHORT).show();
                        S.I_clear(CareGiverRatingActivity.this, CareGiverMainActivity.class, null);

                    } else {
                        Toast.makeText(CareGiverRatingActivity.this, "Rating Not Submited!Try Again", Toast.LENGTH_SHORT).show();
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
        postParams.put("client_id", caregiver_id);
        postParams.put("rating", String.valueOf(ratingBar1.getRating()));
        postParams.put("feedback", describtion.getText().toString());
     /*   Log.e("response", "getParams" + getParam());*/
        return postParams;
    }
}
