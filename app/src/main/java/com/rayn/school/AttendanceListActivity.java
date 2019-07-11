package com.rayn.school;

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
import com.rayn.R;
import com.rayn.adapter.AttendanceAdapter;
import com.rayn.auth.BaseActivity;
import com.rayn.interfaces.OnItemClickCustom;
import com.rayn.models.AttendanceModel;
import com.rayn.session.SessionParam;
import com.rayn.utility.DialogUtil;
import com.rayn.utility.Functions;
import com.tapadoo.alerter.OnHideAlertListener;

import org.json.JSONObject;

import java.util.ArrayList;

import RetroFit.BaseRequest;
import RetroFit.RequestReceiver;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ravi on 6/1/2017.
 */

public class AttendanceListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private TextView mNoItemTV;
    private String  class_name,section_name;
    private AttendanceAdapter mAdapter;
    private BaseRequest baseRequest;
    private ArrayList<AttendanceModel> mList;
    private String requestType;

    @BindView(R.id.progressBar)
    ProgressBar mLoader;
    @BindView(R.id.submit_btn)
    Button mSubmitBtn;
    private String title;
    @BindView(R.id.select_iv)
    ImageView mSelectAllIV;

    private boolean isAllSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        mContext = this;
        ButterKnife.bind(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mNoItemTV = (TextView) findViewById(R.id.no_item_tv);
        class_name = getIntent().getStringExtra("classname");
        section_name = getIntent().getStringExtra("section");
        initToolBar();
        initRecycleView();
        requestStudentList();
    }

    private void initRecycleView(){
        mAdapter = new AttendanceAdapter(mContext,new OnItemClickCustom(){

            @Override
            public void onClick(int id, int position, Object object) {
                mList.get(position).setStatus(id);
                mAdapter.setList(mList);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void requestStudentList() {
        baseRequest = new BaseRequest(this);
        baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                if (dataObject != null) {
                    if(baseRequest.status) {
                        JSONObject json = (JSONObject) dataObject;
                        mList = baseRequest.getDataList(json.optJSONArray("student"),AttendanceModel.class);
                        //     Collections.reverse(mList);
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
                        DialogUtil.Alert(AttendanceListActivity.this, baseRequest.message, DialogUtil.AlertType.Error);
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                if (requestCode==417){
                    DialogUtil.Alert(AttendanceListActivity.this, message, DialogUtil.AlertType.Error);


                }else {
                    DialogUtil.Alert(AttendanceListActivity.this, message, DialogUtil.AlertType.Error);

                }

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });
        JsonObject object = null;
        object = Functions.getInstance().getJsonObject(
                "class", class_name,"section", section_name);
        baseRequest.callAPIPost(1, object, getAppString(R.string.api_student_list));
    }

    public void submitStudentAttendance() {
        baseRequest = new BaseRequest(this);
        baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                if (dataObject != null) {
                    if(baseRequest.status) {
                        JSONObject json = (JSONObject) dataObject;
                        DialogUtil.Alert(AttendanceListActivity.this, baseRequest.message, DialogUtil.AlertType.Success, new OnHideAlertListener() {
                            @Override
                            public void onHide() {
                                finish();
                            }
                        });
                    }
                    else{
                        DialogUtil.Alert(AttendanceListActivity.this, baseRequest.message, DialogUtil.AlertType.Error);
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                if (requestCode==417){
                    DialogUtil.Alert(AttendanceListActivity.this, message, DialogUtil.AlertType.Error);


                }else {
                    DialogUtil.Alert(AttendanceListActivity.this, message, DialogUtil.AlertType.Error);

                }

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });

        JsonObject object = null;

        object = Functions.getInstance().getJsonObject(
                "teacher_id", new SessionParam(mContext).user_guid);
        JsonArray jArray = new JsonArray();
        for(int i=0;i<mList.size();i++){
            JsonObject student = new JsonObject();
            student.addProperty("student_id",mList.get(i).getStudent_id());
            student.addProperty("status",mList.get(i).getStatus());
            jArray.add(student);
        }
        object.add("students",jArray);
        baseRequest.callAPIPost(1, object, getAppString(R.string.api_attendance_submit));
    }

    private ActionBar mActionBar;
    private Toolbar toolbar;

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(getString(R.string.class_attendance));
    }

    @OnClick({R.id.submit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:
                submitStudentAttendance();
                break;
        }
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

    public static Intent getIntent(Context context,String classname,String section){
        Intent intent = new Intent(context,AttendanceListActivity.class);
        intent.putExtra("classname", classname);
        intent.putExtra("section", section);
        return intent;
    }
}
