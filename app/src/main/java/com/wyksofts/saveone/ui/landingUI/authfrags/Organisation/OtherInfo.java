package com.wyksofts.saveone.ui.landingUI.authfrags.Organisation;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.wyksofts.saveone.App.MainActivity;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.util.AlertPopDiag;
import com.wyksofts.saveone.util.showAppToast;

import java.util.HashMap;
import java.util.Map;


public class OtherInfo extends Fragment {

    Button finish_btn;
    FirebaseUser user;
    FirebaseFirestore database;
    EditText org_description,org_children, org_coordinates, location_address;
    Dialog congratulationsDialog;

    public OtherInfo() {
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
        View view = inflater.inflate(R.layout.fragment_other_info, container, false);

        finish_btn = view.findViewById(R.id.register_new_orphanage_other_info);
        user = FirebaseAuth.getInstance().getCurrentUser();

        org_description = view.findViewById(R.id.organisation_description);
        org_children =view.findViewById(R.id.organisation_number_of_children);
        org_coordinates = view.findViewById(R.id.organisation_coordinates);
        location_address = view.findViewById(R.id.organisation_location);

        ViewCompat.setTransitionName(finish_btn, "otherInfo");

        congratulationsDialog = new Dialog(getContext());

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
        String coordinates = org_coordinates.getText().toString();
        String location = location_address.getText().toString();

        if(TextUtils.isEmpty(description)){
            org_description.setError("Please tell us more about the orphanage");
        }else if (description.length()<20){
            org_children.setError("Description is too short");
        }else if (TextUtils.isEmpty(number_of_children)){
            org_children.setError("Number is required");
        }else if (TextUtils.isEmpty(location)){
            location_address.setError("Please enter location");
        }else if (!location.contains(",")){
            location_address.setError("Please separate using a comma (,)");
        }else{
            addInfoToDataBase(description, number_of_children, coordinates, location);
        }
    }


    //add data to other info data base
    private void addInfoToDataBase(String description, String number_of_children,
                                   String coordinates, String location) {

        database = FirebaseFirestore.getInstance();

        String email = user.getEmail();
        String org_name = user.getDisplayName();

        Map<String, Object> data = new HashMap<>();
        data.put("description", description);
        data.put("number_of_children", number_of_children);
        data.put("coordinates", coordinates);
        data.put("location", location);

        //convert to hashmap
        Map<String, Object> docData = new HashMap<>();

        docData.put(org_name, data);

        //insert data to my db
        database.collection("Orphanage")
                .document(email)
                .set(docData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new showAppToast().showSuccess(getContext(),"Done...");
                        showCongratulationsDialog();
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

    @SuppressLint("SetTextI18n")
    private void showCongratulationsDialog() {
        congratulationsDialog.setContentView(R.layout.congratulation_diag);

        TextView header_title = congratulationsDialog.findViewById(R.id.header_title);
        TextView desc = congratulationsDialog.findViewById(R.id.desc);
        header_title.setAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.fall_down));

        String s = getResources().getString(R.string.sms);
        desc.setText("Hello,\t"+user.getDisplayName()+"\tthanks\t"+s);

        congratulationsDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                congratulationsDialog.dismiss();
                startActivity(new Intent(getContext(), MainActivity.class));
            }
        });

        congratulationsDialog.findViewById(R.id.contact_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.EMAIL", new String[] { "wycliffnjenga19@gmail.com" });
                intent.setType("text/html");
                intent.setPackage("com.google.android.gm");
                startActivity(Intent.createChooser(intent, "Send mail Wycliff / N"));

            }
        });

        congratulationsDialog.setCancelable(false);
        congratulationsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        congratulationsDialog.show();

    }


}