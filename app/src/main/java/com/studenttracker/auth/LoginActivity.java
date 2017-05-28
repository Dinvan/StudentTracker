package com.studenttracker.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.studenttracker.R;
import com.studenttracker.session.SessionParam;
import com.studenttracker.utility.Config;
import com.studenttracker.utility.DialogUtil;
import com.studenttracker.utility.Functions;
import com.studenttracker.utility.UtilityFunctions;
import com.studenttracker.utility.ValidationMatcher;

import org.json.JSONObject;

import RetroFit.BaseRequest;
import RetroFit.RequestReceiver;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ravi on 5/11/2017.
 */

public class LoginActivity extends BaseActivity {

    @Bind(R.id.mobile_et)
    EditText mobileEt;
    @Bind(R.id.login_texttv)
    TextView mHeaderTV;
    @Bind(R.id.logo_iv)
    ImageView mLogoIV;

    private BaseRequest baseRequest;
    private String mMobileNo;
    private int LoginType;

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
                if (checkValidation()) {
                    UtilityFunctions.hideSoftKeyboard(mobileEt);
                  //  requestLogin(mMobileNo);

                    startActivity(VerificationActivity.getIntent(mContext,mMobileNo,LoginType));
                    finishAllActivities();
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
        baseRequest.setDefaultLoader();
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                if (dataObject != null) {
                    SessionParam mSessionParam = new SessionParam(((JSONObject) dataObject).optJSONObject("user_profile"));
                    String session_key = ((JSONObject) dataObject).optString("session_key");
                    mSessionParam.setSaveSessionKey(LoginActivity.this, session_key);
                    mSessionParam.persistData(LoginActivity.this);
                    startActivity(VerificationActivity.getIntent(mContext,mobile,LoginType));
                    finishAllActivities();
                }
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                if (requestCode==417){
                    startActivity(new Intent(LoginActivity.this, VerificationActivity.class));


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
            object = Functions.getInstance().getJsonObject(
                    "device_type", Config.DEVICE_TYPE_ID,
                    "device_token", "",
                    "mobile", mobile,
                    "login_type", String.valueOf(LoginType));


        baseRequest.callAPIPost(1, object, getAppString(R.string.api_login));
    }

}
