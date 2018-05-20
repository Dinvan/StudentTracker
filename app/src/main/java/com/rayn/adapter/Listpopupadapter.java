package com.rayn.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rayn.R;
import com.rayn.interfaces.MenuSelectionListener;

import java.util.ArrayList;

/**
 * Created by Ravi on 7/8/2017.
 */

public class Listpopupadapter extends BaseAdapter {
    ArrayList<String> mArrList;
    String mGameName;
    MenuSelectionListener mMenuListener;
    private Context mContext;
    private int mSelectedPos = 0;

    public Listpopupadapter(Context context, ArrayList<String> a, MenuSelectionListener listener) {
        this.mArrList = a;
        this.mMenuListener = listener;
        this.mContext = context;
    }

    public void selectedPos(int pos) {
        mSelectedPos = pos;
    }

    @Override
    public int getCount() {
        return mArrList.size();
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.popup_item, null);
            viewHolder = new ViewHolder();
            viewHolder.mItemTv = (TextView) convertView.findViewById(R.id.item_tv);
            viewHolder.mContainerRL = (RelativeLayout) convertView.findViewById(R.id.main_rl);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mItemTv.setText(mArrList.get(position));

        if (position == mSelectedPos) {
            viewHolder.mContainerRL.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        else{
            viewHolder.mContainerRL.setBackgroundColor(mContext.getResources().getColor(R.color.White));
        }

        viewHolder.mContainerRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMenuListener.selectedMenu(position);
            }
        });

       /* if(position==mArrList.size()-1){
            viewHolder.mShadowView.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.mShadowView.setVisibility(View.GONE);
        }*/

        return convertView;
    }

    class ViewHolder {
        private TextView mItemTv;
        private RelativeLayout mContainerRL;
    }
}
