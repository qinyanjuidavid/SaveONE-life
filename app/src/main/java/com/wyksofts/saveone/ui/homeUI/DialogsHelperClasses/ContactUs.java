package com.wyksofts.saveone.ui.homeUI.DialogsHelperClasses;

import android.content.Context;
import android.content.Intent;

import com.wyksofts.saveone.util.Constants.Constants;

public class ContactUs {

    public void contact_us(Context context){
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.EMAIL", new String[] {Constants.EMAIL});
        intent.setType("text/html");
        intent.setPackage(Constants.ANDROID_GM);
        context.startActivity(Intent.createChooser(intent, "Send mail Wycliff / N"));
    }
}
