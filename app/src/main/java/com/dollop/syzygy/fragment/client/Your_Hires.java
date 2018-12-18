package com.dollop.syzygy.fragment.client;

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
import android.widget.Toast;

import com.dollop.syzygy.Model.HireYourCaregiverModel;
import com.dollop.syzygy.R;
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
 * Use the {@link Your_Hires#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Your_Hires extends Fragment {
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
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Your_Hires() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Your_Hires.
     */
    // TODO: Rename and change types and number of parameters
    public static Your_Hires newInstance(String param1, String param2) {
        Your_Hires fragment = new Your_Hires();
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
        View view = inflater.inflate(R.layout.fragment_your__hires, container, false);
        unbinder = ButterKnife.bind(this, view);
        Token = SavedData.gettocken_id();
        Log.e("Token", "Token121" + Token);

        ((ClientMainActivity) getActivity()).launchFragmentTitle("Your Hires");

        getCareHireGiver();
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

        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.HireCareGiverHistroy_ClientUrl, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("CheckResponse" + response);
                S.E("CheckResponsePArama" + getParams());
                try {
                    /*{"status":200,"message":"success","data":[
                    {"full_name":"Shekhar Ahiwar","profile_pic":"2415f5864061b1ba8de089e0e7e05c23_1.png",
                    "user_id":"248","caregiver_id":"76","amount":"520.00","payment_mode":"online",
                    "transaction_id":"214564245","type":"ambulance","source":"guna","destination":"indore",
                    "total_kilometer":"5.30"},{"full_name":"Sohel care",
                    "profile_pic":"1bcdda1ed62478039ed1f24f40ec3e82_1.png","user_id":"248",
                    "caregiver_id":"72","amount":"600.00","payment_mode":"online",
                    "transaction_id":"214564245","type":"caregiver","start_tme":"10:45",
                    "end_time":"10:51","total_time":"6"},
                    {"full_name":"kaseem ambulance","profile_pic":null,"user_id":"248",
                    "caregiver_id":"73","amount":"800.00","payment_mode":"online",
                    "transaction_id":"214564245","type":"ambulance","source":"bhopal",
                    "destination":"indore","total_kilometer":"5.30"}]}*/
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        JSONArray jsonArray = mainobject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            hireYourCaregiverModel = new HireYourCaregiverModel();
                            if(jsonObject.has("full_name"))
                            hireYourCaregiverModel.setHirename(jsonObject.getString("full_name"));
                            if(jsonObject.has("type"))
                            hireYourCaregiverModel.setType(jsonObject.getString("type"));

                            if(jsonObject.has("transaction_id"))
                            hireYourCaregiverModel.setTransaction_id(jsonObject.getString("transaction_id"));

                            if(jsonObject.has("payment_history_id"))
                            hireYourCaregiverModel.setHiredCaregiverId(jsonObject.getString("payment_history_id"));
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
                              /*  hireYourCaregiverModel.s*/
                            } else if (jsonObject.getString("type").equals("ambulance")){
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
                        Toast.makeText(getActivity(), "No HireGiver", Toast.LENGTH_SHORT).show();
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
