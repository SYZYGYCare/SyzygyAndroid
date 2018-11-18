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
import com.dollop.syzygy.adapter.ReminderHeaderNormalAdapter;
import com.dollop.syzygy.listeners.CustomButtonListener;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;

import java.util.ArrayList;
import java.util.List;


public class RequieredCareFragment extends Fragment implements CustomButtonListener {

    View view;
    private List<ReminderForModel> reminderForModels = new ArrayList<>();
    RecyclerView recyclerView;
    FloatingActionButton btnNext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_requiered_care, container, false);

        btnNext = (FloatingActionButton) view.findViewById(R.id.btnNext);
        recyclerView = (RecyclerView) view.findViewById(R.id.rvReminderForId);
        ReminderForModel reminderForModel = new ReminderForModel();
        reminderForModel.setRemindername("Medication");

        reminderForModels.add(reminderForModel);

        ReminderForModel reminderForModel1 = new ReminderForModel();
        reminderForModel1.setRemindername("Vaccianation");

        reminderForModels.add(reminderForModel1);
        ReminderForModel reminderForModel2 = new ReminderForModel();
        reminderForModel2.setRemindername("Nebulization");

        reminderForModels.add(reminderForModel2);
        ReminderForModel reminderForModel3 = new ReminderForModel();
        reminderForModel3.setRemindername("Feeding");

        reminderForModels.add(reminderForModel3);
        ReminderForModel reminderForModel4 = new ReminderForModel();
        reminderForModel4.setRemindername("Consltant Appointment");

        reminderForModels.add(reminderForModel4);

        ReminderForModel reminderForModel5 = new ReminderForModel();
        reminderForModel5.setRemindername("Blood Pressure");

        reminderForModels.add(reminderForModel5);

        ReminderForModel reminderForModel6 = new ReminderForModel();
        reminderForModel6.setRemindername("Body Tempreture");

        reminderForModels.add(reminderForModel6);
        ReminderForModel reminderForModel7 = new ReminderForModel();
        reminderForModel7.setRemindername("Pathological Investigation");

        reminderForModels.add(reminderForModel7);


        ReminderHeaderNormalAdapter healthPointsAdapter = new ReminderHeaderNormalAdapter(getActivity(), reminderForModels, "requiredCare");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(healthPointsAdapter);
        healthPointsAdapter.setCustomListener(this);
        healthPointsAdapter.notifyDataSetChanged();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SavedData.getRequiredCate().equals("")) {
                    ((ClientReminderActivity) getActivity()).openAnyRemainder();
                } else {
                    S.T(getActivity(), "Please Select Required Care");
                }
            }
        });
        return view;
    }

    @Override
    public void onButtonClick(int position, String buttonText) {
        if (buttonText.equalsIgnoreCase("requiredCareClick")) {
            SavedData.saveRequiredCate(reminderForModels.get(position).getRemindername());
            S.E("requiredCare : " + SavedData.getRequiredCate());
        }
    }
}
