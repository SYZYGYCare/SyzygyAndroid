package com.dollop.syzygy.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dollop.syzygy.Model.CareGiverSpecialization;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.client.ClientSeniorListActivity;
import com.dollop.syzygy.utility.Constants;

import java.util.List;

/**
 * Created by user on 10/12/2017.
 */

public class SpecializationAdapter extends RecyclerView.Adapter<SpecializationAdapter.MyViewHolder> {
    List<CareGiverSpecialization> employeeList;
    Context context;


    public SpecializationAdapter(Context context, List<CareGiverSpecialization> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id_doctor;
        ImageView service_imageview;
        LinearLayout linearSepcialId;


        public MyViewHolder(View view) {
            super(view);

            id_doctor = (TextView) view.findViewById(R.id.id_doctor);
            service_imageview = (ImageView) view.findViewById(R.id.service_imageview);
            linearSepcialId = (LinearLayout) view.findViewById(R.id.linearSepcialId);


        }

    }

    public SpecializationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.specialization, parent, false);

        return new SpecializationAdapter.MyViewHolder(itemView);
    }

    public void onBindViewHolder(SpecializationAdapter.MyViewHolder holder, final int position) {
        final CareGiverSpecialization cardListObject = employeeList.get(position);
        holder.id_doctor.setText(cardListObject.getSpecialization());

        if(Constants.SPECILIZATION_ROLE_DOCTOR){
            holder.service_imageview.setImageResource(R.drawable.ic_caregiver_home);
        }else {
            holder.service_imageview.setImageResource(R.drawable.doctor_other);
        }


        holder.linearSepcialId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
       /* Intent intent=new Intent(context,ClientSeniorListActivity.class);
        context.startActivity(intent);*/

            }
        });


    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }


}

