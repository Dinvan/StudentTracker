package com.studenttracker.school;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.studenttracker.R;
import com.studenttracker.adapter.AnnouncementAdapter;
import com.studenttracker.auth.BaseActivity;
import com.studenttracker.auth.LoginActivity;
import com.studenttracker.auth.ParentDashboardActivity;
import com.studenttracker.auth.VerificationActivity;
import com.studenttracker.models.Announcement;
import com.studenttracker.utility.Config;
import com.studenttracker.utility.DialogUtil;
import com.studenttracker.utility.DividerItemDecoration;
import com.studenttracker.utility.Functions;

import org.json.JSONObject;

import java.util.ArrayList;

import RetroFit.BaseRequest;
import RetroFit.RequestReceiver;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi on 6/1/2017.
 */

public class AnnouncementActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private TextView mNoItemTV;
    private int mType;
    private AnnouncementAdapter mAdapter;
    private BaseRequest baseRequest;
    private ArrayList<Announcement> mList;
    private String requestType;

    @Bind(R.id.progressBar)
    ProgressBar mLoader;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_list);
        mContext = this;
        ButterKnife.bind(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mNoItemTV = (TextView) findViewById(R.id.no_item_tv);
        mType = getIntent().getIntExtra("Type",0);
        if(mType==Config.TYPE_ANNOUNCEMENT){
            mNoItemTV.setText(getString(R.string.no_announcement));
            requestType = "announcement";
            title = getString(R.string.announcement_header);
        }
        else if(mType==Config.TYPE_HOMEWORK){
            mNoItemTV.setText(getString(R.string.no_homework));
            requestType = "homework";
            title = getString(R.string.home_work_header);
        }
        initToolBar();
        initRecycleView();
        requestAnnouncementList();
    }

    private void initRecycleView(){
        mAdapter = new AnnouncementAdapter(mContext);
 //       mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = mRecyclerView.getChildPosition(view);
            }
        });
    }


    public void requestAnnouncementList() {

        baseRequest = new BaseRequest(this);
        baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                if (dataObject != null) {

                    if(baseRequest.status) {
                        JSONObject json = (JSONObject) dataObject;
                        mList = baseRequest.getDataList(json.optJSONArray("list"),Announcement.class);


                        if(null==mList || mList.size()==0){
                            mNoItemTV.setVisibility(View.VISIBLE);
                        }
                        else{
                            mAdapter.setList(mList);
                            mNoItemTV.setVisibility(View.GONE);
                        }
                    }
                    else{
                        mNoItemTV.setVisibility(View.VISIBLE);
                        DialogUtil.Alert(AnnouncementActivity.this, baseRequest.message, DialogUtil.AlertType.Error);
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                if (requestCode==417){
                    DialogUtil.Alert(AnnouncementActivity.this, message, DialogUtil.AlertType.Error);


                }else {
                    DialogUtil.Alert(AnnouncementActivity.this, message, DialogUtil.AlertType.Error);

                }

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });

        JsonObject object = null;

        object = Functions.getInstance().getJsonObject(
                "type", requestType);


        baseRequest.callAPIPost(1, object, getAppString(R.string.api_announcement));
    }

    private ActionBar mActionBar;
    private Toolbar toolbar;

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
               super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public static Intent getIntent(Context context,int type){
        Intent intent = new Intent(context,AnnouncementActivity.class);
        intent.putExtra("Type", type);
        return intent;
    }
}
