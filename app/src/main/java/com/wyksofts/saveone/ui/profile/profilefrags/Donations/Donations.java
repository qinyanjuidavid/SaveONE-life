package com.wyksofts.saveone.ui.profile.profilefrags.Donations;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;
import com.wyksofts.saveone.Adapters.Orphanages.Donations.DonationsListAdapter;
import com.wyksofts.saveone.Interface.DonationViewInterface;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.models.Orphanage.Donations.DonationsModel;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.makeACall;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.sendMail;
import com.wyksofts.saveone.ui.homeUI.PermissionCheck.checkCallPermission;
import com.wyksofts.saveone.util.showAppToast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Donations extends Fragment implements DonationViewInterface {

    private List<DonationsModel> listdata;
    FirebaseUser user;
    private RecyclerView recyclerView;
    private FirebaseFirestore database;
    private DonationsListAdapter adapter;
    private EditText search;

    ImageView groupPhoto, closePage;
    TextView profile_name,donations_list;

    Dialog loading_dialog;

    //shared preference
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    public Donations() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading_dialog = new Dialog(getContext());

        Transition transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image);
        setSharedElementEnterTransition(transition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donations, container, false);

        database = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        recyclerView = view.findViewById(R.id.donations_recyclerView);;
        search = view.findViewById(R.id.search_orphanage);
        groupPhoto = view.findViewById(R.id.profile_image);
        profile_name = view.findViewById(R.id.name);
        donations_list = view.findViewById(R.id.donations_list);
        closePage = view.findViewById(R.id.arrow_back);

        search = view.findViewById(R.id.search_donation);

        user = FirebaseAuth.getInstance().getCurrentUser();

        //init UI
        initUI();

        //adapter
        adapter = new DonationsListAdapter(listdata,getContext(), this);
        database = FirebaseFirestore.getInstance();

        //shared preference
        pref = getContext().getSharedPreferences("user", 0);
        editor = pref.edit();

        ViewCompat.setTransitionName(closePage, "arrow_back");

        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String url = pref.getString("url",null);

        if (user != null) {
            profile_name.setText(user.getDisplayName());

            if (user.getPhotoUrl() != null) {
                //set google url
                Glide.with(getContext())
                        .load(user.getPhotoUrl())
                        .into(groupPhoto);

            }else{
                //set placeholder(no placeholder)
                Glide.with(getContext())
                        .load(R.drawable.donations_bg)
                        .into(groupPhoto);
            }
        }else{

        }


        closePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStackImmediate();
            }
        });

        initSearch();
    }

    private void initSearch() {

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }

    public void filter(String name) {
        //new list
        ArrayList<DonationsModel> arrayList = new ArrayList<>();

        //loop through
        for (DonationsModel model : listdata) {
            if (model.getName().toLowerCase().contains(name)) {
                recyclerView.setVisibility(View.VISIBLE);
                new showAppToast().showSuccess(getContext(),"Donation(s) found");

                arrayList.add(model);
            } else {
                if (arrayList.isEmpty()) {
                    //new showAppToast().showFailure(getContext(),"No donation found");
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }
            if (name.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
                //new showAppToast().showFailure(getContext(),"No donation found");
            }
            adapter.upDateList(arrayList);
        }

    }



    //init UI
    private void initUI(){

        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layout);

        recyclerView.addItemDecoration(new LayoutMarginDecoration(2, 2));
        listdata = new ArrayList<DonationsModel>();
        database = FirebaseFirestore.getInstance();

        initLoadingDialog();

        getDonationData();//get donation data from database
    }

    private void initLoadingDialog() {
        loading_dialog.setContentView(R.layout.upload_image);
        loading_dialog.setCancelable(false);
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }


    //get data from database
    private void getDonationData() {

        loading_dialog.show();

        DocumentReference docRef = database.collection("Donations").document(user.getEmail());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                        loading_dialog.dismiss();

                        //get mpa from fire store
                        Map<String, Object> map = document.getData();

                        //loop through to get key and value(whereby value is in form of a hash map)
                        for(Map.Entry<String, Object> entry : map.entrySet()) {

                            //get wanted data (the inside map)
                            Map<String, Object> value = (Map<String, Object>) entry.getValue();

                            String name = String.valueOf(value.get("name"));
                            String clothing = String.valueOf(value.get("clothes"));
                            String education_materials = String.valueOf(value.get("educational_materials"));
                            String email = String.valueOf(value.get("email"));
                            String location = String.valueOf(value.get("location"));
                            String phone_number = String.valueOf(value.get("phone_number"));
                            String food_stuffs = String.valueOf(value.get("food"));
                            String isDonationReceived = String.valueOf(value.get("isDonationReceived"));
                            String other = String.valueOf(value.get("other"));
                            String health = String.valueOf(value.get("health"));

                            listdata.add(new DonationsModel(name, food_stuffs, clothing,
                                    education_materials, location, phone_number,email,isDonationReceived, other, health));

                            recyclerView.setAdapter(adapter);

                            //get the number of donations
                            donations_list.setText(user.getDisplayName()
                                    +"\t\tYou have\t["+listdata.size()+"]\tdonation(s)");

                        }

                    } else {
                        new showAppToast().showFailure(getContext(), "Oops! we could not load data.");
                        loading_dialog.dismiss();
                    }
                } else {
                    new showAppToast().showFailure(getContext(), "Oops! low internet connection.");
                    loading_dialog.dismiss();
                }
            }
        });

    }


    @Override
    public void makeACall(String phone_number, String email, String name) {

        if(new checkCallPermission(getContext(),phone_number,getActivity()).isCallPermissionGranted()){
            if (!phone_number.isEmpty()){
                new makeACall().callAction(getContext(),phone_number);

                if(!name.isEmpty()){
                    new showAppToast().showFailure(getContext(),"Calling.."+name);
                }else{
                    new showAppToast().showFailure(getContext(),"Calling.."+email);
                }

            }else{
                new showAppToast().showFailure(getContext(),"Invalid phone number, send email instead");
                new sendMail().contact_us(getContext(),email);
            }
        }else{
            new showAppToast().showFailure(getContext(),"Your device is not supported");
        }

    }


}