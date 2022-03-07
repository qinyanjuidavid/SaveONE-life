package com.wyksofts.saveone.ui.landingUI.authfrags.general;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wyksofts.saveone.App.MainActivity;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.MainPage.FragmentHolder;
import com.wyksofts.saveone.ui.landingUI.authfrags.Organisation.OtherInfo.SelectLocationMap;
import com.wyksofts.saveone.ui.landingUI.authfrags.Organisation.SignUpOrganization;
import com.wyksofts.saveone.ui.landingUI.authfrags.others.SignUpDonor;
import com.wyksofts.saveone.util.showAppToast;


public class LandingScreen extends Fragment {

    Button orphanage, donor;
    TextView login_btn,donate_btn;

    //sign in dialog
    Dialog yes_create;

    //get user
    FirebaseUser user;

    public LandingScreen() {
        //Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yes_create = new Dialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing_screen, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        donor = view.findViewById(R.id.register_as_donor_btn);
        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDonorPage(new SignUpDonor());
            }
        });

        orphanage = view.findViewById(R.id.register_new_orphanage_btn);
        orphanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrphanagePage(new SignUpOrganization());//
            }
        });

        login_btn = view.findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoginPage(new Login());
            }
        });

        donate_btn = view.findViewById(R.id.donate_btn);
        donate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser();
            }
        });


        setTransitionName();

        return view;
    }

    public void checkUser(){

        if (user != null){
            //yes user exists continue
            startActivity(new Intent(getContext(), MainActivity.class));

        }else{
            //no user found
            yes_create.setContentView(R.layout.not_logged_in);

            yes_create.findViewById(R.id.yes_create)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            yes_create.dismiss();
                        }
                    });

            yes_create.findViewById(R.id.no)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getContext(), MainActivity.class));
                        }
                    });

            yes_create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            yes_create.setCancelable(true);
            yes_create.show();
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    //transition names
    private void setTransitionName() {
        ViewCompat.setTransitionName(login_btn, "landing");
        ViewCompat.setTransitionName(orphanage, "landing2");
        ViewCompat.setTransitionName(donor, "landing3");
    }

    public void showLoginPage(Fragment fragment){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.fade_in,
                        R.anim.fade_out)
                .addSharedElement(login_btn, "login")
                .replace(R.id.root_layout, fragment)
                .commit();
    }

    public void showOrphanagePage(Fragment fragment) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.fade_in,
                        R.anim.fade_out)
                .addSharedElement(orphanage, "orphanage")
                .replace(R.id.root_layout, fragment)
                .commit();
    }

    public void showDonorPage(Fragment fragment){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.fade_in,
                        R.anim.fade_out)
                .addSharedElement(donor, "donor")
                .replace(R.id.root_layout, fragment)
                .commit();
    }

    public void showHomePage(Fragment fragment){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .setCustomAnimations(R.anim.fade_in,
                        R.anim.fade_out)
                //.addSharedElement(donor, "donor")
                .replace(R.id.root_layout, fragment)
                .commit();
    }






}