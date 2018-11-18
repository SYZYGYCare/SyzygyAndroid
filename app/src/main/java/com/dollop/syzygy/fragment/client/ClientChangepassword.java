package com.dollop.syzygy.fragment.client;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.UserAccount;
import com.dollop.syzygy.activity.client.ClientMainActivity;
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
import butterknife.Unbinder;

public class ClientChangepassword extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.currentpassword)
    EditText currentpassword;
    @BindView(R.id.newpassword)
    EditText newpassword;
    @BindView(R.id.conformpassword)
    EditText conformpassword;
    @BindView(R.id.addnumber)
    Button addnumber;
    Unbinder unbinder;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ClientChangepassword() {
    }

    public static ClientChangepassword newInstance(String param1, String param2) {
        ClientChangepassword fragment = new ClientChangepassword();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_changepassword, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((ClientMainActivity) getActivity()).launchFragmentTitle("Change Password");
        addnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if ((TextUtils.isEmpty(currentpassword.getText().toString()))) {
                    currentpassword.setFocusableInTouchMode(true);
                    currentpassword.setError("Please feel current password");
                    return;
                }else   if (TextUtils.isEmpty(newpassword.getText().toString())) {
                    newpassword.setFocusableInTouchMode(true);
                    newpassword.setError("Please feel new password");
                    return;
                }else   if (TextUtils.isEmpty(conformpassword.getText().toString())) {
                    conformpassword.setFocusableInTouchMode(true);
                    conformpassword.setError("Please feel confirm password");
                    return;
                }else if(!(newpassword.getText().toString()==conformpassword.getText().toString())){
                    S.T(getActivity(),"Pleas valid confirm password");
                }else if(currentpassword.getText().toString().length()>=6){
                    currentpassword.setFocusableInTouchMode(true);
                    currentpassword.setError("Please feel min 6 charecter password");
                }else if(newpassword.getText().toString().length()>=6){
                    newpassword.setFocusableInTouchMode(true);
                    newpassword.setError("Please feel min 6 charecter new password");
                }else if(conformpassword.getText().toString().length()>=6){
                    conformpassword.setFocusableInTouchMode(true);
                    conformpassword.setError("Please feel min 6 charecter new password");
                }
                else {
                    ChangePassword();
                }*/

                if (UserAccount.isEmpty(currentpassword, newpassword, conformpassword)) {
                    if (UserAccount.isPasswordValid(currentpassword, newpassword, conformpassword)) {
                        if (!(newpassword.getText().toString().equals(conformpassword.getText().toString()))) {
                            conformpassword.requestFocus();
                            conformpassword.setError("Invalid Confirm Password!");
                        } else {
                            ChangePassword();
                        }
                    } else {
                        UserAccount.EditTextPointer.requestFocus();
                        UserAccount.EditTextPointer.setError("Grater than 6 char!");
                    }
                } else {
                    UserAccount.EditTextPointer.requestFocus();
                    UserAccount.EditTextPointer.setError("This can't be empty!");
                }

            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void ChangePassword() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.CAREGIVER_CHANGE_PASSWORD, 1, getParamEmergency(), new Helper() {
            public void backResponse(String response) {
                try {
                    S.E("Service ka response" + response);
                    JSONObject object1 = new JSONObject(response);
                    if (object1.getString("status").equals("200")) {
                        S.T(getActivity(), "Password Chnage Successfully!");
                        S.I_clear(getActivity(), ClientMainActivity.class, null);
                    } else {
//                        avi.setVisibility(View.GONE);
                        S.T(getActivity(), object1.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getParamEmergency() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("old_password", currentpassword.getText().toString());
        params.put("new_password", newpassword.getText().toString());

        S.E("getService ki request" + params);
        return params;

    }
}
