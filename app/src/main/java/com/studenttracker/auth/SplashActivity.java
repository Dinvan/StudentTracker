package com.studenttracker.auth;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.studenttracker.R;
import com.studenttracker.session.SessionParam;

/**
 * Created by Ravi on 5/20/2017.
 */

public class SplashActivity extends BaseActivity{

    private final int SplashDuration = 2000;
    String session_key = "";
    private TextView mLogoTV;
    private ImageView mLogoIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;
        SessionParam mSession = new SessionParam(mContext);
        session_key  = mSession.session_key;
        mLogoTV = (TextView) findViewById(R.id.app_logo);
        mLogoIV = (ImageView) findViewById(R.id.app_logo_iv);
        showTranslateAnimation();
    //    showTranslateAnimation();
    }

    private void showTranslateAnimation() {
        Animation slideUp = AnimationUtils.loadAnimation(
                getApplicationContext(), R.anim.slide_center_to_up);

        slideUp.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLogoTV.setVisibility(View.VISIBLE);
                ShimmerFrameLayout container =
                        (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
                container.setDuration(1000);
                container.setRepeatCount(2);
                container.startShimmerAnimation();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (session_key.length() > 0 && !session_key.equalsIgnoreCase("Profile_Session_Key")) {
                            startActivity( MainActivity.getIntent(mContext));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finishAllActivities();
                        }else{
                            Intent intent = new Intent(mContext, PreLoginActivity.class);
                            startActivity(intent);
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            finishAllActivities();
                        }
                    }
                }, SplashDuration);
            }
        });
        mLogoIV.startAnimation(slideUp);
    }

    private  void go(){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (session_key.length() > 0 && !session_key.equalsIgnoreCase("Profile_Session_Key")) {

                        Intent main_activity = new Intent(mContext, MainActivity.class);
                        startActivity(main_activity);

                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();

                    }else{
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    }


                }
            }, SplashDuration);

    }

    private void animateLogo() {
        ObjectAnimator mObjANim = ObjectAnimator.ofInt(mLogoIV.getDrawable(), "level", 0, 10000).setDuration(3000);
        mObjANim.start();
        mObjANim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}

