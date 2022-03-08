package com.wyksofts.saveone.Adapters.Orphanages;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wyksofts.saveone.Interface.OrphanageViewInterface;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.models.Orphanage.OrphanageModel;
import com.wyksofts.saveone.util.showAppToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrphanageListAdapter extends RecyclerView.Adapter<OrphanageViewHolder> {

    List<OrphanageModel> list_array;
    Context context;
    OrphanageViewInterface viewInterface;


    public OrphanageListAdapter(List<OrphanageModel> list_array, Context context,
                                OrphanageViewInterface viewInterface) {
        this.list_array = list_array;
        this.context = context;
        this.viewInterface = viewInterface;
    }

    @NonNull
    @Override
    public OrphanageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrphanageViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orphanage_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrphanageViewHolder holder, int position) {
        OrphanageModel data = list_array.get(position);

        Glide.with(context)
                .load(data.getGroup_image())
                .placeholder(R.drawable.imggg)
                .into(holder.group_image);

        String what_needed = data.getWhat_needed();
        String number = data.getNumber_of_children()+"\tLife's"+"\tin need of\t"+what_needed;
        String address = "Address:\t"+data.getLocation();

        //feed data to the UI card
        holder.name.setText(data.getName());
        holder.location.setText(address);
        holder.number_of_c.setText(number);

        //check whether home is verified or not
        String verified_home = data.getVerified();

        if (verified_home != null){
            holder.verified.setVisibility(View.VISIBLE);
        }else{
            holder.verified.setVisibility(View.GONE);
        }

        //initialize show more about this place
        holder.show_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewInterface.onOrphanageClicked(
                        data.getName(), data.getLocation(),
                        data.getCoordinates(), data.getCountry(),
                        data.getDescription(), data.getNumber_of_children(),
                        data.getPhone_number(), data.getBank_account(),
                        data.getBank_account_name(), data.getTill_number(),
                        data.getEmail(), data.getVerified(),
                        data.getWhat_needed(), holder.name
                );
            }
        });

    }

    //Update new list
    public void upDateList(List<OrphanageModel> listData) {
        ArrayList<OrphanageModel> arrayList = new ArrayList<>();
        list_array = arrayList;
        arrayList.addAll(listData);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list_array.size();
    }
}
