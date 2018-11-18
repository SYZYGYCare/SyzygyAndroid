package com.dollop.syzygy.activity.client;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dollop.syzygy.Model.EmergencyModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
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

public class EmergencyContactClientActivity extends BaseActivity {
    EditText ContactClient1, ContactClient2, ContactClient3;
    Button BtnSubitEmgNumberId;

    String Token;
    List<EmergencyModel> emergencylist = new ArrayList<>();

    @Override
    protected int getContentResId() {
        return R.layout.emergency;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("Emergency Contact");
        Token = SavedData.gettocken_id();

        ContactClient1 = (EditText) findViewById(R.id.ContactClient1);
        ContactClient1.setCursorVisible(false);


        BtnSubitEmgNumberId = (Button) findViewById(R.id.BtnSubitEmgNumberId);
        getEmergencyNumber();
        BtnSubitEmgNumberId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContactClient1.getText().toString().equals("") || ContactClient1.getText().toString().length()<10) {
                    ContactClient1.setError("Please enter valid contact number.");
                    ContactClient1.requestFocus();
                } else {

                    EmergencyContactClient();
                }
            }
        });

    }

    private void getEmergencyNumber() {

        new JSONParser(this).parseVollyStringRequest(Const.URL.Get_Emergency_Number, 1, getParam(), new Helper() {
            @Override
            public void backResponse(String response) {

                try {
                    S.E("emergency No" + getParam() + "*test" + response);
                    emergencylist.clear();
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            EmergencyModel clientModel = new EmergencyModel();
                            clientModel.setEmergencyNo(jsonObject.getString("emergency_no"));

                            emergencylist.add(clientModel);

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private Map<String, String> getParam() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", Token);
        return hashMap;
    }

    private void EmergencyContactClient() {

        new JSONParser(this).parseVollyStringRequest(Const.URL.AddEmerGencyClientContact_Url, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {

                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        Toast.makeText(EmergencyContactClientActivity.this, "Submitted Number", Toast.LENGTH_SHORT).show();
                        getEmergencyNumber();


                    } else {
                        Toast.makeText(EmergencyContactClientActivity.this, "Not Submitted Number", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", Token);
        params.put("emergency_no", ContactClient1.getText().toString());


        return params;
    }


}


