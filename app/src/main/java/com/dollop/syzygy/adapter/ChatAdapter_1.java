package com.dollop.syzygy.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dollop.syzygy.Model.Messages;
import com.dollop.syzygy.R;
import com.dollop.syzygy.sohel.S;

import java.util.List;

import io.github.rockerhieu.emojicon.EmojiconTextView;

/**
 * Created by Sohel-PC on 03/31/2017.
 */
public class ChatAdapter_1 extends RecyclerView.Adapter<ChatAdapter_1.MyViewHolder> {
    private List<Messages> browseList;
    private Context context;
    private String type;
    private static final int TYPE_REQUEST_MESSAGE = 0;
    private static final int TYPE_OWN_MESSAGE = 1;
    private static final int TYPE_OWN_MESSAGE_SECOND = 2;
    private static final int TYPE_OPPONENT_MESSAGE = 3;
    private static final int TYPE_OPPONENT_MESSAGE_SECOND = 4;
    int temp_pos;
    private String userId = "";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        EmojiconTextView messageText;
        TextView actionView;

        public MyViewHolder(View v) {
            super(v);
          try
          {
              messageText = (EmojiconTextView) v.findViewById(R.id.message_text);
              userName = (TextView) v.findViewById(R.id.userName);
              actionView = (TextView) v.findViewById(R.id.action);
          }
          catch (Exception e)
          {
              e.printStackTrace();
          }

        }
    }

    public ChatAdapter_1(Context context, List<Messages> moviesList, String type) {

        try
        {
            this.browseList = moviesList;
            this.context = context;
            this.type = type;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemViewType(int position) {



            temp_pos = position;
            if (browseList.size() > position + 1) {
                return getItemViewType(browseList.get(position + 1), position);
            } else {
                return getItemViewType(browseList.get(position), position);
            }


    }

    public void setFilter(List<Messages> newData) {
        try
        {
            browseList = newData;
            notifyDataSetChanged();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private int getItemViewType(Messages message, int position)
    {
        userId = message.getSender_type();
        S.E("user id : " + message.getSender_type());
        if (type.equals("1")) {
            if (browseList.get(position).getSender_type().equals("1")) {
                S.E(" user if");
                if (position == 0 && userId.equals(browseList.get(position).getSender_type())) {
                    return TYPE_OWN_MESSAGE;
                } else {
                    return TYPE_OWN_MESSAGE_SECOND;
                }
            } else {
                S.E(" user else");
                if (position == 0 && userId.equals(browseList.get(position).getSender_type())) {
                    return TYPE_OPPONENT_MESSAGE;
                } else {
                    return TYPE_OPPONENT_MESSAGE_SECOND;
                }
            }
        } else {
            if (browseList.get(position).getSender_type().equals("2")) {
                S.E(" user if");
                if (position == 0 && userId.equals(browseList.get(position).getSender_type())) {
                    return TYPE_OWN_MESSAGE;
                } else {
                    return TYPE_OWN_MESSAGE_SECOND;
                }
            } else {
                S.E(" user else");
                if (position == 0 && userId.equals(browseList.get(position).getSender_type())) {
                    return TYPE_OPPONENT_MESSAGE;
                } else {
                    return TYPE_OPPONENT_MESSAGE_SECOND;
                }
            }
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        S.E("view type = " + viewType);
        switch (viewType) {
            case TYPE_OWN_MESSAGE:
                return new ChatAdapter_1.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message_own, parent, false));
            case TYPE_OWN_MESSAGE_SECOND:
                return new ChatAdapter_1.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message_own_second, parent, false));
            case TYPE_OPPONENT_MESSAGE:
                return new ChatAdapter_1.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message_opponent, parent, false));
            case TYPE_OPPONENT_MESSAGE_SECOND:
                return new ChatAdapter_1.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message_opponent_second, parent, false));
            case TYPE_REQUEST_MESSAGE:
                return new ChatAdapter_1.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.presence_message, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
       try
       {
           Messages messages = browseList.get(position);
           holder.messageText.setText(messages.getMsg());
       }
       catch (Exception e )
       {
           e.printStackTrace();
       }
    }

    @Override
    public int getItemCount() {
        return browseList.size();
    }
}