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
import com.dollop.syzygy.activity.client.HealthPointsActivity;
import com.dollop.syzygy.sohel.Const;
import com.squareup.picasso.Picasso;
import java.util.List;



public class CoSubHealthAdapter extends RecyclerView.Adapter<CoSubHealthAdapter.MyViewHolder> {

    CoSubHealthAdapter coSubHealthAdapter;
    List<HealthTipsModel> coSubHealthModelList;
    Context context;

    public CoSubHealthAdapter(Context context, List<HealthTipsModel> coSubHealthModelList, String imageForPoint) {
        this.coSubHealthModelList = coSubHealthModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_health_tips_cosubcat, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final HealthTipsModel healthTipsModel = coSubHealthModelList.get(position);
        holder.healthCategoryTV.setText(healthTipsModel.getCategoryName());
        if (healthTipsModel.getImage() != null) {
            Picasso.with(context)
                    .load(Const.URL.Image_Url_health + healthTipsModel.getImage())
                    .into(holder.imageLike);
        } else {
            holder.imageLike.setImageResource(R.drawable.health_image);
        }


        holder.cardViewHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HealthPointsActivity.class);
                Bundle b = new Bundle();
                intent.putExtra("imageforpoint", healthTipsModel.getImage());
                b.putSerializable("AddSaleitems", coSubHealthModelList.get(position));
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    }




    @Override
    public int getItemCount() {
        return coSubHealthModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView healthCategoryTV;
        CardView cardViewHealth;
        ImageView imageLike;


        public MyViewHolder(View itemView) {
            super(itemView);
            healthCategoryTV = (TextView) itemView.findViewById(R.id.healthCategoryTV);
            cardViewHealth = (CardView) itemView.findViewById(R.id.cardViewHealth);
            imageLike = (ImageView) itemView.findViewById(R.id.image);

        }
    }
}


