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
import com.raynsmartschool.auth.ParentDashboardActivity;
import com.raynsmartschool.interfaces.OnItemClickCustom;
import com.raynsmartschool.models.AttendanceModel;
import com.raynsmartschool.models.StudentAttendanceMonthModel;
import com.raynsmartschool.session.SessionParam;
import com.raynsmartschool.utility.DialogUtil;
import com.raynsmartschool.utility.Functions;
import com.tapadoo.alerter.OnHideAlertListener;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

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
    private boolean fromFCM = false;

    private boolean isAllSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance_list);
        mContext = this;
        ButterKnife.bind(this);
        fromFCM = getIntent().getBooleanExtra("FromFCM",false);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mNoItemTV = (TextView) findViewById(R.id.no_item_tv);
        class_name = getIntent().getStringExtra("classname");
        section_name = getIntent().getStringExtra("section");
        SessionParam.resetNotificationPref(this,3);
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
                            if(formatedList().size()==0){
                                mNoItemTV.setVisibility(View.VISIBLE);
                            }
                            else{
                                mNoItemTV.setVisibility(View.GONE);
                            }
                            mAdapter.setList(formatedList());
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
        String StudentId = "";
        if(null!=Functions.getInstance().getmStudent()){
            StudentId = Functions.getInstance().getmStudent().getStudent_id();
        }
        object = Functions.getInstance().getJsonObject(
                "session_key", new SessionParam(mContext).session_key,
                "student_id",StudentId);
        baseRequest.callAPIPost(1, object, getAppString(R.string.api_attendance_list));
    }

    private String[] months = new String[]{"April","May","June","july","August","September","October","November","December","January","February","March"};
    private ArrayList<StudentAttendanceMonthModel> formatedList(){
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(calendar.getTime());
        ArrayList<StudentAttendanceMonthModel> mMonthSortedList = new ArrayList<>();
       /* for(int m=0;m<months.length;m++) {
            for (int i = 0; i < mList.size(); i++) {
                StudentAttendanceMonthModel model = mList.get(i);
                if(months[m].equalsIgnoreCase(model.getMonth_name())){
                    int presentCount = Integer.parseInt(model.getPresent_days());
                    int absentCount = Integer.parseInt(model.getAbsent_count());
                    int holidayCount = Integer.parseInt(model.getHolidays());
                    int sum = presentCount+absentCount+holidayCount;
                    if(sum>0){
                        mMonthSortedList.add(model);
                    }
                    break;
                }
            }
        }*/
       // for(int m=0;m<months.length;m++) {
            for (int i = 0; i < mList.size(); i++) {
                mMonthSortedList.add(mList.get(i));
                if(months[i].equalsIgnoreCase(month_name)) {
                    break;
                }
            }


    //    }
        return mMonthSortedList;
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
                if(fromFCM){
                    startActivity(ParentDashboardActivity.getIntent(mContext));
                    finish();
                }
                else {
                    super.onBackPressed();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if(fromFCM){
            startActivity(ParentDashboardActivity.getIntent(mContext));
            finish();
        }
        else {
            super.onBackPressed();
        }
    }


    public static Intent getIntent(Context context, boolean fromFCM){
        Intent intent = new Intent(context,StudentAttendanceListActivity.class);
        intent.putExtra("FromFCM",fromFCM);
        return intent;
    }
}
