package com.dollop.syzygy.activity.client;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.dollop.syzygy.Model.HealthTipsModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.adapter.CoSubHealthAdapter;
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

public class HealthTipsCoSubActivity extends BaseActivity {
    List<HealthTipsModel> healthTipsModelList = new ArrayList<>();
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    CoSubHealthAdapter coSubHealthAdapter;
    HealthTipsModel healthTipsModel;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.coSububCategoryRecylceView)
    RecyclerView coSububCategoryRecylceView;
    private String Token, CategoryId;
    String imageForPoint="";

    @Override
    protected int getContentResId() {
        return R.layout.activity_healt_tips_co_sub;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        HealthTipsModel story = (HealthTipsModel) b.getSerializable("AddSaleitems");
        ButterKnife.bind(this);
        setToolbarWithBackButton(story.getCategoryName());
        CategoryId = story.getCategoryId();
        coSububCategoryRecylceView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        coSububCategoryRecylceView.setLayoutManager(recyclerViewlayoutManager);
        coSubHealthAdapter = new CoSubHealthAdapter(this, healthTipsModelList,imageForPoint);
        imageForPoint=story.getImage();
        getHealthCoSubData();
    }

    private void getHealthCoSubData() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.GET_HEALTH_CO_SUB_CATEGORY, 1, getParams(), new Helper() {
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
                            healthTipsModel.setCategoryId(jsonObject.getString("CoSubCategory_id"));
                            healthTipsModel.setCategoryName(jsonObject.getString("CoSubCategory_name"));
                            healthTipsModel.setParentId(jsonObject.getString("parent_id"));
                            healthTipsModel.setStatus(jsonObject.getString("status"));
                            healthTipsModel.setImage(jsonObject.getString("image"));
                            healthTipsModelList.add(healthTipsModel);
                        }
                        coSubHealthAdapter = new CoSubHealthAdapter(HealthTipsCoSubActivity.this, healthTipsModelList,imageForPoint);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        coSububCategoryRecylceView.setLayoutManager(layoutManager);
                        coSububCategoryRecylceView.setAdapter(coSubHealthAdapter);
                        coSubHealthAdapter.notifyDataSetChanged();
//                        Collections.reverse(hireyourlist);
                    } else {
                        //   Toast.makeText(getApplicationContext(), "No HireGiver", Toast.LENGTH_SHORT).show();
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
        params.put("subCategory_id", CategoryId);
        Log.e("SaveData.getServiceId()", "" + Token);
        return params;
    }
}
