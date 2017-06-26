package com.raynsmartschool.school;

import android.content.Context;
import android.content.Intent;
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
import com.raynsmartschool.utility.Functions;

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
}
