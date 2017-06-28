package com.raynsmartschool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.raynsmartschool.R;
import com.raynsmartschool.interfaces.OnItemClickCustom;
import com.raynsmartschool.models.AttendanceModel;
import com.raynsmartschool.models.StudentAttendanceMonthModel;

import java.util.ArrayList;

/**
 * Created by Ravi on 6/3/2017.
 */

public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder>  {

    private Context mContext;
    private OnItemClickCustom mListener;

    public StudentAttendanceAdapter(Context  context, OnItemClickCustom listener){
        this.mContext = context;
        this.mListener = listener;
    }

    private ArrayList<StudentAttendanceMonthModel> mList;

    @Override
    public StudentAttendanceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_attendance_item, parent,false);
        return new ViewHolder(v);
    }

    public void setList(ArrayList<StudentAttendanceMonthModel> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(StudentAttendanceAdapter.ViewHolder holder, int position) {
        holder.bind(mList.get(position),mListener,position);
    }

    @Override
    public int getItemCount() {
        return null==mList?0:mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mMonthNameTV,mPresentDaysTV,mAbsentDaysTV,mHolidaysTV;

        public ViewHolder(View itemView) {
            super(itemView);
            mMonthNameTV = (TextView) itemView.findViewById(R.id.month_name_tv);
            mPresentDaysTV = (TextView) itemView.findViewById(R.id.present_days_tv);
            mAbsentDaysTV = (TextView) itemView.findViewById(R.id.absent_days_tv);
            mHolidaysTV = (TextView) itemView.findViewById(R.id.holiday_days_tv);
        }

        public void bind(final StudentAttendanceMonthModel item, final OnItemClickCustom listner, final int position) {
            mMonthNameTV.setText(item.getMonth_name());
            mPresentDaysTV.setText(item.getPresent_days());
            mAbsentDaysTV.setText(item.getAbsent_days());
            mHolidaysTV.setText(item.getHolidays());
        }
    }
}
