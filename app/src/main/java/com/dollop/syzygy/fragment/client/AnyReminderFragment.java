package com.dollop.syzygy.fragment.client;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dollop.syzygy.Model.ReminderForModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.client.ReminderListActivity;
import com.dollop.syzygy.adapter.ReminderHeaderAdapter;
import com.dollop.syzygy.listeners.CustomButtonListener;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnyReminderFragment extends Fragment implements CustomButtonListener {


    RecyclerView rvReminderForId;
    FloatingActionButton btnNext;
    View view;
    private List<ReminderForModel> reminderForModels = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_any_reminder, null);
        btnNext = (FloatingActionButton) view.findViewById(R.id.btnNext);
        rvReminderForId = (RecyclerView) view.findViewById(R.id.rvReminderForId);

        ReminderForModel reminderForModel = new ReminderForModel();
        reminderForModel.setRemindername("Sleeping");

        reminderForModels.add(reminderForModel);

        ReminderForModel reminderForModel1 = new ReminderForModel();
        reminderForModel1.setRemindername("Breakfast");

        reminderForModels.add(reminderForModel1);
        ReminderForModel reminderForModel2 = new ReminderForModel();
        reminderForModel2.setRemindername("Lunch");

        reminderForModels.add(reminderForModel2);
        ReminderForModel reminderForModel3 = new ReminderForModel();
        reminderForModel3.setRemindername("Meeting");

        reminderForModels.add(reminderForModel3);
        ReminderForModel reminderForModel4 = new ReminderForModel();
        reminderForModel4.setRemindername("Talk To Soups");

        reminderForModels.add(reminderForModel4);

        ReminderForModel reminderForModel5 = new ReminderForModel();
        reminderForModel5.setRemindername("Talk To Friends");

        reminderForModels.add(reminderForModel5);

        ReminderForModel reminderForModel6 = new ReminderForModel();
        reminderForModel6.setRemindername("Talk To Child");

        reminderForModels.add(reminderForModel6);


        ReminderHeaderAdapter healthPointsAdapter = new ReminderHeaderAdapter(getActivity(), reminderForModels, "anyRemainder");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvReminderForId.setLayoutManager(layoutManager);
        rvReminderForId.setAdapter(healthPointsAdapter);
        healthPointsAdapter.setCustomListener(this);
        healthPointsAdapter.notifyDataSetChanged();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!SavedData.getRequiredCateType().equals("")) {
                    if (!SavedData.getRequiredCateType().equals("1")) {
                        if (!SavedData.getRemainderHour().equals("")) {
                            if (!SavedData.getRemainderDate().equals("")) {
                                saveRemainderToServer();
                            } else {
                                S.T(getActivity(), "Select Date");
                            }
                        } else {
                            S.T(getActivity(), "Select Interval");
                        }
                    } else {
                        if (!SavedData.getRemainderDate().equals("")) {
                            saveRemainderToServer();
                        } else {
                            S.T(getActivity(), "Select Date");
                        }
                    }
                } else {
                    S.T(getActivity(), "Select Required care type");
                }

                /*if (!SavedData.getRemainderDate().equals("")) {
                    if (!SavedData.getRemainderTime().equals("")) {
                        if (!SavedData.getRequiredCate().equals("")) {
                            if (!SavedData.getRequiredCateType().equals("")) {
                                if (!SavedData.getAnyRemainder().equals("")) {
                                    if (!SavedData.getRemainderHour().equals("1")) {
                                        if (!SavedData.getRemainderHour().equals("")) {
                                            saveRemainderToServer();
                                        } else {
                                            S.T(getActivity(), "Select Interval");
                                        }
                                    } else {
                                        saveRemainderToServer();
                                    }
                                } else {
                                    S.T(getActivity(), "Select Any Remainder");
                                }
                            } else {
                                S.T(getActivity(), "Select Interval");
                            }
                        } else {
                            S.T(getActivity(), "Select Required Care");
                        }
                    } else {
                        S.T(getActivity(), "Select Time");
                    }
                } else {
                    S.T(getActivity(), "Select Date");
                }*/

            }
        });
        return view;
    }

    private void saveRemainderToServer() {
        S.E("remainder params : " + getParams());
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.ADD_REMAINDER, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("remainder response : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        SavedData.saveRemainderFor("");
                        SavedData.saveRequiredCate("");
                        SavedData.saveRequiredCateType("");
                        SavedData.saveRemainderDate("");
                        SavedData.saveRemainderTime("");
                        SavedData.saveAnyRemainder("");
                        SavedData.saveRemainderHour("");
                        S.I(getActivity(), ReminderListActivity.class, null);
                    } else {
                        S.T(getActivity(), "Please try again later");
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
        params.put("start_date", SavedData.getRemainderDate());
        params.put("time", SavedData.getRemainderTime());
        params.put("reminder_for", SavedData.getRemainderFor());
        params.put("required_care", SavedData.getRequiredCate());
        params.put("reminder_type", SavedData.getRequiredCateType());
        params.put("any_reminder", SavedData.getAnyRemainder());

        if (!SavedData.getRemainderHour().equals("1")) {
            params.put("interval", SavedData.getRemainderHour());
        } else {
            params.put("interval", "0");
        }
        return params;
    }

    @Override
    public void onButtonClick(int position, String buttonText) {
        if (buttonText.equalsIgnoreCase("anyRemainderClick")) {
            SavedData.saveAnyRemainder(reminderForModels.get(position).getRemindername());
            S.E("anyRemainder : " + SavedData.getAnyRemainder());
        }
    }
}
