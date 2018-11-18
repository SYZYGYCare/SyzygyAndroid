package com.dollop.syzygy.activity.client;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dollop.syzygy.Model.GetRemainderModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.adapter.GetRemainderAdpter;
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
import java.util.Map;

import butterknife.ButterKnife;

public class ReminderListActivity extends BaseActivity implements CustomButtonListener {
    private RecyclerView getRemainderRecycelView;
    private GetRemainderAdpter adapter;
    ArrayList<GetRemainderModel> arrayList;
    // TODO: Rename parameter arguments, choose names that match

    @Override
    protected int getContentResId() {
        return R.layout.fragment_reminder_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getRemainderRecycelView = (RecyclerView) findViewById(R.id.getRemainderRecycelView);

        setToolbarWithBackButton("Reminder's");

        arrayList = new ArrayList<>();

        getDataFromServer();
    }

    private void getDataFromServer() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.GET_REMAINDER, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("remainder : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200"))
                    {
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject json = data.getJSONObject(i);
                            GetRemainderModel getRemainderModel = new GetRemainderModel();
                            getRemainderModel.setNotification_id(json.getString("notification_id"));
                            getRemainderModel.setUser_id(json.getString("user_id"));
                            getRemainderModel.setStart_date(json.getString("start_date"));
                            getRemainderModel.setTime(json.getString("time"));
                            getRemainderModel.setInterval(json.getString("interval"));
                            getRemainderModel.setReminder_for(json.getString("reminder_for"));
                            getRemainderModel.setRequired_care(json.getString("required_care"));
                            getRemainderModel.setReminder_type(json.getString("reminder_type"));
                            getRemainderModel.setNumber_of_reminder(json.getString("number_of_reminder"));
                            getRemainderModel.setAny_reminder(json.getString("any_reminder"));
                            getRemainderModel.setCreated_date(json.getString("created_date"));
                            arrayList.add(getRemainderModel);
                        }
                        adapter = new GetRemainderAdpter(ReminderListActivity.this, arrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReminderListActivity.this);
                        getRemainderRecycelView.setLayoutManager(layoutManager);
                        getRemainderRecycelView.setAdapter(adapter);
                        adapter.setCustomListener(ReminderListActivity.this);
                    }else{
                        arrayList = new ArrayList<>();
                        adapter = new GetRemainderAdpter(ReminderListActivity.this, arrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ReminderListActivity.this);
                        getRemainderRecycelView.setLayoutManager(layoutManager);
                        getRemainderRecycelView.setAdapter(adapter);
                        adapter.setCustomListener(ReminderListActivity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        return params;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        S.I(ReminderListActivity.this, ClientReminderActivity.class, null);
    }

    @Override
    public void onButtonClick(final int position, String buttonText) {
        if (buttonText.equalsIgnoreCase("deleteClick")) {
            deleteConfirmationDialog(position);
        }
    }

    private void deleteConfirmationDialog(final int position) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Are you sure? You want to delete Reminder!");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteRemainder(position);
                        dialog.cancel();
                    }
                });
        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void deleteRemainder(int pos) {
        new JSONParser(this).parseVollyStringRequest(Const.URL.DELETE_REMAINDER, 1, getParamsForDelete(pos), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("remainder delete : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        S.T(ReminderListActivity.this, "deleted successfully");
                        arrayList.clear();
                        getDataFromServer();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParamsForDelete(int pos) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("notification_id", arrayList.get(pos).getNotification_id());
        return params;
    }
}