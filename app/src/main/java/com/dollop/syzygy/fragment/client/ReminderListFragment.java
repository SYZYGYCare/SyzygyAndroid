package com.dollop.syzygy.fragment.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dollop.syzygy.Model.GetRemainderModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.adapter.GetRemainderAdpter;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

public class ReminderListFragment extends Fragment {
    private RecyclerView getRemainderRecycelView;
    private GetRemainderAdpter adapter;
    ArrayList<GetRemainderModel> arrayList;
    // TODO: Rename parameter arguments, choose names that match

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reminder_list, container, false);
        ButterKnife.bind(getActivity());

        getRemainderRecycelView = (RecyclerView) view.findViewById(R.id.getRemainderRecycelView);

        arrayList = new ArrayList<>();

        getDataFromServer();
        return view;
    }

    private void getDataFromServer() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GET_REMAINDER, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("remainder : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
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
                        adapter = new GetRemainderAdpter(getActivity(), arrayList);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        getRemainderRecycelView.setLayoutManager(layoutManager);
                        getRemainderRecycelView.setAdapter(adapter);
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
}
