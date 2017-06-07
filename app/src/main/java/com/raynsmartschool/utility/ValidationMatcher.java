package com.raynsmartschool.utility;

/**
 * Created by Prashant Maheshwari on 5/13/2017.
 */
public class ValidationMatcher {
    public  static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
}
