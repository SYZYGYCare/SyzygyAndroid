package com.dollop.syzygy.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.syzygy.Model.HealthTipsModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.ChatActivity;
import com.dollop.syzygy.activity.client.HealthPointsActivity;
import com.dollop.syzygy.sohel.S;

import java.util.List;

/**
 * Created by arpit on 4/11/17.
 */

public class HealthPointsAdapter extends RecyclerView.Adapter<HealthPointsAdapter.MyViewHolder> {
    HealthPointsAdapter healthPointsAdapter;
    //HireYourCaregiverModel hireYourCaregiverModel;
    HealthTipsModel healthTipsModel;
    List<HealthTipsModel> healthPointsModelList;
    private ClickListnerCallback clickListnerCallback;
    Context context;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_health_tips_points, parent, false);
        return new MyViewHolder(itemView);
    }

    public HealthPointsAdapter(Context context, List<HealthTipsModel> healthPointsModelList, ClickListnerCallback clickListnerCallback) {
        this.healthPointsModelList = healthPointsModelList;
        this.clickListnerCallback = clickListnerCallback;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        healthTipsModel = healthPointsModelList.get(position);

        holder.healthCategoryTV.setText(Html.fromHtml(healthTipsModel.getCategoryName()));

        if (healthTipsModel.isChecked()) {
            holder.checkBoxSelectUnSelect.setChecked(true);
        } else {
            holder.checkBoxSelectUnSelect.setChecked(false);
        }

        holder.checkBoxSelectUnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.checkBoxSelectUnSelect.isChecked()) {
                    healthTipsModel.setChecked(true);
                    clickListnerCallback.onClick(position,true);
                } else {
                    healthTipsModel.setChecked(false);
                    clickListnerCallback.onClick(position,false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return healthPointsModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView healthCategoryTV;
        CardView cardViewHealth;
        CheckBox checkBoxSelectUnSelect;


        public MyViewHolder(View itemView) {
            super(itemView);
            healthCategoryTV = (TextView) itemView.findViewById(R.id.healthCategoryTV);
            cardViewHealth = (CardView) itemView.findViewById(R.id.cardViewHealth);
            checkBoxSelectUnSelect = (CheckBox) itemView.findViewById(R.id.checkBoxSelectUnSelect);

        }
    }

    public interface ClickListnerCallback {
        void onClick(int position,boolean isChecked);
    }
}
