package com.dollop.syzygy.fragment.caregiver;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dollop.syzygy.Model.HireYourCaregiverModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.caregiver.CareGiverMainActivity;
import com.dollop.syzygy.adapter.HireYourCaregiverAdapter;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CareGiverYourHiresFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CareGiverYourHiresFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @BindView(R.id.id_recyclerviewyourhire)
    RecyclerView idRecyclerviewyourhire;
    @BindView(R.id.activity_your_hire)
    LinearLayout activityYourHire;
    Unbinder unbinder;
    String Token;
    HireYourCaregiverAdapter hireYourCaregiverAdapter;
    HireYourCaregiverModel hireYourCaregiverModel;
    List<HireYourCaregiverModel> hireyourlist = new ArrayList<HireYourCaregiverModel>();
    @BindView(R.id.remainingAmount)
    TextView remainingAmount;

    @BindView(R.id.pay_to_admin)
    TextView pay_to_admin;

    @BindView(R.id.earned_amount)
    TextView earned_amount;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CareGiverYourHiresFragment() {
    }

    public static CareGiverYourHiresFragment newInstance(String param1, String param2) {
        CareGiverYourHiresFragment fragment = new CareGiverYourHiresFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your__hires_caregiver, container, false);
        unbinder = ButterKnife.bind(this, view);
        Token = SavedData.gettocken_id();
        Log.e("Token", "Token121" + Token);

        ((CareGiverMainActivity) getActivity()).launchFragmentTitle("Your Hires");

        getCareHireGiver();
        getREmainingAmount();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getCareHireGiver() {

        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.HireCareGiverHistroy_Url, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("CheckResponse" + response);
                S.E("CheckResponsePArama" + getParams());
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");

                        /*if (mainobject.getString("remainingAmount") != null) {
                            remainingAmount.setText(mainobject.getString("remainingAmount"));
                        } else {
                            remainingAmount.setText("0");
                        }*/

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            hireYourCaregiverModel = new HireYourCaregiverModel();
                            if(jsonObject.has("full_name"))
                            hireYourCaregiverModel.setHirename(jsonObject.getString("full_name"));
                            if(jsonObject.has("type"))
                            hireYourCaregiverModel.setType(jsonObject.getString("type"));
                            if(jsonObject.has("transaction_id"))
                            hireYourCaregiverModel.setTransaction_id(jsonObject.getString("transaction_id"));
                         /*   Log.e("", "" + jsonObject.getString("full_name"));*/
                            if (jsonObject.getString("type").equals("caregiver")) {
                                if(jsonObject.has("profile_pic"))
                                hireYourCaregiverModel.setHireimage(jsonObject.getString("profile_pic"));
                                if(jsonObject.has("ratingReview"))
                                hireYourCaregiverModel.setHirerating(jsonObject.getString("ratingReview") + "");
                                if(jsonObject.has("start_time"))
                                hireYourCaregiverModel.setStart_tme(jsonObject.getString("start_time"));
                                if(jsonObject.has("end_time"))
                                hireYourCaregiverModel.setEnd_time(jsonObject.getString("end_time"));
                                if(jsonObject.has("total_time"))
                                hireYourCaregiverModel.setTotalkM(jsonObject.getString("total_time"));
                                if(jsonObject.has("payment_mode"))
                                hireYourCaregiverModel.setPaymentMode(jsonObject.getString("payment_mode"));
                                if(jsonObject.has("amount"))
                                hireYourCaregiverModel.setTotalAmount(jsonObject.getString("amount"));
                                if(jsonObject.has("payment_history_id"))
                                hireYourCaregiverModel.setHiredCaregiverId(jsonObject.getString("payment_history_id"));

                            } else if (jsonObject.getString("type").equals("ambulance")) {
                                if(jsonObject.has("payment_mode"))
                                hireYourCaregiverModel.setPaymentMode(jsonObject.getString("payment_mode"));
                                if(jsonObject.has("profile_pic"))
                                hireYourCaregiverModel.setHireimage(jsonObject.getString("profile_pic"));
                                if(jsonObject.has("ratingReview"))
                                hireYourCaregiverModel.setHirerating(jsonObject.getString("ratingReview") + "");
                                if(jsonObject.has("source"))
                                hireYourCaregiverModel.setSourcename(jsonObject.getString("source"));
                                if(jsonObject.has("destination"))
                                hireYourCaregiverModel.setDestianme(jsonObject.getString("destination"));
                                if(jsonObject.has("total_kilometer"))
                                hireYourCaregiverModel.setTotalkM(jsonObject.getString("total_kilometer"));
                                if(jsonObject.has("amount"))
                                hireYourCaregiverModel.setTotalAmount(jsonObject.getString("amount"));
                                if(jsonObject.has("payment_history_id"))
                                hireYourCaregiverModel.setHiredCaregiverId(jsonObject.getString("payment_history_id"));

                            }
                            hireyourlist.add(hireYourCaregiverModel);
                        }
                        Collections.reverse(hireyourlist);
                        hireYourCaregiverAdapter = new HireYourCaregiverAdapter(getActivity(), hireyourlist);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        idRecyclerviewyourhire.setLayoutManager(layoutManager);
                        idRecyclerviewyourhire.setAdapter(hireYourCaregiverAdapter);
                        hireYourCaregiverAdapter.notifyDataSetChanged();
//                        Collections.reverse(hireyourlist);
                    } else {
/*
                        Toast.makeText(getActivity(), "No HireGiver", Toast.LENGTH_SHORT).show();
*/
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("checkMessage" + e);
                }
            }
        });
    }

    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", Token);

        Log.e("SaveData.getServiceId()", "" + Token);
        return params;
    }

    private void getREmainingAmount() {

        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.Remaining_Amount, 1, getParams3(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("CheckResponse3" + response);
                S.E("CheckResponsePArama3" + getParams());
                try {
                    JSONObject mainobject = new JSONObject(response);
                    if (mainobject.getString("status").equals("200"))
                    {
                        String remain_amnt = mainobject.getString("remaining amount");
                        if(remain_amnt.startsWith("-"))
                        {
                            remain_amnt = remain_amnt.replace("-","");
                            pay_to_admin.setText("Pay to Admin");
                            remainingAmount.setText(getString(R.string.rupyee_symbol) + " " + remain_amnt + " /-");
                        }
                        else
                        {
                            pay_to_admin.setText("Get from Admin");
                            remainingAmount.setText(getString(R.string.rupyee_symbol) + " " + remain_amnt + " /-");
                        }

                    } else {

                    }
                   /* int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");

                        *//*if (mainobject.getString("remainingAmount") != null) {
                            remainingAmount.setText(mainobject.getString("remainingAmount"));
                        } else {
                            remainingAmount.setText("0");
                        }*//*

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            hireYourCaregiverModel = new HireYourCaregiverModel();
                            hireYourCaregiverModel.setHirename(jsonObject.getString("full_name"));
                            hireYourCaregiverModel.setType(jsonObject.getString("type"));
                         *//*   Log.e("", "" + jsonObject.getString("full_name"));*//*
                            if (jsonObject.getString("type").equals("caregiver")) {
                                hireYourCaregiverModel.setHireimage(jsonObject.getString("profile_pic"));
                               *//* hireYourCaregiverModel.setHirerating(jsonObject.getString("rating") + "");*//*
                                hireYourCaregiverModel.setStart_tme(jsonObject.getString("start_time"));
                                hireYourCaregiverModel.setEnd_time(jsonObject.getString("end_time"));
                                hireYourCaregiverModel.setTotalkM(jsonObject.getString("total_time"));
                                hireYourCaregiverModel.setPaymentMode(jsonObject.getString("payment_mode"));


                            } else if (jsonObject.getString("type").equals("ambulance")) {

                                hireYourCaregiverModel.setPaymentMode(jsonObject.getString("payment_mode"));
                                hireYourCaregiverModel.setHireimage(jsonObject.getString("profile_pic"));
                                *//*hireYourCaregiverModel.setHirerating(jsonObject.getString("rating") + "");*//*
                                hireYourCaregiverModel.setSourcename(jsonObject.getString("source"));
                                hireYourCaregiverModel.setDestianme(jsonObject.getString("destination"));
                                hireYourCaregiverModel.setTotalkM(jsonObject.getString("total_kilometer"));
                            }

                            hireyourlist.add(hireYourCaregiverModel);
                        }
                        hireYourCaregiverAdapter = new HireYourCaregiverAdapter(getActivity(), hireyourlist);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        idRecyclerviewyourhire.setLayoutManager(layoutManager);
                        idRecyclerviewyourhire.setAdapter(hireYourCaregiverAdapter);
                        hireYourCaregiverAdapter.notifyDataSetChanged();
//                        Collections.reverse(hireyourlist);
                    } else {
                        Toast.makeText(getActivity(), "No HireGiver", Toast.LENGTH_SHORT).show();
                    }*/
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("checkMessage" + e);
                }
            }
        });
    }

    private Map<String, String> getParams3() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", Token);

        Log.e("SaveData.getServiceId()", "" + Token);
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
