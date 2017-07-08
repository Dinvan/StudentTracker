package com.raynsmartschool.parent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.raynsmartschool.R;
import com.raynsmartschool.auth.BaseFragment;
import com.raynsmartschool.auth.ParentDashboardActivity;
import com.raynsmartschool.models.StudentsModel;
import com.raynsmartschool.models.TeachersClassModel;
import com.raynsmartschool.models.TeachersModel;
import com.raynsmartschool.session.SessionParam;
import com.raynsmartschool.utility.Config;
import com.raynsmartschool.utility.DialogUtil;
import com.raynsmartschool.utility.Functions;

import org.json.JSONObject;

import java.util.ArrayList;

import RetroFit.BaseRequest;
import RetroFit.RequestReceiver;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi on 6/29/2017.
 */

public class TeacherProfileFragment extends BaseFragment {

    @Bind(R.id.full_name_tv)
    TextView mFullNameTV;
    @Bind(R.id.class_tv)
    TextView mClassTV;
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
    SessionParam session;
    TeachersModel mTeachersModel;
    private  ArrayList<TeachersClassModel> myClasses;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher_profile,container,false);
        initToolBar(view);
        ButterKnife.bind(this,view);

        session = new SessionParam(getActivity());
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
        if(null!=mTeachersModel){
            mFullNameTV.setText(mTeachersModel.getFirst_name()+" "+mTeachersModel.getLast_name());

            mAddressTV.setText(mTeachersModel.getUser_address());
            mMobileTV.setText(mTeachersModel.getUser_phone());
            mGenderTV.setText(mTeachersModel.getUser_gender());
            mEmailTV.setText(mTeachersModel.getEmail());
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
                        Gson gson = new Gson();
                        mTeachersModel = gson.fromJson(json.toString(),TeachersModel.class);
                        setProfileData();
                        requestStudentClassList();
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
        String type;
        if(session.loginType== Config.LOGIN_TYPE_TEACHER){
            type = "teacher";
        }
        else{
            type = "parent";
        }
        JsonObject object = null;

        object = Functions.getInstance().getJsonObject("session_key",session.session_key,
                "type",type);
        baseRequest.callAPIPost(1, object, getAppString(R.string.api_get_profile));
    }

    public void requestStudentClassList() {

        final BaseRequest baseRequest = new BaseRequest(getActivity());
        baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                if (dataObject != null) {
                    if(baseRequest.status) {
                        JSONObject json = (JSONObject) dataObject;
                        myClasses = baseRequest.getDataList(json.optJSONArray("class"),TeachersClassModel.class);
                        if(null==myClasses || myClasses.size()==0){
                            mClassTV.setText("There is no class assigned to you");
                        }
                        else{
                            if(null!=myClasses && myClasses.size()>0){
                                StringBuilder builder = new StringBuilder();
                                for(int i=0;i<myClasses.size();i++){
                                    if(!TextUtils.isEmpty(builder.toString())){
                                        builder.append("\n");
                                    }
                                    builder.append(myClasses.get(i).getClass_name()+" "+myClasses.get(i).getClass_section());
                                }
                                mClassTV.setText("Class :"+builder.toString());
                            }
                            else{
                                mClassTV.setText("There is no class assigned to you");
                            }
                        }
                    }
                    else{
                        mClassTV.setText("There is no class assigned to you");
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

        JsonObject object = new JsonObject();
        object.addProperty("user_id",session.user_guid);

        baseRequest.callAPIPost(1, object, getAppString(R.string.api_class_list));
    }

}
