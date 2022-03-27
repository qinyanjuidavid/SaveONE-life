package com.wyksofts.saveone.ui.landingUI.authfrags.Orphanage.OtherInfo;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.PermissionCheck.checkLocationPermission;
import com.wyksofts.saveone.util.AlertPopDiag;
import com.wyksofts.saveone.util.getBitmap;
import com.wyksofts.saveone.util.showAppToast;
import com.wyksofts.saveone.util.showCongratulationDialog;

import java.util.HashMap;
import java.util.Map;

public class SelectLocationMap extends Fragment implements
        OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener{

    //current location
    double current_lat, current_long;
    LatLng curr_position;

    final Marker marker_final = null;
    SupportMapFragment mapFragment;

    Dialog congratulationsDialog;
    FirebaseUser user;

    //enable location dialog
    Dialog enable_locationDialog;
    LocationManager lm;
    boolean isGPS = false;
    boolean isNetwork = false;

    private FusedLocationProviderClient fusedLocationClient;

    //shared preference
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    LinearLayout finish_layout;
    Button finishBtn;

    FirebaseFirestore database;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image);
        setSharedElementEnterTransition(transition);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_select_location_map, container, false);
        congratulationsDialog = new Dialog(getContext());
        enable_locationDialog = new Dialog(getContext());

        finish_layout = v.findViewById(R.id.finish_layout);
        finishBtn = v.findViewById(R.id.finish_btn);

        ViewCompat.setTransitionName(finishBtn, "addInfo");

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());

        //get current location from db
        pref = getContext().getSharedPreferences("location", 0);
        editor = pref.edit();

        double latitude = Double.parseDouble(pref.getString("latitude","null"));
        double longitude = Double.parseDouble(pref.getString("longitude","null"));

        curr_position = new LatLng(latitude, longitude);


        //get location
        lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        user = FirebaseAuth.getInstance().getCurrentUser();
        return v;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.select_location_map);

        if (curr_position !=null && mapFragment != null){
            mapFragment.getMapAsync(this);
        }else{
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                OnGPS();
            } else {
                getLocation();
                mapFragment.getMapAsync(this);
            }
        }

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCoordinatesToDataBase();
            }
        });
    }


    //add info to database
    private void addCoordinatesToDataBase() {
        database = FirebaseFirestore.getInstance();
        String email = user.getEmail();

        String coordinates = curr_position.latitude+","+curr_position.longitude;

        Map<String, Object> data = new HashMap<>();
        data.put("coordinates", coordinates);

        database.collection("Orphanage")
                .document(email)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        saveLocationCoordinates(user.getDisplayName(),email);
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        new AlertPopDiag(getContext()).show(
                                "Oops we have experienced an error while uploading",
                                "Connection error");
                    }
                });

    }
    //add coordinates to the map coordinates
    public void saveLocationCoordinates(String name, String email){

        String coordinates = curr_position.latitude+","+curr_position.longitude;

        Map<String, Object> data = new HashMap<>();
        data.put("coordinates", coordinates);
        data.put("name", name);


        database.collection("Coordinates")
                .document(email)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        new showAppToast().showSuccess(getContext(),"Done...All set");
                        new showCongratulationDialog().showCongratulationsDialog(getContext(),
                                user.getDisplayName().toString());
                    }
                })

                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        new AlertPopDiag(getContext()).show(
                                "Oops we have experienced an error while uploading",
                                "Connection error");
                    }
                });
    }

    //callback map view with the current location
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        if (curr_position != null) {

            googleMap.addMarker(new MarkerOptions()
                    .position(curr_position)
                    .title("My Current Location")
                    .icon(BitmapDescriptorFactory.fromBitmap(new getBitmap()
                            .getBitmap(String.valueOf(R.drawable.custom_maker),
                                    120,120, getContext())))
            );

            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curr_position, 14.0f));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
            googleMap.animateCamera(zoom);

            if(user.getDisplayName() != null) {

                googleMap.addMarker(new MarkerOptions()
                        .title("Home Orphanage")
                        .snippet("Is this the right location of\t" + user.getDisplayName() + "?")
                        .icon(BitmapDescriptorFactory.fromBitmap(new getBitmap()
                                .getBitmap(String.valueOf(R.drawable.custom_maker),
                                        120,120, getContext())))
                        .position(curr_position))
                        .setDraggable(true);

            }else{
                googleMap.addMarker(new MarkerOptions()
                        .title("Home Orphanage")
                        .snippet("Is this the right location of the home orphanage your are registering?")
                        .icon(BitmapDescriptorFactory.fromBitmap(new getBitmap()
                                .getBitmap(String.valueOf(R.drawable.custom_maker),
                                        120,120, getContext())))
                        .position(curr_position))
                        .setDraggable(true);
            }

            //googleMap.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
            googleMap.setOnInfoWindowClickListener(this);
            googleMap.setOnMarkerDragListener(this);

        } else {
            OnGPS();
        }
    }



    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        LatLng position0 = marker.getPosition();

        Log.d(getClass().getSimpleName(),
                String.format("Drag from %f:%f",
                position0.latitude,
                position0.longitude));
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LatLng position0 = marker.getPosition();

        Log.d(getClass().getSimpleName(),
                String.format("Dragging to %f:%f",
                        position0.latitude,
                        position0.longitude));

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        curr_position = marker.getPosition();

        Log.d(getClass().getSimpleName(),
                String.format("Dragged to %f:%f",
                curr_position.latitude,
                curr_position.longitude));
    }


    //get location
    public void getLocation() {
        new checkLocationPermission(getContext(), getActivity()).getLocation();
    }


    //open gps
    private void OnGPS() {

        enable_locationDialog.setContentView(R.layout.enable_location_diag);

        enable_locationDialog.findViewById(R.id.enable_location)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        enable_locationDialog.dismiss();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

        enable_locationDialog.setCancelable(false);
        enable_locationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        enable_locationDialog.show();
    }
}