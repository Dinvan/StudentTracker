package com.studenttracker.parent;

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

import com.studenttracker.R;
import com.studenttracker.auth.BaseFragment;
import com.studenttracker.auth.ParentDashboardActivity;

/**
 * Created by Ravi on 5/28/2017.
 */

public class ParentHomeFragment extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parent_home,container,false);
        initToolBar(view);
        return view;
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
