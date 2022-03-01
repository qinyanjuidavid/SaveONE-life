package com.wyksofts.saveone.ui.landingUI.authfrags.general;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wyksofts.saveone.App.MainActivity;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.landingUI.authfrags.Organisation.OtherInfo.SelectLocationMap;
import com.wyksofts.saveone.ui.landingUI.authfrags.Organisation.SignUpOrganization;
import com.wyksofts.saveone.ui.landingUI.authfrags.others.SignUpDonor;


public class LandingScreen extends Fragment {

    Button orphanage, donor;
    TextView login_btn,donate_btn;


    public LandingScreen() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_landing_screen, container, false);

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
                showOrphanagePage(new SelectLocationMap());//SignUpOrganization
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
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });


        setTransitionName();

        return view;
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
                //.addSharedElement(orphanage, "orphanage")
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
}