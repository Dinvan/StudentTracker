package com.rayn.utility;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by ravib on 05/21/2017.
 */
public class UtilityFunctions {

    public static void hideSoftKeyboard(EditText editText) {
        if (null != editText) {
            InputMethodManager imm = (InputMethodManager) editText.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }
}
