package com.wyksofts.saveone.ui.homeUI.MainPage;

import static com.google.firebase.inappmessaging.internal.Logging.TAG;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;
import com.wyksofts.saveone.Adapters.OrphanageListAdapter;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.models.Organisation.OrphanageModel;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends Fragment {

    private List<OrphanageModel> listdata;
    FirebaseUser user;
    private RecyclerView recyclerView;
    private FirebaseFirestore database;
    private OrphanageListAdapter adapter;
    private EditText search;
    private LinearLayout no_result_found,loading_products;
    private Button search_again, retry_connecting;

    Uri group_image;
    LinearLayout loading_orphanage;

    public HomePage() {
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
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        database = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = view.findViewById(R.id.recyclerView);
        loading_orphanage = view.findViewById(R.id.loading_orphanage);

        initUI();

        adapter = new OrphanageListAdapter(listdata,getContext());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void initUI() {
        GridLayoutManager layout = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(layout);

        recyclerView.addItemDecoration(new LayoutMarginDecoration(2, 2));
        listdata = new ArrayList<OrphanageModel>();
        database = FirebaseFirestore.getInstance();
        getData();
    }


    public void getData(){

        loading_orphanage.setVisibility(View.VISIBLE);

        database.collection("Orphanage")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){

                            loading_orphanage.setVisibility(View.GONE);

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


                              StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                              storageRef.child("images/"+email).getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                group_image = uri;
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //failed to find
                                    }
                                });


                                listdata.add(new OrphanageModel(name,location, coordinates,
                                         country,  description, number_of_children,
                                         phone_number, bank_account,bank_account_name,
                                        till_number, group_image, email));

                                recyclerView.setAdapter(adapter);
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