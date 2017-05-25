package com.studenttracker.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.studenttracker.R;
import com.studenttracker.session.SessionParam;
import com.studenttracker.utility.Config;
import com.studenttracker.utility.DialogUtil;
import com.studenttracker.utility.Functions;

import org.json.JSONObject;

import RetroFit.BaseRequest;
import RetroFit.RequestReceiver;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerificationActivity extends BaseActivity {


    @Bind(R.id.verify_btn)
    Button verifyBtn;
    @Bind(R.id.otp_1)
    EditText otp1;
    @Bind(R.id.otp_2)
    EditText otp2;
    @Bind(R.id.otp_3)
    EditText otp3;
    @Bind(R.id.otp_4)
    EditText otp4;
    @Bind(R.id.otp_5)
    EditText otp5;
    @Bind(R.id.otp_6)
    EditText otp6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    moveFocus(R.id.otp_2);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    moveFocus(R.id.otp_3);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    moveFocus(R.id.otp_4);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    moveFocus(R.id.otp_5);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    moveFocus(R.id.otp_6);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        otp6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    moveFocus(777);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void moveFocus(int currentFocus) {
        switch (currentFocus) {
            case R.id.otp_2: {

                otp1.clearFocus();
                otp2.requestFocus();
                otp2.setCursorVisible(true);

            }
            break;
            case R.id.otp_3: {

                otp2.clearFocus();
                otp3.requestFocus();
                otp3.setCursorVisible(true);

            }
            break;
            case R.id.otp_4: {

                otp3.clearFocus();
                otp4.requestFocus();
                otp4.setCursorVisible(true);

            }
            break;
            case R.id.otp_5: {

                otp4.clearFocus();
                otp5.requestFocus();
                otp5.setCursorVisible(true);

            }
            break;
            case R.id.otp_6: {

                otp5.clearFocus();

                otp6.requestFocus();
                otp6.setCursorVisible(true);

                hideSoftKeyboard(this);

            }
            break;
            case 777: {

                otp6.clearFocus();


                hideSoftKeyboard(this);

            }
            break;

        }
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, VerificationActivity.class);
        return intent;
    }


    @OnClick(R.id.verify_btn)
    public void onClick() {
        if (otp1.getText().toString().length() == 0 ||
                otp2.getText().toString().length() == 0 ||
                otp3.getText().toString().length() == 0 ||
                otp4.getText().toString().length() == 0 ||
                otp5.getText().toString().length() == 0 ||
                otp6.getText().toString().length() == 0) {
            DialogUtil.Alert(VerificationActivity.this, getString(R.string.enter_code_sent), DialogUtil.AlertType.Error);

        } else {

            requestAPI(otp1.getText().toString() + otp2.getText().toString() +
                    otp3.getText().toString() + otp4.getText().toString() +
                    otp5.getText().toString() + otp6.getText().toString());
        }
    }

    private BaseRequest baseRequest;

    public void requestAPI(String otp) {
        baseRequest = new BaseRequest(mContext);

        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                if (dataObject != null) {
                    SessionParam mSessionParam = new SessionParam(((JSONObject) dataObject));
                    String session_key = ((JSONObject) dataObject).optString("session_key");
                    mSessionParam.setSaveSessionKey(VerificationActivity.this, session_key);
                    mSessionParam.persistData(VerificationActivity.this);
                    startActivity(MainActivity.getIntent(mContext));
                    finishAllActivities();
                }
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                DialogUtil.Alert(VerificationActivity.this, message, DialogUtil.AlertType.Error);
            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });
        JsonObject object = null;
        object = Functions.getInstance().getJsonObject(
                "device_type", Config.DEVICE_TYPE_ID,
                "device_token", "",
                "verify_code", otp);
        baseRequest.callAPIPost(4, object, getAppString(R.string.api_verify));
    }
}
