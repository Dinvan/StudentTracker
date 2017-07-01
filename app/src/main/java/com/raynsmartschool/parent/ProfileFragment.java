package com.raynsmartschool.parent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.raynsmartschool.R;
import com.raynsmartschool.auth.BaseFragment;
import com.raynsmartschool.auth.ParentDashboardActivity;
import com.raynsmartschool.models.Announcement;
import com.raynsmartschool.models.StudentModel;
import com.raynsmartschool.models.StudentsModel;
import com.raynsmartschool.school.AnnouncementActivity;
import com.raynsmartschool.session.SessionParam;
import com.raynsmartschool.utility.DialogUtil;
import com.raynsmartschool.utility.Functions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import RetroFit.BaseRequest;
import RetroFit.RequestReceiver;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi on 6/29/2017.
 */

public class ProfileFragment extends BaseFragment {

    @Bind(R.id.full_name_tv)
    TextView mFullNameTV;
    @Bind(R.id.class_tv)
    TextView mClassTV;
    @Bind(R.id.fathername_tv)
    TextView mFatherNameTV;
    @Bind(R.id.mother_tv)
    TextView mMotherNameTV;
    @Bind(R.id.mobile_tv)
    TextView mMobileTV;
    @Bind(R.id.address_tv)
    TextView mAddressTV;
    @Bind(R.id.email_tv)
    TextView mEmailTV;
    @Bind(R.id.gender_tv)
    TextView mGenderTV;
    @Bind(R.id.progressBar)
    ProgressBar mLoader;

    ArrayList<StudentsModel> mStudentAL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        initToolBar(view);
        ButterKnife.bind(this,view);
        requestProfile();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ((ParentDashboardActivity) getActivity()).hideSoftKeyboard(getActivity());
        ((ParentDashboardActivity) getActivity()).mDrawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mActionBar != null)
                    mActionBar.invalidateOptionsMenu();
                if (!((ParentDashboardActivity) getActivity()).mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    ((ParentDashboardActivity) getActivity()).mDrawerLayout.openDrawer(GravityCompat.START);
                    ((ParentDashboardActivity) getActivity()).mMenuListview.bringToFront();
                    ((ParentDashboardActivity) getActivity()).mDrawerLayout.requestLayout();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ActionBar mActionBar;
    private Toolbar toolbar;

    private void initToolBar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_default);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(getString(R.string.profile));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void setProfileData(){
        if(null!=mStudentAL && mStudentAL.size()>0){
            StudentsModel student = mStudentAL.get(0);
            mFullNameTV.setText(student.getMname()+" "+student.getSname());
            mClassTV.setText(student.getClassName()+" "+student.getSection());
            mFatherNameTV.setText(student.getFname()+" "+student.getFsname());
            mMotherNameTV.setText(student.getMothname()+" "+student.getMothsname());
            mAddressTV.setText(student.getAddress());
            mMobileTV.setText(student.getMobile());
            mGenderTV.setText(student.getSex());
            mEmailTV.setText(student.getEmail());
        }
    }

    public void requestProfile() {

        final BaseRequest baseRequest = new BaseRequest(getActivity(),this);
        baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                if (dataObject != null) {
                    if(baseRequest.status) {
                        JSONObject json = (JSONObject) dataObject;
                        mStudentAL = baseRequest.getDataList(json.optJSONArray("Student"),StudentsModel.class);
                        setProfileData();
                    }
                    else{
                        DialogUtil.Alert(getActivity(), baseRequest.message, DialogUtil.AlertType.Error);
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                if (requestCode==417){
                    DialogUtil.Alert(getActivity(), message, DialogUtil.AlertType.Error);


                }else {
                    DialogUtil.Alert(getActivity(), message, DialogUtil.AlertType.Error);

                }

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });

        JsonObject object = null;

        object = Functions.getInstance().getJsonObject("session_key",new SessionParam(getActivity()).session_key);


        baseRequest.callAPIPost(1, object, getAppString(R.string.api_get_profile));
    }

}
