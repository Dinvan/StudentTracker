package com.raynsmartschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.raynsmartschool.R;
import com.raynsmartschool.interfaces.OnItemClickCustom;
import com.raynsmartschool.models.Announcement;
import com.raynsmartschool.utility.Functions;

import java.util.ArrayList;

/**
 * Created by Ravi on 6/3/2017.
 */

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>  {

    private Context mContext;
    private OnItemClickCustom mListener;
    public AnnouncementAdapter(Context  context, OnItemClickCustom listener){
        this.mContext = context;
        this.mListener = listener;
    }

    private ArrayList<Announcement> mList;

    @Override
    public AnnouncementAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.announcement_row, parent,false);
        return new ViewHolder(v);
    }

    public void setList(ArrayList<Announcement> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AnnouncementAdapter.ViewHolder holder, int position) {
        holder.bind(mList.get(position),mListener,position);
       /* if(!TextUtils.isEmpty(mList.get(position).getMessage())){
            holder.mTitleTV.setText(mList.get(position).getMessage());
        }
        else{
            holder.mTitleTV.setText("Image");
        }
        try {
            String date = Functions.getInstance().formatDate(mList.get(position).getDate_created(), "dd-MM-yyyy hh:mm:ss", "MMM dd, yyyy hh:mm a");
            holder.mDateTV.setText(date);
        }
        catch (Exception e){
            e.printStackTrace();
            holder.mDateTV.setText(mList.get(position).getDate_created());
        }*/

    }

    @Override
    public int getItemCount() {
        return null==mList?0:mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTitleTV;
        public TextView mDateTV;
        public RelativeLayout mBoxRL;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitleTV = (TextView) itemView.findViewById(R.id.title_tv);
            mDateTV = (TextView) itemView.findViewById(R.id.time_tv);
            mBoxRL = (RelativeLayout) itemView.findViewById(R.id.box_rl);

        }

        public void bind(final Announcement item, final OnItemClickCustom listner, final int position) {
            if(!TextUtils.isEmpty(mList.get(position).getTitle())){
                mTitleTV.setText(mList.get(position).getTitle());
            }
            else{
                mTitleTV.setText(mList.get(position).getTitle());
            }
            try {
                String date = Functions.getInstance().formatDate(mList.get(position).getDate_created(), "dd-MM-yyyy hh:mm:ss", "MMM dd, yyyy hh:mm a");
                mDateTV.setText(date);
            }
            catch (Exception e){
                e.printStackTrace();
                mDateTV.setText(mList.get(position).getDate_created());
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onClick(0,position,item);
                }
            });
            if(mList.get(position).getRead_status().equals("1")){
                mBoxRL.setBackgroundColor(mContext.getResources().getColor(R.color.White));
            }
            else{
                mBoxRL.setBackgroundColor(mContext.getResources().getColor(R.color.White));
            }
        }
    }
}
