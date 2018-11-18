package com.dollop.syzygy.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dollop.syzygy.Model.ReminderForModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.listeners.CustomButtonListener;
import com.dollop.syzygy.sohel.SavedData;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static com.dollop.syzygy.fragment.client.ClientMainFragment.getDate;

/**
 * Created by arpit on 4/11/17.
 */

public class ReminderHeaderNormalAdapter extends RecyclerView.Adapter<ReminderHeaderNormalAdapter.MyViewHolder> {

    ReminderHeaderNormalAdapter coSubHealthAdapter;
    //HireYourCaregiverModel hireYourCaregiverModel;
    List<ReminderForModel> coSubHealthModelList;
    private CustomButtonListener customButtonListener = null;
    String callingFrom;
    private int selectedPosition = -1;

    Context context;


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_reminder_for_anynew_expand, parent, false);

        return new MyViewHolder(itemView);

    }

    public ReminderHeaderNormalAdapter(Context context, List<ReminderForModel> coSubHealthModelList, String callingFrom) {
        this.coSubHealthModelList = coSubHealthModelList;
        this.context = context;
        this.callingFrom = callingFrom;
    }

    public void setCustomListener(CustomButtonListener customButtonListener) {
        this.customButtonListener = customButtonListener;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ReminderForModel ReminderForModel = coSubHealthModelList.get(position);

        holder.healthCategoryTV.setText(ReminderForModel.getRemindername());

        holder.cBForTipsId.setChecked(selectedPosition == position);

        if (selectedPosition == position) {
            if (callingFrom.equals("requiredCare")) {
                holder.extraLayout.setVisibility(View.VISIBLE);
            }
        } else {
            holder.extraLayout.setVisibility(View.GONE);
        }

        holder.checkOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.checkOB.setChecked(true);
                holder.checkBD.setChecked(false);
                holder.checkSOS.setChecked(false);
                SavedData.saveRequiredCateType("1");
                holder.editHours.setVisibility(View.INVISIBLE);
            }
        });
        holder.checkBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.checkOB.setChecked(false);
                holder.checkBD.setChecked(true);
                holder.checkSOS.setChecked(false);
                SavedData.saveRequiredCateType("2");
                holder.editHours.setVisibility(View.VISIBLE);
            }
        });
        holder.checkSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.checkOB.setChecked(false);
                holder.checkBD.setChecked(false);
                holder.checkSOS.setChecked(true);
                SavedData.saveRequiredCateType("3");
                holder.editHours.setVisibility(View.VISIBLE);
            }
        });

        holder.editHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                SavedData.saveRemainderHour(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.btnSelectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRemainderPopup(holder.textDateTime);
            }
        });

        holder.cBForTipsId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
                clickEvent(position);
            }
        });
    }

    private void addRemainderPopup(final TextView textDateTime) {
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
                SavedData.saveRemainderTime(getDate(time, "HH:mm"));
                SavedData.saveRemainderDate(getDate(time, "yyyy-MM-dd"));
                textDateTime.setText(SavedData.getRemainderDate() + " / " + SavedData.getRemainderTime());
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(dialogView);
        alertDialog.show();
    }

    private void clickEvent(int position) {
        switch (callingFrom) {
            case "remainderFor":
                if (customButtonListener != null) {
                    customButtonListener.onButtonClick(position, "remainderForClick");
                }
                break;
            case "requiredCare":
                if (customButtonListener != null) {
                    customButtonListener.onButtonClick(position, "requiredCareClick");
                }
                break;
            case "anyRemainder":
                if (customButtonListener != null) {
                    customButtonListener.onButtonClick(position, "anyRemainderClick");
                }
                break;
            case "remainderList":
                if (customButtonListener != null) {
                    customButtonListener.onButtonClick(position, "remainderListClick");
                }
                break;
        }
    }


    @Override
    public int getItemCount() {
        return coSubHealthModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView healthCategoryTV;
        CardView cardViewHealth;
        ImageView imageView;
        CheckBox cBForTipsId;
        LinearLayout extraLayout;
        CheckBox checkOB, checkBD, checkSOS;
        EditText editHours;
        TextView textDateTime;
        ImageView btnSelectDateTime;


        public MyViewHolder(View itemView) {
            super(itemView);
            cBForTipsId = (CheckBox) itemView.findViewById(R.id.cBForTipsId);
            healthCategoryTV = (TextView) itemView.findViewById(R.id.healthCategoryTV);
            cardViewHealth = (CardView) itemView.findViewById(R.id.cardViewHealth);
            extraLayout = (LinearLayout) itemView.findViewById(R.id.extraLayout);

            checkOB = (CheckBox) itemView.findViewById(R.id.checkOB);
            checkBD = (CheckBox) itemView.findViewById(R.id.checkBD);
            checkSOS = (CheckBox) itemView.findViewById(R.id.checkSOS);

            editHours = (EditText) itemView.findViewById(R.id.editHours);

            textDateTime = (TextView) itemView.findViewById(R.id.textDateTime);

            btnSelectDateTime = (ImageView) itemView.findViewById(R.id.btnSelectDateTime);

        }
    }
}