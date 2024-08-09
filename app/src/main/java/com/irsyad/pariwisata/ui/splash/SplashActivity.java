package com.irsyad.pariwisata.ui.splash;

import android.os.Bundle;
import android.os.Handler;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.irsyad.pariwisata.R;
import com.irsyad.pariwisata.session.SessionManager;

public class SplashActivity extends AppCompatActivity {

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(SplashActivity.this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                sessionManager.checkLogin();
            }
        }, 3000);

    }


}