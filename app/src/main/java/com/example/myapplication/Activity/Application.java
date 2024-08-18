package com.example.myapplication.Activity;

import com.google.firebase.FirebaseApp;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
