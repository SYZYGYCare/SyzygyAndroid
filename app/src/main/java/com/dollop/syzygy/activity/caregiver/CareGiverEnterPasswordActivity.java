package com.dollop.syzygy.activity.caregiver;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.activity.UserAccount;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sohel on 9/26/2017.
 */

public class CareGiverEnterPasswordActivity extends BaseActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.signInBtnNext)
    FloatingActionButton signInBtnNext;
    @BindView(R.id.editPassword)
    EditText editPassword;
    String strPassword, mobileno;
    @BindView(R.id.tvForgotPasswordId)
    TextView forgetpasswordTv;

    @BindView(R.id.show_pass)
    ImageView show_pass;

    boolean show_password = false;

    @Override
    protected int getContentResId() {
        return R.layout.activity_enter_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolbarWithBackButton("Enter Password");
        mobileno = getIntent().getStringExtra("contact_no");

        signInBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strPassword = editPassword.getText().toString();
                if (UserAccount.isEmpty(editPassword)) {
                    if (UserAccount.isPasswordValid(editPassword)) {
                        logInCaregiver();
                    } else {
                        UserAccount.EditTextPointer.requestFocus();
                        UserAccount.EditTextPointer.setError("Must be greater than 6 char!");
                    }
                } else {
                    UserAccount.EditTextPointer.requestFocus();
                    UserAccount.EditTextPointer.setError("This can't be empty!");
                }

            }
        });
        forgetpasswordTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CareGiverEnterPasswordActivity.this, CaregiverForgotPasswordActivity.class);
                intent.putExtra("mobileno", mobileno);
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        show_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!show_password) {
                    show_password = true;
                    show_pass.setImageResource(R.drawable.view_pass);
                    editPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    show_password = false;
                    show_pass.setImageResource(R.drawable.hide_pass);
                    editPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

            }
        });


    }

    private void logInCaregiver()
    {
     /*   gifTextView.setVisibility(View.VISIBLE);*/
        new JSONParser(this).parseVollyStringRequest(Const.URL.Login_Url, 1, getParms1(), new Helper() {

            @Override
            public void backResponse(String response) {
               /* gifTextView.setVisibility(View.GONE);*/
                S.E("CheckcaregiverLogin" + response);
                S.E("CheckParams" + getParms1());

                try {

                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");

                    if (status == 200) {
                        JSONObject ClientToken = mainobject.getJSONObject("Token");
                        String clienttocken = ClientToken.getString("token");

                        String caregiver_type = ClientToken.getString("caregiver_type");
                        String type = ClientToken.getString("type");
                        SavedData.saveTocken(clienttocken);
                        SavedData.saveTockenUserType(type);
                        SavedData.saveTockenUserType(type);
                        SavedData.saveType(caregiver_type);
                        Toast.makeText(CareGiverEnterPasswordActivity.this, mainobject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CareGiverEnterPasswordActivity.this, CareGiverMainActivity.class);
                        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();

                    } else {
                        Toast.makeText(CareGiverEnterPasswordActivity.this, mainobject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParms1() {
        HashMap<String, String> params = new HashMap<>();
        params.put("authority_id", "2");
        params.put("user_name", mobileno);
        params.put("password", strPassword);
        params.put("device_type", "1");

        return params;
    }
}