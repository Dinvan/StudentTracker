package com.studenttracker.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

    @Bind(R.id.email_et)
    EditText emailEt;
    @Bind(R.id.password_et)
    EditText passwordEt;

    private BaseRequest baseRequest;
    private String mEmailId,mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;

//
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @OnClick({R.id.login_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                if (checkValidation()) {
                    UtilityFunctions.hideSoftKeyboard(emailEt);
                    requestLogin(mEmailId,mPassword);
                }
                break;
        }
    }
    public boolean checkValidation(){
        mEmailId = emailEt.getText().toString().trim();
        mPassword = passwordEt.getText().toString().trim();
        if (!ValidationMatcher.isValidEmail(mEmailId)) {
            DialogUtil.Alert(LoginActivity.this, getString(R.string.err_msg_email), DialogUtil.AlertType.Error);
            return false;
        } else if (mPassword.length()<6) {
            DialogUtil.Alert(LoginActivity.this, getString(R.string.err_msg_password), DialogUtil.AlertType.Error);
            return false;
        }else{
            return true;
        }
    }

    public void requestLogin(String email, String password) {

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
                    startActivity(MainActivity.getIntent(mContext));
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
                    "email", email,
                    "password", password);


        baseRequest.callAPIPost(1, object, getAppString(R.string.api_login));
    }

}
