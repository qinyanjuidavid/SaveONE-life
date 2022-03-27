package com.wyksofts.saveone.ui.homeUI.MainPage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.models.LocationModel.LocationModel;
import com.wyksofts.saveone.util.Constants.Constants;
import com.wyksofts.saveone.util.getBitmap;
import com.wyksofts.saveone.util.showAppToast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MapsView extends Fragment implements OnMapReadyCallback {


    Double latitude, longitude;

    LatLng locationList;

    SupportMapFragment mapFragment;

    SharedPreferences.Editor editor;
    SharedPreferences pref;


    EditText search;

    GoogleMap map;
    FloatingActionButton change_map;

    //coordinates model
    List<LocationModel> listdata;

    private LatLng mOrigin;

    FirebaseFirestore database;
    FirebaseUser user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_view, container, false);

        pref = getContext().getSharedPreferences("location", 0);
        editor = pref.edit();

        database = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        listdata = new ArrayList<LocationModel>();

        search = view.findViewById(R.id.search_orphanage);
        change_map = view.findViewById(R.id.change_map);

        change_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeMapDialog(view);
            }
        });

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

        //search filter for the orphanages
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }


    //search an orphanage in the map
    public void filter(String orphanage_name) {
        ArrayList<LocationModel> list_array = new ArrayList<>();

        //if orphanage is empty
        if (orphanage_name.isEmpty()){
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin, 6));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(6);
            map.animateCamera(zoom);
        }else{

            //orphanage is not empty
            for (LocationModel model : listdata){
                if (model.getTitle().toLowerCase().contains(orphanage_name)){

                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(model.getLatLng(), 17));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
                    map.animateCamera(zoom);

                    list_array.add(model);

                }else{
                    //no result found
                }
            }
        }
    }

    //show map
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;

        //loop through coordinates data
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
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setMapToolbarEnabled(true);
            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            String curr_lat = pref.getString("latitude", null);
            String curr_long = pref.getString("longitude", null);

            mOrigin = new LatLng(Double.parseDouble(curr_lat), Double.parseDouble(curr_long));

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mOrigin, 8.0f));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(8);
            googleMap.animateCamera(zoom);
        }
    }


    PopupWindow popupWindow;
    LinearLayout normal, satellite, terrain, hybrid;

    //show choose map type popup
    private void showChangeMapDialog(View view) {

        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) view.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.change_map_dialog, null);

        //length and width
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;

        //window height and width
        popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(view, Gravity.CENTER | Gravity.BOTTOM, 0, 0);

        //set animation
        popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);

        normal = popupView.findViewById(R.id.normal);
        satellite = popupView.findViewById(R.id.satellite);
        terrain = popupView.findViewById(R.id.terrain);
        hybrid = popupView.findViewById(R.id.hybrid);

        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNormalMap(view);
            }
        });

        satellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSatelliteMap(view);
            }
        });

        terrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTerrainMap(view);
            }
        });

        hybrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onHybridMap(view);
            }
        });

        //close if other parts of the window are touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                popupWindow.dismiss();
                return true;
            }
        });
    }


    public void onNormalMap(View view) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void onSatelliteMap(View view) {
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void onTerrainMap(View view) {
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
    }


    public void onHybridMap(View view) {
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }


}