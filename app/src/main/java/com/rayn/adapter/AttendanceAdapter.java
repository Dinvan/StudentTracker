package com.rayn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rayn.R;
import com.rayn.interfaces.OnItemClickCustom;
import com.rayn.models.AttendanceModel;

import java.util.ArrayList;

/**
 * Created by Ravi on 6/3/2017.
 */

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder>  {

    private Context mContext;
    private OnItemClickCustom mListener;

    public AttendanceAdapter(Context  context, OnItemClickCustom listener){
        this.mContext = context;
        this.mListener = listener;
    }

    private ArrayList<AttendanceModel> mList;

    @Override
    public AttendanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_item_row, parent,false);
        return new ViewHolder(v);
    }

    public void setList(ArrayList<AttendanceModel> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(AttendanceAdapter.ViewHolder holder, int position) {
        holder.bind(mList.get(position),mListener,position);
    }

    @Override
    public int getItemCount() {
        return null==mList?0:mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mNameTV;
        public ImageView mPresentIV,mAbsentIV,mHolidayIV;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameTV = (TextView) itemView.findViewById(R.id.name_tv);
            mPresentIV = (ImageView) itemView.findViewById(R.id.present_iv);
            mAbsentIV = (ImageView) itemView.findViewById(R.id.absent_iv);
            mHolidayIV = (ImageView) itemView.findViewById(R.id.holiday_iv);
        }

        public void bind(final AttendanceModel item, final OnItemClickCustom listner, final int position) {
            if(item.getStatus()==1){
                mPresentIV.setImageResource(R.drawable.ic_green_check);
                mAbsentIV.setImageResource(R.drawable.ic_green_uncheck);
                mHolidayIV.setImageResource(R.drawable.ic_green_uncheck);
            }
            else if(item.getStatus()==2){
                mPresentIV.setImageResource(R.drawable.ic_green_uncheck);
                mAbsentIV.setImageResource(R.drawable.ic_green_check);
                mHolidayIV.setImageResource(R.drawable.ic_green_uncheck);
            }
            else if(item.getStatus()==3){
                mPresentIV.setImageResource(R.drawable.ic_green_uncheck);
                mAbsentIV.setImageResource(R.drawable.ic_green_uncheck);
                mHolidayIV.setImageResource(R.drawable.ic_green_check);
            }
            else{
                mPresentIV.setImageResource(R.drawable.ic_green_uncheck);
                mAbsentIV.setImageResource(R.drawable.ic_green_uncheck);
                mHolidayIV.setImageResource(R.drawable.ic_green_uncheck);
            }
            mPresentIV.setTag(position);
            mPresentIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) mPresentIV.getTag();
                    mListener.onClick(1,pos,null);

                }
            });
            mAbsentIV.setTag(position);
            mAbsentIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) mAbsentIV.getTag();
                    mListener.onClick(2,pos,null);

                }
            });
            mHolidayIV.setTag(position);
            mHolidayIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) mHolidayIV.getTag();
                    mListener.onClick(3,pos,null);

                }
            });
            mNameTV.setText(item.getFname()+" "+item.getLname());
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onClick(0,position,item);
                }
            });*/
        }
    }
}
