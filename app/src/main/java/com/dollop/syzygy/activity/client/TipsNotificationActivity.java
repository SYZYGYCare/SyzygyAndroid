package com.dollop.syzygy.activity.client;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.BaseActivity;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;

import butterknife.BindView;

/**
 * Created by sohel on 11/22/2017.
 */

public class TipsNotificationActivity extends BaseActivity {
    @BindView(R.id.tool_bar)
    Toolbar tool_bar;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    @Override
    protected int getContentResId() {
        return R.layout.activity_tips_notification;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolbarWithBackButton("Tips Notification");

        getTipsNotification();
    }

    private void getTipsNotification() {
       /* new JSONParser(this).parseVollyStringRequest(Const.URL.GET_TIPS_NOTIFICATION, 0, null, new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("Tips Notification response : " + response);
            }
        });*/
    }
}
