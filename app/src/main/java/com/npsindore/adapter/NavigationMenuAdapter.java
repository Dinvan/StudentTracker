package com.npsindore.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.npsindore.R;
import com.npsindore.auth.ParentDashboardActivity;
import com.npsindore.models.NavigationMenuModel;

import java.util.ArrayList;


/**
 * Created by ravib on 05/27/2017.
 */
public class NavigationMenuAdapter extends BaseAdapter {

    public static TextView count_tv;
    ArrayList<NavigationMenuModel> menuList;
    private LayoutInflater mInflater;
    private Context mContext;

    Typeface robotoRegular;
    Typeface robotoMedium;
    Typeface robotoBold;
    int count = 0;

    public NavigationMenuAdapter(Context ct, ArrayList<NavigationMenuModel> menuList_) {
        mContext = ct;
        menuList = menuList_;
        mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return null == menuList ? 0 : menuList.size();
    }

    @Override
    public NavigationMenuModel getItem(int position) {
        return menuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.navigation_row, null);

        ImageView mIconIV = (ImageView) convertView.findViewById(R.id.icon_iv);
        View selected = (View) convertView.findViewById(R.id.selected);
        View lastDiv = (View) convertView.findViewById(R.id.last_div);
        TextView mMenuTV = (TextView) convertView.findViewById(R.id.menu_tv);
        count_tv = (TextView) convertView.findViewById(R.id.count_tv);
        NavigationMenuModel ncn = getItem(position);
        mMenuTV.setText(ncn.getName());
        mIconIV.setImageResource(ncn.getIcon());
        Drawable drawable = mIconIV.getDrawable();
        mMenuTV.setTextColor(mContext.getResources().getColor(R.color.White));
        if (((ParentDashboardActivity) mContext).getSelectedItemPosi(position)) {
            selected.setVisibility(View.GONE);
            mMenuTV.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            mMenuTV.setTypeface(null, Typeface.BOLD);
        } else {
            selected.setVisibility(View.GONE);
            mMenuTV.setTextColor(mContext.getResources().getColor(R.color.White));
            mMenuTV.setTypeface(null, Typeface.BOLD);
        }
        if(position==menuList.size()-1){
            lastDiv.setVisibility(View.VISIBLE);
        }
        else{
            lastDiv.setVisibility(View.GONE);
        }

       /* if (position == 6) {
            if (count == 0) {
                count_tv.setVisibility(View.GONE);
                count_tv.setText("" + count);
            } else {
                count_tv.setVisibility(View.VISIBLE);
                count_tv.setText("" + count);
            }

        }
*/
        return convertView;
    }

    public void updatecount(String count_) {
        count = Integer.parseInt(count_);
        notifyDataSetChanged();
    }
}
