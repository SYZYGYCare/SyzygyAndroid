package com.dollop.syzygy.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.syzygy.Model.AddSeniorModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.client.AddSeniorActivity;
import com.dollop.syzygy.sohel.Const;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SeniorListServiceAdpterMain extends RecyclerView.Adapter<SeniorListServiceAdpterMain.MyViewHolder> {
    List<AddSeniorModel> employeeList;
    Activity context;
    String getWhere;


    public SeniorListServiceAdpterMain(Activity context, List<AddSeniorModel> employeeList, String getWhere) {
        this.context = context;
        this.employeeList = employeeList;
        this.getWhere = getWhere;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.senior_select_service_row, parent, false);

        return new MyViewHolder(itemView);
    }
    //Bug Resolved By Yash Verma
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AddSeniorModel cardListObject = employeeList.get(position);
        holder.SeniorNameTv.setText("Name : " + cardListObject.getSeniorName());
        holder.ageNameTv.setText("Age : " + cardListObject.getSeniorAge());
        holder.GenderNameTv.setText("Gender : " + cardListObject.getSeniorGender());
        holder.DescriptionNameTv.setText("Description : " + cardListObject.getSeniorDecription());
        holder.tvRelationShipId.setText("Relationship : " + cardListObject.getRelationShip());
        holder.linearLayoutId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getWhere.equals("Drawer")) {
                    Intent intent = new Intent(context, AddSeniorActivity.class);
                    intent.putExtra("onlyshow", "show");
                    Bundle b = new Bundle();
                    b.putSerializable("show", cardListObject);
                    intent.putExtras(b);
                    context.startActivity(intent);
                } else {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("id", cardListObject.getUser_senior_id());
                    context.setResult(22, returnIntent);
                    context.finish();
                }
            /*  setREs(Activity.RESULT_OK,returnIntent);
                finish();*/
            }
        });

        if (cardListObject.getImage().equals("")) {
            holder.seniorImage.setImageResource(R.drawable.user_profile_pic);

        } else {
            Picasso.with(context).load(Const.URL.Image_Url + cardListObject.getImage()).into(holder.seniorImage);
        }

    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvRelationShipId;
        TextView SeniorNameTv, ageNameTv, GenderNameTv, DescriptionNameTv;
        ImageView seniorImage;
        CardView linearLayoutId;

        public MyViewHolder(View view) {
            super(view);

            SeniorNameTv = (TextView) view.findViewById(R.id.SeniorNameTv);
            ageNameTv = (TextView) view.findViewById(R.id.ageNameTv);
            GenderNameTv = (TextView) view.findViewById(R.id.GenderNameTv);
            seniorImage = (ImageView) view.findViewById(R.id.seniorImage);
            DescriptionNameTv = (TextView) view.findViewById(R.id.DescriptionNameTv);
            tvRelationShipId = (TextView) view.findViewById(R.id.tvRelationShipId);
            linearLayoutId = (CardView) view.findViewById(R.id.card1);
        }

    }


}
