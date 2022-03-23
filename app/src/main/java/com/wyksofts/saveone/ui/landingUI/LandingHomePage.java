package com.wyksofts.saveone.ui.landingUI;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.PermissionCheck.checkLocationPermission;
import com.wyksofts.saveone.ui.landingUI.authfrags.general.LandingScreen;

public class LandingHomePage extends AppCompatActivity {

    Dialog closeDiag;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent, this.getTheme()));


        closeDiag = new Dialog(this);

        getLocation();

        showLandingScreen();
    }

    private void showLandingScreen() {
        getSupportFragmentManager().beginTransaction().add(R.id.root_layout,
                new LandingScreen()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void getLocation() {
        new checkLocationPermission(getApplicationContext(), this).getLocation();
    }


    public void onBackPressed() {

        if(getSupportFragmentManager().getBackStackEntryCount()>0) {
            getSupportFragmentManager().popBackStack();
        }else {
            closeDiag.setContentView(R.layout.close_diag);
            closeDiag.findViewById(R.id.yes_exit).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finishAffinity();
                }
            });
            closeDiag.findViewById(R.id.undo).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    closeDiag.dismiss();
                }
            });
            closeDiag.setCancelable(false);
            closeDiag.getWindow().setBackgroundDrawable((Drawable) new ColorDrawable(0));
            closeDiag.show();
        }
    }

}