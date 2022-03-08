package com.wyksofts.saveone.ui.homeUI.DialogsHelperClasses;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.landingUI.LandingHomePage;

public class NoAccountFound {

    Dialog dialog;

    public void showCreateAccountDialog(Context context) {

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.not_logged_in);

        dialog.findViewById(R.id.yes_create)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(new Intent(context, LandingHomePage.class));
                    }
                });

        dialog.findViewById(R.id.no)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        dialog.show();
    }
}
