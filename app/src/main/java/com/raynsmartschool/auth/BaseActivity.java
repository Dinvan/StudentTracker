package com.raynsmartschool.auth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.splunk.mint.Mint;
import com.raynsmartschool.R;
import com.raynsmartschool.utility.Config;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravi on 5/11/2017.
 */

public class BaseActivity extends AppCompatActivity {

    public Context mContext;
    private static List<Activity> sActivities = new ArrayList<Activity>();
    public static final int REQ_CODE_CREATE_TEAM = 100;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setcolorLollipop();
        mContext = this;
        sActivities.add(this);
        //    updateLanguage();
        if(Config.ENVIRONMENT.equals("devel")){
            Mint.initAndStartSession(this.getApplication(), "01d276a4");
        }
    }
    public void setAppBar(String title, boolean isBackVisible) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_default);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(isBackVisible);
        getSupportActionBar().setDisplayShowHomeEnabled(isBackVisible);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                hideSoftKeyboard(BaseActivity.this);
            }
        });
        getSupportActionBar().setTitle(title);

        //   existingMsg

    }
    public void hideSoftKeyboard(Context cy) {
        Activity activity = ((Activity) cy);
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity
                            .INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken
                    (), 0);
        } catch (Exception e) {

        }

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    protected <V> V findView(int id) {
        //noinspection unchecked
        return (V) findViewById(id);
    }

   /* public void setMandatoryHint(TextInputLayout NameET, int name) {
        String txthint = getResources().getString(name);
        String astrk = "*";
        SpannableStringBuilder builder = new SpannableStringBuilder();

        builder.append(txthint);
        int start = builder.length();
        builder.append(astrk);
        int end = builder.length();

        builder.setSpan(new ForegroundColorSpan(Color.RED), start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        NameET.setHint(builder);
    }*/

   /* public void setMandatoryHintET(EditText NameET, int name) {
        CenteredImageSpan imageHint = new CenteredImageSpan(mContext, (R.drawable.icon_red_star));
        SpannableStringBuilder spannableString = new SpannableStringBuilder("_" + getResources().getString(name));
        spannableString.setSpan(imageHint, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        NameET.setHint(spannableString);
    }*/



/*    public void setMandatoryTextViewHint(TextView NameET, int name) {
        CenteredImageSpan imageHint = new CenteredImageSpan(mContext, (R.drawable.icon_red_star));
        SpannableStringBuilder spannableString = new SpannableStringBuilder("_" + getResources().getString(name));
        spannableString.setSpan(imageHint, 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        NameET.setHint(spannableString);
    }*/


    public String getAppString(int id) {
        String str = "";
        if (!TextUtils.isEmpty(this.getResources().getString(id))) {
            str = this.getResources().getString(id);
        } else {
            str = "";
        }
        return str;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getBaseContext().getResources().updateConfiguration(newConfig,
                getBaseContext().getResources().getDisplayMetrics());
    }

    public void startActivityWithAnim(Intent intent, BaseAnimation.EFFECT_TYPE type) {
        startActivity(intent);
        setAnimationTransition(type);
    }

    public void startActivityWithResultAnim(Intent intent, int requestCode) {
        startActivityWithResultAnim(intent, requestCode,
                BaseAnimation.EFFECT_TYPE.TAB_DEAFULT);
    }

    public void startActivityWithResultAnim(Intent intent, int requestCode,
                                            BaseAnimation.EFFECT_TYPE type) {
        startActivityForResult(intent, requestCode);
        setAnimationTransition(type);
    }

    public void finishActivityWithAnim() {
        finishActivityWithAnim(BaseAnimation.EFFECT_TYPE.TAB_DEAFULT);
    }

    public void finishActivityWithAnim(BaseAnimation.EFFECT_TYPE type) {
        finish();
        setAnimationTransition(type);
    }

    public void startActivityWithAnimFade(Intent intent) {
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    public void finishActivityWithAnimFade() {
        finish();
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    private void setAnimationTransition(BaseAnimation.EFFECT_TYPE type) {
        BaseAnimation.setAnimationTransition(this, type);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return (path == null) ? null : Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null,
                    null);
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        } else {
            return null;
        }
    }

    public void finishAllActivities() {
        if (sActivities != null) {
            for (Activity activity : sActivities) {
                if (Config.DEBUG) {
                    Log.d("BaseActivity", "finishAllActivities activity=" + activity.getClass()
                            .getSimpleName());
                }
                activity.finish();
            }
        }
    }
    /*public void  HideKeyBoard()
    {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }*/

    public boolean checkNullNLength(String mEmail2) {
        if (mEmail2 != null && mEmail2.length() > 0) {
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        sActivities.remove(this);
        super.onDestroy();
    }

    @SuppressLint("NewApi")
    public void setcolorLollipop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public void showSoftKeyboard(EditText editText) {
        editText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) mContext
                .getSystemService(Activity
                        .INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(editText, 0);
    }
}
