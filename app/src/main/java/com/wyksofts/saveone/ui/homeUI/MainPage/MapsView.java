package com.wyksofts.saveone.ui.homeUI.MainPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
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
import com.wyksofts.saveone.util.getBitmap;
import com.wyksofts.saveone.util.showAppToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MapsView extends Fragment implements OnMapReadyCallback{

    private FirebaseFirestore database;

    Double latitude, longitude;

    LatLng locationList;

    SupportMapFragment mapFragment;

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    //coordinates model
    List<LocationModel> listdata;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps_view, container, false);


        database = FirebaseFirestore.getInstance();

        listdata = new ArrayList<LocationModel>();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        //get locations
        getLocationsData();

    }

    //get location data
    public void getLocationsData() {

        database.collection("Coordinates")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){


                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String coordinates = document.getString("coordinates");

                                String names = document.getString("name");

                                if(!coordinates.isEmpty()){

                                    //create a list of lat-long from coordinates string and separate coordinates string
                                    List<String> latlong = Arrays.asList(coordinates.split(","));

                                    //set latitude and longitude
                                    latitude = Double.valueOf(latlong.get(0));
                                    longitude = Double.valueOf(latlong.get(1));

                                    locationList = new LatLng(latitude,longitude);


                                    //add data to model
                                    listdata.add(new LocationModel(names,locationList));

                                    //show map
                                    showMarkers();

                                }else{
                                    new showAppToast().showFailure(getContext(),"Encountered an error while loading...");
                                }
                            }
                        } else {
                            //error getting coordinates
                            new showAppToast().showFailure(getContext(),"No internet connection");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

    }

    //show markers and location of the orphanages
    public void showMarkers(){
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }


    //show map
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        //loop through data
        for (int i = 0; i < listdata.size(); i++) {

            //add makers
            googleMap.addMarker(new MarkerOptions()
                    .position(listdata.get(i).getLatLng())
                    .title(listdata.get(i).getTitle())

                    .icon(BitmapDescriptorFactory.fromBitmap(
                            new getBitmap().getBitmap(String.valueOf(R.drawable.custom_maker),
                                            120,120, getContext()))));

            //show control UI
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            GoogleMapOptions options = new GoogleMapOptions()
                    .liteMode(true);


            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(listdata.get(i).getLatLng(),
                    8.0f));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(8);
            googleMap.animateCamera(zoom);
        }
    }

}