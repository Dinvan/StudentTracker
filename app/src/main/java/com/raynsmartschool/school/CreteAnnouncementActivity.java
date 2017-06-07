package com.raynsmartschool.school;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.raynsmartschool.R;
import com.raynsmartschool.utility.Config;
import com.raynsmartschool.utility.DialogUtil;
import com.raynsmartschool.utility.Functions;
import com.raynsmartschool.utility.MediaPickerActivity;
import com.tapadoo.alerter.OnHideAlertListener;

import java.io.File;

import RetroFit.BaseRequest;
import RetroFit.RequestReceiver;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Ravi on 6/4/2017.
 */

public class CreteAnnouncementActivity extends MediaPickerActivity {


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
    @Bind(R.id.image_iv)
    ImageView mHomeWorkIV;
    private String title;
    private String requestType;
    private File fileUri;
    private int type = 0;
    private String path;
    private String mFileTemp;

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

                    mSubmitBtn.setClickable(false);
                    DialogUtil.Alert(CreteAnnouncementActivity.this, baseRequest.message, DialogUtil.AlertType.Success, new OnHideAlertListener() {
                        @Override
                        public void onHide() {
                            finish();
                        }
                    });
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
                "type", requestType, "message", mMessageET.getText().toString().trim());


        baseRequest.callAPIPost(1, object, getAppString(R.string.api_create_announcement));
    }

    @OnClick({R.id.image_rb,R.id.text_rb,R.id.submit_btn,R.id.image_container_rl})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_container_rl:
                chooseImageDialog("", getString(R.string.image_require), mContext, false);
                break;
            case R.id.image_rb:
                type = 1;
                mImageRB.setChecked(true);
                mTextRB.setChecked(false);
                mMessageET.setVisibility(View.GONE);
                mImageRL.setVisibility(View.VISIBLE);
                mMessageET.setText("");
                break;
            case R.id.text_rb:
                type = 0;
                mTextRB.setChecked(true);
                mImageRB.setChecked(false);
                mMessageET.setVisibility(View.VISIBLE);
                mImageRL.setVisibility(View.GONE);
                fileUri = null;
                mHomeWorkIV.setImageBitmap(null);
                break;
            case R.id.submit_btn:
                if(type==1){
                    if(null==fileUri){
                        DialogUtil.Alert(CreteAnnouncementActivity.this, getString(R.string.image_require), DialogUtil.AlertType.Error);
                    }
                    else{
                        UploadProfileImage(fileUri);
                    }
                }

                else if(type==0){
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

    private void chooseImageDialog(final String title, String message, final Context context, final boolean redirectToPreviousScreen) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton(getString(R.string.existing_picture),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        PickMedia(MediaPickerActivity.MediaPicker.GelleryWithCropper);

                    }
                });
        alertDialog.setNegativeButton(getString(R.string.take_picture),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        PickMedia(MediaPickerActivity.MediaPicker.CameraWithCropper);

                    }
                });
        alertDialog.show();
    }

    @Override
    protected void onSingleImageSelected(int starterCode, File mFileUri, String imagPath, Bitmap bitmap) {

            if (bitmap != null) {
                path = imagPath;
                mFileTemp = mFileUri.getPath();
                System.out.println("Path: " + mFileTemp);
                mHomeWorkIV.setImageBitmap(bitmap);
                fileUri = mFileUri;
            }
    }

    private void UploadProfileImage(File file) {

        baseRequest = new RetroFit.BaseRequest(mContext);
        baseRequest.setLoaderView(mLoader);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {

                if (dataObject != null) {

                    if(baseRequest.status) {
                        mSubmitBtn.setClickable(false);
                        DialogUtil.Alert(CreteAnnouncementActivity.this, baseRequest.message, DialogUtil.AlertType.Success, new OnHideAlertListener() {
                            @Override
                            public void onHide() {
                                finish();
                            }
                        });

                    }
                    else{
                        DialogUtil.Alert(CreteAnnouncementActivity.this, baseRequest.message, DialogUtil.AlertType.Error);
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        JsonObject input = new JsonObject();
        input.addProperty("type", requestType);
      //  MultipartBody.Part body1 = MultipartBody.Part.createFormData("type", requestType);
      //  MultipartBody.Part body2 = MultipartBody.Part.createFormData("message", "");
        RequestBody reqType = RequestBody.create(MediaType.parse("text/plain"), requestType);
        RequestBody reqMessage = RequestBody.create(MediaType.parse("text/plain"), "");
        baseRequest.callAPIPostImage(5, reqFile,getString( R.string.api_create_announcement),reqType,reqMessage);

    }


    @Override
    protected void onVideoCaptured(String videoPath) {
    }

    @Override
    protected void onMediaPickCanceled(MediaPicker reqCode) {
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