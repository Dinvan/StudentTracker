package com.ibsglobal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ibsglobal.R;
import com.ibsglobal.interfaces.OnItemClickCustom;
import com.ibsglobal.models.StudentAttendanceMonthModel;
import com.ibsglobal.utility.Dialogs;
import com.ibsglobal.utility.Functions;

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
        public RelativeLayout mAbsentRL;

        public ViewHolder(View itemView) {
            super(itemView);
            mMonthNameTV = (TextView) itemView.findViewById(R.id.month_name_tv);
            mPresentDaysTV = (TextView) itemView.findViewById(R.id.present_days_tv);
            mAbsentDaysTV = (TextView) itemView.findViewById(R.id.absent_days_tv);
            mHolidaysTV = (TextView) itemView.findViewById(R.id.holiday_days_tv);
            mAbsentRL = (RelativeLayout) itemView.findViewById(R.id.absents_rl);
        }

        public void bind(final StudentAttendanceMonthModel item, final OnItemClickCustom listner, final int position) {
            mMonthNameTV.setText(item.getMonth_name());
            mPresentDaysTV.setText(item.getPresent_days());
            mAbsentDaysTV.setText(item.getAbsent_count());
            mHolidaysTV.setText(item.getHolidays());
            if(!TextUtils.isEmpty(item.getAbsent_count())){
                int abs = Integer.parseInt(item.getAbsent_count());
                if(abs>0){
                    mAbsentRL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ArrayList<String> dates = new ArrayList<String>();
                            for(int i=0;i<item.getAbsent_days().length;i++){
                                String date = Functions.getInstance().formatDate(item.getAbsent_days()[i], "yyyy-MM-dd HH:mm:ss", "MMM dd, yyyy");
                                dates.add(date);
                            }
                            Dialogs.showListSelection(mContext,R.string.absents,null,dates);
                        }
                    });
                }
            }

        }
    }
}
