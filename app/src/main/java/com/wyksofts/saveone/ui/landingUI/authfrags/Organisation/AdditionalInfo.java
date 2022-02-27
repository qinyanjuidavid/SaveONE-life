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


public class AdditionalInfo extends Fragment {

    Button btn;

    public AdditionalInfo() {
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
        View view = inflater.inflate(R.layout.fragment_additional_info, container, false);

        btn = view.findViewById(R.id.register_new_orphanage_info);

        ViewCompat.setTransitionName(btn, "addInfo");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .setCustomAnimations(R.anim.fade_in,
                                R.anim.fade_out)
                        .addSharedElement(btn, "otherInfo")
                        .replace(R.id.root_layout, new OtherInfo())
                        .commit();
            }
        });
        return view;
    }
}