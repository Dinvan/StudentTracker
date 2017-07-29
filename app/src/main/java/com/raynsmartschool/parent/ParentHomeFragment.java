package com.raynsmartschool.parent;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.raynsmartschool.R;
import com.raynsmartschool.adapter.Listpopupadapter;
import com.raynsmartschool.auth.BaseFragment;
import com.raynsmartschool.auth.ParentDashboardActivity;
import com.raynsmartschool.interfaces.MenuSelectionListener;
import com.raynsmartschool.models.Announcement;
import com.raynsmartschool.models.StudentsModel;
import com.raynsmartschool.models.TeachersClassModel;
import com.raynsmartschool.school.AnnouncementActivity;
import com.raynsmartschool.school.AttendanceListActivity;
import com.raynsmartschool.school.CreteAnnouncementActivity;
import com.raynsmartschool.school.StudentAttendanceListActivity;
import com.raynsmartschool.session.SessionParam;
import com.raynsmartschool.utility.Config;
import com.raynsmartschool.utility.DialogUtil;
import com.raynsmartschool.utility.Dialogs;
import com.raynsmartschool.utility.Functions;
import com.tapadoo.alerter.OnHideAlertListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

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
    @Bind(R.id.clickable_view)
    View mMenuAnchorView;

    @Bind(R.id.add_homework_rl)
    RelativeLayout mAddHomeWorkRL;
    @Bind(R.id.add_attendance_rl)
    RelativeLayout mAddAttendanceRL;
    @Bind(R.id.add_notification_rl)
    RelativeLayout mAddNotificationRL;
    @Bind(R.id.progressBar)
    ProgressBar mLoader;

    @Bind(R.id.homework_count_tv)
    TextView mHomeworkCountTV;
    @Bind(R.id.announcement_count_tv)
    TextView mAnnouncementCountTV;
    @Bind(R.id.attendance_count_tv)
    TextView mAttendanceCountTV;

    private BaseRequest baseRequest;
    private SessionParam mSessionParam;
    private Context mContext;
    private ArrayList<TeachersClassModel> mTeachersClassAL;
    private boolean isOpenPopUp = false;
    private boolean isTeacherSwitchAvail  = false;
    private boolean isStudentSwitchAvail  = false;


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
            requestTeacherClassList();
        }
        else if(mSessionParam.loginType== Config.LOGIN_TYPE_PARENT){
            mParentLL.setVisibility(View.VISIBLE);
            mTeacherLL.setVisibility(View.GONE);
            requestProfile();
        }
        return view;
    }

    private void refreshNotificationCount(){
        Map<String,Integer> notiCount = SessionParam.getNotificationCount(getActivity());
        if(notiCount.get("homework")!=null){
            int count =  notiCount.get("homework");
            if(count>0){
                mHomeworkCountTV.setVisibility(View.VISIBLE);
                mHomeworkCountTV.setText(""+count);
            }
            else{
                mHomeworkCountTV.setVisibility(View.GONE);
            }
        }
        if(notiCount.get("announcement")!=null){
            int count =  notiCount.get("announcement");
            if(count>0){
                mAnnouncementCountTV.setVisibility(View.VISIBLE);
                mAnnouncementCountTV.setText(""+count);
            }
            else{

                mAnnouncementCountTV.setVisibility(View.GONE);
            }
        }
        if(notiCount.get("attendance")!=null){
            int count =  notiCount.get("attendance");
            if(count>0){
                mAttendanceCountTV.setVisibility(View.VISIBLE);
                mAttendanceCountTV.setText(""+count);
            }
            else{
                mAttendanceCountTV.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mSessionParam = new SessionParam(mContext);
        if(mSessionParam.loginType== Config.LOGIN_TYPE_PARENT){
            refreshNotificationCount();
        }
    }

    MenuItem mSwitchTeacherMenu,mSwitchStudentMenu;
    Menu mMenu;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        mMenu = menu;
        mSwitchTeacherMenu = menu.findItem(R.id.action_switch_teacher);
        mSwitchStudentMenu = menu.findItem(R.id.action_switch_student);
        if(mSessionParam.loginType== Config.LOGIN_TYPE_TEACHER) {
            if (isTeacherSwitchAvail) {
                mSwitchTeacherMenu.setVisible(true);
            } else {
                mSwitchTeacherMenu.setVisible(false);
            }
        }
        else{
            if (isStudentSwitchAvail) {
                mSwitchStudentMenu.setVisible(true);
            } else {
                mSwitchStudentMenu.setVisible(false);
            }
        }

    }

    @OnClick({R.id.homework_rl,R.id.attendance_rl,R.id.notification_rl,R.id.add_homework_rl,R.id.add_attendance_rl,R.id.add_notification_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homework_rl:
                startActivity(AnnouncementActivity.getIntent(mContext,Config.TYPE_HOMEWORK,false));
                break;
            case R.id.attendance_rl:
                startActivity(StudentAttendanceListActivity.getIntent(mContext,false));
                break;
            case R.id.notification_rl:
                startActivity(AnnouncementActivity.getIntent(mContext,Config.TYPE_ANNOUNCEMENT,false));
                break;
            case R.id.add_homework_rl:
                if(null==mTeachersClassAL || mTeachersClassAL.size()==0){
                    showErrorNoClass();
                }
                else {
                    startActivity(CreteAnnouncementActivity.getIntent(mContext, Config.TYPE_HOMEWORK));
                }
                break;
            case R.id.add_attendance_rl:
                if(null==mTeachersClassAL || mTeachersClassAL.size()==0){
                    showErrorNoClass();
                }
                else {
                    startActivity(AttendanceListActivity.getIntent(mContext, Functions.getInstance().getTeacher().getClass_name(), Functions.getInstance().getTeacher().getClass_section()));
                }
                break;
            case R.id.add_notification_rl:
                if(null==mTeachersClassAL || mTeachersClassAL.size()==0){
                    showErrorNoClass();
                }
                else {
                    startActivity(CreteAnnouncementActivity.getIntent(mContext, Config.TYPE_ANNOUNCEMENT));
                }
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
            case R.id.action_switch_teacher:
                showPopupMenu();
                return true;
            case R.id.action_switch_student:
                showStudentPopupMenu();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void requestTeacherClassList() {

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
                            Functions.getInstance().setmTeachersClassAL(new ArrayList<TeachersClassModel>());
                            showErrorNoClass();
                        }
                        else{
                            Functions.getInstance().setmTeachersClassAL(mTeachersClassAL);
                            Functions.getInstance().setTeacher(mTeachersClassAL.get(0));

                            if(mTeachersClassAL.size()>1) {
                                mSwitchTeacherMenu.setVisible(true);
                                isTeacherSwitchAvail = true;
                            }
                            else{
                                mSwitchTeacherMenu.setVisible(false);
                                isTeacherSwitchAvail = false;
                            }
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

    private void showErrorNoClass(){
        Dialogs.showAlertDialog("No Class Found!","There is no class assigned to you.",mContext);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (listPopupWindow != null) {
            listPopupWindow.dismiss();
        }
    }

    private void showPopupMenu() {
        ArrayList<TeachersClassModel> nList = Functions.getInstance().getmTeachersClassAL();
        if(null!=nList) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i <nList.size();i++){
                list.add(nList.get(i).getClass_name()+" "+nList.get(i).getClass_section());
            }
            if (isOpenPopUp && listPopupWindow != null) {
                isOpenPopUp = false;
                listPopupWindow.dismiss();
            } else {
                isOpenPopUp = true;
                setPopUpWindow(list,nList);
            }
        }
    }

    /*

    show sorting popup
     */

    private void setPopUpWindow(final ArrayList<String> list,final ArrayList<TeachersClassModel> classList) {
        listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setContentWidth(500);
        listPopupWindow.setDropDownGravity(Gravity.CENTER | Gravity.RIGHT);
        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isOpenPopUp = false;
                listPopupWindow.dismiss();
            }
        });
        Listpopupadapter adapter = new Listpopupadapter(mContext, list, new MenuSelectionListener() {
            @Override
            public void selectedMenu(int pos) {
                mSelectedPos = pos;
                isOpenPopUp = false;
                Functions.getInstance().setTeacher(classList.get(pos));
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.setAdapter(adapter);
        adapter.selectedPos(mSelectedPos);
        listPopupWindow.setAnchorView(mMenuAnchorView);
        listPopupWindow.setForceIgnoreOutsideTouch(false);
        listPopupWindow.setBackgroundDrawable(new ColorDrawable( Color.GRAY));
        listPopupWindow.show();
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
                        ArrayList<StudentsModel> mStudentAL = baseRequest.getDataList(json.optJSONArray("Student"),StudentsModel.class);
                        if(null==mStudentAL || mStudentAL.size()==0){
                            Functions.getInstance().setmTeachersClassAL(new ArrayList<TeachersClassModel>());

                        }
                        else {
                            Functions.getInstance().setmStudentsAL(mStudentAL);
                            Functions.getInstance().setmStudent(mStudentAL.get(0));
                            mSessionParam.name = mStudentAL.get(0).getFname()+" "+mStudentAL.get(0).getFsname();
                            mSessionParam.persistData(mContext);
                            if(mStudentAL.size()>1) {
                                mSwitchStudentMenu.setVisible(true);
                                isStudentSwitchAvail = true;
                            }
                            else{
                                mSwitchStudentMenu.setVisible(false);
                                isStudentSwitchAvail = false;
                            }

                            refreshNotificationCount();
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
        SessionParam session = new SessionParam(getActivity());
        JsonObject object = null;

        object = Functions.getInstance().getJsonObject("session_key",session.session_key,
                "type","parent");
        baseRequest.callAPIPost(1, object, getAppString(R.string.api_get_profile));
    }

    private void showStudentPopupMenu() {
        ArrayList<StudentsModel> nList = Functions.getInstance().getmStudentsAL();
        if(null!=nList) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i <nList.size();i++){
                list.add(nList.get(i).getMname()+" "+nList.get(i).getSname());
            }
            if (isOpenPopUp && listPopupWindow != null) {
                isOpenPopUp = false;
                listPopupWindow.dismiss();
            } else {
                isOpenPopUp = true;
                setStudentPopUpWindow(list,nList);
            }
        }
    }

    /*

    show sorting popup
     */
    private ListPopupWindow listPopupWindow;
    private int mSelectedPos = 0;
    private String mSortOrder = "";
    private void setStudentPopUpWindow(final ArrayList<String> list,final ArrayList<StudentsModel> students) {
        listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setContentWidth(500);
        listPopupWindow.setDropDownGravity(Gravity.CENTER | Gravity.RIGHT);
        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isOpenPopUp = false;
                listPopupWindow.dismiss();
            }
        });
        Listpopupadapter adapter = new Listpopupadapter(mContext, list, new MenuSelectionListener() {
            @Override
            public void selectedMenu(int pos) {
                mSelectedPos = pos;
                isOpenPopUp = false;
                Functions.getInstance().setmStudent(students.get(pos));
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.setAdapter(adapter);
        adapter.selectedPos(mSelectedPos);
        listPopupWindow.setAnchorView(mMenuAnchorView);
        listPopupWindow.setForceIgnoreOutsideTouch(false);
        listPopupWindow.setBackgroundDrawable(new ColorDrawable( Color.GRAY));
        listPopupWindow.show();
    }

}
