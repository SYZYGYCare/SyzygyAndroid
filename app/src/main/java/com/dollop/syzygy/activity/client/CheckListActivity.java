package com.dollop.syzygy.activity.client;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.dollop.syzygy.Model.HealthTipsModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.adapter.CheckListAdapter;
import com.dollop.syzygy.listeners.CustomButtonListener;
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

public class CheckListActivity extends BaseActivity implements CustomButtonListener {
    List<HealthTipsModel> healthTipsModelList = new ArrayList<>();
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    CheckListAdapter coSubHealthAdapter;
    HealthTipsModel healthTipsModel;
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.coSububCategoryRecylceView)
    RecyclerView coSububCategoryRecylceView;
    private String Token, CategoryId;

    @Override
    protected int getContentResId() {
        return R.layout.activity_healt_tips_co_sub;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();

        ButterKnife.bind(this);
        setToolbarWithBackButton("ToDo List");
        finish();
        S.I(CheckListActivity.this,HealthPointsToDoActivity.class,null);

        coSububCategoryRecylceView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        coSububCategoryRecylceView.setLayoutManager(recyclerViewlayoutManager);
        coSubHealthAdapter = new CheckListAdapter(this, healthTipsModelList);

        getHealthCoSubData();
    }

    @Override
    public void onButtonClick(int position, String buttonText) {
        if (buttonText.equalsIgnoreCase("heatClick")) {
            addCheckList(position);
        }
    }

    private void addCheckList(int position) {
        new JSONParser(this).parseVollyStringRequest(Const.URL.ADD_CHECKLIST, 1, getPrams(position), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("Add to checklist : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200"))
                    {
                       } else if (jsonObject.getString("status").equals("300"))
                    {
                    }
                    healthTipsModelList.clear();
                    getHealthCoSubData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getPrams(int position) {
        HashMap<String, String> prams = new HashMap<>();
        prams.put("token", SavedData.gettocken_id());
        prams.put("CoSubCategory_id", healthTipsModelList.get(position).getCategoryId());
        return prams;
    }

    private void getHealthCoSubData() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.GET_CHECKLIST, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("CheckResponse" + response);
                S.E("Const.URL.GET_CHECKLIST" + Const.URL.GET_CHECKLIST);
                S.E("Const.URL.parans" + Const.URL.GET_CHECKLIST);
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
                            healthTipsModel.setChecklist_id(jsonObject.getString("check_list_id"));
                            healthTipsModel.setStatus(jsonObject.getString("status"));
                            healthTipsModelList.add(healthTipsModel);
                        }
                        coSubHealthAdapter = new CheckListAdapter(CheckListActivity.this, healthTipsModelList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        coSububCategoryRecylceView.setLayoutManager(layoutManager);
                        coSububCategoryRecylceView.setAdapter(coSubHealthAdapter);
                        coSubHealthAdapter.notifyDataSetChanged();
                        coSubHealthAdapter.setCustomListener(CheckListActivity.this);
//                        Collections.reverse(hireyourlist);
                    } else {
                        coSubHealthAdapter = new CheckListAdapter(CheckListActivity.this, healthTipsModelList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        coSububCategoryRecylceView.setLayoutManager(layoutManager);
                        coSububCategoryRecylceView.setAdapter(coSubHealthAdapter);
                        coSubHealthAdapter.notifyDataSetChanged();
                        coSubHealthAdapter.setCustomListener(CheckListActivity.this);
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
        return params;
    }
}
