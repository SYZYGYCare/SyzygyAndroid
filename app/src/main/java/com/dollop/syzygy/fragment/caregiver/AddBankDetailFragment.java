package com.dollop.syzygy.fragment.caregiver;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.UserAccount;
import com.dollop.syzygy.activity.caregiver.CareGiverMainActivity;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 11/15/2017.
 */

public class AddBankDetailFragment extends Fragment {
    View view;
    EditText etNameId;
    private EditText etIFCId;
    private EditText etAccId;
    private Button btnSubmitId;
    TextView accountHolderNameId;
    TextView accountIfcCodeID;
    TextView accountNoId;
    ImageView imageView;
    LinearLayout enterDetailId;
    CardView cardViewHealth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_bank_detail, null);
        etNameId = (EditText) view.findViewById(R.id.etNameId);
        etIFCId = (EditText) view.findViewById(R.id.etIFCId);
        cardViewHealth = (CardView) view.findViewById(R.id.cardViewHealth);

        etAccId = (EditText) view.findViewById(R.id.etAccId);
        btnSubmitId = (Button) view.findViewById(R.id.btnSubmitId);
        accountHolderNameId = (TextView) view.findViewById(R.id.accountHolderNameId);
        accountNoId = (TextView) view.findViewById(R.id.accountNoId);
        accountIfcCodeID = (TextView) view.findViewById(R.id.accountIfcCodeID);
        imageView = (ImageView) view.findViewById(R.id.editId);
        enterDetailId=(LinearLayout)view.findViewById(R.id.enterDetailId);

        accountHolderNameId.setText("Account Holder name:"+SavedData.getAccountnoholder());
        accountIfcCodeID.setText("IFC Code:"+SavedData.getIfccode());
        accountNoId.setText("Account No:"+SavedData.getACCOUNTNo());


        etNameId.setText(SavedData.getAccountnoholder());
        etIFCId.setText(SavedData.getIfccode());
        etAccId.setText(SavedData.getACCOUNTNo());

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterDetailId.setVisibility(View.VISIBLE);
                cardViewHealth.setVisibility(View.GONE);
            }
        });

        GetProfile();
        ((CareGiverMainActivity) getActivity()).launchFragmentTitle("Account Detail");
        btnSubmitId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserAccount.isEmpty(etNameId, etIFCId, etAccId)) {
                    addBankProfile();

                } else {
                    UserAccount.EditTextPointer.requestFocus();
                    UserAccount.EditTextPointer.setError("This can't be empty!");


                }

            }
        });

        return view;
    }

    private void GetProfile() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GET_PROFILE, 1, getParms5(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    Log.e("payment", "response" + response);
                    Log.e("Params", "getParam" + getParms());

                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");


                    if (status == 200) {

                        JSONObject jsonObject = mainobject.getJSONObject("data");

                        String bank_account_no = jsonObject.getString("bank_account_no");
                        String bank_ifsc_code = jsonObject.getString("bank_ifsc_code");
                        String ac_holder_namestr = jsonObject.getString("ac_holder_name");



                        if (bank_account_no.equals("null") || bank_ifsc_code.equals("null") || ac_holder_namestr.equals("null")) {

                            enterDetailId.setVisibility(View.VISIBLE);
                            cardViewHealth.setVisibility(View.GONE);

                        } else {

                            SavedData.saveAccountHolderName(ac_holder_namestr);
                            SavedData.saveIFCCODE(bank_ifsc_code);
                            SavedData.saveAccountNo(bank_account_no);
                            cardViewHealth.setVisibility(View.VISIBLE);
                            enterDetailId.setVisibility(View.GONE);


                            accountHolderNameId.setText(SavedData.getAccountnoholder());
                            accountIfcCodeID.setText(SavedData.getIfccode());
                            accountNoId.setText(SavedData.getACCOUNTNo());


                            etNameId.setText(SavedData.getAccountnoholder());
                            etIFCId.setText(SavedData.getIfccode());
                            etAccId.setText(SavedData.getACCOUNTNo());
                        }


                    } else {


                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("error", "++++++--error--++++" + e);
                }
            }
        });
    }

    private Map<String, String> getParms5() {

        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("authority_id", "2");

        return params;
    }

    private void addBankProfile() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.UPDATE_PROFILE, 1, getParms(), new Helper() {

            @Override
            public void backResponse(String response) {
                try {
                    Log.e("payment", "response" + response);
                    Log.e("Params", "getParam" + getParms());

                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");


                    if (status == 200) {

                        GetProfile();
                        S.T(getActivity(), message);

                    } else {
//                        Toast.makeText(TrustBudgesActivity.this, "Sorry Id And Password Not Match..", Toast.LENGTH_SHORT).show();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("error", "++++++--error--++++" + e);
                }
            }
        });
    }


    private Map<String, String> getParms() {

        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("authority_id", "2");
        params.put("ac_holder_name", etNameId.getText().toString());
        params.put("bank_account_no", etAccId.getText().toString());
        params.put("bank_ifsc_code", etIFCId.getText().toString());


        return params;
    }

}
