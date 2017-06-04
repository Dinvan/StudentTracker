package com.studenttracker.parent;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.iid.FirebaseInstanceId;
import com.studenttracker.R;
import com.studenttracker.auth.BaseFragment;
import com.studenttracker.auth.ParentDashboardActivity;
import com.studenttracker.school.AnnouncementActivity;
import com.studenttracker.school.CreteAnnouncementActivity;
import com.studenttracker.session.SessionParam;
import com.studenttracker.utility.Config;
import com.studenttracker.utility.UtilityFunctions;

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


    private SessionParam mSessionParam;
    private Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_home,container,false);
        ButterKnife.bind(this,view);
        initToolBar(view);
        mContext = getActivity();
        mSessionParam = new SessionParam(mContext);
        if(mSessionParam.loginType== Config.LOGIN_TYPE_TEACHER){
            mTeacherLL.setVisibility(View.VISIBLE);
            mParentLL.setVisibility(View.GONE);
           /* mParentLL.setVisibility(View.VISIBLE);
            mTeacherLL.setVisibility(View.GONE);*/
        }
        else if(mSessionParam.loginType== Config.LOGIN_TYPE_PARENT){
            mParentLL.setVisibility(View.VISIBLE);
            mTeacherLL.setVisibility(View.GONE);
        }
        return view;
    }

    @OnClick({R.id.homework_rl,R.id.attendance_rl,R.id.notification_rl,R.id.add_homework_rl,R.id.add_attendance_rl,R.id.add_notification_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homework_rl:
                startActivity(AnnouncementActivity.getIntent(mContext,Config.TYPE_HOMEWORK));
                break;
            case R.id.attendance_rl:

                break;
            case R.id.notification_rl:
                startActivity(AnnouncementActivity.getIntent(mContext,Config.TYPE_ANNOUNCEMENT));
                break;
            case R.id.add_homework_rl:
                startActivity(CreteAnnouncementActivity.getIntent(mContext,Config.TYPE_HOMEWORK));
                break;
            case R.id.add_attendance_rl:

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
}
