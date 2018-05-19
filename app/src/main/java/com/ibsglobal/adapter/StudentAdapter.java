package com.ibsglobal.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ibsglobal.R;
import com.ibsglobal.interfaces.OnItemClickCustom;
import com.ibsglobal.models.StudentModel;

import java.util.ArrayList;

/**
 * Created by Ravi on 6/3/2017.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder>  {

    private Context mContext;
    private OnItemClickCustom mListener;

    public StudentAdapter(Context  context, OnItemClickCustom listener){
        this.mContext = context;
        this.mListener = listener;
    }

    private ArrayList<StudentModel> mList;

    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item_row, parent,false);
        return new ViewHolder(v);
    }

    public void setList(ArrayList<StudentModel> list){
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(StudentAdapter.ViewHolder holder, int position) {
        holder.bind(mList.get(position),mListener,position);
    }

    @Override
    public int getItemCount() {
        return null==mList?0:mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView mNameTV;
        public ImageView mSelectedIV;

        public ViewHolder(View itemView) {
            super(itemView);
            mNameTV = (TextView) itemView.findViewById(R.id.name_tv);
            mSelectedIV = (ImageView) itemView.findViewById(R.id.select_iv);
        }

        public void bind(final StudentModel item, final OnItemClickCustom listner, final int position) {
            if(item.isSelected()){
                mSelectedIV.setImageResource(R.drawable.ic_green_check);
            }
            else{
                mSelectedIV.setImageResource(R.drawable.ic_green_uncheck);
            }
            mSelectedIV.setTag(position);
            mSelectedIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = (int) mSelectedIV.getTag();
                    mListener.onClick(0,pos,null);
                    /*if(mList.get(pos).isSelected()){
                        mList.get(pos).setSelected(false);
                    }
                    else{
                        mList.get(pos).setSelected(true);
                    }
                    notifyDataSetChanged();*/
                }
            });
            mNameTV.setText(item.getFname()+" "+item.getLname());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onClick(0,position,item);
                }
            });
        }
    }
}
