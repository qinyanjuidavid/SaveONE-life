package com.wyksofts.saveone.ui.homeUI.HelperClasses;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.landingUI.LandingHomePage;

public class LandingPageDialog extends View {

    Context mcontext;
    Dialog landingScreenDialog;

    public LandingPageDialog(Context context) {
        super(context);
        this.mcontext = context;
    }

    public void show(){
        landingScreenDialog = new Dialog(mcontext);
        landingScreenDialog.setContentView(R.layout.go_back_landing_page);

        landingScreenDialog.findViewById(R.id.undo)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        landingScreenDialog.dismiss();
                    }
                });

        landingScreenDialog.findViewById(R.id.yes)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        landingScreenDialog.dismiss();
                        mcontext.startActivity(new Intent(mcontext, LandingHomePage.class));
                    }
                });

        landingScreenDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        landingScreenDialog.setCancelable(true);
        landingScreenDialog.show();


    }
}
