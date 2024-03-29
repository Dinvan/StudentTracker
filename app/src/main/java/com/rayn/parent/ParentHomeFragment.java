package com.rayn.parent;

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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.rayn.R;
import com.rayn.adapter.Listpopupadapter;
import com.rayn.auth.BaseFragment;
import com.rayn.auth.ParentDashboardActivity;
import com.rayn.interfaces.MenuSelectionListener;
import com.rayn.models.StudentsModel;
import com.rayn.models.TeachersClassModel;
import com.rayn.school.AnnouncementActivity;
import com.rayn.school.AttendanceListActivity;
import com.rayn.school.CreteAnnouncementActivity;
import com.rayn.school.StudentAttendanceListActivity;
import com.rayn.session.SessionParam;
import com.rayn.utility.Config;
import com.rayn.utility.DialogUtil;
import com.rayn.utility.Dialogs;
import com.rayn.utility.Functions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import RetroFit.BaseRequest;
import RetroFit.RequestReceiver;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ravi on 5/28/2017.
 */

public class ParentHomeFragment extends BaseFragment{

    @BindView(R.id.teacher_ll)
    LinearLayout mTeacherLL;
    @BindView(R.id.parent_ll)
    LinearLayout mParentLL;
    @BindView(R.id.homework_rl)
    RelativeLayout mHomeWorkRL;
    @BindView(R.id.attendance_rl)
    RelativeLayout mAttendanceRL;
    @BindView(R.id.notification_rl)
    RelativeLayout mNotificationRL;
    @BindView(R.id.clickable_view)
    View mMenuAnchorView;

    @BindView(R.id.add_homework_rl)
    RelativeLayout mAddHomeWorkRL;
    @BindView(R.id.add_attendance_rl)
    RelativeLayout mAddAttendanceRL;
    @BindView(R.id.add_notification_rl)
    RelativeLayout mAddNotificationRL;
    @BindView(R.id.progressBar)
    ProgressBar mLoader;

    @BindView(R.id.homework_count_tv)
    TextView mHomeworkCountTV;
    @BindView(R.id.announcement_count_tv)
    TextView mAnnouncementCountTV;
    @BindView(R.id.attendance_count_tv)
    TextView mAttendanceCountTV;
    @BindView(R.id.child_selection_tv)
    TextView mChildSelectionTV;

    private BaseRequest baseRequest;
    private SessionParam mSessionParam;
    private Context mContext;
    private ArrayList<TeachersClassModel> mTeachersClassAL;
    private boolean isOpenPopUp = false;
    private boolean isTeacherSwitchAvail  = false;
    private boolean isStudentSwitchAvail  = false;

    private String ChildName = "";


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

        if(mSessionParam.loginType== Config.LOGIN_TYPE_TEACHER) {
            if (isTeacherSwitchAvail) {
           //     mSwitchTeacherMenu.setVisible(true);
                mChildSelectionTV.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_down_arrow,0);
            } else {
              //  mSwitchTeacherMenu.setVisible(false);
                mChildSelectionTV.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            }
        }
        else{
            if (isStudentSwitchAvail) {
               // mSwitchStudentMenu.setVisible(true);
                mChildSelectionTV.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_down_arrow,0);
            } else {
              //  mSwitchStudentMenu.setVisible(false);
                mChildSelectionTV.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            }
        }
        return view;
    }

    private void refreshNotificationCount(){
        if(null!=Functions.getInstance().getmStudent()) {
            Map<String, Integer> notiCount = SessionParam.getNotificationCount(getActivity(), Functions.getInstance().getmStudent().getStudent_id());
            if (notiCount.get("homework") != null) {
                int count = notiCount.get("homework");
                if (count > 0) {
                    mHomeworkCountTV.setVisibility(View.VISIBLE);
                    mHomeworkCountTV.setText("" + count);
                } else {
                    mHomeworkCountTV.setVisibility(View.GONE);
                }
            }
            if (notiCount.get("announcement") != null) {
                int count = notiCount.get("announcement");
                if (count > 0) {
                    mAnnouncementCountTV.setVisibility(View.VISIBLE);
                    mAnnouncementCountTV.setText("" + count);
                } else {

                    mAnnouncementCountTV.setVisibility(View.GONE);
                }
            }
            if (notiCount.get("attendance") != null) {
                int count = notiCount.get("attendance");
                if (count > 0) {
                    mAttendanceCountTV.setVisibility(View.VISIBLE);
                    mAttendanceCountTV.setText("" + count);
                } else {
                    mAttendanceCountTV.setVisibility(View.GONE);
                }
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

   /* MenuItem mSwitchTeacherMenu,mSwitchStudentMenu;
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

    }*/

    @OnClick({R.id.homework_rl,R.id.attendance_rl,R.id.notification_rl,R.id.add_homework_rl,R.id.add_attendance_rl,R.id.add_notification_rl,R.id.child_selection_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homework_rl:
                startActivity(AnnouncementActivity.getIntent(mContext,Config.TYPE_HOMEWORK,false,ChildName));
                break;
            case R.id.attendance_rl:
                startActivity(StudentAttendanceListActivity.getIntent(mContext,false,ChildName));
                break;
            case R.id.notification_rl:
                startActivity(AnnouncementActivity.getIntent(mContext,Config.TYPE_ANNOUNCEMENT,false,ChildName));
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
            case R.id.child_selection_tv:
                if(mSessionParam.loginType== Config.LOGIN_TYPE_TEACHER) {
                    showPopupMenu();
                }
                else{
                    showStudentPopupMenu();
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

                            mChildSelectionTV.setText(mTeachersClassAL.get(0).getClass_name()+" "+mTeachersClassAL.get(0).getClass_section());
                            if(mTeachersClassAL.size()>1) {
                             //   mSwitchTeacherMenu.setVisible(true);

                                mChildSelectionTV.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_down_arrow,0);
                                isTeacherSwitchAvail = true;
                            }
                            else{
                             //   mSwitchTeacherMenu.setVisible(false);
                                mChildSelectionTV.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
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
        listPopupWindow.setDropDownGravity(Gravity.LEFT | Gravity.LEFT);
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
                mChildSelectionTV.setText(classList.get(pos).getClass_name()+" "+classList.get(pos).getClass_section());
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.setAdapter(adapter);
        adapter.selectedPos(mSelectedPos);
        listPopupWindow.setAnchorView(mChildSelectionTV);
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
                            ChildName = mStudentAL.get(0).getMname()+ " "+mStudentAL.get(0).getSname();
                            mChildSelectionTV.setText(ChildName);
                            if(mStudentAL.size()>1) {
                                //mSwitchStudentMenu.setVisible(true);
                                mChildSelectionTV.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_down_arrow,0);
                                isStudentSwitchAvail = true;
                            }
                            else{
                             //   mSwitchStudentMenu.setVisible(false);
                                mChildSelectionTV.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
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
        listPopupWindow.setDropDownGravity(Gravity.LEFT | Gravity.LEFT);
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
                ChildName = students.get(pos).getMname()+ " "+students.get(pos).getSname();
                mChildSelectionTV.setText(ChildName);
                listPopupWindow.dismiss();
                refreshNotificationCount();
            }
        });
        listPopupWindow.setAdapter(adapter);
        adapter.selectedPos(mSelectedPos);
        listPopupWindow.setAnchorView(mChildSelectionTV);
        listPopupWindow.setForceIgnoreOutsideTouch(false);
        listPopupWindow.setBackgroundDrawable(new ColorDrawable( Color.GRAY));
        listPopupWindow.show();
    }

}
