package com.studenttracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.studenttracker.R;
import com.studenttracker.models.Announcement;
import com.studenttracker.utility.Functions;
import com.studenttracker.utility.UtilityFunctions;

import java.util.ArrayList;

/**
 * Created by Ravi on 6/3/2017.
 */

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder>  {

    private Context mContext;
    public AnnouncementAdapter(Context  context){
        this.mContext = context;
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
        holder.mTitleTV.setText(mList.get(position).getMessage());
        try {
            String date = Functions.getInstance().formatDate(mList.get(position).getDate_created(), "yyyy-MM-dd hh:mm:ss.a", "MMM dd, yyyy hh:mm a");
            holder.mDateTV.setText(date);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return null==mList?0:mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mTitleTV;
        public TextView mDateTV;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitleTV = (TextView) itemView.findViewById(R.id.title_tv);
            mDateTV = (TextView) itemView.findViewById(R.id.time_tv);
        }
    }
}
