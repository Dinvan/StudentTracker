package com.studenttracker.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.studenttracker.R;

/**
 * Created by Ravi on 5/23/2017.
 */

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public static Intent getIntent(Context context){
        Intent in =  new Intent(context,MainActivity.class);
        return in;

    }
}
