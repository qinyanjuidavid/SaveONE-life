package com.wyksofts.saveone.ui.homeUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.landingUI.LandingHomePage;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        startActivity();
    }

    private void startActivity()
    {
        Thread timer = new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(new Intent(getApplicationContext(), LandingHomePage.class));
                }
            }
        };
        timer.start();
    }
}