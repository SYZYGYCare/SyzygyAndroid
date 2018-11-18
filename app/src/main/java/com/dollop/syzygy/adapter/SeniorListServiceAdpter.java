package com.dollop.syzygy.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.dollop.syzygy.Model.AddSeniorModel;
import com.dollop.syzygy.R;

import java.util.List;

public class SeniorListServiceAdpter extends RecyclerView.Adapter<SeniorListServiceAdpter.MyViewHolder> {
    List<AddSeniorModel> employeeList;
    Context context;


    public SeniorListServiceAdpter(Context context, List<AddSeniorModel> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView SeniorNameTv, ageNameTv, GenderNameTv, DescriptionNameTv;
        ImageView seniorImage;
        CardView card;


        public MyViewHolder(View view) {
            super(view);

            SeniorNameTv = (TextView) view.findViewById(R.id.SeniorNameTv);
            ageNameTv = (TextView) view.findViewById(R.id.ageNameTv);
            GenderNameTv = (TextView) view.findViewById(R.id.GenderNameTv);
            seniorImage = (ImageView) view.findViewById(R.id.seniorImage);
            DescriptionNameTv = (TextView) view.findViewById(R.id.DescriptionNameTv);
            card = (CardView) view.findViewById(R.id.card1);
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.senior_select_service_row, parent, false);

        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AddSeniorModel cardListObject = employeeList.get(position);
        holder.SeniorNameTv.setText("Name:" +  cardListObject.getSeniorName());
        holder.ageNameTv.setText("Age:" +  cardListObject.getSeniorAge());
        holder.GenderNameTv.setText("Gender:" + cardListObject.getSeniorGender());
        holder.DescriptionNameTv.setText("Description:" + cardListObject.getSeniorDecription());

//        Picasso.with(context)
//                .load(WebServiceURL.URL.Image_Url + cardListObject.getImage())
//                .into(holder.seniorImage);

    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }


}
