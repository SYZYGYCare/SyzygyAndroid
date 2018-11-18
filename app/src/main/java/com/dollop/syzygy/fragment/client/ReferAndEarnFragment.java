package com.dollop.syzygy.fragment.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.client.ClientMainActivity;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.sohel.UserAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReferAndEarnFragment extends Fragment {
    private Button referBtn;
    private TextView textReferCode, textWalletBalance;
    private EditText editCode;
    private Button btnGo;

    /* @BindView(R.id.referBtn)
         Button referBtn;
         @BindView(R.id.activity_refer)
         RelativeLayout activity_refer;
     */
    public ReferAndEarnFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_refer, container, false);

        referBtn = (Button) view.findViewById(R.id.referBtn);
        textReferCode = (TextView) view.findViewById(R.id.textReferCode);
        textWalletBalance = (TextView) view.findViewById(R.id.textWalletBalance);
        editCode = (EditText) view.findViewById(R.id.editCode);
        btnGo = (Button) view.findViewById(R.id.btnGo);

        ((ClientMainActivity) getActivity()).launchFragmentTitle("Refer & Earn");

        getReferCode();
        getWalletBalance();

        referBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                S.share(getActivity(), "", "Hey! Join me on " + getString(R.string.app_name) + " Use my code " + textReferCode.getText().toString() + " and earn signup bonus.\n To Download : " + "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
            }
        });
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserAccount.isEmpty(editCode)) {
                    useReferCode();
                } else {
                    UserAccount.EditTextPointer.requestFocus();
                    UserAccount.EditTextPointer.setError("This can't be empty!");
                }
            }
        });
        return view;
    }

    private void getWalletBalance() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GET_WALLET_BALANCE, 1, getParamsForWallet(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("refer code : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        editCode.setText("");
                        textWalletBalance.setText(getString(R.string.rupyee_symbol) + " " + jsonObject.getString("wallet"));
                    } else if (jsonObject.getString("status").equals("1")) {
                        S.T(getActivity(), jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParamsForWallet() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());

        return params;
    }

    private void useReferCode() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.USER_REFER_CODE, 1, getParamsForApply(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("refer code : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        editCode.setText("");
                        textWalletBalance.setText(getString(R.string.rupyee_symbol) + " " + jsonObject.getString("wallet"));
                    } else if (jsonObject.getString("status").equals("1")) {
                        S.T(getActivity(), jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParamsForApply() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("refferal_code", editCode.getText().toString());
        return params;
    }

    private void getReferCode() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.GET_REFER_CODE, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("refer code : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        textReferCode.setText(jsonObject.getString("refer_code"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        return params;
    }
}
