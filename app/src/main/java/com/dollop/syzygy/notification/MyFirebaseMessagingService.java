package com.dollop.syzygy.notification;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.dollop.syzygy.activity.ChatActivity;
import com.dollop.syzygy.activity.caregiver.CareGiverMainActivity;
import com.dollop.syzygy.activity.client.CheckListActivity;
import com.dollop.syzygy.activity.client.ClientMainActivity;
import com.dollop.syzygy.activity.client.HealthTipsActivity;
import com.dollop.syzygy.activity.client.ReminderListActivity;
import com.dollop.syzygy.fragment.client.ClientMainFragment;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    String title, message, imageUrl, timestamp, action, userType;
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        Log.e(TAG, "From:  fghfdghdfghdfh       " + remoteMessage.getFrom());

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body:  dfghdfghdf       ----   " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("data");

            title = data.getString("title");
            userType = data.getString("user_type");
            message = data.getString("message");
            action = data.getString("action");
            boolean isBackground = data.getBoolean("is_background");
            imageUrl = data.getString("image");
            timestamp = data.getString("timestamp");
            JSONObject payload = data.getJSONObject("payload");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "action: " + action);
            Log.e(TAG, "user_type: " + userType);
            Log.e(TAG, "message: " + message);
            Log.e(TAG, "isBackground: " + isBackground);
            Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);
            if (userType.equals("10")) {

            } else {
                SavedData.saveNotificationRequest("yes");
                SavedData.saveNotificationJson(message);
                SavedData.saveMessageForsummery(message);
                if (userType.equalsIgnoreCase("1")) {
                    Date currentTime = Calendar.getInstance().getTime();
                    long time_inmi = currentTime.getTime();
                    SavedData.saveNotificationTIme("" + time_inmi);
                }
            }
            switch (userType) {
                case "1":
                    requestToCareGiver();
                    //Notification  for show incoming request from client on caregiver
                    break;
                case "3":
                    responseFromCaregiver();
                    break;
                case "7":
                    //REquest canclation for both side
                    if (SavedData.gettockenUserType().equals("client")) {
                        cancelRequestFromCareGiver();
                    } else {
                        SavedData.saveCancelRequestFromClient("yes");
                        cancelRequestFromClient();
                    }
                    break;
                case "5":
                    StartTimeAmbulance();
                    break;
                case "9":
                    action = "Stop Timer";
                    //clock pause client side
                    responseFromCaregiver();
                    break;
                case "8":
                    //clock resume client side
                    action = "Start Timer";
                    responseFromCaregiver();
                    break;
                case "6":


                    stopService();
                    break;
                case "2":
                    //if one of them caregive or ambulance accept request notification come to remove request from another caregiver
                    AcceptAnotherCaregiver();
                    break;
                case "10":

                    //reminder notification it is showing to do list
                    remainderNotification();
                    break;
                case "11":

                    //on message recive both side client or caregiver chat
                    chatNotification();
                    break;
                case "12":

                    // reminder notification use for update reminder
                    remainderMessage();
                    break;
                case "13":
                    //for update Health tip list
                    newAddedInHealthTips();
                    break;
                case "14":
                    //update wallet balance  on mainpage client
                    walletBalanceAdded();
                    break;
                case "15":
                    NewReminderAdded();
                    break;
            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void walletBalanceAdded() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
            S.newRemainderDialog(this);
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), ClientMainActivity.class);
            resultIntent.putExtra("message", message);
            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }

    private void NewReminderAdded() {
        if ( ClientMainFragment.is_on_map) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
            S.newRemainderDialog(this);
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), ClientMainActivity.class);
            resultIntent.putExtra("message", message);
            SavedData.saveHireLaterMessage(String.valueOf(message));

            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }

    private void newAddedInHealthTips() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
            S.newRemainderDialog(this);
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), HealthTipsActivity.class);
            resultIntent.putExtra("message", message);
            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }

    private void remainderMessage() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
            S.newRemainderDialog(this);
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), ReminderListActivity.class);
            resultIntent.putExtra("message", message);
            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }

    private void cancelRequestFromCareGiver() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), ClientMainActivity.class);
            resultIntent.putExtra("message", message);
            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }

    private void cancelRequestFromClient() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), CareGiverMainActivity.class);
            resultIntent.putExtra("message", message);
            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }

    private void StartTimeractivity() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), ClientMainActivity.class);
            resultIntent.putExtra("message", message);
            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }

    private void StopTimeractivity() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), ClientMainActivity.class);
            resultIntent.putExtra("message", message);
            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }

    private void requestToCareGiver() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), CareGiverMainActivity.class);
            resultIntent.putExtra("message", message);
            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }


    private void StartTimeAmbulance() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), ClientMainActivity.class);
            resultIntent.putExtra("message", message);
            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }


    private void responseFromCaregiver() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), ClientMainActivity.class);
            resultIntent.putExtra("message", message);
            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }

    private void stopService() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), ClientMainActivity.class);
            resultIntent.putExtra("message", message);
            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }

    /**
     * Showing notification with text only
     */

    private void StartServiceforClient() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), ClientMainActivity.class);
            resultIntent.putExtra("message", message);
            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }

    private void showNotificationMessage(Context context, String title, String action, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, action, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String action, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, action, message, timeStamp, intent, imageUrl);
    }

    private void AcceptAnotherCaregiver() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), CareGiverMainActivity.class);
            resultIntent.putExtra("message", message);
            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }

    private void remainderNotification() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), CheckListActivity.class);
            resultIntent.putExtra("message", message);
            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }

    private void chatNotification() {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            Log.e("check if", "......");
            // app is not in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", String.valueOf(message));
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            S.E("newdistributor");
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), ChatActivity.class);
            resultIntent.putExtra("message", message);
            S.E("message" + message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, action, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, action, message, timestamp, resultIntent, imageUrl);
            }
        }
    }
}