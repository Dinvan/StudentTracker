package com.studenttracker.school;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.studenttracker.R;
import com.studenttracker.auth.BaseActivity;
import com.studenttracker.models.Announcement;
import com.studenttracker.utility.Config;
import com.studenttracker.utility.DialogUtil;
import com.studenttracker.utility.Functions;

import org.json.JSONObject;

import RetroFit.BaseRequest;
import RetroFit.RequestReceiver;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Ravi on 6/4/2017.
 */

public class CreteAnnouncementActivity extends BaseActivity {


    private int mType;
    private BaseRequest baseRequest;
    @Bind(R.id.text_rb)
    RadioButton mTextRB;
    @Bind(R.id.image_rb)
    RadioButton mImageRB;
    @Bind(R.id.message_et)
    EditText mMessageET;
    @Bind(R.id.image_container_rl)
    RelativeLayout mImageRL;
    @Bind(R.id.progressBar)
    ProgressBar mLoader;
    @Bind(R.id.submit_btn)
    Button mSubmitBtn;
    private String title;
    private String requestType;
    private String filePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);
        mContext = this;
        ButterKnife.bind(this);
        mType = getIntent().getIntExtra("Type",0);
        if(mType== Config.TYPE_ANNOUNCEMENT){
            requestType = "announcement";
            title = getString(R.string.announcement_header);
        }
        else if(mType==Config.TYPE_HOMEWORK){
            requestType = "homework";
            title = getString(R.string.home_work_header);
        }
        initToolBar();
        mTextRB.setChecked(true);
    }

    public void requestCreateAnnouncement() {

        baseRequest = new BaseRequest(this);
        baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                if (dataObject != null) {

                    if(baseRequest.status) {
                        JSONObject json = (JSONObject) dataObject;

                    }
                    else{
                        DialogUtil.Alert(CreteAnnouncementActivity.this, baseRequest.message, DialogUtil.AlertType.Error);
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                if (requestCode==417){
                    DialogUtil.Alert(CreteAnnouncementActivity.this, message, DialogUtil.AlertType.Error);


                }else {
                    DialogUtil.Alert(CreteAnnouncementActivity.this, message, DialogUtil.AlertType.Error);

                }

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });

        JsonObject object = null;

        object = Functions.getInstance().getJsonObject(
                "type", requestType);


        baseRequest.callAPIPost(1, object, getAppString(R.string.api_create_announcement));
    }

    @OnClick({R.id.image_rb,R.id.text_rb,R.id.submit_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_rb:
                mImageRB.setChecked(true);
                mTextRB.setChecked(false);
                mMessageET.setVisibility(View.GONE);
                mImageRL.setVisibility(View.VISIBLE);
                mMessageET.setText("");
                break;
            case R.id.text_rb:
                mTextRB.setChecked(true);
                mImageRB.setChecked(false);
                mMessageET.setVisibility(View.VISIBLE);
                mImageRL.setVisibility(View.GONE);
                filePath = "";
                break;
            case R.id.submit_btn:
                if(mImageRB.isChecked()){
                    if(TextUtils.isEmpty(filePath)){
                        DialogUtil.Alert(CreteAnnouncementActivity.this, getString(R.string.image_require), DialogUtil.AlertType.Error);
                    }
                    else{
                        requestCreateAnnouncement();
                    }
                }

                else if(mImageRB.isChecked()){
                    if(TextUtils.isEmpty(mMessageET.getText().toString())){
                        if(mType== Config.TYPE_ANNOUNCEMENT){
                            DialogUtil.Alert(CreteAnnouncementActivity.this, getString(R.string.text_require), DialogUtil.AlertType.Error);
                        }
                        else if(mType==Config.TYPE_HOMEWORK){
                            DialogUtil.Alert(CreteAnnouncementActivity.this, getString(R.string.homework_require), DialogUtil.AlertType.Error);
                        }
                    } else{
                        requestCreateAnnouncement();
                    }
                }

                break;
        }
    }


    private ActionBar mActionBar;
    private Toolbar toolbar;

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public static Intent getIntent(Context context,int type){
        Intent in = new Intent(context,CreteAnnouncementActivity.class);
        in.putExtra("Type",type);
        return in;
    }
}
