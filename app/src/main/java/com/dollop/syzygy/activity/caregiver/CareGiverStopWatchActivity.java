package com.dollop.syzygy.activity.caregiver;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.database.datahelper.TymDataHelper;
import com.dollop.syzygy.database.model.TymModel;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sohel on 10/12/2017.
 */

public class CareGiverStopWatchActivity extends BaseActivity {
    private static final String TAG = CareGiverStopWatchActivity.class.getSimpleName();
    // Message type for the handler
    private final static int MSG_UPDATE_TIME = 0;
    private final Handler mUpdateTimeHandler = new UIUpdateHandler(this);
    TextView careGiverStopWatchText;
    Button careGiverStopWatchBtnStart;
    Button careGiverStopWatchBtnEnd;
    String pauseResumeTime = "";
    ImageView pauseId;
    ArrayList<Long> listForTym = new ArrayList<>();
    long tym = 0;
    int flag = 0;
    private TimerService timerService;
    private boolean serviceBound;
    private String watchRunningStatus;
    /**
     * Callback for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            S.E("Service bound");
            TimerService.RunServiceBinder binder = (TimerService.RunServiceBinder) service;
            timerService = binder.getService();
            serviceBound = true;
            // Ensure the service is not in the foreground when bound
            timerService.background();
            // Update the UI if the service is already running the timer
            if (timerService.isTimerRunning()) {
                updateUIStartRun();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            S.E("Service disconnect");
            serviceBound = false;
        }
    };

    @Override
    protected int getContentResId() {
        return R.layout.activity_caregiver_stop_watch;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initTitleToolbar("Timer");

        careGiverStopWatchText = (TextView) findViewById(R.id.careGiverStopWatchText);

        careGiverStopWatchBtnStart = (Button) findViewById(R.id.careGiverStopWatchBtnStart);
        careGiverStopWatchBtnEnd = (Button) findViewById(R.id.careGiverStopWatchBtnEnd);
        pauseId = (ImageView) findViewById(R.id.pauseId);

        if (TymDataHelper.getInstance().getList().size() > 0) {
            long currentTym = 0;
            for (int i = 0; i < TymDataHelper.getInstance().getList().size(); i++) {
                currentTym = currentTym + Long.parseLong(TymDataHelper.getInstance().getList().get(i).getStopTym());
                S.E("tym -- " + currentTym);
            }
            long sec = currentTym % 60;
            long min = (currentTym / 60) % 60;
            long hours = (currentTym / 60) / 60;

            String time = "";

            if(hours < 10)
                time = "0"+hours;
            else
                time = ""+hours;

            if(min < 10)
                time = time+":0"+min;
            else
                time = time+":"+min;


            if(sec < 10)
                time = time+":0"+sec;
            else
                time = time+":"+sec;

            careGiverStopWatchText.setText(time);
        }


        careGiverStopWatchBtnStart.setOnClickListener(new View.OnClickListener() {
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
                if (flag == 0) {
                    UpdateStartstatus();
                    startServiceForClient();
                } else if (flag == 1) {
                    if (careGiverStopWatchBtnStart.getText().toString().equals("Resume")) {
                        watchRunningStatus = "2";
                        long currentTym = 0;
                        for (int i = 0; i < TymDataHelper.getInstance().getList().size(); i++) {
                            currentTym = currentTym + Long.parseLong(TymDataHelper.getInstance().getList().get(i).getStopTym());
                            S.E("tym -- " + currentTym);
                        }
                        pauseResumeTime = String.valueOf(currentTym);
                        pauseORresumeForClient();
                    } else {
                        watchRunningStatus = "1";
                        pauseORresumeForClient();
                    }
                }
            }
        });

        careGiverStopWatchBtnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePaymentstatus();
                stopServiceForClient();
            }
        });


    }

    private void stopServiceForClient() {
        S.E("prams -=-=-=-=-==-    " + getPramsForStopHiringTime());
        new JSONParser(this).parseVollyStringRequest(Const.URL.CAREGIVER_START_STOP_HIRING_TIME, 1, getPramsForStopHiringTime(), new Helper() {
            @Override
            public void backResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        S.E("Stop service : " + response);
                        S.E("Stop service response : " + getPramsForStopHiringTime());
                        TymDataHelper.getInstance().deleteAll();
                        SavedData.saveStartTimer("no");
                        Intent i = new Intent(CareGiverStopWatchActivity.this, TimerService.class);
                        stopService(i);

                        SavedData.saveCaregiverMessageForsummery(response);

                        Bundle bundle = new Bundle();
                        bundle.putString("message", response);

                        S.I_clear(CareGiverStopWatchActivity.this, CareGiverSummeryPageActivity.class, bundle);
                       // SavedData.saveHireCareGiverId("");
                    } else {
                        S.T(CareGiverStopWatchActivity.this, "Try Again!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private Map<String, String> getPramsForStopHiringTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String s = sdf.format(new Date());

        long currentTym = 0;
        for (int i = 0; i < TymDataHelper.getInstance().getList().size(); i++) {
            currentTym = currentTym + Long.parseLong(TymDataHelper.getInstance().getList().get(i).getStopTym());
            S.E("tym --________________ " + currentTym);
        }
        S.E("tym -- " + currentTym);
        long minutes = (currentTym + timerService.elapsedTime() % 3600) / (60);

        HashMap<String, String> prams = new HashMap<>();
        prams.put("token", SavedData.gettocken_id());
        prams.put("type", "1");
        prams.put("client_id", SavedData.getClientId());
        prams.put("hire_caregiver_id", SavedData.getHireCareGiverId());
        prams.put("stop_time", s);
    /*    if (TymDataHelper.getInstance().getList().size() > 0) {
            prams.put("total_time", String.valueOf(minutes));
        } else {
            double minutes1 = (timerService.elapsedTime() % 3600) / 60;
            prams.put("total_time", String.valueOf(minutes1));
            S.E("TotalTime__________________+++++=====" + minutes1);
        }*/
        String totalNewTime =  careGiverStopWatchText.getText().toString();
        String[] separated = totalNewTime.split(":");
        int newHours =Integer.valueOf(separated[0])*60;
        int newMinutes= Integer.valueOf(separated[1]);

        int newTotalTimeInMinutes=0;
        if(newHours==0 && newMinutes ==0){
            newTotalTimeInMinutes = 1;
        }else {
            newTotalTimeInMinutes = newHours+newMinutes;
        }

        prams.put("total_time", String.valueOf(newTotalTimeInMinutes));
        return prams;
    }


    private void UpdateStartstatus() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.UPDATE_CANCEL_REQUEST, 1, getCancel_parameter(), new Helper() {
            public void backResponse(String response) {
                if (response != null) {

                }
            }
        });
    }


    private Map<String, String> getCancel_parameter() {

        String hire_id = SavedData.getHireCareGiverId();
        HashMap<String, String> param = new HashMap<>();
        param.put("hire_caregiver_id", hire_id);
        param.put("status", "start");
        S.E(" token..." + SavedData.gettocken_id());
        return param;
    }

    private void UpdatePaymentstatus() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.UPDATE_CANCEL_REQUEST, 1, getPayment_parameter(), new Helper() {
            public void backResponse(String response) {
                if (response != null) {

                }
            }
        });
    }


    private Map<String, String> getPayment_parameter() {

        String hire_id = SavedData.getHireCareGiverId();
        HashMap<String, String> param = new HashMap<>();
        param.put("hire_caregiver_id", hire_id);
        param.put("status", "payment");
        S.E(" token..." + SavedData.gettocken_id());
        return param;
    }


    private void startServiceForClient() {


        new JSONParser(this).parseVollyStringRequest(Const.URL.CAREGIVER_START_STOP_HIRING_TIME, 1, getPramsForStartStopHiringTime(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("startStopHiringTime : " + response);
                S.E("getPramsForStartStopHiringTime : " + getPramsForStartStopHiringTime());
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        SavedData.saveStartTimer("yes");
                        careGiverStopWatchBtnStart.setText("Pause");


                    }


                } catch (Exception e) {

                }
            }
        });
    }

    private void pauseORresumeForClient() {
        new JSONParser(this).parseVollyStringRequest(Const.URL.CAREGIVER_PAUSE_OR_RESUME, 1, getPauseResume(), new Helper() {


            @Override
            public void backResponse(String response) {
                S.E("startStopHiringTime : " + response);
                S.E("check Parameter : " + getPauseResume());

            }
        });
    }

    private Map<String, String> getPauseResume() {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String s = sdf.format(new Date());

        HashMap<String, String> prams = new HashMap<>();
        prams.put("token", SavedData.gettocken_id());
        prams.put("client_id", SavedData.getClientId());
        prams.put("hire_caregiver_id", SavedData.getHireCareGiverId());
        prams.put("current_time", pauseResumeTime);
        prams.put("type", watchRunningStatus);
        return prams;
    }

    private Map<String, String> getPramsForStartStopHiringTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String s = sdf.format(new Date());
        HashMap<String, String> prams = new HashMap<>();
        prams.put("token", SavedData.gettocken_id());
        prams.put("client_id", SavedData.getClientId());
        prams.put("hire_caregiver_id", SavedData.getHireCareGiverId());
        prams.put("start_time", s);
        return prams;
    }

    @Override
    protected void onStart() {
        super.onStart();
        S.E("Starting and binding service");
        Intent i = new Intent(this, TimerService.class);
        startService(i);
        bindService(i, mConnection, 0);
    }

    @Override
    protected void onStop() {
        super.onStop();
        updateUIStopRun();
        if (serviceBound) {
            timerService.foreground();
            unbindService(mConnection);
            serviceBound = false;
        }
    }

    /**
     * Updates the UI when a run starts
     */
    private void updateUIStartRun() {
        mUpdateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
        careGiverStopWatchBtnStart.setText("Pause");
        Drawable image = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_pause);
        pauseId.setImageDrawable(image);

    }

    /**
     * Updates the UI when a run stops
     */
    private void updateUIStopRun() {
        mUpdateTimeHandler.removeMessages(MSG_UPDATE_TIME);
        careGiverStopWatchBtnStart.setText("Resume");
        Drawable image = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_play_button_arrowhead);
        pauseId.setImageDrawable(image);

    }

    /**
     * Updates the timer readout in the UI; the service must be bound
     */

    private void updateUITimer() {
        if (serviceBound) {
//            careGiverStopWatchText.setText(timerService.elapsedTime() + " seconds");

            if (TymDataHelper.getInstance().getList().size() > 0) {
                long currentTym = 0;
                for (int i = 0; i < TymDataHelper.getInstance().getList().size(); i++) {
                    currentTym = currentTym + Long.parseLong(TymDataHelper.getInstance().getList().get(i).getStopTym());
                    S.E("tym -- " + currentTym);
                }
                currentTym += timerService.elapsedTime();
                long sec = currentTym % 60;
                long min = (currentTym / 60) % 60;
                long hours = (currentTym / 60) / 60;

                String time = "";

                if(hours < 10)
                    time = "0"+hours;
                else
                    time = ""+hours;

                if(min < 10)
                    time = time+":0"+min;
                else
                    time = time+":"+min;


                if(sec < 10)
                    time = time+":0"+sec;
                else
                    time = time+":"+sec;



                careGiverStopWatchText.setText(time);
            } else {
                long sec = timerService.elapsedTime() % 60;
                long min = (timerService.elapsedTime() / 60) % 60;
                long hours = (timerService.elapsedTime() / 60) / 60;
                String time = "";

                if(hours < 10)
                    time = "0"+hours;
                else
                    time = ""+hours;

                if(min < 10)
                    time = time+":0"+min;
                else
                    time = time+":"+min;


                if(sec < 10)
                    time = time+":0"+sec;
                else
                    time = time+":"+sec;



                careGiverStopWatchText.setText(time);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * When the timer is running, use this handler to update
     * the UI every second to show timer progress
     */
    static class UIUpdateHandler extends Handler {

        private final static int UPDATE_RATE_MS = 1000;
        private final WeakReference<CareGiverStopWatchActivity> activity;

        UIUpdateHandler(CareGiverStopWatchActivity activity) {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message message) {
            if (MSG_UPDATE_TIME == message.what) {
                S.E("updating time");
                activity.get().updateUITimer();
                sendEmptyMessageDelayed(MSG_UPDATE_TIME, UPDATE_RATE_MS);
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        updateUIStopRun();
        if (serviceBound) {
            timerService.foreground();
            unbindService(mConnection);
            serviceBound = false;
        }
        S.E("Destroying service");
    }
    /**
     * Timer service tracks the start and end time of timer; service can be placed into the
     * foreground to prevent it being killed when the activity goes away
     */
    public static class TimerService extends Service {

        private static final String TAG = TimerService.class.getSimpleName();
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

        /**
         * Starts the timer
         */
        public void startTimer() {
            if (!isTimerRunning) {
                startTime = System.currentTimeMillis();
                isTimerRunning = true;
            } else {
                S.E("startTimer request for an already running timer");
            }
        }

        /**
         * Stops the timer
         */
        public void stopTimer() {
            if (isTimerRunning) {
                endTime = System.currentTimeMillis();
                isTimerRunning = false;
            } else {
                S.E("stopTimer request for a timer that isn't running");
            }
        }

        /**
         * @return whether the timer is running
         */
        public boolean isTimerRunning() {
            return isTimerRunning;
        }

        /**
         * Returns the  elapsed time
         *
         * @return the elapsed time in seconds
         */
        public long elapsedTime() {
            // If the timer is running, the end time will be zero
            return endTime > startTime ?
                    (endTime - startTime) / 1000 :
                    (System.currentTimeMillis() - startTime) / 1000;
        }

        /**
         * Place the service into the foreground
         */
        public void foreground() {
            startForeground(NOTIFICATION_ID, createNotification());
        }

        /**
         * Return the service to the background
         */
        public void background() {
            stopForeground(true);
        }

        /**
         * Creates a notification for placing the service into the foreground
         *
         * @return a notification for interacting with the service when in the foreground
         */
        private Notification createNotification() {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Return to the timer")
                    .setSmallIcon(R.drawable.logo_new);

            Intent resultIntent = new Intent(this, CareGiverStopWatchActivity.class);
            PendingIntent resultPendingIntent =
                    PendingIntent.getActivity(this, 0, resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);



            return builder.build();
        }

        public class RunServiceBinder extends Binder {
            TimerService getService() {
                return TimerService.this;
            }
        }
    }
}
