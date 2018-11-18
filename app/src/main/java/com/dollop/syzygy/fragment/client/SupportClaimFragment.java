package com.dollop.syzygy.fragment.client;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dollop.syzygy.Model.HireYourCaregiverModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.UserAccount;
import com.dollop.syzygy.activity.client.ClientMainActivity;
import com.dollop.syzygy.adapter.HireYourCaregiverAdapter;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class SupportClaimFragment extends Fragment {

    Button btnSubmitId;
    EditText etHourReportedId;
    EditText etHourActualId;
    EditText etReasonId;
    EditText etServiceId;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_supprot_claim, container, false);
        btnSubmitId=(Button)view.findViewById(R.id.btnSubmitId);
        etHourReportedId=(EditText)view.findViewById(R.id.etHourReportedId);
        etHourActualId=(EditText)view.findViewById(R.id.etHourActualId);
        etReasonId=(EditText)view.findViewById(R.id.etReasonId);
        etServiceId=(EditText)view.findViewById(R.id.etServiceId);
        btnSubmitId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserAccount.isEmpty(etServiceId,etHourReportedId,etHourActualId,etReasonId)){
                    saveClaim();

                }else{
                    UserAccount.EditTextPointer.setError("Field can't be empty");
                }
            }
        });

        return view;
    }

    private void saveClaim() {

        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.User_claim, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("CheckResponse" + response);
                S.E("CheckResponsePArama" + getParams());
                try {
                    JSONObject mainobject = new JSONObject(response);
                    if(mainobject.getString("status").equals("200")){
                        S.T(getActivity(),mainobject.getString("message"));
                        confirm(mainobject.getString("message"));
                        etServiceId.getText().clear();
                        etHourReportedId.getText().clear();
                        etHourActualId.getText().clear();
                        etReasonId.getText().clear();

                    }else{
                        S.T(getActivity(),mainobject.getString("message"));
                        etServiceId.getText().clear();
                        etHourReportedId.getText().clear();
                        etHourActualId.getText().clear();
                        etReasonId.getText().clear();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("checkMessage" + e);
                }
            }
        });
    }

    public void confirm(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Your Request Send Successfully");
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("caregiver_id", etServiceId.getText().toString());
        params.put("hour_reported", etHourReportedId.getText().toString());
        params.put("hour_actual_work", etHourActualId.getText().toString());
        params.put("reason_discrepancy", etReasonId.getText().toString());

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String dateStr=simpleDateFormat.format(new Date());

        params.put("report_register_date",dateStr);


        return params;
    }

}
