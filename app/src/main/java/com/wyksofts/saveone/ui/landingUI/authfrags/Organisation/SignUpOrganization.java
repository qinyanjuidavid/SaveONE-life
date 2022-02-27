package com.wyksofts.saveone.ui.landingUI.authfrags.Organisation;

import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wyksofts.saveone.R;


public class SignUpOrganization extends Fragment {

    Button register_new_orphanage;

    public SignUpOrganization() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image);
        setSharedElementEnterTransition(transition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_organisation, container, false);

        register_new_orphanage = view.findViewById(R.id.register_new_orphanage);

        ViewCompat.setTransitionName(register_new_orphanage, "orphanage");

        register_new_orphanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .setCustomAnimations(R.anim.fade_in,
                        R.anim.fade_out)
                        .addSharedElement(register_new_orphanage, "addInfo")
                        .replace(R.id.root_layout, new AdditionalInfo())
                        .commit();
            }
        });

        return view;
    }
}