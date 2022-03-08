package com.wyksofts.saveone.ui.homeUI.MainPage.detailedInfo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.util.AlertPopDiag;
import com.wyksofts.saveone.util.getBitmap;
import com.wyksofts.saveone.util.showAppToast;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailedInfo extends Fragment implements OnMapReadyCallback {

    TextView name, description, mpesa, phone_number, country,
            bank_account_name, location, number_of_children, email;

    SupportMapFragment mapFragment;

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    String coordinates;
    Double latitude, longitude;

    //values keys
    String pphone_number,pname,pemail, pbank_account, pbank_account_name;
    LatLng curr_location = null;

    //donate
    FloatingActionButton donate;
    Dialog donateDialog;

    //firebase
    FirebaseUser user;
    FirebaseFirestore database;


    public DetailedInfo() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image);
        setSharedElementEnterTransition(transition);

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseFirestore.getInstance();

        donateDialog = new Dialog(getActivity(), R.style.DialogAnimation);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detailed_info, container, false);

        //shared preference
        pref = getContext().getSharedPreferences("OrphanageDetailedData", 0);
        editor = pref.edit();

        name = view.findViewById(R.id.orp_name);
        donate = view.findViewById(R.id.donate);
        description = view.findViewById(R.id.orp_description);
        mpesa = view.findViewById(R.id.orp_mpesa);
        phone_number = view.findViewById(R.id.orp_phone_number);
        bank_account_name = view.findViewById(R.id.orp_bank);
        location = view.findViewById(R.id.orp_location);
        number_of_children = view.findViewById(R.id.orp_number_message);
        email = view.findViewById(R.id.orp_email);
        country = view.findViewById(R.id.orp_country);

        //ViewCompat.setTransitionName(name, "name");

        getValues();

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.orp_map);

        //check if coordinates exits
       if(!coordinates.isEmpty()){
           //create a list of latlong from coordinates string
           List<String> latlong = Arrays.asList(coordinates.split(","));

           //set latitude and longitude
           latitude = Double.valueOf(latlong.get(0));
           longitude = Double.valueOf(latlong.get(1));

           curr_location = new LatLng(latitude,longitude);


           if (curr_location !=null){
               if (mapFragment != null) {
                   mapFragment.getMapAsync(this);
               }
           }else{
               new showAppToast().showFailure(getContext(),"Oops, Unable to get location history.");
           }

       }else{
           new showAppToast().showFailure(getContext(),"Oops, Unable to locate orphanage at the moment.");
       }

       //call phone
       phone_number.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if(isCallPermissionGranted()){
                   callAction();
               }
           }
       });

       //email the orphanage
       email.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent("android.intent.action.SEND");
               intent.putExtra("android.intent.extra.EMAIL", new String[] { pemail });
               intent.setType("text/html");
               intent.setPackage("com.google.android.gm");
               getContext().startActivity(Intent.createChooser(intent, "Send mail\t"+pname));
           }
       });

       //copy bank account number
       bank_account_name.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
               ClipData clip = ClipData.newPlainText(pbank_account_name, pbank_account);
               clipboard.setPrimaryClip(clip);

               new showAppToast().showSuccess(getContext(),"Bank Account Copied");
           }
       });

        donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show donate dialog
                showDonateDialog();
            }
        });

    }

    //show donating dialog
    private void showDonateDialog() {
        donateDialog.setContentView(R.layout.donate_dialog);


        donateDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donateDialog.dismiss();
            }
        });
        EditText phone_number = donateDialog.findViewById(R.id.donor_phone_number);
        EditText location = donateDialog.findViewById(R.id.donor_location);

        String donor_phone_number = phone_number.getText().toString();
        String donor_location = location.getText().toString();

        CheckBox food_stuffs = donateDialog.findViewById(R.id.food_stuffs);
        CheckBox clothing = donateDialog.findViewById(R.id.clothings);
        CheckBox education_materials = donateDialog.findViewById(R.id.education_materials);

        if (food_stuffs.isChecked()){
            String food = "Food Stuffs";
        }
        if (clothing.isChecked()){
            String clothes = "Clothing's";
        }
        if (education_materials.isChecked()){
            String school = "Educational materials";
        }

        donateDialog.findViewById(R.id.donate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordDonation(donor_phone_number,donor_location, food, finalClothes, finalSchool);
            }
        });

        donateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        donateDialog.setCancelable(false);
        donateDialog.show();

    }

    //add donation to database
    private void recordDonation(String donor_phone_number, String donor_location,
                                String finalFood, String finalClothes, String finalSchool) {

        String orphanage_email = pref.getString("email",null);

        Map<String, Object> data = new HashMap<>();
        data.put("phone_number", donor_phone_number);
        data.put("location", donor_location);
        data.put("email", user.getEmail());
        data.put("name", user.getDisplayName());
        data.put("food", finalFood);
        data.put("clothes", finalClothes);
        data.put("educational_materials", finalSchool);

        Map<String, Object> docData = new HashMap<>();
        docData.put(user.getEmail(), Arrays.asList(data));

        database.collection("Donations")
                .document(orphanage_email)
                .set(docData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new showAppToast().showSuccess(getContext(),
                                "Your donation was recorded successfully\t"+
                                        pname
                                        +"\t will contact you for more information");
                        donateDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        new AlertPopDiag(getContext()).show(
                                "Oops we have experienced an error while receiving your donation",
                                "Connection error");
                    }
                });

    }


    //call action
    private void callAction() {
        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" +pphone_number ));
        startActivity(intent);
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        //get orphanage name
        pname = pref.getString("name","Orphanage Home");
        name.setText("About\t"+pname);

        googleMap.addMarker(new MarkerOptions()
                .position(curr_location)
                .title(pname)
                .icon(BitmapDescriptorFactory.fromBitmap(new getBitmap()
                        .getBitmap(String.valueOf(R.drawable.custom_maker),
                                120,120,getContext())))
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.custom_maker))
        );

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curr_location, 12.0f));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        googleMap.animateCamera(zoom);
    }



    //get detailed data for the orphanage
    @SuppressLint("SetTextI18n")
    private void getValues() {

        String plocation = pref.getString("location",null);
        coordinates = pref.getString("coordinates","0,0");

        String pcountry = pref.getString("country",null);
        String pdescription = pref.getString("description",null);
        String pnoc = pref.getString("number_of_children",null);
        pphone_number = pref.getString("phone_number",null);
        pbank_account = pref.getString("bank_account", null);
        pbank_account_name = pref.getString("bank_account_name",null);
        String ptill_number = pref.getString("till_number",null);
        pemail = pref.getString("email",null);
        String pwhat_needed = pref.getString("what_needed",null);
        String pverified = pref.getString("verified",null);

        location.setText(plocation);
        country.setText(pcountry);
        description.setText(pdescription);
        number_of_children.setText("We have\t"+pnoc+"\tchildren's in need of\t"+pwhat_needed);
        phone_number.setText(pphone_number);
        bank_account_name.setText("Bank Account:\t"+pbank_account+"\tBank Name:\t"+pbank_account_name);
        mpesa.setText("Donate na mpesa ("+ptill_number+")");
        email.setText(pemail);
    }





    //check call permission
    @SuppressLint("ObsoleteSdkInt")
    public  boolean isCallPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Permission granted
                return true;
            } else {
                //Permission is revoked
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else {
            //permission is automatically granted
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new showAppToast().showSuccess(getContext(), "Permission granted");
                    callAction();
                } else {
                    new showAppToast().showSuccess(getContext(), "Permission denied");
                }
                return;
            }
        }
    }
}