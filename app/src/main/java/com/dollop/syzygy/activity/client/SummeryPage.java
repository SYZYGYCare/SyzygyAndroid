package com.dollop.syzygy.activity.client;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.sohel.UserAccount;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class SummeryPage extends BaseActivity {

    @BindView(R.id.caregiver_name)
    TextView caregiverName;
    @BindView(R.id.start_time)
    TextView startTime;
    @BindView(R.id.end_time)
    TextView endTime;
    @BindView(R.id.working_time)
    TextView workingTime;
    @BindView(R.id.amount)
    TextView amount;
    @BindView(R.id.finish)
    Button finish;
    RatingBar ratingBar1;
    EditText describtion;
    String caregiver_id;
    @BindView(R.id.Client_image)
    CircleImageView ClientImage;
    int screenWidth = 500;
    JSONObject mainobject;
    @Override
    protected int getContentResId() {
        return R.layout.activity_summery_page;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTitleToolbar("   Summary");
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("Message");
        try {
             mainobject = new JSONObject(message);
            caregiver_id = mainobject.getString("caregiver_id");
            endTime.setText(mainobject.getString("stop_time"));
            caregiverName.setText(mainobject.getString("full_name"));
            startTime.setText(mainobject.getString("start_time"));
            amount.setText(mainobject.getString("amount"));
            workingTime.setText(mainobject.getString("total_time"));
            Picasso.with(SummeryPage.this).load(Const.URL.Image_Url + mainobject.getString("profile_pic")).error(R.drawable.user_profile_pic).resize(screenWidth, 500)
                    .into(ClientImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingPopup();
            }
        });
    }

    private void RatingPopup() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rating_barpopup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        CircleImageView user_pic = (CircleImageView) dialog.findViewById(R.id.user_pic);
        TextView user_name = (TextView) dialog.findViewById(R.id.user_name);
        ratingBar1 = (RatingBar) dialog.findViewById(R.id.ratingBar1);
        describtion = (EditText) dialog.findViewById(R.id.describtion);
        Button submit = (Button) dialog.findViewById(R.id.submit);
        try {
            user_name.setText(mainobject.getString("full_name"));
            Picasso.with(SummeryPage.this).load(Const.URL.Image_Url + mainobject.getString("profile_pic")).error(R.drawable.user_profile_pic)
                    .into(user_pic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserAccount.isEmpty(describtion) || ratingBar1.equals("0.0")) {
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
                try {
                    JSONObject mainobject = new JSONObject(response);
                    Log.e("response", "response" + response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    /*if (status == 200) {
                        Toast.makeText(SummeryPage.this, "sucessfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SummeryPage.this, ClientSignUpActivity.class);
                        intent.putExtra("contact_no", strNumber);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SummeryPage.this, "Otp Not Match..", Toast.LENGTH_SHORT).show();
                    }*/
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
}
