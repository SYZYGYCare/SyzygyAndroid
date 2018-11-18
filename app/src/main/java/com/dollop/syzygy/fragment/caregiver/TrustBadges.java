package com.dollop.syzygy.fragment.caregiver;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.dollop.syzygy.R;
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
 * Created by sohel on 11/1/2017.
 */

public class TrustBadges extends Fragment {
    @BindView(R.id.trustBadgesActiveMobile)
    ImageView trustBadgesActiveMobile;
    @BindView(R.id.trustBadgesNonActiveMobile)
    ImageView trustBadgesNonActiveMobile;
    @BindView(R.id.trustBadgesActiveEmail)
    ImageView trustBadgesActiveEmail;
    @BindView(R.id.trustBadgesNonActiveEmail)
    ImageView trustBadgesNonActiveEmail;
    @BindView(R.id.trustBadgesActiveAccount)
    ImageView trustBadgesActiveAccount;
    @BindView(R.id.trustBadgesNonActiveAccount)
    ImageView trustBadgesNonActiveAccount;
    @BindView(R.id.trustBadgesActivePolice)
    ImageView trustBadgesActivePolice;
    @BindView(R.id.trustBadgesNonActivePolice)
    ImageView trustBadgesNonActivePolice;
    @BindView(R.id.trustBadgesActiveProfile)
    ImageView trustBadgesActiveProfile;
    @BindView(R.id.trustBadgesNonActiveProfile)
    ImageView trustBadgesNonActiveProfile;
    @BindView(R.id.trustBadgesActiveSocial)
    ImageView trustBadgesActiveSocial;
    @BindView(R.id.trustBadgesNonActiveSocial)
    ImageView trustBadgesNonActiveSocial;
    @BindView(R.id.activity_trust_budges)
    ScrollView activity_trust_budges;

    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_trust_budges, container, false);
        unbinder = ButterKnife.bind(this, view);
        ((CareGiverMainActivity) getActivity()).launchFragmentTitle("Trust Badges");
        getTrustBadges();
        return view;
    }

    private void getTrustBadges() {
        new JSONParser(getActivity()).parseVollyStringRequest(Const.URL.TRUST_BADGES, 1, getPrams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("trust badges : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        if (data.getString("bank_verified").equals("0")) {
                            trustBadgesActiveAccount.setVisibility(View.GONE);
                            trustBadgesNonActiveAccount.setVisibility(View.VISIBLE);
                        } else {
                            trustBadgesActiveAccount.setVisibility(View.VISIBLE);
                            trustBadgesNonActiveAccount.setVisibility(View.GONE);
                        }
                        if (data.getString("police_verified").equals("0")) {
                            trustBadgesActivePolice.setVisibility(View.GONE);
                            trustBadgesNonActivePolice.setVisibility(View.VISIBLE);
                        } else {
                            trustBadgesActivePolice.setVisibility(View.VISIBLE);
                            trustBadgesNonActivePolice.setVisibility(View.GONE);
                        }
                        if (data.getString("no_verified").equals("0")) {
                            trustBadgesActiveMobile.setVisibility(View.GONE);
                            trustBadgesNonActiveMobile.setVisibility(View.VISIBLE);
                        } else {
                            trustBadgesActiveMobile.setVisibility(View.VISIBLE);
                            trustBadgesNonActiveMobile.setVisibility(View.GONE);
                        }
                        if (data.getString("email_verified").equals("0")) {
                            trustBadgesActiveEmail.setVisibility(View.GONE);
                            trustBadgesNonActiveEmail.setVisibility(View.VISIBLE);
                        } else {
                            trustBadgesActiveEmail.setVisibility(View.VISIBLE);
                            trustBadgesNonActiveEmail.setVisibility(View.GONE);
                        }
                        if (data.getString("profile_pic_verified").equals("0")) {
                            trustBadgesActiveProfile.setVisibility(View.GONE);
                            trustBadgesNonActiveProfile.setVisibility(View.VISIBLE);
                        } else {
                            trustBadgesActiveProfile.setVisibility(View.VISIBLE);
                            trustBadgesNonActiveProfile.setVisibility(View.GONE);
                        }
                        if (data.getString("social_link_verified").equals("0")) {
                            trustBadgesActiveSocial.setVisibility(View.GONE);
                            trustBadgesNonActiveSocial.setVisibility(View.VISIBLE);
                        } else {
                            trustBadgesActiveSocial.setVisibility(View.VISIBLE);
                            trustBadgesNonActiveSocial.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getPrams() {
        HashMap<String, String> prams = new HashMap<>();
        prams.put("token", SavedData.gettocken_id());
        prams.put("authority_id", "2");
        return prams;
    }
}
