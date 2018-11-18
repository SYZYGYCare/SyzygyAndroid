package com.dollop.syzygy.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.client.ReminderListActivity;
import com.dollop.syzygy.adapter.base.BaseRecyclerViewAdapter;
import com.dollop.syzygy.notification.Config;
import com.dollop.syzygy.notification.NotificationUtils;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Sohel on 2/23/2016.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    private TextView textViewToolbarTitle;

    protected abstract int getContentResId();

    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLayoutManager;
    protected ViewFlipper viewFlipper;
    BaseRecyclerViewAdapter adapter;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentResId());

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                S.E("call 1");
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    S.E("call 2");
//                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    S.E("call 3");
                    SavedData.saveNotificationRequest("no");
                    String message = intent.getStringExtra("message");

                    try {
                        SavedData.saveNotificationRequest("no");
                        JSONObject jsonObject = new JSONObject(message);
                        S.E("jsonObject ----   " + jsonObject.toString());
                        JSONObject msg = jsonObject.getJSONObject("msg");

                        if (msg.getString("user_type").equals("12"))
                        {
                            Snackbar snackbar = Snackbar
                                    .make(getWindow().getDecorView(), "Reminder", Snackbar.LENGTH_LONG)
                                    .setDuration(4000)
                                    .setAction("Go", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent1 = new Intent(getApplicationContext(), ReminderListActivity.class);
                                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent1);
                                        }
                                    });

                            snackbar.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    protected void setToolbarWithBackButton(String title) {
        initToolbar();
        getSupportActionBar().setTitle("");
        textViewToolbarTitle.setText(title);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
    }

/*    protected void setToolbarWithSubSubTitle(String title, String subtitle) {
        initToolbar();
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
    }

    protected void setCollaspingToolbarWithSubSubTitle(CollapsingToolbarLayout collapsingToolbarLayout, String title, String subtitle) {
        initToolbar();
        collapsingToolbarLayout.setTitle(title);
        getSupportActionBar().setSubtitle(subtitle);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
    }*/

    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        textViewToolbarTitle = (TextView) findViewById(R.id.textViewToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    protected void initTitleToolbar(String title) {
        initToolbar();
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    protected void initRecyclerView() {
        initViewFlipper();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // The number of Columns
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    protected void initViewFlipper() {
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
    }


    protected void setProgressBar() {
        viewFlipper.setDisplayedChild(1);
    }

    protected void setUpdatedLayout() {
        viewFlipper.setDisplayedChild(1);
    }

    protected void setMainData() {
        viewFlipper.setDisplayedChild(0);
    }

    protected void setNoData() {
        viewFlipper.setDisplayedChild(3);
    }

    protected void setNetworkError() {
        viewFlipper.setDisplayedChild(2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigateToParent();
                break;
           /* case R.id.action_search:
                Bundle bundle = new Bundle();
                bundle.putBoolean("from", true);
                M.I(BaseActivity.this, Search.class, bundle);
                break;
            case R.id.action_chat:
                M.T(this, "Coming soon");
                break;*/
        }
        return true;
    }

    private void navigateToParent() {
        Intent intent = NavUtils.getParentActivityIntent(this);
        if (intent == null) {
            this.finish();
        } else {
            NavUtils.navigateUpFromSameTask(this);
        }
    }

}
