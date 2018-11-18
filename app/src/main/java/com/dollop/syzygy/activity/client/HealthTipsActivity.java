package com.dollop.syzygy.activity.client;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.dollop.syzygy.Model.HealthTipsModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.adapter.HealthTipsAdapter;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HealthTipsActivity extends BaseActivity {
    List<HealthTipsModel> healthTipsModelList = new ArrayList<>();
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    HealthTipsAdapter healthTipsAdapter;
    HealthTipsModel healthTipsModel;

    @BindView(R.id.healthRecyclerView)
    RecyclerView healthRecyclerView;
    @BindView(R.id.healthParentLayout)
    LinearLayout healthParentLayout;
    private String Token;

    @Override
    protected int getContentResId() {
        return R.layout.activity_health_tips;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolbarWithBackButton("Health Tips");
        healthRecyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        healthRecyclerView.setLayoutManager(recyclerViewlayoutManager);
        healthTipsAdapter = new HealthTipsAdapter(this, healthTipsModelList);

        healthJsonData();
    }

    private void healthJsonData() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.GET_HEALTH_CATEGORY, 1, getParams(), new Helper() {
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
                            healthTipsModel = new HealthTipsModel();
                            healthTipsModel.setCategoryId(jsonObject.getString("category_id"));
                            healthTipsModel.setCategoryName(jsonObject.getString("category_name"));
                            healthTipsModel.setParentId(jsonObject.getString("parent_id"));
                            healthTipsModel.setImage(jsonObject.getString("image"));
                            healthTipsModelList.add(healthTipsModel);
                        }
                        healthTipsAdapter = new HealthTipsAdapter(HealthTipsActivity.this, healthTipsModelList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        healthRecyclerView.setLayoutManager(layoutManager);
                        healthRecyclerView.setAdapter(healthTipsAdapter);
                        healthTipsAdapter.notifyDataSetChanged();
//                        Collections.reverse(hireyourlist);
                    } else {
/*
                        Toast.makeText(getApplicationContext(), "No HireGiver", Toast.LENGTH_SHORT).show();
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
        params.put("token", SavedData.gettocken_id());

        Log.e("SaveData.getServiceId()", "" + Token);
        return params;
    }
}
