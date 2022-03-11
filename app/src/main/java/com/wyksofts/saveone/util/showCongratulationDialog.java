package com.wyksofts.saveone.util;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.wyksofts.saveone.App.MainActivity;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.sendMail;
import com.wyksofts.saveone.util.Constants.Constants;

public class showCongratulationDialog {

    Dialog congratulationsDialog;

    @SuppressLint("SetTextI18n")
    public void showCongratulationsDialog(Context context, String user) {

        congratulationsDialog = new Dialog(context);

        congratulationsDialog.setContentView(R.layout.congratulation_diag);

        TextView header_title = congratulationsDialog.findViewById(R.id.header_title);
        TextView desc = congratulationsDialog.findViewById(R.id.desc);
        header_title.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fall_down));

        String s = context.getResources().getString(R.string.sms);
        desc.setText("Hello,\t"+user+"\tthanks\t"+s);

        congratulationsDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                congratulationsDialog.dismiss();
                context.startActivity(new Intent(context, MainActivity.class));
            }
        });

        congratulationsDialog.findViewById(R.id.contact_us).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new sendMail().contact_us(context, Constants.EMAIL);

            }
        });

        congratulationsDialog.setCancelable(false);
        congratulationsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        congratulationsDialog.show();

    }
}
