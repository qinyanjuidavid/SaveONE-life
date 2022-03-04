package com.wyksofts.saveone.util.HelperClasses;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.landingUI.LandingHomePage;

public class LogOutDialog extends View {
    Context mcontext;
    Dialog logoutDialog;

    public LogOutDialog(Context context) {
        super(context);
        this.mcontext = context;
        logoutDialog = new Dialog(mcontext);
    }

    public void show() {
        logoutDialog.setContentView(R.layout.logout_dialog);

        logoutDialog.findViewById(R.id.log_out).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                mcontext.startActivity(new Intent(getContext(), LandingHomePage.class));
            }
        });
        logoutDialog.findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutDialog.dismiss();
            }
        });

        logoutDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logoutDialog.setCancelable(true);
        logoutDialog.show();

    }
}
