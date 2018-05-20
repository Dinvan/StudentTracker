package com.rayn.auth;

import android.app.Activity;

import com.rayn.R;


public class BaseAnimation {

	public enum EFFECT_TYPE {
		TAB_DEAFULT, TAB_SLIDE, TAB_FADE, TAB_SLIDE_RIGHT, TAB_SLIDE_RIGHT_FINISH, TAB_SLIDE_FINISH;
	}

	public static void setAnimationTransition(Activity context, EFFECT_TYPE type) {
		switch (type) {
		case TAB_FADE:
			context.overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			break;
		case TAB_SLIDE:
			context.overridePendingTransition(R.anim.slide_in_right,
					android.R.anim.fade_out);
			break;
		case TAB_SLIDE_FINISH:
			context.overridePendingTransition(android.R.anim.fade_in,
					R.anim.slide_out_right);
			break;
		case TAB_SLIDE_RIGHT_FINISH:
			context.overridePendingTransition(android.R.anim.fade_in,
					R.anim.slide_out_left);
			break;
		default:
			context.overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
			break;
		}

	}

}
