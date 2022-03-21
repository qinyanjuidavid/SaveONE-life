package com.wyksofts.saveone.Adapters.Orphanages.Donations;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.wyksofts.saveone.Interface.DonationViewInterface;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.models.Orphanage.Donations.DonationsModel;
import com.wyksofts.saveone.util.AlertPopDiag;
import com.wyksofts.saveone.util.showAppToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DonationsListAdapter extends RecyclerView.Adapter<DonationViewHolder> {
    List<DonationsModel> list_array;
    Context context;
    DonationViewInterface vI;
    FirebaseUser user;
    FirebaseFirestore database;

    public DonationsListAdapter(List<DonationsModel> list_array, Context context, DonationViewInterface vI) {
        this.list_array = list_array;
        this.context = context;
        this.vI = vI;

    }

    @NonNull
    @Override
    public DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DonationViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orphanage_donations_card,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DonationViewHolder holder, int position) {
        DonationsModel data = list_array.get(position);

        //label donation card title/name
        String name = data.getName();
        String phone_number = data.getPhone_number();
        String email = data.getEmail();
        String isDonationReceived = data.getIsDonationReceived();

        if (!name.isEmpty()){
            holder.who_donated.setText(name);
        }else{
            holder.who_donated.setText(phone_number);
        }

        if (!email.isEmpty()){
            holder.is_donation_received.setVisibility(View.VISIBLE);
        }else{
            holder.is_donation_received.setVisibility(View.GONE);
        }

        //label donation what offered
        String location = data.getLocation();
        String food = data.getFood_stuffs();
        String education_materials = data.getEducation_materials();
        String clothing = data.getClothing();
        String other = data.getOther();
        String health = data.getHealth();

        String f,e, c, h, others;//data holders

        if (food.equals("Yes")){
            f = "\tfood stuffs\t";
        }else{
            f = "";
        }
        if(education_materials.equals("Yes")){
            e = "\teducational materials\t";
        }else{
            e ="";
        }
        if(clothing.equals("Yes")){
            c = "\tclothings\t";
        }else{
            c = "";
        }
        if(health.equals("Yes")){
            h = "\thealth services.\t";
        }else{
            h = "";
        }

        if (other != null){
            others = data.getOther();
        }else{
            others = "";
        }

        if (f.isEmpty() && c.isEmpty() && e.isEmpty() && h.isEmpty()){
            holder.whats_donated.setText("Donated other items.");
        }

        String donations = "From\t"+location+"\tdonated\t"+f+e+c+h+others;

        holder.whats_donated.setText(donations);

        holder.donation_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vI.makeACall(phone_number,email,name);
            }
        });

        if (isDonationReceived.equals("true")){
            holder.is_donation_received.setChecked(true);
        }else{
            holder.is_donation_received.setChecked(false);
        }

        holder.is_donation_received.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    //send data to the database
                    addInfoToDataBase("true",email);
                }else{
                    addInfoToDataBase("false",email);
                }
            }
        });
    }


    public void addInfoToDataBase(String condition, String donerUID){

        Dialog loadingDialog = new Dialog(context);

        loadingDialog.setContentView(R.layout.loading_diag);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        loadingDialog.show();

        database = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        Map<String, Object> data = new HashMap<>();;
        Map<String, Object> docData = new HashMap<>();

        String email = user.getEmail();


        data.put("isDonationReceived", condition);
        docData.put(donerUID, data);

        database.collection("Donations")
                .document(email)
                .set(docData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new showAppToast().showSuccess(context, "Thanks for the confirmation");
                        loadingDialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        new AlertPopDiag(context).show(
                                "Oops we have experienced an error while processing your data",
                                "Connection error");
                    }
                });
    }

    @Override
    public int getItemCount() {
        return list_array.size();
    }

    //Update new list
    public void upDateList(List<DonationsModel> listData) {
        ArrayList<DonationsModel> arrayList = new ArrayList<>();
        list_array = arrayList;
        arrayList.addAll(listData);
        notifyDataSetChanged();
    }
}
