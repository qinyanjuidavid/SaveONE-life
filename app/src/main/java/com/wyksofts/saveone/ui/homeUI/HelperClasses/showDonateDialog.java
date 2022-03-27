package com.wyksofts.saveone.ui.homeUI.HelperClasses;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.models.Orphanage.Donations.recordDonation;

public class showDonateDialog {

    Context context;
    Dialog donateDialog;

    FirebaseUser user;
    FirebaseFirestore database;

    public showDonateDialog(Context context) {
        this.context = context;
        donateDialog = new Dialog(context);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseFirestore.getInstance();
    }


    //show donating dialog
    public void showDonateDialog(String pname, String orphanage_email) {
        donateDialog.setContentView(R.layout.donate_dialog);

        donateDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donateDialog.dismiss();
            }
        });
        EditText phone_number = donateDialog.findViewById(R.id.donor_phone_number);
        EditText location = donateDialog.findViewById(R.id.donor_location);
        EditText others = donateDialog.findViewById(R.id.others);

        donateDialog.findViewById(R.id.donate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new recordDonation(context,user, database,pname)
                        .recordDonation(phone_number,location,others,donateDialog,orphanage_email);
            }
        });
        donateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        donateDialog.setCancelable(false);
        donateDialog.show();
    }
}
