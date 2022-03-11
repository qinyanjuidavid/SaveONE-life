package com.wyksofts.saveone.ui.homeUI.HelperClasses;

import android.content.Context;
import android.content.Intent;

import com.wyksofts.saveone.util.Constants.Constants;

public class sendMail {

    public void contact_us(Context context, String email){
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.EMAIL", new String[] {email});
        intent.setType("text/html");
        intent.setPackage(Constants.ANDROID_GM);
        context.startActivity(Intent.createChooser(intent, "Send mail"));
    }
}
