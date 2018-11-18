package com.dollop.syzygy.activity.client;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dollop.syzygy.Model.HealthTipsModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.adapter.HealthPointsAdapter;
import com.dollop.syzygy.adapter.HealthPointsToDoAdapter;
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

public class HealthPointsToDoActivity extends AppCompatActivity {
    List<HealthTipsModel> healthTipsModelList = new ArrayList<>();
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    HealthPointsToDoAdapter healthPointsAdapter;




    @BindView(R.id.healthPointRecyclerView)
    RecyclerView healthPointRecyclerView;
    @BindView(R.id.textViewToolbar)
    TextView textViewToolbar;

    @BindView(R.id.no_data_found)
    TextView no_data_found;
    @BindView(R.id.to_list)
    RelativeLayout to_list;

    @BindView(R.id.back_arrow)
    ImageView back_arrow;
    @BindView(R.id.healthParentLayout)
    LinearLayout healthParentLayout;


    String imageString = "";
    ArrayList<String> selectedId = new ArrayList<>();
    private String Token;
    private String pointsIds = "";


/*    @Override
    protected int getContentResId() {
        return R.layout.activity_health_pointstodo;
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_pointstodo);
        ButterKnife.bind(this);
      //  setToolbarWithBackButton("To Do");
        textViewToolbar.setText("To Do");
        healthPointRecyclerView.setHasFixedSize(true);
        recyclerViewlayoutManager = new LinearLayoutManager(this);
        healthPointRecyclerView.setLayoutManager(recyclerViewlayoutManager);
        healthPointsAdapter = new HealthPointsToDoAdapter(this, healthTipsModelList);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        healthPointRecyclerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        getHealthPoints();
    }


    private void getHealthPoints() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.GetToDo, 1, getParams(), new Helper() {
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
                            selectedId.add("0");
                            HealthTipsModel healthTipsModel = new HealthTipsModel();
                            healthTipsModel = new HealthTipsModel();
                            healthTipsModel.setCategoryId(jsonObject.getString("point_id"));
                            healthTipsModel.setCategoryName(jsonObject.getString("point_name"));

                            if(jsonObject.has("parent_id"))
                            healthTipsModel.setParentId(jsonObject.getString("parent_id"));

                           // healthTipsModel.setStatus(jsonObject.getString("status"));
                            healthTipsModelList.add(healthTipsModel);
                        }
                        healthPointsAdapter = new HealthPointsToDoAdapter(HealthPointsToDoActivity.this, healthTipsModelList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        healthPointRecyclerView.setLayoutManager(layoutManager);
                        healthPointRecyclerView.setAdapter(healthPointsAdapter);
                        healthPointsAdapter.notifyDataSetChanged();
                        to_list.setVisibility(View.VISIBLE);
                        no_data_found.setVisibility(View.GONE);
//                        Collections.reverse(hireyourlist);
                    } else {
                        to_list.setVisibility(View.GONE);
                        no_data_found.setVisibility(View.VISIBLE);
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
        return params;
    }

    /*
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
    }*/
}
