package com.dollop.syzygy.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.syzygy.Model.CareGiverServiceModel;
import com.dollop.syzygy.R;

import java.util.List;

/**
 * Created by user on 10/12/2017.
 */

public class ClientGetServiceAdapter extends RecyclerView.Adapter<ClientGetServiceAdapter.MyViewHolder> {
    List<CareGiverServiceModel> employeeList;
    Context context;


    public ClientGetServiceAdapter(Context context, List<CareGiverServiceModel> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
    }

    public ClientGetServiceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.getservice, parent, false);

        return new ClientGetServiceAdapter.MyViewHolder(itemView);
    }

    public void onBindViewHolder(ClientGetServiceAdapter.MyViewHolder holder, final int position) {
        final CareGiverServiceModel cardListObject = employeeList.get(position);
        holder.service_Tv_name.setText(cardListObject.getService_name());

        if (cardListObject.getService_name().equals("Doctor")) {

        } else {
            holder.service_imageview.setImageResource(R.drawable.doctor_other);
        }
        /*else  if(cardListObject.getService_name().equals("NURSE")){
            holder.service_imageview.setImageResource(R.drawable.ic_nurse);
        }else  if(cardListObject.getService_name().equals("DENTIST")){
            holder.service_imageview.setImageResource(R.drawable.ic_daitisian);
        }else  if(cardListObject.getService_name().equals("PYSICIAN")){
            holder.service_imageview.setImageResource(R.drawable.ic_physician);
        }else {
            holder.service_imageview.setImageResource(R.drawable.logo_new);
        }*/


//        Picasso.with(context)
//                .load(WebServiceURL.URL.Image_Url + cardListObject.getImage())
//                .into(holder.seniorImage);

    }

    @Override
    public int getItemCount() {
        return employeeList.size();
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


}
