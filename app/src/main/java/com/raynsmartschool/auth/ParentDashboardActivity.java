package com.raynsmartschool.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.raynsmartschool.R;
import com.raynsmartschool.adapter.NavigationMenuAdapter;
import com.raynsmartschool.interfaces.OnItemClickInAdapter;
import com.raynsmartschool.models.NavigationMenuModel;
import com.raynsmartschool.parent.ParentHomeFragment;
import com.raynsmartschool.parent.ParentProfileFragment;
import com.raynsmartschool.parent.TeacherProfileFragment;
import com.raynsmartschool.session.SessionParam;
import com.raynsmartschool.utility.Config;
import com.raynsmartschool.utility.Dialogs;

import java.util.ArrayList;

/**
 * Created by Ravi on 5/23/2017.
 */

public class ParentDashboardActivity extends BaseActivity {

    private FragmentManager fragmentManager;
    private Fragment mFragment;

    public DrawerLayout mDrawerLayout;
    public ListView mMenuListview;
    NavigationMenuAdapter mNavigationMenuAdapter;
    private String[] items;
    ArrayList<NavigationMenuModel> menuList = new ArrayList<NavigationMenuModel>();
    public int gravity = GravityCompat.START;
    private TextView mUserName;
    private ImageView mprofile_image_iv;
    private SessionParam mSessionParam;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);
        mContext = this;
        mSessionParam = new SessionParam(mContext);
        initViews();
        setDrawer();
        setMenuInDrawer();
        switchFragment(getString(R.string.nav_home));
    }


    public void initViews() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mMenuListview = (ListView) findViewById(R.id.navigation_lv);

        mMenuListview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(gravity)) {
            mDrawerLayout.closeDrawer(gravity);
        } else {
            if (mFragment instanceof ParentHomeFragment) {
                finishAllActivities();
            } else {
                switchFragment(getAppString(R.string.nav_home));
            }
        }
    }

    private void switchFragment(String listString){
        fragmentManager = getSupportFragmentManager();
        mFragment = fragmentManager.findFragmentById(R.id.fragment_container);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        int Selectedposition = -1;

        if (listString.equalsIgnoreCase(getString(R.string.nav_home))) {
            if (!(mFragment instanceof ParentHomeFragment)) {
                mFragment = new ParentHomeFragment();
                mDrawerLayout.closeDrawer(gravity);
                mMenuListview.setItemChecked(Selectedposition, true);
                mNavigationMenuAdapter.notifyDataSetChanged();
                ft.replace(R.id.fragment_container, mFragment);
                ft.commit();
            }
        }
        else if (listString.equalsIgnoreCase(getString(R.string.profile_parent))) {
            if (!(mFragment instanceof ParentProfileFragment)) {
                mFragment = new ParentProfileFragment();
                mDrawerLayout.closeDrawer(gravity);
                mMenuListview.setItemChecked(Selectedposition, true);
                mNavigationMenuAdapter.notifyDataSetChanged();
                ft.replace(R.id.fragment_container, mFragment);
                ft.commit();
            }
        }
        else if (listString.equalsIgnoreCase(getString(R.string.profile_teacher))) {
            if (!(mFragment instanceof TeacherProfileFragment)) {
                mFragment = new TeacherProfileFragment();
                mDrawerLayout.closeDrawer(gravity);
                mMenuListview.setItemChecked(Selectedposition, true);
                mNavigationMenuAdapter.notifyDataSetChanged();
                ft.replace(R.id.fragment_container, mFragment);
                ft.commit();
            }
        }
        if(listString.equalsIgnoreCase(getString(R.string.nav_logout))){
            if (mDrawerLayout.isDrawerOpen(gravity)) {
                mDrawerLayout.closeDrawer(gravity);
            }
            Dialogs.confirmationDialog(mContext, new OnItemClickInAdapter() {
                @Override
                public void onClickItems(int clickID, int position, Object obje) {
                    if(clickID==1) {
                        mSessionParam.setSaveSessionKey(mContext, "");
                        mSessionParam.deletePrefrenceData(mContext);
                        finishAllActivities();
                        Intent i = new Intent(mContext,
                                SplashActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                }
            }, getString(R.string.app_name), getString(R.string.sure_logout), getString(R.string.cancel), getString(R.string.ok));
        }
    }

    public void setDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    public void setMenuInDrawer() {
        items = getResources().getStringArray(R.array.nav_menu);


        for (int i = 0; i < items.length; i++) {
            menuList.add(new NavigationMenuModel(items[i]));
        }
        mNavigationMenuAdapter = new NavigationMenuAdapter(this, menuList);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.nav_header_main, null, false);
        mUserName = (TextView) header.findViewById(R.id.user_name_tv);
        mprofile_image_iv = (ImageView) header.findViewById(R.id.profile_image_iv);
        mprofile_image_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(gravity);
                if(mSessionParam.loginType== Config.LOGIN_TYPE_TEACHER){
                    switchFragment(getString(R.string.profile_teacher));
                }
                else{
                    switchFragment(getString(R.string.profile_parent));
                }
            }
        });

        if (!TextUtils.isEmpty(mSessionParam.name)) {
            mUserName.setText(mSessionParam.name);
        } else if (!TextUtils.isEmpty(mSessionParam.email)) {
            mUserName.setText(mSessionParam.email);
        }
        else {
            mUserName.setText("Welcome "+((mSessionParam.loginType== Config.LOGIN_TYPE_PARENT)?getString(R.string.parent):getString(R.string.teacher)));
        }


        mMenuListview.addHeaderView(header, null, false);
        mMenuListview.setAdapter(mNavigationMenuAdapter);
        mMenuListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                mDrawerLayout.closeDrawer(gravity);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        switchFragment(menuList.get(position - 1).getName());

                    }
                }, 300);
            }
        });
        mMenuListview.setItemChecked(0, true);
    }



    public static Intent getIntent(Context context){
        Intent in =  new Intent(context,ParentDashboardActivity.class);
        return in;

    }

    public boolean getSelectedItemPosi(int posi) {
        return mMenuListview.isItemChecked(posi);
    }
}
