package com.dollop.syzygy.activity.caregiver;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.rilixtech.CountryCodePicker;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sohel on 9/26/2017.
 */

public class CareGiverEnterMobileActivity extends BaseActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.signInBtnNext)
    FloatingActionButton signInBtnNext;
    @BindView(R.id.editMobile)
    EditText editMobile;

    /*  @BindView(R.id.spCountryCodeId)
      Spinner spCountryCodeId;
  */
    String strMobilleNo = "";
    CountryCodePicker ccp;
    private String countryCode="";
    private String mobileNo="";


    @Override
    protected int getContentResId() {
        return R.layout.activity_enter_mobile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initTitleToolbar("Enter Mobile Number");
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
      /*  String[] recourseList=this.getResources().getStringArray(R.array.CountryCodes);
        ArrayAdapter<String> stringArrayAdapter=new ArrayAdapter<String>(CareGiverEnterMobileActivity.this,android.R.layout.simple_dropdown_item_1line,recourseList);
        spCountryCodeId.setAdapter(stringArrayAdapter);*/

        signInBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  S.I(ClientEnterMobileActivity.this, ClientEnterOTPActivity.class, null);*/
                strMobilleNo = editMobile.getText().toString();


                if (strMobilleNo.equals("")) {


                    editMobile.setError("Field can't be empty");
                } else {
                    countryCode=  ccp.getSelectedCountryCode().toString();
                mobileNo=  countryCode+  editMobile.getText().toString();

                 S.E("mobileNo"+mobileNo);
                    GetOtp();
                }

            }
        });
    }


    private void GetOtp() {

        new JSONParser(this).parseVollyStringRequest(Const.URL.GetOtp_Url, 1, getParms(), new Helper() {
            @Override
            public void backResponse(String response) {
                /*
                gifTextView.setVisibility(View.GONE);*/

                try {
                    JSONObject mainobject = new JSONObject(response);
                    Log.e("response", "" + response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");

                    if (message.equals("user already registered")) {
                        Intent intent = new Intent(CareGiverEnterMobileActivity.this, CareGiverEnterPasswordActivity.class);
                        intent.putExtra("contact_no", mobileNo);
                        startActivity(intent);
                        finish();

                    } else if (message.equals("failed")) {
                        S.T(CareGiverEnterMobileActivity.this, "Invalid Mobile No");

                    } else if (message.equals("success")) {

                        Intent intent = new Intent(CareGiverEnterMobileActivity.this, CareGiverEnterOTPActivity.class);
                        intent.putExtra("contact_no", mobileNo);

                        startActivity(intent);
                        finish();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("khghk", "exception" + e);
                }
            }
        });
    }

    private Map<String, String> getParms() {
        HashMap<String, String> postParams = new HashMap<>();
        postParams.put("contact_no",mobileNo);
        return postParams;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}