package com.dollop.syzygy.activity.client;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.dollop.syzygy.activity.caregiver.CareGiverEnterPasswordActivity;
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

public class ClientEnterPasswordActivity extends BaseActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolBar;
    @BindView(R.id.signInBtnNext)
    FloatingActionButton signInBtnNext;

    @BindView(R.id.editPassword)
    EditText editPassword;

    @BindView(R.id.show_pass)
    ImageView show_pass;

    @BindView(R.id.tvForgotPasswordId)
    TextView tvForgotPasswordId;

    String strPassword, mobileno;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    boolean show_password = false;
    @Override
    protected int getContentResId() {
        return R.layout.activity_enter_password;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setToolbarWithBackButton("Enter User Password");
        mobileno = getIntent().getStringExtra("contact_no");
        sharedPreferences = getSharedPreferences("LoginCheck", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        tvForgotPasswordId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientEnterPasswordActivity.this, ClientForgotPasswordActivity.class);

                intent.putExtra("mobileno", mobileno);
                startActivity(intent);
            }
        });
        signInBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strPassword = editPassword.getText().toString();
                if (UserAccount.isEmpty(editPassword)) {
                    if (UserAccount.isPasswordValid(editPassword)) {
                        logInClinet();
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

    private void logInClinet() {
     /*   gifTextView.setVisibility(View.VISIBLE);*/
        new JSONParser(this).parseVollyStringRequest(Const.URL.Login_Url, 1, getParms1(), new Helper() {

            @Override
            public void backResponse(String response) {
               /* gifTextView.setVisibility(View.GONE);*/
                S.E("Checked by Ani" + response);

                try {

                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");

                    if (status == 200) {
                        JSONObject ClientToken = mainobject.getJSONObject("Token");
                        String clienttocken = ClientToken.getString("token");

                        String type = ClientToken.getString("type");
                        SavedData.saveTocken(clienttocken);
                        SavedData.saveTockenUserType(type);
                        SavedData.saveUserPhone(mobileno);
                        editor.putString("checklogin", "login");
                        editor.commit();
                        Toast.makeText(ClientEnterPasswordActivity.this, mainobject.getString("message"), Toast.LENGTH_SHORT).show();
                        S.I_clear(ClientEnterPasswordActivity.this, ClientMainActivity.class, null);

                    } else {
                        Toast.makeText(ClientEnterPasswordActivity.this, mainobject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("exception Checked By ani" + e);
                }
            }
        });
    }

    private Map<String, String> getParms1() {

        HashMap<String, String> params = new HashMap<>();
        params.put("authority_id", "1");
        params.put("user_name", mobileno);
        params.put("password", strPassword);
        params.put("device_type", "1");

        return params;
    }
}