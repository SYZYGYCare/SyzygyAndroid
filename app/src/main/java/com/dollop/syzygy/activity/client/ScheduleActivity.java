package com.dollop.syzygy.activity.client;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dollop.syzygy.Model.AnyRemainder;
import com.dollop.syzygy.Model.RemainderFor;
import com.dollop.syzygy.Model.RequiredCare;
import com.dollop.syzygy.Model.ScheduledCaregiverDTO;
import com.dollop.syzygy.R;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduleActivity extends AppCompatActivity {

    ImageView back_button = null;
    LinearLayout main_layout = null;
    int width = 0, height = 0;
TextView no_data_found = null;
    Context ct= this;

List<ScheduledCaregiverDTO> scheduledCaregiverDTOS = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        back_button = (ImageView) findViewById(R.id.back_button);
        main_layout = (LinearLayout) findViewById(R.id.main_layout);
        no_data_found = (TextView) findViewById(R.id.no_data_found);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        width = displayMetrics.widthPixels;
        height = displayMetrics.heightPixels;
        loadData();
    }



    private void loadData() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.GET_SCHEDULED_CAREGIVER_DATA, 0, getParam(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("reminder data : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200"))
                    {

                        JSONArray reminder_for = jsonObject.getJSONArray("data");

                        Gson gson = new Gson();
                        JSONArray arrayData = jsonObject.getJSONArray("data");
                        Type listtype = new TypeToken<List<ScheduledCaregiverDTO>>() {
                        }.getType();
                        scheduledCaregiverDTOS = gson.fromJson(arrayData.toString(), listtype);
                        if(scheduledCaregiverDTOS!= null && scheduledCaregiverDTOS.size()>0)
                        {
                            Drawshedule();
                        }
                        else
                        {
                            no_data_found.setVisibility(View.VISIBLE);
                        }

                    }
                    else
                    {
                        no_data_found.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParam() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", SavedData.getUserId());

        S.E("getService ki request" + params);
        return params;

    }


    private void Drawshedule()
    {
        try {

            main_layout.removeAllViews();

            for (int i = 0; i < scheduledCaregiverDTOS.size(); i++)
            {
                String[] date_time = scheduledCaregiverDTOS.get(i).getDate().split(" ");




                    RelativeLayout Accepted_layout = new RelativeLayout(ct);
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    // lp.(width/30,width/30,width/30,width/30);
                    Accepted_layout.setLayoutParams(lp);
                    Accepted_layout.setBackgroundColor(getResources().getColor(R.color.white));
                    Accepted_layout.setPadding(width/30,width/30,width/30,width/30);
                    //  Accepted_layout.setBackgroundColor(Color.parseColor("#ffffff"));
                    Accepted_layout.setId(i + 1);
                    Accepted_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int index = v.getId();
                            index = index - 1;
                        }
                    });


                    CardView cardView = new CardView(ct);
                    cardView.setRadius(width/30);
                    cardView.setLayoutParams(lp);


                    LinearLayout Acceptdenylayout = new LinearLayout(ct);
                    lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    Acceptdenylayout.setLayoutParams(lp);
                    Acceptdenylayout.setOrientation(LinearLayout.VERTICAL);


                    TextView counselltime = new TextView(ct);
                    lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(width/100,width/100,width/100,width/100);
                    counselltime.setLayoutParams(lp);
                    counselltime.setText("Type: "+scheduledCaregiverDTOS.get(i).getType());
                    counselltime.setTextColor(Color.parseColor("#ffffff"));
                    counselltime.setTextSize(16);
                    counselltime.setTypeface(Typeface.DEFAULT_BOLD);
                    counselltime.setGravity(Gravity.CENTER);


                    TextView Duration = new TextView(ct);
                    lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(width/100,width/100,width/100,width/100);
                    Duration.setLayoutParams(lp);
                    Duration.setText("Service Id: "+scheduledCaregiverDTOS.get(i).getServiceId());
                    Duration.setTextColor(Color.parseColor("#ffffff"));
                    Duration.setTextSize(13);
                    Duration.setGravity(Gravity.CENTER);



                    TextView sessionleft = new TextView(ct);
                    lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(width/100,width/100,width/100,width/100);
                    sessionleft.setLayoutParams(lp);
                    sessionleft.setText("Service: "+scheduledCaregiverDTOS.get(i).getServiceId());
                    sessionleft.setTextColor(Color.parseColor("#ffffff"));
                    sessionleft.setTextSize(14);
                    sessionleft.setGravity(Gravity.CENTER);

                    TextView date_time_text = new TextView(ct);
                    lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(width/100,width/100,width/100,width/100);
                    date_time_text.setLayoutParams(lp);
                    date_time_text.setText("Date and Time: "+scheduledCaregiverDTOS.get(i).getDate() + " " +scheduledCaregiverDTOS.get(i).getTime());
                    date_time_text.setTextColor(Color.parseColor("#ffffff"));
                    date_time_text.setTextSize(14);
                    date_time_text.setGravity(Gravity.CENTER);

                    TextView spaclayout = new TextView(ct);
                    lp = new RelativeLayout.LayoutParams(width, width / 50);
                    spaclayout.setLayoutParams(lp);

                /*    Acceptdenylayout.addView(spaclayout);
                    Acceptdenylayout.addView(radiobutton);*/
                    Acceptdenylayout.addView(counselltime);
                    Acceptdenylayout.addView(Duration);
                    Acceptdenylayout.addView(date_time_text);
                    Acceptdenylayout.addView(sessionleft);
                    Accepted_layout.addView(Acceptdenylayout);
                    cardView.addView(Accepted_layout);
                    main_layout.addView(cardView);
                    main_layout.addView(spaclayout);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}