package com.npsindore.school;

import android.Manifest;
import android.app.DownloadManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.npsindore.R;
import com.npsindore.auth.BaseActivity;
import com.npsindore.interfaces.OnItemClickAdapter;
import com.npsindore.models.Announcement;
import com.npsindore.session.SessionParam;
import com.npsindore.utility.Config;
import com.npsindore.utility.DialogUtil;
import com.npsindore.utility.Dialogs;
import com.npsindore.utility.Functions;
import com.npsindore.utility.PermissionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import RetroFit.BaseRequest;
import RetroFit.RequestReceiver;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ravi on 6/10/2017.
 */

public class AnnouncementDetailActivity extends BaseActivity {

    @BindView(R.id.announcement_iv)
    ImageView mAnnouncementIV;
    @BindView(R.id.date_tv)
    TextView mDateTV;
    @BindView(R.id.message_tv)
    TextView mMessageTV;
    @BindView(R.id.title_tv)
    TextView mMessageTitleTV;
    @BindView(R.id.created_by_tv)
    TextView mCreatorTV;
    private int mType;
    private BaseRequest baseRequest;
    @BindView(R.id.progressBar)
    ProgressBar mLoader;
    private String title;
    private Announcement mAnnouncement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anouncement_detail);
        mContext = this;
        ButterKnife.bind(this);
        mType = getIntent().getIntExtra("Type",0);
        if(mType== Config.TYPE_ANNOUNCEMENT){
            title = getString(R.string.announcement_header);
        }
        else if(mType==Config.TYPE_HOMEWORK){
            title = getString(R.string.home_work_header);
        }
        initToolBar();
        mAnnouncement = (Announcement) getIntent().getSerializableExtra("Announcement");
        mMessageTitleTV.setText(mAnnouncement.getTitle());
        mCreatorTV.setText(getString(R.string.created_by)+" "+mAnnouncement.getSender_name());
        if(null!=mAnnouncement){
            if(!TextUtils.isEmpty(mAnnouncement.getMessage())){
                mMessageTV.setText(mAnnouncement.getMessage());
                mMessageTV.setVisibility(View.VISIBLE);
                mAnnouncementIV.setVisibility(View.GONE);
            }
            else{
                mAnnouncementIV.setVisibility(View.INVISIBLE);
                mMessageTV.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(mAnnouncement.getImage()) && !mAnnouncement.getImage().equals("false")){
                Functions.getInstance().displayImagePlain(mContext,mAnnouncement.getImage(),false,mAnnouncementIV);
                mAnnouncementIV.setVisibility(View.VISIBLE);
                mAnnouncementIV.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ArrayList<String> options = new ArrayList<String>();
                        options.add("Download Image");
                        Dialogs.showListSelection(mContext, 0, new OnItemClickAdapter() {
                            @Override
                            public void onClick(int i, int position, String item) {
                               downloadImage();         }
                        },options);

                        return false;
                    }
                });
            }
            else{
                mAnnouncementIV.setVisibility(View.INVISIBLE);
            }
            String date = Functions.getInstance().formatDate(mAnnouncement.getDate_created(), "dd-MM-yyyy hh:mm:ss", "MMM dd, yyyy hh:mm a");
            mDateTV.setText(date);
            requestMarkAsRead(mAnnouncement.getMid());
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

    public void downloadImage(){
        String[] permission={Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        if(!PermissionUtils.isAllPermissionGranted(AnnouncementDetailActivity.this,permission)){
            requestMultiplePermissions(permission);
        }else{
            Uri uri=Uri.parse(mAnnouncement.getImage());
            DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |
                    DownloadManager.Request.NETWORK_MOBILE);

// set title and description
            String fileName=System.currentTimeMillis()+".jpg";
            request.setTitle(fileName);
            request.setDescription(mAnnouncement.getTitle());

            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

//set the local destination for download file to a path within the application's external files directory
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);
            request.setMimeType("image/*");
            downloadManager.enqueue(request);

        }
    }

    private void  requestMultiplePermissions(String[] permissions){


        Dexter.withActivity(this)
                .withPermissions(
                        permissions
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            downloadImage();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
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

    public static Intent getIntent(Context context, int type, Announcement model){
        Intent intent = new Intent(context,AnnouncementDetailActivity.class);
        intent.putExtra("Type", type);
        intent.putExtra("Announcement", model);
        return intent;
    }

    private String saveToInternalStorage(Bitmap bitmapImage,String fileName){
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir


        String appDirectoryName = getString(R.string.app_name_full);
        File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), appDirectoryName);

        if(!imageRoot.exists())     //check if file already exists
        {
            imageRoot.mkdirs();     //if not, create it
        }
        File mypath=new File(imageRoot,fileName+".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            addImageToGallery(mypath.getAbsolutePath(),AnnouncementDetailActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    public static void addImageToGallery(final String filePath, final Context context) {

        ContentValues values = new ContentValues();

        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);

        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }


    public void requestMarkAsRead(String mid) {

        baseRequest = new BaseRequest(this);
        baseRequest.setRunInBackground(true);
        baseRequest.setBaseRequestListner(new RequestReceiver() {
            @Override
            public void onSuccess(int requestCode, String fullResponse, Object dataObject) {
                if (dataObject != null) {

                }
            }

            @Override
            public void onFailure(int requestCode, String errorCode, String message) {
                if (requestCode==417){
                    DialogUtil.Alert(AnnouncementDetailActivity.this, message, DialogUtil.AlertType.Error);


                }else {
                    DialogUtil.Alert(AnnouncementDetailActivity.this, message, DialogUtil.AlertType.Error);

                }

            }

            @Override
            public void onNetworkFailure(int requestCode, String message) {

            }
        });
        JsonObject object = Functions.getInstance().getJsonObject(
                "session_key", new SessionParam(mContext).session_key,
                "mid", mid,
                "type", title.toLowerCase());
        baseRequest.callAPIPost(1, object, getAppString(R.string.api_mark_read));
    }
}
