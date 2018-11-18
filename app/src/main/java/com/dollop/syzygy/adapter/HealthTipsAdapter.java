package com.dollop.syzygy.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.syzygy.Model.HealthTipsModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.client.HealthTipsSubActivity;
import com.dollop.syzygy.sohel.Const;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by arpit on 4/11/17.
 */

public class HealthTipsAdapter extends RecyclerView.Adapter<HealthTipsAdapter.MyViewHolder> {

    HealthTipsAdapter healthTipsAdapter;
    //HireYourCaregiverModel hireYourCaregiverModel;
    HealthTipsModel healthTipsModel;
    List<HealthTipsModel> healthTipsModelList;


    Context context;


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_health_tips, parent, false);

        return new MyViewHolder(itemView);

    }

    public HealthTipsAdapter(Context context, List<HealthTipsModel> healthTipsModelList) {
        this.healthTipsModelList = healthTipsModelList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        healthTipsModel = healthTipsModelList.get(position);
        holder.healthCategoryTV.setText(healthTipsModel.getCategoryName());

        if (healthTipsModel.getImage() != null) {
            Picasso.with(context)
                    .load(Const.URL.Image_Url_health + healthTipsModel.getImage())
                    .into(holder.image);
        }

        holder.cardViewHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HealthTipsSubActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("AddSaleitems", healthTipsModelList.get(position));
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return healthTipsModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView healthCategoryTV;
        CardView cardViewHealth;
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            healthCategoryTV = (TextView) itemView.findViewById(R.id.healthCategoryTV);
            cardViewHealth = (CardView) itemView.findViewById(R.id.cardViewHealth);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }
}
