package com.wyksofts.saveone.Adapters.Orphanages.Orphanage;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyksofts.saveone.R;

public class OrphanageViewHolder extends RecyclerView.ViewHolder {
    ImageView group_image, verified;
    TextView name, location, number_of_c;
    Button show_more;

    public OrphanageViewHolder(@NonNull View itemView) {
        super(itemView);
        group_image = itemView.findViewById(R.id.org_image);
        name = itemView.findViewById(R.id.org_name);
        location = itemView.findViewById(R.id.org_location);
        number_of_c = itemView.findViewById(R.id.org_number);
        verified = itemView.findViewById(R.id.verified_home);
        show_more = itemView.findViewById(R.id.detailed_view_btn);
    }
}