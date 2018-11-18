package com.dollop.syzygy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.dollop.syzygy.Model.GetRemainderModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.listeners.CustomButtonListener;
import java.util.List;


public class GetRemainderAdpter extends RecyclerView.Adapter<GetRemainderAdpter.MyViewHolder> {
    List<GetRemainderModel> employeeList;
    Context context;
    private CustomButtonListener customButtonListener;

    public GetRemainderAdpter(Context context, List<GetRemainderModel> employeeList) {
        this.context = context;
        this.employeeList = employeeList;
    }

    public void setCustomListener(CustomButtonListener customButtonListener) {
        this.customButtonListener = customButtonListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textTime, textRemainderFor, textRequiredCare, textViewDateAndTime;
        ImageView btnCross;

        public MyViewHolder(View view) {
            super(view);
            textTime = (TextView) view.findViewById(R.id.textTime);
            textRemainderFor = (TextView) view.findViewById(R.id.textRemainderFor);
            textRequiredCare = (TextView) view.findViewById(R.id.textRequiredCare);
            textViewDateAndTime = (TextView) view.findViewById(R.id.textViewDateAndTime);
            btnCross = (ImageView) view.findViewById(R.id.btnCross);
        }

    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_get_reminder, parent, false);
        return new MyViewHolder(itemView);
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final GetRemainderModel cardListObject = employeeList.get(position);
        holder.textTime.setText(cardListObject.getTime());
        holder.textRemainderFor.setText(cardListObject.getReminder_for());
        holder.textRequiredCare.setText(cardListObject.getRequired_care());
        holder.textViewDateAndTime.setText(cardListObject.getStart_date()+"/"+cardListObject.getTime());

        holder.btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (customButtonListener != null) {
                    customButtonListener.onButtonClick(position, "deleteClick");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }


}
