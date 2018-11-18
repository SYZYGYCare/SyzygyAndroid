package com.dollop.syzygy.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dollop.syzygy.Model.HealthTipsModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.client.HealthPointsActivity;
import com.dollop.syzygy.listeners.CustomButtonListener;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.dollop.syzygy.fragment.client.ClientMainFragment.getDate;

/**
 * Created by arpit on 4/11/17.
 */

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.MyViewHolder> {

    CheckListAdapter coSubHealthAdapter;
    //HireYourCaregiverModel hireYourCaregiverModel;
    HealthTipsModel healthTipsModel;
    List<HealthTipsModel> coSubHealthModelList;
    private CustomButtonListener customButtonListener = null;

    Context context;


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_health_tips_cosubcat, parent, false);

        return new MyViewHolder(itemView);

    }

    public CheckListAdapter(Context context, List<HealthTipsModel> coSubHealthModelList) {
        this.coSubHealthModelList = coSubHealthModelList;
        this.context = context;
    }

    public void setCustomListener(CustomButtonListener customButtonListener) {
        this.customButtonListener = customButtonListener;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        healthTipsModel = coSubHealthModelList.get(position);
        holder.healthCategoryTV.setText(healthTipsModel.getCategoryName());

        holder.imageRemainder.setVisibility(View.GONE);

      /*  if (healthTipsModel.getStatus().equals("1")) {
            S.E("if working");
            holder.imageDisLike.setVisibility(View.VISIBLE);
            holder.imageLike.setVisibility(View.GONE);
        } else {
            S.E("else working");
            holder.imageDisLike.setVisibility(View.GONE);
            holder.imageLike.setVisibility(View.VISIBLE);
        }*/

        holder.cardViewHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HealthPointsActivity.class);
                Bundle b = new Bundle();
                b.putSerializable("AddSaleitems", coSubHealthModelList.get(position));
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });
    /*    holder.imageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customButtonListener != null) {
                    customButtonListener.onButtonClick(position, "heatClick");
                }
            }
        });
        holder.imageDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customButtonListener != null) {
                    customButtonListener.onButtonClick(position, "heatClick");
                }
            }
        });*/
        holder.imageRemainder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRemainderPopup();
            }
        });
    }

    private void addRemainderPopup() {
        final View dialogView = View.inflate(context, R.layout.dialog_date_time, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);
                TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);

                Calendar calendar = new GregorianCalendar(datePicker.getYear(),
                        datePicker.getMonth(),
                        datePicker.getDayOfMonth(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());

                long time = calendar.getTimeInMillis();
                String timeStr = getDate(time, "HH:mm");
                String dateStr = getDate(time, "yyyy-MM-dd");
                schedualRemainder(dateStr, timeStr);
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    private void schedualRemainder(String dateStr, String timeStr) {
        new JSONParser(context).parseVollyStringRequest(Const.URL.ADD_REMAINDER, 1, getRemainderPrams(dateStr, timeStr, healthTipsModel.getCategoryId(), healthTipsModel.getChecklist_id()), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("remainder : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        S.T(context, "Reminder Added Successfully!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getRemainderPrams(String dateStr, String timeStr, String position, String checkListId) {
        HashMap<String, String> prams = new HashMap<>();
        prams.put("token", SavedData.gettocken_id());
        prams.put("co_sub_cat_id", position);
        prams.put("check_list_id", checkListId);
        prams.put("start_date", dateStr);
        prams.put("time", timeStr);
        return prams;
    }

    @Override
    public int getItemCount() {
        return coSubHealthModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView healthCategoryTV;
        CardView cardViewHealth;
        ImageView imageLike;
        ImageView imageDisLike;
        ImageView imageRemainder;

        public MyViewHolder(View itemView) {
            super(itemView);
            healthCategoryTV = (TextView) itemView.findViewById(R.id.healthCategoryTV);
            cardViewHealth = (CardView) itemView.findViewById(R.id.cardViewHealth);
         /*   imageLike = (ImageView) itemView.findViewById(R.id.imageLike);
            imageDisLike = (ImageView) itemView.findViewById(R.id.imageDisLike);*/
            imageRemainder = (ImageView) itemView.findViewById(R.id.imageRemainder);
        }
    }
}