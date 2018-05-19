package com.ibsglobal.auth;

import android.support.v4.app.Fragment;
import android.text.TextUtils;

/**
 * Created by ravib on 5/20/2017.
 */

public class BaseFragment  extends Fragment{

    public String getAppString(int id) {
        String str = "";
        if (!TextUtils.isEmpty(this.getResources().getString(id))) {
            str = this.getResources().getString(id);
        } else {
            str = "";
        }
        return str;
    }




}
