package com.rayn.auth;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class StudentTrackerApplication  extends Application {
    // Overriding this method is totally optional!
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}