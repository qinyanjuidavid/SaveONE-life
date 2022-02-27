package com.wyksofts.saveone.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.tapadoo.alerter.Alerter;
import com.wyksofts.saveone.R;


public class ErrorDialog extends View {

    Context mcontext;

    public ErrorDialog(Context context) {
        super(context);
        this.mcontext = context;
    }

    public void show(String errorMessage, String title){
        Alerter.create((Activity) mcontext)
                .setTitle(title)
                .setText(errorMessage)
                .setBackgroundColorRes(R.color.red)
                .enableVibration(true)
                .setDuration(1500)
                .show();
    }
}
