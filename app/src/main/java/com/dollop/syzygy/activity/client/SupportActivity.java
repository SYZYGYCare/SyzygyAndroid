package com.dollop.syzygy.activity.client;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.activity.UserAccount;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.SavedData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class SupportActivity extends BaseActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.supportReportHrsET)
    EditText supportReportHrsET;
    @BindView(R.id.supportActualHrsET)
    EditText supportActualHrsET;
    @BindView(R.id.supportReasonET)
    EditText supportReasonET;
    @BindView(R.id.supportSubmitBtn)
    Button supportSubmitBtn;
    private String token;
    private String caregiverId,regDate;

    @Override
    protected int getContentResId() {
        return R.layout.activity_support;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (UserAccount.isEmpty(supportReasonET))
        {
            getClaimData();
        }
        else {
            UserAccount.EditTextPointer.requestFocus();
            UserAccount.EditTextPointer.setError("Enter Your Reason!");
        }
    }

    private void getClaimData() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.GET_CLAIM, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject mainobject = new JSONObject(response);
                    Log.e("response", "response" + response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                 /*   String token = mainobject.getString("token");*/
                    if (status == 200) {
/*
                        Toast.makeText(SupportDetailActivity.this, "Your claim request has been submitted", Toast.LENGTH_SHORT).show();
*/

                      //  S.I_clear(SupportDetailActivity.this, ClientEnterMobileActivity.class, null);

                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                supportReportHrsET.setText("");
                supportActualHrsET.setText("");
                supportReasonET.setText("");
            }

        });
    }

    private Map<String, String> getParams() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("token", SavedData.gettocken_id());

        postParams.put("caregiver_id", caregiverId);
        postParams.put("hour_reported", supportReportHrsET.getText().toString());
        postParams.put("hour_actual_work", supportActualHrsET.getText().toString());
        postParams.put("reason_discrepancy", supportReasonET.getText().toString());
        postParams.put("report_register_date", regDate);
         return postParams;    }
}
