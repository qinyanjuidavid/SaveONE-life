package com.wyksofts.saveone.models.Orphanage.Donations;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.util.AlertPopDiag;
import com.wyksofts.saveone.util.getRandomString;
import com.wyksofts.saveone.util.showAppToast;

import java.util.HashMap;
import java.util.Map;

public class recordDonation {

    Context context;
    FirebaseUser user;
    FirebaseFirestore database;
    String name;

    public recordDonation(Context context, FirebaseUser user, FirebaseFirestore database, String name) {
        this.context = context;
        this.user = user;
        this.database = database;
        this.name = name;
    }

    //add donation to database
    public void recordDonation(EditText phone_number, EditText location, EditText others, Dialog donateDialog, String orphanage_email) {

        final String donor_phone_number = phone_number.getText().toString();
        final String donor_location = location.getText().toString();
        final String other_donation = others.getText().toString();
        String clothes, school, food, health;

        CheckBox food_stuffs = donateDialog.findViewById(R.id.food_stuffs);
        CheckBox clothing = donateDialog.findViewById(R.id.clothings);
        CheckBox education_materials = donateDialog.findViewById(R.id.education_materials);

        if (food_stuffs.isChecked()){
            food = "Yes";
        }else{
            food ="";
        }
        if (clothing.isChecked()){
            clothes = "Yes";
        }else{
            clothes = "";
        }
        if (education_materials.isChecked()){
            school = "Yes";
        }else{
            school = "";
        }
        if (education_materials.isChecked()){
            health = "Yes";
        }else{
            health = "";
        }

        if (TextUtils.isEmpty(donor_phone_number)){
            phone_number.setError("Phone is required");
        }else if (TextUtils.isEmpty(donor_location)){
            location.setError("Location is required");
        }


        Map<String, Object> data = new HashMap<>();;
        Map<String, Object> docData = new HashMap<>();

        if (user !=null) {
            data.put("clothes", clothes);
            data.put("educational_materials", school);
            data.put("email", user.getEmail());
            data.put("food", food);
            data.put("health", health);
            data.put("location", donor_location);
            data.put("name", user.getDisplayName());
            data.put("other", other_donation);
            data.put("phone_number", donor_phone_number);
            docData.put(user.getEmail(), data);
        }
        else{

            String randomName = new getRandomString().getRandomString(10);
            data.put("clothes", clothes);
            data.put("educational_materials", school);
            data.put("email", randomName);
            data.put("food", food);
            data.put("health", health);
            data.put("location", donor_location);
            data.put("name", "");
            data.put("other", other_donation);
            data.put("phone_number", donor_phone_number);
            docData.put(randomName, data);
        }

        database.collection("Donations")
                .document(orphanage_email)
                .set(docData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new showAppToast().showSuccess(context,
                                "Your donation was recorded successfully\t"+
                                        name
                                        +"\t will contact you for more information");
                        donateDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        new AlertPopDiag(context).show(
                                "Oops we have experienced an error while receiving your donation",
                                "Connection error");
                    }
                });
    }


}
