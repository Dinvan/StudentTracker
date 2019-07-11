package com.rayn.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;


/**
 * Created by ravib 30/05/2017.
 */

public class MyFirebaseInstanceIDService  extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

}