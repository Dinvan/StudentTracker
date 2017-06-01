package com.studenttracker.school;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.studenttracker.R;
import com.studenttracker.auth.BaseActivity;

/**
 * Created by Ravi on 6/1/2017.
 */

public class AnnouncementActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private TextView mNoItemTV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_list);
        mContext = this;
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mNoItemTV = (TextView) findViewById(R.id.no_item_tv);
    }


    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,AnnouncementActivity.class);
        return intent;
    }
}
