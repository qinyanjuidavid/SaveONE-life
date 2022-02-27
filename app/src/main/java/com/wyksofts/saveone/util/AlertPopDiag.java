package com.wyksofts.saveone.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.tapadoo.alerter.Alerter;
import com.wyksofts.saveone.R;


public class AlertPopDiag extends View {

    Context mcontext;

    public AlertPopDiag(Context context) {
        super(context);
        this.mcontext = context;
    }

    public void show(String errorMessage, String title){
        Alerter.create((Activity) mcontext)
                .setTitle(title)
                .setText(errorMessage)
                .setBackgroundColorRes(R.color.colorAccent)
                .enableVibration(true)
                .setDuration(2000)
                .show();
    }
}
