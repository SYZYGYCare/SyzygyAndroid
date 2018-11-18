package com.dollop.syzygy.activity.client;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.fragment.client.ClientMainFragment;
import com.dollop.syzygy.notification.Config;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by sohel on 10/12/2017.
 */

/*public class ClientStopWatchActivity extends BaseActivity {
    private static final String TAG = ClientStopWatchActivity.class.getSimpleName();
    // Message type for the handler
    private final static int MSG_UPDATE_TIME = 0;
    static ClientStopWatchActivity clientStopWatchActivity;
    private final Handler mUpdateTimeHandler = new ClientUIUpdateHandler(this);
    TextView careGiverStopWatchText;
    ArrayList<Long> listForTym = new ArrayList<>();
    long tym = 0;
    int flag = 0;
    LinearLayout linearLayoutId;
    private ClientTimerService timerService = new ClientTimerService();
    private boolean serviceBound;
    private BroadcastReceiver mstopRegistrationBroadcastReceiver;
    *//**
     * Callback for service binding, passed to bindService()
     *//*
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            S.E("Service bound");
            ClientTimerService.RunServiceBinder binder = (ClientTimerService.RunServiceBinder) service;
            timerService = binder.getService();
            serviceBound = true;
            // Ensure the service is not in the foreground when bound
            timerService.background();
            // Update the UI if the service is already running the timer
            if (timerService.isTimerRunning()) {
                ClientupdateUIStartRun();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            S.E("Service disconnect");
            serviceBound = false;
        }
    };

    public static ClientStopWatchActivity getInstance() {

        return clientStopWatchActivity;
    }

    @Override
    protected int getContentResId() {
        return R.layout.activity_client_stop_watch;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initTitleToolbar("Timer");
        clientStopWatchActivity = this;
        careGiverStopWatchText = (TextView) findViewById(R.id.careGiverStopWatchText);

        S.E("onCreate");

        *//*careGiverStopWatchBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (serviceBound && !timerService.isTimerRunning()) {
                    S.E("Starting timer");
                    timerService.startTimer();
                    updateUIStartRun();
                } else if (serviceBound && timerService.isTimerRunning()) {
                    S.E("Stopping timer");
                    flag = 1;
                    TymModel tymModel = new TymModel();
                    tymModel.setStopTym(String.valueOf(timerService.elapsedTime()));
                    TymDataHelper.getInstance().insertData(tymModel);
                    S.E("stopTym == " + TymDataHelper.getInstance().getList().size());
                    timerService.stopTimer();
                    updateUIStopRun();
                }
                if (flag == 0)
                    startServiceForClient();
            }
        });*//*

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SavedData.getNotificationRequest() != null) {
                    if (SavedData.getNotificationRequest().equals("yes")) {

                        try {
                            SavedData.saveNotificationRequest("no");
                            JSONObject jsonObject = new JSONObject(SavedData.getNotificationJson());

                            JSONObject msg = jsonObject.getJSONObject("msg");
                            S.E("sohel - stopwatch " + jsonObject.getJSONObject("msg"));
                            if (msg.getString("user_type").equals("6")) {

                                S.I_clear(ClientStopWatchActivity.this, SummeryPageActivity.class, null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("Checked", "StopAgain" + e);
                        }
                    }
                }
            }
        }, 2000);

        mstopRegistrationBroadcastReceiver = new BroadcastReceiver() {
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
                    String message = intent.getStringExtra("message");

                    try {
                        SavedData.saveNotificationRequest("no");
                        JSONObject jsonObject = new JSONObject(message);
                        JSONObject msg = jsonObject.getJSONObject("msg");
                        S.E("sohel - stopwatch " + jsonObject.getJSONObject("msg"));

                        if (msg.getString("user_type").equals("6")) {

                            S.I(ClientStopWatchActivity.this, SummeryPageActivity.class, null);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("Checked", "stopwatch" + e);
                    }
                }
            }
        };


        *//*timerService.startTimer();
        ClientupdateUIStartRun();*//*

    }

    @Override
    protected void onStart() {
        super.onStart();
        S.E("Starting and binding service");
        Intent i = new Intent(this, ClientTimerService.class);
        startService(i);
        bindService(i, mConnection, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ClientupdateUIStopRun();
        if (serviceBound) {
            timerService.foreground();
            unbindService(mConnection);
            serviceBound = false;
        }
    }

    *//**
     * Updates the UI when a run starts
     *//*
    private void ClientupdateUIStartRun() {
        mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
    }

    *//**
     * Updates the UI when a run stops
     *//*
    private void ClientupdateUIStopRun() {
        mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
    }

    *//**
     * Updates the timer readout in the UI; the service must be bound
     *//*

    private void ClientupdateUITimer() {
        if (serviceBound) {
//            careGiverStopWatchText.setText(timerService.elapsedTime() + " seconds");

            long sec = timerService.elapsedTime() % 60;
            long min = (timerService.elapsedTime() / 60) % 60;
            long hours = (timerService.elapsedTime() / 60) / 60;
            careGiverStopWatchText.setText(hours + ":" + min + ":" + sec);

            *//*if (TymDataHelper.getInstance().getList().size() > 0) {
                long currentTym = 0;
                for (int i = 0; i < TymDataHelper.getInstance().getList().size(); i++) {
                    currentTym = currentTym + Long.parseLong(TymDataHelper.getInstance().getList().get(i).getStopTym());
                    S.E("tym -- " + currentTym);
                }
                currentTym += timerService.elapsedTime();
                long sec = currentTym % 60;
                long min = (currentTym / 60) % 60;
                long hours = (currentTym / 60) / 60;
                careGiverStopWatchText.setText(hours + ":" + min + ":" + sec);
            } else {

            }*//*
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    *//**
     * When the timer is running, use this handler to update
     * the UI every second to show timer progress
     *//*
    private static class ClientUIUpdateHandler extends Handler {

        private final static int UPDATE_RATE_MS = 1000;
        private final WeakReference<ClientStopWatchActivity> activity;

        ClientUIUpdateHandler(ClientStopWatchActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message message) {
            if (MSG_UPDATE_TIME == message.what) {
                S.E("updating time");
                activity.get().ClientupdateUITimer();
                sendEmptyMessageDelayed(MSG_UPDATE_TIME, UPDATE_RATE_MS);
            }
        }
    }

    *//**
     * Timer service tracks the start and end time of timer; service can be placed into the
     * foreground to prevent it being killed when the activity goes away
     *//*
    public static class ClientTimerService extends Service {

        private static final String TAG = ClientTimerService.class.getSimpleName();
        // Foreground notification id
        private static final int NOTIFICATION_ID = 1;
        // Service binder
        private final IBinder serviceBinder = new RunServiceBinder();
        // Start and end times in milliseconds
        private long startTime, endTime;
        // Is the service tracking time?
        private boolean isTimerRunning;

        @Override
        public void onCreate() {
            S.E("Creating service");
            startTime = 0;
            endTime = 0;
            isTimerRunning = false;
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            S.E("Starting service");
            return Service.START_STICKY;
        }

        @Override
        public IBinder onBind(Intent intent) {
            S.E("Binding service");
            return serviceBinder;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            S.E("Destroying service");
        }

        *//**
         * Starts the timer
         *//*
        public void startTimer() {
            if (!isTimerRunning) {
                startTime = System.currentTimeMillis();
                isTimerRunning = true;
            } else {
                S.E("startTimer request for an already running timer");
            }
        }

        *//**
         * Stops the timer
         *//*
        public void stopTimer() {
            if (isTimerRunning) {
                endTime = System.currentTimeMillis();
                isTimerRunning = false;
            } else {
                S.E("stopTimer request for a timer that isn't running");
            }
        }

        *//**
         * @return whether the timer is running
         *//*
        public boolean isTimerRunning() {
            return isTimerRunning;
        }

        *//**
         * Returns the  elapsed time
         *
         * @return the elapsed time in seconds
         *//*
        public long elapsedTime() {
            // If the timer is running, the end time will be zero
            return endTime > startTime ?
                    (endTime - startTime) / 1000 :
                    (System.currentTimeMillis() - startTime) / 1000;
        }

        *//**
         * Place the service into the foreground
         *//*
        public void foreground() {
            startForeground(NOTIFICATION_ID, createNotification());
        }

        *//**
         * Return the service to the background
         *//*
        public void background() {
            stopForeground(true);
        }

        *//**
         * Creates a notification for placing the service into the foreground
         *
         * @return a notification for interacting with the service when in the foreground
         *//*
        private Notification createNotification() {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Return to the timer")
                    .setSmallIcon(R.drawable.logo_new);

            Intent resultIntent = new Intent(this, ClientStopWatchActivity.class);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(this, 0, resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);

            return builder.build();
        }

        public class RunServiceBinder extends Binder {
            public ClientTimerService getService() {
                return ClientTimerService.this;
            }
        }
    }
}*/
