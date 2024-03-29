package com.rayn.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.JsonObject;
import com.rayn.R;
import com.rayn.utility.Config;
import com.rayn.utility.DialogUtil;
import com.rayn.utility.Functions;
import com.rayn.utility.UtilityFunctions;

import RetroFit.BaseRequest;
import RetroFit.RequestReceiver;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ravi on 5/11/2017.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.mobile_et)
    EditText mobileEt;
    @BindView(R.id.login_texttv)
    TextView mHeaderTV;
    @BindView(R.id.logo_iv)
    ImageView mLogoIV;
    @BindView(R.id.progressBar)
    ProgressBar mLoader;

    private String refreshedToken;
    private BaseRequest baseRequest;
    private String mMobileNo;
    private int LoginType;
    private String mType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;
        LoginType = getIntent().getIntExtra(Config.LOGIN_TYPE,0);
        if(LoginType==Config.LOGIN_TYPE_TEACHER){
            mHeaderTV.setText(getString(R.string.teacher_login));
            mLogoIV.setImageResource(R.drawable.ic_teacther);
        }
        else{
            mHeaderTV.setText(getString(R.string.parent_login));
            mLogoIV.setImageResource(R.drawable.ic_parent);
        }

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        // Get new Instance ID token
                        refreshedToken = task.getResult().getToken();
                    }
                });
//
    }

    public static Intent getIntent(Context context,int loginType) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.putExtra(Config.LOGIN_TYPE,loginType);
        return intent;
    }

    @OnClick({R.id.login_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:

                if (refreshedToken == null || refreshedToken.length() <= 0)
                    refreshedToken = "abcd";
                if (checkValidation()) {
                    UtilityFunctions.hideSoftKeyboard(mobileEt);
                    requestLogin(mMobileNo);

                    /*startActivity(VerificationActivity.getIntent(mContext,mMobileNo,LoginType));
                    finishAllActivities();*/
                }
                break;
        }
    }
    public boolean checkValidation(){
        mMobileNo = mobileEt.getText().toString().trim();
        if (mMobileNo.length()!=10) {
            DialogUtil.Alert(LoginActivity.this, getString(R.string.phone_require), DialogUtil.AlertType.Error);
            return false;
        } else{
            return true;
        }
    }

    public void requestLogin(final String mobile) {

        baseRequest = new BaseRequest(this);
        baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                if (dataObject != null) {
                   /* SessionParam mSessionParam = new SessionParam(((JSONObject) dataObject).optJSONObject("user_profile"));
                    String session_key = ((JSONObject) dataObject).optString("session_key");
                    mSessionParam.setSaveSessionKey(LoginActivity.this, session_key);
                    mSessionParam.persistData(LoginActivity.this);*/
                    if(baseRequest.status) {
                        startActivity(VerificationActivity.getIntent(mContext, mobile, LoginType,mType));
                        finishAllActivities();
                    }
                    else{
                        DialogUtil.Alert(LoginActivity.this, baseRequest.message, DialogUtil.AlertType.Error);
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                if (requestCode==417){
                    DialogUtil.Alert(LoginActivity.this, message, DialogUtil.AlertType.Error);


                }else {
                    DialogUtil.Alert(LoginActivity.this, message, DialogUtil.AlertType.Error);

                }

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });
      /*  String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (refreshedToken == null || refreshedToken.length() <= 0)
            refreshedToken = "abcd";*/
        JsonObject object = null;
        if(LoginType==Config.LOGIN_TYPE_PARENT){
            mType = "parent";
        }
        else{
            mType = "teacher";
        }
        object = Functions.getInstance().getJsonObject(
                "device_token", refreshedToken,
                "mobile", mobile,
                "login_type", mType);


        baseRequest.callAPIPost(1, object, getAppString(R.string.api_login));
    }
}
