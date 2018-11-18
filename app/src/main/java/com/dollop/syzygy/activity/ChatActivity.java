package com.dollop.syzygy.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dollop.syzygy.Model.Messages;
import com.dollop.syzygy.R;
import com.dollop.syzygy.adapter.ChatAdapter_1;
import com.dollop.syzygy.notification.Config;
import com.dollop.syzygy.notification.NotificationUtils;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;
import com.dollop.syzygy.views.KeyboardUtils;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.rockerhieu.emojicon.EmojiconEditText;
import io.github.rockerhieu.emojicon.EmojiconGridFragment;
import io.github.rockerhieu.emojicon.EmojiconsFragment;
import io.github.rockerhieu.emojicon.emoji.Emojicon;

/**
 * Created by sohel on 3/31/2017.
 */

public class ChatActivity extends BaseActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener, View.OnClickListener {
    FloatingActionButton sendMessage;
    int channel_id;
    EmojiconEditText editEmojicon;
    private View emojiconsFragment;
    LinearLayout inputeTextLayout;
    ImageView emojiIcon;
    private Handler mainThreadHandler;
    private static final int DELAY_SCROLLING_LIST = 300;
    private static final int DELAY_SHOWING_SMILE_PANEL = 200;
    private ArrayList<String> presentUsers = new ArrayList<>();
    private ArrayList<String> usersCurrentlyTyping = new ArrayList<>();
    private boolean activityPaused = false;
    private boolean typingFlag = false;
    private Handler isUserTypingHandler = new Handler();
    private ImageButton mentionBtn;
    private ArrayList<Messages> msgArrayList;
    ChatAdapter_1 adapter_1;
    private String reveiverId;
    private String type;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected int getContentResId() {
        return R.layout.single_chat_screen;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton(getString(R.string.app_name) + " Chat");
        mainThreadHandler = new Handler(Looper.getMainLooper());
        msgArrayList = new ArrayList<>();
        reveiverId = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        S.E("chgeck type" + type);
        initRecyclerView();
        initViews();
        initListener();
        hideSmileLayout();
        msgArrayList.clear();
        getMessages();

        notificationCallFromHandler();

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
                        reveiverId = msg.getString("sender_id");
                        type = msg.getString("token_user_type");

                        if (msg.getString("user_type").equals("11")) {
                            msgArrayList.clear();
                            getMessages();
                        } else if (msg.getString("user_type").equals("7")) {
                            S.T(ChatActivity.this, "Client Canceled your last Appoinment !");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private void hideSmileLayout() {
        emojiconsFragment.setVisibility(View.GONE);
        setSmilePanelIcon(R.drawable.happy);
    }

    private void setSmilePanelIcon(int resourceId) {
        emojiIcon.setImageResource(resourceId);
    }

    private void initListener() {
        emojiIcon.setOnClickListener(this);
        sendMessage.setOnClickListener(this);
        mentionBtn.setOnClickListener(this);
        editEmojicon.setOnClickListener(this);
    }

    private void initViews() {
        mLayoutManager.setStackFromEnd(true);
        editEmojicon = (EmojiconEditText) findViewById(R.id.editEmojicon);
        sendMessage = (FloatingActionButton) findViewById(R.id.send);
        inputeTextLayout = (LinearLayout) findViewById(R.id.inpute_TextLayout);
        emojiconsFragment = (View) findViewById(R.id.emojicons);
        emojiIcon = (ImageView) findViewById(R.id.emojiIcon);
        mentionBtn = (ImageButton) findViewById(R.id.mentionBtn);
    }

    protected void initRecyclerView() {
        initViewFlipper();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // The number of Columns
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    private void getMessages() {
        setMainData();
        S.E("get chat prams : " + getChatPrams());
        new JSONParser(this).parseVollyStringRequestWithautProgressBar(Const.URL.GET_CHAT_MESSAGE, 1, getChatPrams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("get chat : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200")) {
                        msgArrayList.clear();
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject json = data.getJSONObject(i);
                            Messages messages = new Messages();
                            messages.setId(json.getString("id"));
                            messages.setSender(json.getString("sender"));
                            messages.setReceiver(json.getString("receiver"));
                            messages.setMsg(json.getString("msg"));
                            messages.setSender_type(json.getString("sender_type"));
                            messages.setReceiver_type(json.getString("receiver_type"));
                            messages.setCreated_date(json.getString("created_date"));
                            msgArrayList.add(messages);
                            adapter_1 = new ChatAdapter_1(ChatActivity.this, msgArrayList, type);
                            mRecyclerView.setAdapter(adapter_1);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private Map<String, String> getChatPrams() {
        HashMap<String, String> prams = new HashMap<>();
        prams.put("token", SavedData.gettocken_id());
        prams.put("receiver", reveiverId);
        prams.put("token_user_type", type);
        return prams;
    }

    private void visibleOrHideSmilePanel() {
        if (isSmilesLayoutShowing()) {
            hideSmileLayout();
            KeyboardUtils.showKeyboard(ChatActivity.this);
        } else {
            KeyboardUtils.hideKeyboard(ChatActivity.this);
            showSmileLayout();
        }
        editEmojicon.requestFocus();
    }

    private boolean isSmilesLayoutShowing() {
        return emojiconsFragment.getVisibility() == View.VISIBLE;
    }

    private void showSmileLayout() {
        mainThreadHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                emojiconsFragment.setVisibility(View.VISIBLE);
                setSmilePanelIcon(R.drawable.keyboard);
            }
        }, DELAY_SHOWING_SMILE_PANEL);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send:
                if (editEmojicon.getText().toString().equals("")) {

                } else {
                    setMessageTo();
                    editEmojicon.getText().clear();
                }

                break;
            case R.id.emojiIcon:
                visibleOrHideSmilePanel();
                break;
            case R.id.mentionBtn:
                break;
            case R.id.editEmojicon:
                break;
        }
    }

    private void setMessageTo() {
        S.E("chat prams : " + getPrams());
        new JSONParser(this).parseVollyStringRequestWithautProgressBar(Const.URL.SEND_CHAT_MESSAGE, 1, getPrams(), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("send message : " + response);
                getMessages();
            }
        });
    }


    private Map<String, String> getPrams() {
        HashMap<String, String> prams = new HashMap<>();
        prams.put("token", SavedData.gettocken_id());
        prams.put("receiver", reveiverId);
        prams.put("token_user_type", type);
        prams.put("msg", editEmojicon.getText().toString());
        return prams;
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(editEmojicon, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(editEmojicon);
    }

    @Override
    public void onResume() {
        super.onResume();
        notificationCallFromHandler();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver, new IntentFilter(Config.PUSH_NOTIFICATION));
        NotificationUtils.clearNotifications(this);
    }

    private void notificationCallFromHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SavedData.getNotificationRequest() != null) {
                    if (SavedData.getNotificationRequest().equals("yes")) {
                        try {
                            SavedData.saveNotificationRequest("no");
                            S.E(" NOtification  responce ---     " + SavedData.getNotificationJson());
                            JSONObject jsonObject = new JSONObject(SavedData.getNotificationJson());
                            S.E("jsonObject ----   " + jsonObject.toString());
                            JSONObject msg = jsonObject.getJSONObject("msg");
                            reveiverId = msg.getString("sender_id");
                            type = msg.getString("token_user_type");
                            if (msg.getString("user_type").equals("11")) {
                                msgArrayList.clear();
                                getMessages();
                            } else if (msg.getString("user_type").equals("7")) {
                                S.T(ChatActivity.this, "Client Canceled your last Appoinment !");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, 2000);
    }
}
