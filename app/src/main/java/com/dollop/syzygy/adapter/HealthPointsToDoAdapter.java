package com.dollop.syzygy.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dollop.syzygy.Model.HealthTipsModel;
import com.dollop.syzygy.R;
import com.dollop.syzygy.activity.client.HealthPointsActivity;
import com.dollop.syzygy.sohel.Const;
import com.dollop.syzygy.sohel.Helper;
import com.dollop.syzygy.sohel.JSONParser;
import com.dollop.syzygy.sohel.S;
import com.dollop.syzygy.sohel.SavedData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by arpit on 4/11/17.
 */

public class HealthPointsToDoAdapter extends RecyclerView.Adapter<HealthPointsToDoAdapter.MyViewHolder> {
    HealthPointsToDoAdapter healthPointsAdapter;
    //HireYourCaregiverModel hireYourCaregiverModel;
    HealthTipsModel healthTipsModel;
    List<HealthTipsModel> healthPointsModelList;
String CategoryId = "289";
    Context context;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_health_tips_todo_points, parent, false);


        return new MyViewHolder(itemView);
    }

    public HealthPointsToDoAdapter(Context context, List<HealthTipsModel> healthPointsModelList) {
        this.healthPointsModelList = healthPointsModelList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        healthTipsModel = healthPointsModelList.get(position);

        holder.healthCategoryTV.setText(Html.fromHtml(healthTipsModel.getCategoryName()));
        holder.cardViewHealth.setTag(""+position);
        holder.cardViewHealth.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
               try
               {
                   String index = v.getTag().toString();
                  final int pos = Integer.parseInt(index);
                   v.setBackgroundColor(Color.parseColor("#aaaaaa"));
                  // Toast.makeText(context,""+index,Toast.LENGTH_SHORT).show();

                   AlertDialog.Builder builder = new AlertDialog.Builder(context);
                   builder.setMessage("Are you sure you want to delete this from list?");

                   builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           //perform any action
                           v.setBackgroundColor(Color.parseColor("#ffffff"));
                           CategoryId = healthPointsModelList.get(pos).getParentId();
//String point_id = healthPointsModelList.get(pos).getCategoryId();
                          String pointsIds = "";
                           for (int i = 0; i < healthPointsModelList.size(); i++)
                           {
                               if (healthPointsModelList.get(i).getParentId().equalsIgnoreCase(CategoryId))
                               {
                                   if(pos != i)
                                   {
                                       if ("".equals(pointsIds))
                                       {
                                           pointsIds = healthPointsModelList.get(i).getCategoryId();
                                       } else {
                                           pointsIds = pointsIds + "," + healthPointsModelList.get(i).getCategoryId();
                                       }
                                   }

                               }
                           }

                           addChecked(pointsIds);

                           healthPointsModelList.remove(pos);
                           notifyDataSetChanged();

                       }
                   });

                   builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           //perform any action
                           v.setBackgroundColor(Color.parseColor("#ffffff"));

                       }
                   });

                   //creating alert dialog
                   AlertDialog alertDialog = builder.create();
                   alertDialog.show();


               }
               catch (Exception e)
               {
                   e.printStackTrace();
               }
                return false;
            }
        });
     /*   if (healthTipsModel.getStatus().equals("1")) {
            S.E("if working");
            holder.imageDisLike.setVisibility(View.VISIBLE);
            holder.imageLike.setVisibility(View.GONE);
        } else {
            S.E("else working");
            holder.imageDisLike.setVisibility(View.GONE);
            holder.imageLike.setVisibility(View.VISIBLE);
        }*/

      /*  holder.cardViewHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, HealthPointsActivity.class);
                Bundle b = new Bundle();
                intent.putExtra("imageforpoint",healthTipsModel.getImage());
                b.putSerializable("AddSaleitems", coSubHealthModelList.get(position));
                intent.putExtras(b);
                context.startActivity(intent);
            }
        });*/
       /* holder.imageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //addCheckList(healthTipsModel, holder);

                holder.imageDisLike.setVisibility(View.VISIBLE);
                holder.imageLike.setVisibility(View.GONE);
                ((HealthPointsActivity)context).addSelectedListFromAdapter(position);
            }
        });
        holder.imageDisLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addCheckList(healthTipsModel, holder);

                holder.imageLike.setVisibility(View.VISIBLE);
                holder.imageDisLike.setVisibility(View.GONE);
                ((HealthPointsActivity)context).addUnselectedId(position);
            }
        });*/
    }



    private void addChecked(String pointsIds) {
        new JSONParser(context).parseVollyStringRequest(Const.URL.AddHealthPoint, 1, getParams2(pointsIds), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("CheckResponse" + response);
                try {
                    JSONObject mainobject = new JSONObject(response);
                    int status = mainobject.getInt("status");
                    String messgae = mainobject.getString("message");
                    if (status == 200) {
                        Toast.makeText(context,"Todo list updated successfully",Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    S.E("checkMessage" + e);
                }
            }
        });
    }

    private Map<String, String> getParams2(String pointsIdssssss) {
        HashMap<String, String> params = new HashMap<>();
        params.put("token", SavedData.gettocken_id());
        params.put("CoSubCategory_id", CategoryId);
        params.put("points_ids", pointsIdssssss);


        return params;
    }

    /*
    private void addCheckList(int position) {
        new JSONParser(context).parseVollyStringRequest(Const.URL.ADD_CHECKLIST, 1, getPrams(position), new Helper() {
            @Override
            public void backResponse(String response) {
                S.E("Add to checklist : " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equals("200"))
                    {
                    } else if (jsonObject.getString("status").equals("300"))
                    {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }*/

    private Map<String, String> getPrams(int position)
    {
        HashMap<String, String> prams = new HashMap<>();
        prams.put("token", SavedData.gettocken_id());
        prams.put("CoSubCategory_id", healthPointsModelList.get(position).getCategoryId());
        return prams;
    }

    @Override
    public int getItemCount() {
        return healthPointsModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView healthCategoryTV;
        CardView cardViewHealth;
    /*    ImageView imageLike;
        ImageView imageDisLike;*/

        public MyViewHolder(View itemView) {
            super(itemView);
            healthCategoryTV = (TextView) itemView.findViewById(R.id.healthCategoryTV);
            cardViewHealth = (CardView) itemView.findViewById(R.id.cardViewHealth);
         /*   imageLike = (ImageView) itemView.findViewById(R.id.imageLike);
            imageDisLike = (ImageView) itemView.findViewById(R.id.imageDisLike);*/
        }
    }
}
