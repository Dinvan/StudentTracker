package com.raynsmartschool.school;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.raynsmartschool.R;
import com.raynsmartschool.adapter.AttendanceAdapter;
import com.raynsmartschool.adapter.StudentAttendanceAdapter;
import com.raynsmartschool.auth.BaseActivity;
import com.raynsmartschool.interfaces.OnItemClickCustom;
import com.raynsmartschool.models.AttendanceModel;
import com.raynsmartschool.models.StudentAttendanceMonthModel;
import com.raynsmartschool.session.SessionParam;
import com.raynsmartschool.utility.DialogUtil;
import com.raynsmartschool.utility.Functions;
import com.tapadoo.alerter.OnHideAlertListener;

import org.json.JSONObject;

import java.util.ArrayList;

import RetroFit.BaseRequest;
import RetroFit.RequestReceiver;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ravi on 6/1/2017.
 */

public class StudentAttendanceListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private TextView mNoItemTV;
    private String  class_name,section_name;
    private StudentAttendanceAdapter mAdapter;
    private BaseRequest baseRequest;
    private ArrayList<StudentAttendanceMonthModel> mList;
    private String requestType;

    @Bind(R.id.progressBar)
    ProgressBar mLoader;
    private String title;

    private boolean isAllSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance_list);
        mContext = this;
        ButterKnife.bind(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mNoItemTV = (TextView) findViewById(R.id.no_item_tv);
        class_name = getIntent().getStringExtra("classname");
        section_name = getIntent().getStringExtra("section");
        initToolBar();
        initRecycleView();
        requestAttendanceList();
    }

    private void initRecycleView(){
        mAdapter = new StudentAttendanceAdapter(mContext,new OnItemClickCustom(){

            @Override
            public void onClick(int id, int position, Object object) {

            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
    }


    public void requestAttendanceList() {

        baseRequest = new BaseRequest(this);
        baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                if (dataObject != null) {
                    if(baseRequest.status) {
                        JSONObject json = (JSONObject) dataObject;
                        mList = baseRequest.getDataList(json.optJSONArray("attandance"),StudentAttendanceMonthModel.class);
                        //     Collections.reverse(mList);
                        if(null==mList || mList.size()==0){
                            mNoItemTV.setVisibility(View.GONE);
                        }
                        else{
                            mAdapter.setList(mList);
                            mNoItemTV.setVisibility(View.GONE);
                        }
                    }
                    else{
                        mNoItemTV.setVisibility(View.GONE);
                        DialogUtil.Alert(StudentAttendanceListActivity.this, baseRequest.message, DialogUtil.AlertType.Error);
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                if (requestCode==417){
                    DialogUtil.Alert(StudentAttendanceListActivity.this, message, DialogUtil.AlertType.Error);


                }else {
                    DialogUtil.Alert(StudentAttendanceListActivity.this, message, DialogUtil.AlertType.Error);

                }
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });
        JsonObject object = null;
        object = Functions.getInstance().getJsonObject(
                "session_key", new SessionParam(mContext).session_key);
        baseRequest.callAPIPost(1, object, getAppString(R.string.api_attendance_list));
    }

    private ActionBar mActionBar;
    private Toolbar toolbar;

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(getString(R.string.your_attendance));
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

    public static Intent getIntent(Context context){
        Intent intent = new Intent(context,StudentAttendanceListActivity.class);
        return intent;
    }
}
