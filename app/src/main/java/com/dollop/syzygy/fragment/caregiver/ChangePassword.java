package com.dollop.syzygy.fragment.caregiver;

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
import com.dollop.syzygy.activity.caregiver.CareGiverMainActivity;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangePassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePassword extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
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

    public ChangePassword() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePassword.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePassword newInstance(String param1, String param2) {
        ChangePassword fragment = new ChangePassword();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((CareGiverMainActivity) getActivity()).launchFragmentTitle("Change Password");
        addnumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if ((TextUtils.isEmpty(currentpassword.getText().toString()))) {
                    currentpassword.setFocusableInTouchMode(true);
                    currentpassword.setError("Please fill current password");
                    return;
                } else if (TextUtils.isEmpty(newpassword.getText().toString())) {
                    newpassword.setFocusableInTouchMode(true);
                    newpassword.setError("Please fill new password");
                    return;
                } else if (TextUtils.isEmpty(conformpassword.getText().toString())) {
                    conformpassword.setFocusableInTouchMode(true);
                    conformpassword.setError("Please fill confirm password");
                    return;
                } else if (!(newpassword.getText().toString().equals(conformpassword.getText().toString()))) {
                    S.T(getActivity(), "Pleas valid confirm password");
                } else if (currentpassword.getText().toString().length() < 6) {
                    currentpassword.setFocusableInTouchMode(true);
                    currentpassword.setError("Please fill min 6 charecter password");
                } else if (newpassword.getText().toString().length() < 6) {
                    newpassword.setFocusableInTouchMode(true);
                    newpassword.setError("Please fill min 6 charecter new password");
                } else if (conformpassword.getText().toString().length() < 6) {
                    conformpassword.setFocusableInTouchMode(true);
                    conformpassword.setError("Please fill min 6 charecter new password");
                } else {
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

    private void ChangePassword() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.CAREGIVER_CHANGE_PASSWORD, 1, getParamEmergency(), new Helper() {
            public void backResponse(String response) {
                try {
                    S.E("Service ka response" + response);
                    JSONObject object1 = new JSONObject(response);
                    if (object1.getString("status").equals("200")) {
                        S.T(getActivity(), "Password Chnage Successfully!");
                        S.I_clear(getActivity(), CareGiverMainActivity.class, null);
                    } else {
/*                        message*/
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
