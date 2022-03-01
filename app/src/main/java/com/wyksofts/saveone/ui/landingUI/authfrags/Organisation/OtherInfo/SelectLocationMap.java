package com.wyksofts.saveone.ui.landingUI.authfrags.Organisation.OtherInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wyksofts.saveone.App.MainActivity;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.util.showAppToast;

public class SelectLocationMap extends Fragment{


    //current location
    double current_lat, current_long;
    LatLng curr_position;
    SupportMapFragment mapFragment;


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private FusedLocationProviderClient fusedLocationClient;

    Dialog congratulationsDialog;
    FirebaseUser user;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_select_location_map, container, false);
        congratulationsDialog = new Dialog(getContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        user = FirebaseAuth.getInstance().getCurrentUser();
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.select_location_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        //get location
        LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            checkLocationPermission();

            return;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        curr_position = new LatLng(latitude, longitude);

        new showAppToast().showSuccess(getContext(),""+curr_position);
    }

    //callback
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {

            googleMap.addMarker(new MarkerOptions().position(curr_position).title("Orphanage Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curr_position, 13));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            googleMap.animateCamera(zoom);

            googleMap.addMarker(new MarkerOptions()
                            .title("Shop")
                            .snippet("Is this the right location?")
                    .position(curr_position))
                    .setDraggable(true);


            //googleMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
            //googleMap.setOnInfoWindowClickListener(this);
            //googleMap.setOnMarkerDragListener(this);
        }
    };



    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {


            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        fusedLocationClient.getLastLocation()
                                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        // Got last known location. In some rare situations this can be null.
                                        if (location != null) {

                                            // Logic to handle location object
                                            current_lat = location.getLatitude();
                                            current_lat  = location.getLongitude();

                                            new showAppToast().showSuccess(getContext(),
                                                    "Permission Granted"+current_lat+","+current_long);
                                        }
                                    }
                                });
                    }
                }
                else {
                    //permission denied__ask for permission
                    checkLocationPermission();
                    new showAppToast().showFailure(getContext(),"Permission is needed to continue");
                }
                return;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void showCongratulationsDialog() {
        congratulationsDialog.setContentView(R.layout.congratulation_diag);

        TextView header_title = congratulationsDialog.findViewById(R.id.header_title);
        TextView desc = congratulationsDialog.findViewById(R.id.desc);
        header_title.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fall_down));

        String s = getResources().getString(R.string.sms);
        desc.setText("Hello,\t"+user.getDisplayName()+"\tthanks\t"+s);

        congratulationsDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                congratulationsDialog.dismiss();
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        congratulationsDialog.findViewById(R.id.contact_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.EMAIL", new String[] { "wycliffnjenga19@gmail.com" });
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail Wycliff / N"));

            }
        });

        congratulationsDialog.setCancelable(false);
        congratulationsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        congratulationsDialog.show();

    }
}