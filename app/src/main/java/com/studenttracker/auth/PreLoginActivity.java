package com.studenttracker.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.studenttracker.R;

/**
 * Created by Ravi on 5/25/2017.
 */

public class PreLoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prelogin);
    }

    public static Intent getIntent(Context context){
        Intent in = new Intent(context,PreLoginActivity.class);
        return in;
    }
}
