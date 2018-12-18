package com.dollop.syzygy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dollop.syzygy.Model.HireYourCaregiverModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.sohel.Const;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sarvesh on 9/4/2017.
 */

public class HireYourCaregiverAdapter extends RecyclerView.Adapter<HireYourCaregiverAdapter.MyViewHolder> {

    HireYourCaregiverAdapter hireYourCaregiverAdapter;
    HireYourCaregiverModel hireYourCaregiverModel;
    List<HireYourCaregiverModel> hireyourlist;


    Context context;


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hireyourcaregiver, parent, false);

        return new MyViewHolder(itemView);

    }

    public HireYourCaregiverAdapter(Context context, List<HireYourCaregiverModel> hireyourlist) {
        this.hireyourlist = hireyourlist;
        this.context = context;
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HireYourCaregiverModel hireYourCaregiverModel = hireyourlist.get(position);

        /*if (hireYourCaregiverModel.getType().equals("caregiver")) {
            holder.id_desti.setVisibility(View.GONE);
            holder.id_source.setVisibility(View.GONE);
            holder.total_amount.setVisibility(View.GONE);
        } else {
            holder.id_startTime.setVisibility(View.GONE);
            holder.id_endtime.setVisibility(View.GONE);
            holder.total_km.setVisibility(View.GONE);
        }*/

        holder.txtname.setText(hireYourCaregiverModel.getHirename());

        holder.total_amount.setText(hireYourCaregiverModel.getTotalTime());
        holder.tvCareGiverId.setText("Hired Service Id : "+hireYourCaregiverModel.getHiredCaregiverId());
        holder.tvTransactionId.setText("Transaction Id : "+hireYourCaregiverModel.getTransaction_id());


        if (hireYourCaregiverModel.getType().equals("caregiver")) {
            holder.id_desti.setVisibility(View.GONE);
            holder.id_source.setVisibility(View.GONE);
            holder.id_startTime.setVisibility(View.VISIBLE);
            holder.id_endtime.setVisibility(View.VISIBLE);

            holder.id_startTime.setText("Start Time : " + hireYourCaregiverModel.getStart_tme());
            holder.id_endtime.setText("End Time : " + hireYourCaregiverModel.getEnd_time());
            holder.total_km.setText("Total Time : " + hireYourCaregiverModel.getTotalkM());
            holder.total_amount.setText("₹" + hireYourCaregiverModel.getTotalAmount());
        } else {
            holder.id_desti.setVisibility(View.VISIBLE);
            holder.id_source.setVisibility(View.VISIBLE);
            holder.id_startTime.setVisibility(View.GONE);
            holder.id_endtime.setVisibility(View.GONE);

            holder.id_desti.setText("Destination : " + hireYourCaregiverModel.getDestianme());
            holder.id_source.setText("Source: " + hireYourCaregiverModel.getSourcename());
            holder.total_km.setText("Total Km : " + hireYourCaregiverModel.getTotalkM());
            holder.total_amount.setText("₹" + hireYourCaregiverModel.getTotalAmount());
        }

        holder.ratingBar.setRating(Float.parseFloat(hireYourCaregiverModel.getHirerating()));

        if (!hireYourCaregiverModel.getHiredesc().equals("null"))
            if (!hireYourCaregiverModel.getHireimage().equals("null")) {
                Picasso.with(context).load(Const.URL.Image_Url + hireYourCaregiverModel.getHireimage()).error(R.drawable.user_profile_pic)
                        .into(holder.hyourimage);
            } else {
                holder.hyourimage.setImageResource(R.drawable.user_profile_pic);
            }

    }

    @Override
    public int getItemCount() {
        return hireyourlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView id_startTime;
        private final TextView id_endtime;
        private final TextView total_amount;
        private final TextView id_source;
        private final TextView id_desti;
        private final TextView total_km;
        private final TextView tvTransactionId;
        private final TextView tvCareGiverId;
        public ImageView hyourimage;
        TextView txtname;
        RatingBar ratingBar;
        LinearLayout linearLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.id_linearlayoutfirst);
            hyourimage = (ImageView) itemView.findViewById(R.id.id_hireyurimage);
            txtname = (TextView) itemView.findViewById(R.id.id_txtname);
            id_startTime = (TextView) itemView.findViewById(R.id.id_startTime);
            id_endtime = (TextView) itemView.findViewById(R.id.id_endtime);
            total_amount = (TextView) itemView.findViewById(R.id.total_amount);
            id_source = (TextView) itemView.findViewById(R.id.id_source);
            id_desti = (TextView) itemView.findViewById(R.id.id_desti);
            total_km = (TextView) itemView.findViewById(R.id.total_km);
            tvCareGiverId = (TextView) itemView.findViewById(R.id.tvCareGiverId);
            tvTransactionId = (TextView) itemView.findViewById(R.id.tvTransactionId);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

        }
    }
}
