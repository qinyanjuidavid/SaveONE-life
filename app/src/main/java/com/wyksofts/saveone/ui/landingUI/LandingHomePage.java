package com.wyksofts.saveone.ui.landingUI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.entry.SplashScreen;
import com.wyksofts.saveone.ui.landingUI.authfrags.general.LandingScreen;
import com.wyksofts.saveone.util.showAppToast;

public class LandingHomePage extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;

    SharedPreferences.Editor editor;
    SharedPreferences pref;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_screen);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        pref = getApplicationContext().getSharedPreferences("location", 0);
        editor = pref.edit();

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

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            showPermissionAlert();

            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            // Logic to handle location object
                            Log.e("LAST LOCATION: ", location.toString());
                            new showAppToast().showSuccess(getApplicationContext(),""+location.toString());

                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            //save location to shared preference
                            editor.putString("latitude", String.valueOf(latitude));
                            editor.putString("longitude", String.valueOf(longitude));
                            editor.commit();

                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults[0] == PackageManager.PERMISSION_DENIED) {

                    // permission was denied, show alert to explain permission
                    showPermissionAlert();

                } else {
                    //permission is granted now start a background service
                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //getLocation();
                        new showAppToast().showSuccess(this,"Permission Granted");
                    }
                }
            }
        }
    }

    //show location request
    private void showPermissionAlert(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
    }

}