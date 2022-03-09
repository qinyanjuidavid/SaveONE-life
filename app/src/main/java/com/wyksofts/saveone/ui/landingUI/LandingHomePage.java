package com.wyksofts.saveone.ui.landingUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.PermissionCheck.checkLocationPermission;
import com.wyksofts.saveone.ui.homeUI.entry.SplashScreen;
import com.wyksofts.saveone.ui.landingUI.authfrags.general.LandingScreen;
import com.wyksofts.saveone.util.showAppToast;

public class LandingHomePage extends AppCompatActivity {

    Dialog closeDiag;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent, this.getTheme()));
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        }


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