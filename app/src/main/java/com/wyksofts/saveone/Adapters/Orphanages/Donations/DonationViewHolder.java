package com.wyksofts.saveone.Adapters.Orphanages.Donations;

import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyksofts.saveone.R;

public class DonationViewHolder extends RecyclerView.ViewHolder {

    TextView who_donated, whats_donated;
    CheckBox is_donation_received;
    LinearLayout donation_card;

    public DonationViewHolder(@NonNull View itemView) {
        super(itemView);
        who_donated = itemView.findViewById(R.id.who_donated);
        whats_donated = itemView.findViewById(R.id.whats_donated);
        is_donation_received = itemView.findViewById(R.id.is_donation_received);
        donation_card = itemView.findViewById(R.id.donation_card);
    }
}