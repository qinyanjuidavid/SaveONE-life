package com.wyksofts.saveone.ui.homeUI.MainPage;

import static com.google.firebase.inappmessaging.internal.Logging.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.models.LocationModel.LocationModel;
import com.wyksofts.saveone.models.Organisation.OrphanageModel;
import com.wyksofts.saveone.util.getBitmap;
import com.wyksofts.saveone.util.showAppToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class MapsView extends Fragment implements OnMapReadyCallback{

    private FirebaseFirestore database;

    Double latitude, longitude;

    LatLng locationList;

    SupportMapFragment mapFragment;


    // creating array list for adding all our locations.
    public ArrayList<LatLng> locationArrayList;//location
    public List<String> orphanage_names;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps_view, container, false);


        database = FirebaseFirestore.getInstance();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        //get locations
        //getLocationsData();

        //initialize arraylist
        locationArrayList = new ArrayList<>();
        orphanage_names = new ArrayList<>();
    }


    public void getLocationsData() {

        database.collection("Orphanage")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){


                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String coordinates = document.getString("coordinates");

                                String names = document.getString("name");

                                if(!coordinates.isEmpty()){

                                    //create a list of latlong from coordinates string and seperate the string
                                    List<String> latlong = Arrays.asList(coordinates.split(","));

                                    //set latitude and longitude
                                    latitude = Double.valueOf(latlong.get(0));
                                    longitude = Double.valueOf(latlong.get(1));

                                    locationList = new LatLng(latitude,longitude);

                                    //init $$ add list
                                    locationArrayList.add(locationList);
                                    orphanage_names.add(names);

                                    showMarkers();

                                    //new showAppToast().showSuccess(getContext(),""+locationList);

                                }else{

                                }
                            }
                        } else {
                            //error getting doc
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }

    public void showMarkers(){
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }


    //show map
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        new showAppToast().showSuccess(getContext(),""+orphanage_names);

        for (int i = 0; i < orphanage_names.size(); i++) {//for locations

            for (int k = 0; k < locationArrayList.size(); k++) {//for orphanage names

                //add makers
                googleMap.addMarker(new MarkerOptions()
                        .position(locationArrayList.get(k))
                        //.title(orphanage_names.get(i)));
                        .icon(BitmapDescriptorFactory
                                .fromBitmap(new getBitmap()
                                        .getBitmap(String.valueOf(R.drawable.custom_maker),
                                                120,120, getContext()))));
            }

            //show control UI
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationArrayList.get(i), 12.0f));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(8);
            googleMap.animateCamera(zoom);

        }
    }

}