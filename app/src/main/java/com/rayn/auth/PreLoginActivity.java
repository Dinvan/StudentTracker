package com.rayn.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.rayn.R;
import com.rayn.utility.Config;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ravi on 5/25/2017.
 */

public class PreLoginActivity extends BaseActivity {

    @Bind(R.id.parent_rl)
    RelativeLayout mParentRL;
    @Bind(R.id.teacher_rl)
    RelativeLayout mTeacherRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prelogin);
        mContext = this;
        ButterKnife.bind(this);
    }

    @OnClick({R.id.parent_rl,R.id.teacher_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.parent_rl:
                startActivity(LoginActivity.getIntent(mContext, Config.LOGIN_TYPE_PARENT));
                break;
            case R.id.teacher_rl:
                startActivity(LoginActivity.getIntent(mContext, Config.LOGIN_TYPE_TEACHER));
                break;
        }
    }

    public static Intent getIntent(Context context){
        Intent in = new Intent(context,PreLoginActivity.class);
        return in;
    }
}
