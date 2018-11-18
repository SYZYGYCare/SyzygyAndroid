package com.dollop.syzygy.activity.client;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.syzygy.Model.HealthTipsModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.adapter.HealthPointsAdapter;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HealthPointsActivity extends BaseActivity implements HealthPointsAdapter.ClickListnerCallback {
    List<HealthTipsModel> healthTipsModelList = new ArrayList<>();
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    HealthPointsAdapter healthPointsAdapter;
    HealthTipsModel healthTipsModel;

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.textCoSubCatName)
    TextView textCoSubCatName;
    @BindView(R.id.healthPointRecyclerView)
    RecyclerView healthPointRecyclerView;
    @BindView(R.id.healthParentLayout)
    LinearLayout healthParentLayout;
    @BindView(R.id.ivHealthTypeId)
    ImageView ivHealthTypeId;
    @BindView(R.id.btnAddId)
    Button btnAddId;
    String imageString = "";
    ArrayList<String> selectedId = new ArrayList<>();
    private String Token, CategoryId;
    private String pointsIds = "";
    private HealthPointsAdapter.ClickListnerCallback clickListnerCallback = this;


    @Override
    protected int getContentResId() {
        return R.layout.activity_health_points;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Bundle b = getIntent().getExtras();
        HealthTipsModel story = (HealthTipsModel) b.getSerializable("AddSaleitems");
        setToolbarWithBackButton(story.getCategoryName());
        textCoSubCatName.setText(story.getCategoryName());
        CategoryId = story.getCategoryId();
        S.E("storycheckImage" + story.getImage());

        if (story.getImage() != null) {
            Picasso.with(HealthPointsActivity.this)
                    .load(Const.URL.Image_Url_health + story.getImage())
                    .into(ivHealthTypeId);
        } else {
            ivHealthTypeId.setImageResource(R.drawable.health_image);
        }
        healthPointRecyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        healthPointRecyclerView.setLayoutManager(recyclerViewlayoutManager);
        healthPointsAdapter = new HealthPointsAdapter(this, healthTipsModelList, clickListnerCallback);

        getHealthPoints();
        btnAddId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pointsIds = "";
                for (int i = 0; i < healthTipsModelList.size(); i++) {
                    if (healthTipsModelList.get(i).isChecked()) {
                        if ("".equals(pointsIds)) {
                            pointsIds = healthTipsModelList.get(i).getCategoryId();
                        } else {
                            pointsIds = pointsIds + "," + healthTipsModelList.get(i).getCategoryId();
                        }

                    }
                }
                addChecked(pointsIds);
            }
        });
    }


    private void getHealthPoints() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.GET_HEALTH_POINTS, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("CheckResponse" + response);
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        selectedId.clear();
                        healthTipsModelList.clear();
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            selectedId.add(jsonObject.getString("status"));
                            healthTipsModel = new HealthTipsModel();
                            healthTipsModel.setCategoryId(jsonObject.getString("point_id"));
                            healthTipsModel.setCategoryName(jsonObject.getString("point_name"));
                           /* healthTipsModel.setParentId(jsonObject.getString("parent_id"));*/
                            healthTipsModel.setParentId(jsonObject.getString("status"));
                            if (healthTipsModel.getParentId().equals("1")) {
                                healthTipsModel.setChecked(true);
                            } else {
                                healthTipsModel.setChecked(false);
                            }
                            healthTipsModelList.add(healthTipsModel);
                        }
                        healthPointsAdapter = new HealthPointsAdapter(HealthPointsActivity.this, healthTipsModelList, clickListnerCallback);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        healthPointRecyclerView.setLayoutManager(layoutManager);
                        healthPointRecyclerView.setAdapter(healthPointsAdapter);
                        healthPointsAdapter.notifyDataSetChanged();
//
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
        params.put("CoSubCategory_id", CategoryId);
        Log.e("SaveData.getServiceId()", "" + Token);
        return params;
    }

    private void addChecked(String pointsIds) {
        new JSONParser(this).parseVollyStringRequest(Const.URL.AddHealthPoint, 1, getParams2(pointsIds), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("CheckResponse" + response);
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        Toast.makeText(HealthPointsActivity.this,"Todo list updated successfully",Toast.LENGTH_SHORT).show();

                        getHealthPoints();
                    }

//                        Collections.reverse(hireyourlist);
                    else {
                        //   Toast.makeText(getApplicationContext(), "No HireGiver", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("checkMessage" + e);
                }
            }
        });
    }

    private Map<String, String> getParams2(String pointsIdssssss) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("CoSubCategory_id", CategoryId);
        params.put("points_ids", pointsIdssssss);

        Log.e("SaveData.getServiceId()", "" + Token);
        return params;
    }

    @Override
    public void onClick(int position, boolean isChecked) {
        this.healthTipsModelList.get(position).setChecked(isChecked);
    }
}
