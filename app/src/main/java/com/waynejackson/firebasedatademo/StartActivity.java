package com.waynejackson.firebasedatademo;

import com.google.firebase.auth.FirebaseAuth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.waynejackson.firebasedatademo.login.LoginActivity;
import com.waynejackson.firebasedatademo.topics.TopicsActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            LoginActivity.start(this, true);
        } else {
            TopicsActivity.start(this, true);
        }
    }
}
