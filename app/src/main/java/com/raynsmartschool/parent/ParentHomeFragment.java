package com.raynsmartschool.parent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.raynsmartschool.R;
import com.raynsmartschool.auth.BaseFragment;
import com.raynsmartschool.auth.ParentDashboardActivity;
import com.raynsmartschool.models.Announcement;
import com.raynsmartschool.models.TeachersClassModel;
import com.raynsmartschool.school.AnnouncementActivity;
import com.raynsmartschool.school.AttendanceListActivity;
import com.raynsmartschool.school.CreteAnnouncementActivity;
import com.raynsmartschool.school.StudentAttendanceListActivity;
import com.raynsmartschool.session.SessionParam;
import com.raynsmartschool.utility.Config;
import com.raynsmartschool.utility.DialogUtil;
import com.raynsmartschool.utility.Functions;
import com.tapadoo.alerter.OnHideAlertListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import RetroFit.BaseRequest;
import RetroFit.RequestReceiver;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ravi on 5/28/2017.
 */

public class ParentHomeFragment extends BaseFragment{

    @Bind(R.id.teacher_ll)
    LinearLayout mTeacherLL;
    @Bind(R.id.parent_ll)
    LinearLayout mParentLL;
    @Bind(R.id.homework_rl)
    RelativeLayout mHomeWorkRL;
    @Bind(R.id.attendance_rl)
    RelativeLayout mAttendanceRL;
    @Bind(R.id.notification_rl)
    RelativeLayout mNotificationRL;

    @Bind(R.id.add_homework_rl)
    RelativeLayout mAddHomeWorkRL;
    @Bind(R.id.add_attendance_rl)
    RelativeLayout mAddAttendanceRL;
    @Bind(R.id.add_notification_rl)
    RelativeLayout mAddNotificationRL;
    @Bind(R.id.progressBar)
    ProgressBar mLoader;

    private BaseRequest baseRequest;
    private SessionParam mSessionParam;
    private Context mContext;
    private ArrayList<TeachersClassModel> mTeachersClassAL;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_home,container,false);
        ButterKnife.bind(this,view);
        initToolBar(view);
        mContext = getActivity();
        setHasOptionsMenu(true);
        mSessionParam = new SessionParam(mContext);
        if(mSessionParam.loginType== Config.LOGIN_TYPE_TEACHER){
            mTeacherLL.setVisibility(View.VISIBLE);
            mParentLL.setVisibility(View.GONE);
           /* mParentLL.setVisibility(View.VISIBLE);
            mTeacherLL.setVisibility(View.GONE);*/
            requestStudentClassList();
        }
        else if(mSessionParam.loginType== Config.LOGIN_TYPE_PARENT){
            mParentLL.setVisibility(View.VISIBLE);
            mTeacherLL.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @OnClick({R.id.homework_rl,R.id.attendance_rl,R.id.notification_rl,R.id.add_homework_rl,R.id.add_attendance_rl,R.id.add_notification_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homework_rl:
                startActivity(AnnouncementActivity.getIntent(mContext,Config.TYPE_HOMEWORK));
                break;
            case R.id.attendance_rl:
                startActivity(StudentAttendanceListActivity.getIntent(mContext));
                break;
            case R.id.notification_rl:
                startActivity(AnnouncementActivity.getIntent(mContext,Config.TYPE_ANNOUNCEMENT));
                break;
            case R.id.add_homework_rl:
                startActivity(CreteAnnouncementActivity.getIntent(mContext,Config.TYPE_HOMEWORK));
                break;
            case R.id.add_attendance_rl:
                startActivity(AttendanceListActivity.getIntent(mContext,Functions.getInstance().getTeacher().getClass_name(),Functions.getInstance().getTeacher().getClass_section()));
                break;
            case R.id.add_notification_rl:
                startActivity(CreteAnnouncementActivity.getIntent(mContext,Config.TYPE_ANNOUNCEMENT));
                break;
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private ActionBar mActionBar;
    private Toolbar toolbar;

    private void initToolBar(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar_default);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(getString(R.string.nav_home));
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

    public void requestStudentClassList() {

        baseRequest = new BaseRequest(getActivity());
        baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                if (dataObject != null) {
                    if(baseRequest.status) {
                        JSONObject json = (JSONObject) dataObject;
                        mTeachersClassAL = baseRequest.getDataList(json.optJSONArray("class"),TeachersClassModel.class);
                     //   Collections.reverse(mTeachersClassAL);
                        if(null==mTeachersClassAL || mTeachersClassAL.size()==0){
                            DialogUtil.Alert(getActivity(), "You don't have any assigned class", DialogUtil.AlertType.Error, new OnHideAlertListener() {
                                @Override
                                public void onHide() {
                                    getActivity().finish();
                                }
                            });
                        }
                        else{
                            Functions.getInstance().setTeacher(mTeachersClassAL.get(0));
                        }
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

        JsonObject object = new JsonObject();
        object.addProperty("user_id",mSessionParam.user_guid);

        baseRequest.callAPIPost(1, object, getAppString(R.string.api_class_list));
    }

}
