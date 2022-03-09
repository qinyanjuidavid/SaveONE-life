package com.wyksofts.saveone.models.Orphanage.Orphanage;

import static com.google.firebase.inappmessaging.internal.Logging.TAG;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class OrphanageData extends ViewModel {

    FirebaseFirestore database;
    List<String> list;

    //get orphanage data
    public void getOrphanageData(LinearLayout loading_view){

        database = FirebaseFirestore.getInstance();

        loading_view.setVisibility(View.VISIBLE);

        database.collection("Orphanage")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            loading_view.setVisibility(View.GONE);

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());

                                String name = document.getString("name");
                                String bank_account = document.getString("bank_account");
                                String bank_account_name = document.getString("bank_name");
                                String till_number = document.getString("till_number");
                                String coordinates = document.getString("coordinates");
                                String country = document.getString("country");
                                String description = document.getString("description");
                                String location = document.getString("location");
                                String number_of_children = document.getString("number_of_children");
                                String phone_number = document.getString("phone_number");
                                String email = document.getString("email");
                                String verified = document.getString("verified");
                                String what_needed = document.getString("in_need_of");
                                String url = document.getString("url");

                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
}
