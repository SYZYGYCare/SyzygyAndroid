package com.dollop.syzygy.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.syzygy.Model.CareGiverSpecialization;
import com.dollop.syzygy.R;

import java.util.List;

/**
 * Created by user on 10/12/2017.
 */

public class AmbulanceTypeAdapter extends RecyclerView.Adapter<AmbulanceTypeAdapter.MyViewHolder> {
    List<CareGiverSpecialization> employeeList;
    Context context;


    public AmbulanceTypeAdapter(Context context, List<CareGiverSpecialization> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView service_Tv_name;
        ImageView service_imageview;
        CardView card;


        public MyViewHolder(View view) {
            super(view);

            service_Tv_name = (TextView) view.findViewById(R.id.service_Tv_name);
            service_imageview = (ImageView) view.findViewById(R.id.service_imageview);

        }

    }

    public AmbulanceTypeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.getservice, parent, false);

        return new AmbulanceTypeAdapter.MyViewHolder(itemView);
    }

    public void onBindViewHolder(AmbulanceTypeAdapter.MyViewHolder holder, final int position) {
        final CareGiverSpecialization cardListObject = employeeList.get(position);
        holder.service_Tv_name.setText(cardListObject.getSpecialization());

        if(cardListObject.getSpecialization().equals("LARGE")){
            holder.service_imageview.setImageResource(R.drawable.ic_ambulance_home);
        }else  if(cardListObject.getSpecialization().equals("MINI")){
            holder.service_imageview.setImageResource(R.drawable.ic_ambulance_home);
        }else {
            holder.service_imageview.setImageResource(R.drawable.ic_ambulance_home);
        }


//        Picasso.with(context)
//                .load(WebServiceURL.URL.Image_Url + cardListObject.getImage())
//                .into(holder.seniorImage);

    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }



}

