package com.rayn.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.rayn.R;

/**
 * Created by Ravi on 5/23/2017.
 */

public class TeacherDashboardActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_dashboard);
    }


    public static Intent getIntent(Context context){
        Intent in =  new Intent(context,TeacherDashboardActivity.class);
        return in;

    }
}
