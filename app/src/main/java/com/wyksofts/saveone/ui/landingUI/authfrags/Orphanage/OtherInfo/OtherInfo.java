package com.wyksofts.saveone.ui.landingUI.authfrags.Orphanage.OtherInfo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.util.AlertPopDiag;

import java.util.HashMap;
import java.util.Map;


public class OtherInfo extends Fragment{

    //views elements
    Button finish_btn;
    FirebaseUser user;
    FirebaseFirestore database;
    EditText org_description,org_children, location_address, org_in_need;

    Dialog congratulationsDialog;


    public OtherInfo() {
        // Required empty public constructor
    }

    @SuppressLint("MissingPermission")
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
        View view = inflater.inflate(R.layout.fragment_other_info, container, false);

        finish_btn = view.findViewById(R.id.register_new_orphanage_other_info);
        user = FirebaseAuth.getInstance().getCurrentUser();

        org_description = view.findViewById(R.id.organisation_description);
        org_children =view.findViewById(R.id.organisation_number_of_children);
        location_address = view.findViewById(R.id.organisation_location);
        org_in_need = view.findViewById(R.id.organisation_needed);

        ViewCompat.setTransitionName(finish_btn, "otherInfo");


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyFields();
            }
        });
    }

    //get text info from fields and continue
    private void verifyFields() {

        String description = org_description.getText().toString();
        String number_of_children = org_children.getText().toString();
        String location = location_address.getText().toString();
        String what_needed = org_in_need.getText().toString();

        if(TextUtils.isEmpty(description)){
            org_description.setError("Please tell us more about the orphanage");
        }else if (description.length()<20){
            org_description.setError("Description is too short");
        }else if (TextUtils.isEmpty(number_of_children)){
            org_children.setError("Number is required");
        }else if (TextUtils.isEmpty(what_needed)){
            org_in_need.setError("What needed most is required");
        }else if (TextUtils.isEmpty(location)){
            location_address.setError("Please enter location");
        }else if (!location.contains(",")){
            location_address.setError("Please separate using a comma (,)");
        }else{
            //add details
            addInfoToDataBase(description, number_of_children, location, what_needed);
        }
    }


    //add data to other info data base
    private void addInfoToDataBase(String description,
                                   String number_of_children,
                                   String location,
                                   String what_needed) {

        database = FirebaseFirestore.getInstance();

        String email = user.getEmail();
        String org_name = user.getDisplayName();


        Map<String, Object> data = new HashMap<>();
        data.put("description", description);
        data.put("number_of_children", number_of_children);
        data.put("location", location);
        data.put("in_need_of",what_needed);

        //insert data to my db
        database.collection("Orphanage")
                .document(email)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .setCustomAnimations(R.anim.fade_in,
                                        R.anim.fade_out)
                                .addToBackStack(null)
                                .addSharedElement(finish_btn, "addInfo")
                                .replace(R.id.root_layout, new SelectLocationMap())
                                .commit();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        new AlertPopDiag(getContext()).show(
                                "Oops we have experienced an error while uploading",
                                "Connection error");
                    }
                });
    }
}