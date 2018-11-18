package com.dollop.syzygy.activity.client;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.dollop.syzygy.Model.HireYourCaregiverModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.adapter.HireYourCaregiverAdapter;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HireYourCaregiverActivity extends BaseActivity {
    RecyclerView recyclerView;
    LinearLayout loadinLayout;
    HireYourCaregiverAdapter hireYourCaregiverAdapter;
    HireYourCaregiverModel hireYourCaregiverModel;
    Context context;
    List<HireYourCaregiverModel> hireyourlist = new ArrayList<HireYourCaregiverModel>();
    String Token;

    int image[] = new int[]{R.drawable.image, R.drawable.image, R.drawable.image, R.drawable.image, R.drawable.image};

    @Override
    protected int getContentResId() {
        return R.layout.activity_ur_caregiver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("Hires Your Caregiver");
        Token = SavedData.gettocken_id();
        Log.e("Token", "Token121" + Token);
        recyclerView = (RecyclerView) findViewById(R.id.id_recyclerviewyourhire);

        getCareHireGiver();
    }

    private void getCareHireGiver() {

        new JSONParser(this).parseVollyStringRequest(Const.URL.HireCareGiverHistroy_Url, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("CheckResponse" + response);
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            hireYourCaregiverModel = new HireYourCaregiverModel();
                            hireYourCaregiverModel.setHirename(jsonObject.getString("name"));
                            Log.e("", "" + jsonObject.getString("name"));
                            hireYourCaregiverModel.setHiredesc(jsonObject.getString("description"));
                            hireYourCaregiverModel.setHirespecility(jsonObject.getString("post"));
                            hireYourCaregiverModel.setHireimage(jsonObject.getString("profile_pic"));
                            hireyourlist.add(hireYourCaregiverModel);
                        }
                        Collections.reverse(hireyourlist);
                        hireYourCaregiverAdapter = new HireYourCaregiverAdapter(HireYourCaregiverActivity.this, hireyourlist);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(hireYourCaregiverAdapter);
                        hireYourCaregiverAdapter.notifyDataSetChanged();
//                        Collections.reverse(hireyourlist);
                    } else {
/*
                        Toast.makeText(HireYourCaregiverActivity.this, "No HireGiver", Toast.LENGTH_SHORT).show();
*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("checkMessage" + e);
                }
            }
        });
    }


    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", Token);

        Log.e("SaveData.getServiceId()", "" + Token);
        return params;
    }
}



