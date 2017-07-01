package com.raynsmartschool.school;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.raynsmartschool.R;
import com.raynsmartschool.auth.BaseActivity;
import com.raynsmartschool.models.Announcement;
import com.raynsmartschool.utility.Config;
import com.raynsmartschool.utility.DialogUtil;
import com.raynsmartschool.utility.Functions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import RetroFit.BaseRequest;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Ravi on 6/10/2017.
 */

public class AnnouncementDetailActivity extends BaseActivity {

    @Bind(R.id.announcement_iv)
    ImageView mAnnouncementIV;
    @Bind(R.id.date_tv)
    TextView mDateTV;
    @Bind(R.id.message_tv)
    TextView mMessageTV;
    @Bind(R.id.title_tv)
    TextView mMessageTitleTV;
    @Bind(R.id.created_by_tv)
    TextView mCreatorTV;
    private int mType;
    private BaseRequest baseRequest;
    @Bind(R.id.progressBar)
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
            }
            else{
                mMessageTV.setVisibility(View.GONE);
            }
            if(!TextUtils.isEmpty(mAnnouncement.getImage())){
                Functions.getInstance().displayImagePlain(mContext,mAnnouncement.getImage(),false,mAnnouncementIV);
                mAnnouncementIV.setVisibility(View.VISIBLE);
               /* mAnnouncementIV.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Bitmap bitmap = ((BitmapDrawable)mAnnouncementIV.getDrawable()).getBitmap();
                        if(null!=bitmap) {
                            String fileName;
                            if(mType==Config.TYPE_HOMEWORK){
                                fileName = "homework_"+mAnnouncement.getDate_created();
                            }
                            else{
                                fileName = "announcement"+mAnnouncement.getDate_created();
                            }

                            String filePath = saveToInternalStorage(bitmap,fileName);
                            DialogUtil.Alert(AnnouncementDetailActivity.this, "File saved as "+filePath, DialogUtil.AlertType.Success);
                        }
                        return false;
                    }
                });*/
            }
            else{
                mAnnouncementIV.setVisibility(View.GONE);
            }
            String date = Functions.getInstance().formatDate(mAnnouncement.getDate_created(), "dd-MM-yyyy hh:mm:ss", "MMM dd, yyyy hh:mm a");
            mDateTV.setText(date);
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
        File mypath=new File(directory,fileName+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
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
}
