package com.dollop.syzygy.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.syzygy.Model.EmerGencyClientModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.fragment.client.ClientEmergencyContact;
import com.dollop.syzygy.listeners.CustomButtonListener;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arpit on 4/11/17.
 */

public class EmergencyAdapter extends RecyclerView.Adapter<EmergencyAdapter.MyViewHolder> {

    EmergencyAdapter coSubHealthAdapter;
    //HireYourCaregiverModel hireYourCaregiverModel;
    EmerGencyClientModel EmerGencyClientModel;
    List<EmerGencyClientModel> coSubHealthModelList;
    Context context;
    private CustomButtonListener customButtonListener = null;


    public EmergencyAdapter(Context context, List<EmerGencyClientModel> coSubHealthModelList) {
        this.coSubHealthModelList = coSubHealthModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_emergencyno, parent, false);

        return new MyViewHolder(itemView);

    }

    public void setCustomListener(CustomButtonListener customButtonListener) {
        this.customButtonListener = customButtonListener;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        EmerGencyClientModel = coSubHealthModelList.get(position);
        holder.emergencyContactNo.setText(EmerGencyClientModel.getEmergency_no1());
        holder.ivCallId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + EmerGencyClientModel.getEmergency_no1()));
                context.startActivity(intent);
            }
        });
        holder.ivDeleteId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                S.E("AnicheckThis" + EmerGencyClientModel.getcontactId());
                EmergencyDeleteContactClient(coSubHealthModelList.get(position).getcontactId());

            }
        });
    }


    @Override
    public int getItemCount() {
        return coSubHealthModelList.size();
    }

    private void EmergencyDeleteContactClient(String emergencyId) {
        S.E("prams for delete - " + getParams2(emergencyId));
        new JSONParser(context).parseVollyStringRequest(Const.URL.DeleteEmerGencyClientContact_Url, 1, getParams2(emergencyId), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("Delete Emergency" + response);
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        customButtonListener.onButtonClick(0, "Delete");
                    } else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("Add exception" + e);
                }
            }
        });
    }

    private Map<String, String> getParams2(String emergencyId) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("emergency_no_id", emergencyId);


        return params;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView emergencyContactNo;
        ImageView ivCallId, ivDeleteId;

        public MyViewHolder(View itemView) {
            super(itemView);
            emergencyContactNo = (TextView) itemView.findViewById(R.id.emergencyContactNo);
            ivCallId = (ImageView) itemView.findViewById(R.id.ivCallId);
            ivDeleteId = (ImageView) itemView.findViewById(R.id.ivDeleteId);

        }
    }

}