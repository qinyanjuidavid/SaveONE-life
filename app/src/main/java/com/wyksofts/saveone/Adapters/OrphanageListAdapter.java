package com.wyksofts.saveone.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.models.Organisation.OrphanageModel;

import java.util.List;

public class OrphanageListAdapter extends RecyclerView.Adapter<OrphanageListAdapter.ViewHolder> {

    List<OrphanageModel> list_array;
    Context context;

    public OrphanageListAdapter(List<OrphanageModel> list_array, Context context) {
        this.list_array = list_array;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orphanage_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrphanageModel data = list_array.get(position);

        Glide.with(context)
                .load(data.getGroup_image())
                .placeholder(R.drawable.imggg)
                .into(holder.group_image);

        String number = data.getNumber_of_children()+"\tLife's";
        String address = "Address:\t"+data.getLocation();

        holder.name.setText(data.getName());
        holder.location.setText(address);
        holder.number_of_c.setText(number);

    }

    @Override
    public int getItemCount() {
        return list_array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView group_image;
        TextView name, location, number_of_c;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            group_image = itemView.findViewById(R.id.org_image);
            name = itemView.findViewById(R.id.org_name);
            location = itemView.findViewById(R.id.org_location);
            number_of_c = itemView.findViewById(R.id.org_number);
        }
    }
}
