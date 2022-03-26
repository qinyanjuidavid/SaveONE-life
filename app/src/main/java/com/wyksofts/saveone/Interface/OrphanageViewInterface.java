package com.wyksofts.saveone.Interface;

import android.widget.ImageView;
import android.widget.TextView;

public interface OrphanageViewInterface {

    void onOrphanageClicked(String name, String location, String coordinates,
                            String country, String description, String number_of_children,
                            String phone_number, String bank_account, String bank_account_name,
                            String till_number, String email,
                            String verified, String what_needed, TextView text);

    void onNavigationItemSelected(String name);
}
