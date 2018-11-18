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
import com.dollop.syzygy.activity.client.ClientReminderActivity;
import com.dollop.syzygy.adapter.ReminderHeaderAdapter;
import com.dollop.syzygy.listeners.CustomButtonListener;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReminderForFragment extends Fragment implements CustomButtonListener {

    @BindView(R.id.rvReminderForId)
    RecyclerView rvReminderForId;
    FloatingActionButton btnNext;
    ArrayList<ReminderForModel> reminderForModels = new ArrayList<>();
    private ReminderHeaderAdapter healthPointsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_reminderfor, container, false);
        ButterKnife.bind(getActivity());
        rvReminderForId = (RecyclerView) view.findViewById(R.id.rvReminderForId);
        btnNext = (FloatingActionButton) view.findViewById(R.id.btnNext);
        ReminderForModel reminderForModel = new ReminderForModel();
        reminderForModel.setRemindername("Self");

        reminderForModels.add(reminderForModel);

        ReminderForModel reminderForModel1 = new ReminderForModel();
        reminderForModel1.setRemindername("Spouse");

        reminderForModels.add(reminderForModel1);

        ReminderForModel reminderForModel2 = new ReminderForModel();
        reminderForModel2.setRemindername("Child");

        reminderForModels.add(reminderForModel2);
        ReminderForModel reminderForModel3 = new ReminderForModel();
        reminderForModel3.setRemindername("Friend");

        reminderForModels.add(reminderForModel3);
        ReminderForModel reminderForModel4 = new ReminderForModel();
        reminderForModel4.setRemindername("Parents");

        reminderForModels.add(reminderForModel4);
        ReminderForModel reminderForModel5 = new ReminderForModel();
        reminderForModel5.setRemindername("Other");

        reminderForModels.add(reminderForModel5);

        healthPointsAdapter = new ReminderHeaderAdapter(getActivity(), reminderForModels, "remainderFor");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvReminderForId.setLayoutManager(layoutManager);
        rvReminderForId.setAdapter(healthPointsAdapter);
        healthPointsAdapter.setCustomListener(this);
        healthPointsAdapter.notifyDataSetChanged();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SavedData.getRemainderFor().equals("")) {
                    ((ClientReminderActivity) getActivity()).openReequiredCare();
                } else {
                    S.T(getActivity(), "Please Select Remainder For");
                }
            }
        });
        return view;
    }

    @Override
    public void onButtonClick(int position, String buttonText) {
        if (buttonText.equalsIgnoreCase("remainderForClick")) {
            SavedData.saveRemainderFor(reminderForModels.get(position).getRemindername());
            S.E("remainderFor : " + SavedData.getRemainderFor());
        }
    }
}