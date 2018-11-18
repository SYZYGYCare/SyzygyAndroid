package com.dollop.syzygy.activity.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.dollop.syzygy.Model.AddSeniorModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.adapter.SeniorListServiceAdpterMain;
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

public class ClientSeniorListActivity extends BaseActivity {
    FloatingActionButton floatingActionButton;
    RecyclerView RecyclerviewListSeniorId;
    LinearLayout loadinLayout;
    SeniorListServiceAdpterMain adapter;

    String getWhere = "";
    List<AddSeniorModel> employeeList = new ArrayList<>();
    String Token;
    static ClientSeniorListActivity clientSeniorListActivity;

    public final static int REQUESTCODE = 22;

    public static ClientSeniorListActivity getInstance() {

        return clientSeniorListActivity;
    }


    @Override
    protected int getContentResId() {
        return R.layout.activity_listsenior;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("People You Care");
        clientSeniorListActivity = this;
        Token = SavedData.gettocken_id();
        Log.e("Token", "Token121" + Token);
        RecyclerviewListSeniorId = (RecyclerView) findViewById(R.id.RecyclerviewListSeniorId);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        if (getIntent() != null) {
            getWhere = getIntent().getStringExtra("whereKey");

        }//        loadinLayout = (LinearLayout) findViewById(R.id.loadinLayout);
//        getAllSeniorList();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getWhere.equals("Drawer")) {
                    Intent intent = new Intent(ClientSeniorListActivity.this, AddSeniorActivity.class);
                    intent.putExtra("onlyshow", "edit");
                    startActivityForResult(intent, 1001);
                } else {
                    Intent intent = new Intent(ClientSeniorListActivity.this, AddSeniorActivity.class);
                    intent.putExtra("onlyshow", "edit");
                    startActivityForResult(intent, 1002);

                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1001) {
            getWhere = "Drawer";

            getAllSeniorList();


        } else {
            getWhere = "Main";

            getAllSeniorList();
        }
    }

    private void getAllSeniorList() {
//        loadinLayout.setVisibility(View.VISIBLE);
        new JSONParser(this).parseVollyStringRequest(Const.URL.Get_Senior_Url, 1, getParam(), new Helper() {
            @Override
            public void backResponse(String response) {
                Log.e("responce", "" + response);

                try {
                    JSONObject mainobject = new JSONObject(response);
                    Log.e("responce", "SeniorResponse" + response);
                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        employeeList.clear();
                        JSONArray content = mainobject.getJSONArray("data");
                        Log.e("content", "" + content);
                        for (int i = 0; i < content.length(); i++) {
                            JSONObject jsonObject = content.getJSONObject(i);
                            AddSeniorModel addEmpModel = new AddSeniorModel();

                            /*
                            {"status":200,"message":"success","data":[{"user_senior_id":"2","senior_name":"senior ani",
                            "senior_age":"24","profile_pic":"",
                            "senior_gender":"Male","senior_phone":"9907282204
                            ","address":"address near by me","special_instruction":"scpecial instruction","special_needs":"abbs sbbb","description":"description"},{"user_senior_id":"3","senior_name":"ghj","senior_age":"2696","profile_pic":"","senior_gender":"Male","senior_phone":"985465555","address":"ghh","special_instruction":"fhj hhh","special_needs":"fhhh hhh","description":"fhhj"},{"user_senior_id":"4","senior_name":"sgj vhh gh","senior_age":"24","profile_pic":"","senior_gender":"Male","senior_phone":"9907282204","address":"ggdf","special_instruction":"dghj gg","special_needs":"ghj","description":"dhn"},{"user_senior_id":"5","senior_name":"dudho ki","senior_age":"45","profile_pic":"","senior_gender":"Male","senior_phone":"9907282204","address":"dgjj","special_instruction":"rfbh","special_needs":"fvg","description":"tgj"}]}*/


                            addEmpModel.setSeniorName(jsonObject.getString("senior_name"));
                            addEmpModel.setUser_senior_id(jsonObject.getString("user_senior_id"));
                            addEmpModel.setSeniorAge(jsonObject.getString("senior_age"));
                            addEmpModel.setSeniorGender(jsonObject.getString("senior_gender"));
                            addEmpModel.setImage(jsonObject.getString("profile_pic"));
                            addEmpModel.setSeniorDecription(jsonObject.getString("description"));
                            addEmpModel.setSeniorSpical_need(jsonObject.getString("special_needs"));
                            addEmpModel.setSeniorcontact(jsonObject.getString("senior_phone"));
                            addEmpModel.setSeniorAddress(jsonObject.getString("address"));
                            addEmpModel.setRelationShip(jsonObject.getString("senior_relation"));

                            addEmpModel.setSpecial_instruction(jsonObject.getString("special_instruction"));

                            employeeList.add(addEmpModel);
                        }

                        adapter = new SeniorListServiceAdpterMain(ClientSeniorListActivity.this, employeeList, getWhere);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        RecyclerviewListSeniorId.setLayoutManager(layoutManager);
                        RecyclerviewListSeniorId.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        Collections.reverse(employeeList);
                    } else {
                        S.T(ClientSeniorListActivity.this, "No People");

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public Map<String, String> getParam() {
        HashMap<String, String> postParameters = new HashMap<>();
        postParameters.put("token", Token);
//        M.E("param.." + postParameters);
        return postParameters;
    }

    @Override
    protected void onResume() {
        super.onResume();
        employeeList.clear();
        getAllSeniorList();
    }
}