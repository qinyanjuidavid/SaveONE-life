package com.wyksofts.saveone.ui.landingUI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.landingUI.authfrags.general.LandingScreen;

public class LandingHomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        showLandingScreen();
    }

    private void showLandingScreen() {
        getSupportFragmentManager().beginTransaction().add(R.id.root_layout,
                new LandingScreen()).commit();
    }
}