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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.raynsmartschool.R;
import com.raynsmartschool.adapter.AnnouncementAdapter;
import com.raynsmartschool.adapter.StudentAdapter;
import com.raynsmartschool.auth.BaseActivity;
import com.raynsmartschool.interfaces.OnItemClickCustom;
import com.raynsmartschool.models.Announcement;
import com.raynsmartschool.models.StudentModel;
import com.raynsmartschool.utility.Config;
import com.raynsmartschool.utility.DialogUtil;
import com.raynsmartschool.utility.Functions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import RetroFit.BaseRequest;
import RetroFit.RequestReceiver;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ravi on 6/1/2017.
 */

public class StudentListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private TextView mNoItemTV;
    private String  class_name,section_name;
    private StudentAdapter mAdapter;
    private BaseRequest baseRequest;
    private ArrayList<StudentModel> mList;
    private String requestType;

    @Bind(R.id.progressBar)
    ProgressBar mLoader;
    @Bind(R.id.submit_btn)
    Button mSubmitBtn;
    private String title;
    @Bind(R.id.select_iv)
    ImageView mSelectAllIV;
    @Bind(R.id.all_rl)
    RelativeLayout mAllRL;

    private boolean isAllSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        mContext = this;
        ButterKnife.bind(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mNoItemTV = (TextView) findViewById(R.id.no_item_tv);
        class_name = getIntent().getStringExtra("classname");
        section_name = getIntent().getStringExtra("section");
       /* mType = getIntent().getIntExtra("Type",0);
        if(mType==Config.TYPE_ANNOUNCEMENT){
            mNoItemTV.setText(getString(R.string.no_announcement));
            requestType = "announcement";
            title = getString(R.string.announcement_header);
        }
        else if(mType==Config.TYPE_HOMEWORK){
            mNoItemTV.setText(getString(R.string.no_homework));
            requestType = "homework";
            title = getString(R.string.home_work_header);
        }*/
        initToolBar();
        initRecycleView();
        requestStudentList();
    }

    private void initRecycleView(){
        mAdapter = new StudentAdapter(mContext,new OnItemClickCustom(){

            @Override
            public void onClick(int id, int position, Object object) {
                if(mList.get(position).isSelected()){
                    mList.get(position).setSelected(false);
                }
                else{
                    mList.get(position).setSelected(true);
                }
                if(getStudentSelected()==mList.size()){
                    isAllSelected = true;
                    mSelectAllIV.setImageResource(R.drawable.ic_green_check);
                }
                else{
                    isAllSelected = false;
                    mSelectAllIV.setImageResource(R.drawable.ic_green_uncheck);
                }
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
                        mList = baseRequest.getDataList(json.optJSONArray("student"),StudentModel.class);
                   //     Collections.reverse(mList);
                        if(null==mList || mList.size()==0){
                            mNoItemTV.setVisibility(View.GONE);
                            mAllRL.setVisibility(View.GONE);
                        }
                        else{
                            mAdapter.setList(mList);
                            mAllRL.setVisibility(View.VISIBLE);
                            mNoItemTV.setVisibility(View.GONE);
                        }
                    }
                    else{
                        mAllRL.setVisibility(View.GONE);
                        mNoItemTV.setVisibility(View.GONE);
                        DialogUtil.Alert(StudentListActivity.this, baseRequest.message, DialogUtil.AlertType.Error);
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                if (requestCode==417){
                    DialogUtil.Alert(StudentListActivity.this, message, DialogUtil.AlertType.Error);


                }else {
                    DialogUtil.Alert(StudentListActivity.this, message, DialogUtil.AlertType.Error);

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

    private ActionBar mActionBar;
    private Toolbar toolbar;

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("Select Student");
    }

    @OnClick({R.id.submit_btn,R.id.select_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_btn:
                if(isStudentSelected()){
                    Intent in = new Intent();
                    in.putExtra("students",mList);
                    setResult(RESULT_OK,in);
                    finish();
                }
                else{
                    DialogUtil.Alert(StudentListActivity.this, "Please select student", DialogUtil.AlertType.Error);
                }
                break;

            case R.id.select_iv:
                if(isAllSelected){
                    setSelectUnSelect(false);
                    isAllSelected = false;
                    mSelectAllIV.setImageResource(R.drawable.ic_green_uncheck);
                }
                else{
                    setSelectUnSelect(true);
                    isAllSelected = true;
                    mSelectAllIV.setImageResource(R.drawable.ic_green_check);

                }
                mAdapter.setList(mList);
                break;

        }
    }

    private void setSelectUnSelect(boolean flag){
        for(int i=0;i<mList.size();i++){
            mList.get(i).setSelected(flag);
        }
    }

    private boolean isStudentSelected(){
        boolean isSelected = false;
        for(int i=0;i<mList.size();i++){
            if(mList.get(i).isSelected()){
                isSelected = true;
                break;
            }
        }
        return isSelected;
    }

    private int getStudentSelected(){
        int totalSelected = 0;
        if(null==mList){
            return 0;
        }
        else {
            for (int i = 0; i < mList.size(); i++) {
                if (mList.get(i).isSelected()) {
                    totalSelected++;
                }
            }
        }
        return totalSelected;
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
        Intent intent = new Intent(context,StudentListActivity.class);
        intent.putExtra("classname", classname);
        intent.putExtra("section", section);
        return intent;
    }
}
