package com.wyksofts.saveone.ui.homeUI.PermissionCheck;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.wyksofts.saveone.util.showAppToast;

public class checkLocationPermission extends Activity{

    private FusedLocationProviderClient fusedLocationClient;

    SharedPreferences.Editor editor;
    SharedPreferences pref;
    
    Context context;
    Activity activity;

    public checkLocationPermission(Context context, Activity activity) {
        this.activity = activity;
        this.context = context;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        pref = context.getSharedPreferences("location", 0);
        editor = pref.edit();
    }

    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            showPermissionAlert();

            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        // Got last known location. In some rare situations activity can be null.
                        if (location != null) {

                            // Logic to handle location object
                            Log.e("LAST LOCATION: ", location.toString());
                            //new showAppToast().showSuccess(getApplicationContext(),""+location.toString());

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
                    if (ActivityCompat.checkSelfPermission(activity,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(activity,
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        //getLocation();
                        new showAppToast().showSuccess(activity,"Permission Granted");
                    }
                }
            }
        }
    }

    //show location request
    private void showPermissionAlert(){
        if (ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        }
    }
    
}
