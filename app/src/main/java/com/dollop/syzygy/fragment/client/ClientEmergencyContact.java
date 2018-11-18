package com.dollop.syzygy.fragment.client;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.syzygy.Model.EmerGencyClientModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.client.ClientMainActivity;
import com.dollop.syzygy.adapter.EmergencyAdapter;
import com.dollop.syzygy.listeners.CustomButtonListener;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ClientEmergencyContact extends Fragment implements CustomButtonListener {

    @BindView(R.id.ContactClient1)
    EditText ContactClient1;

    @BindView(R.id.BtnSubitEmgNumberId)
    Button BtnSubitEmgNumberId;
    @BindView(R.id.activity_ambulance)
    RelativeLayout activityAmbulance;
    Unbinder unbinder;
    String Token;
    List<EmerGencyClientModel> emergencylist = new ArrayList<>();
    RecyclerView emergencyList;
    EmergencyAdapter emergencyAdapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private String emergency_no_id;
    private LinearLayout llLayoutId;
    private TextView emergencyTextViewId;

    public ClientEmergencyContact() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_emergency_contact, container, false);
        ((ClientMainActivity) getActivity()).launchFragmentTitle("Emergency Contact Number");
        unbinder = ButterKnife.bind(this, view);
        Token = SavedData.gettocken_id();
        emergencyList = (RecyclerView) view.findViewById(R.id.recycleEmegency);
        emergencyTextViewId = (TextView) view.findViewById(R.id.emergencyTextViewId);
        llLayoutId = (LinearLayout) view.findViewById(R.id.llLayoutId);

        ContactClient1.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });


        getEmergencyNumber();


        BtnSubitEmgNumberId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContactClient1.getText().toString().equals("") || ContactClient1.getText().toString().length()<10) {
                    ContactClient1.setError("Please enter valid contact number.");
                    ContactClient1.requestFocus();
                } else {
                    EmergencyContactClient();

                }
            }
        });
        emergencyAdapter = new EmergencyAdapter(getActivity(), emergencylist);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        emergencyList.setLayoutManager(layoutManager);
        emergencyList.setAdapter(emergencyAdapter);

        return view;
    }

    public void methodEmergencyUpdate() {
        getEmergencyNumber();
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

    public void getEmergencyNumber() {
        S.E("prams " + getParam1());
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.Get_Emergency_Number, 1, getParam1(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("getEmergency ka response" + response);
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String message = mainobject.getString("message");
                    emergencylist.clear();
                    if (status == 200) {

                        JSONArray jsonArray = mainobject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            EmerGencyClientModel emerGencyClientModel = new EmerGencyClientModel();
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            emerGencyClientModel.setcontactId(jsonObject.getString("emergency_no_id"));
                            S.E("Chcek Here Also"+jsonObject.getString("emergency_no_id"));
                            emerGencyClientModel.setEmergency_no1(jsonObject.getString("emergency_no"));
                            emergencylist.add(emerGencyClientModel);
                        }
                        emergencyAdapter = new EmergencyAdapter(getActivity(), emergencylist);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        emergencyList.setLayoutManager(layoutManager);
                        emergencyList.setAdapter(emergencyAdapter);
                        emergencyAdapter.setCustomListener(new CustomButtonListener() {
                            @Override
                            public void onButtonClick(int position, String buttonText) {
                                S.E("Emergency"+emergencylist.get(position).getcontactId());

                                getEmergencyNumber();
                            }
                        });

                        emergencyAdapter.notifyDataSetChanged();
                        if (3 <= emergencylist.size()) {
                            llLayoutId.setVisibility(View.GONE);
                            emergencyTextViewId.setVisibility(View.VISIBLE);
                        } else {

                            llLayoutId.setVisibility(View.VISIBLE);
                            emergencyTextViewId.setVisibility(View.GONE);

                        }
                    } else {

                        llLayoutId.setVisibility(View.VISIBLE);
                        emergencyTextViewId.setVisibility(View.GONE);
                        emergencyAdapter.notifyDataSetChanged();

                       /* emergencyList.setVisibility(View.GONE);*/
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("Exception" + e);
                }

            }
        });
    }

    private Map<String, String> getParam1() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("token", SavedData.gettocken_id());
        return hashMap;
    }

    private void EmergencyContactClient() {
        S.E("prams for add - " + getParams());
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.AddEmerGencyClientContact_Url, 1, getParams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("Add Emergency" + response);
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        Toast.makeText(getActivity(), "Submitted Number", Toast.LENGTH_SHORT).show();
                        ContactClient1.setText("");
                        getEmergencyNumber();
                      /*  S.I(getActivity(), ClientMainActivity.class, null);*/
                    } else {
                        Toast.makeText(getActivity(), "Not Submitted Number", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("Add exception" + e);
                }
            }
        });
    }

    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", Token);
        params.put("emergency_no", ContactClient1.getText().toString());


        return params;
    }

    @Override
    public void onButtonClick(int position, String buttonText) {
        getEmergencyNumber();


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
