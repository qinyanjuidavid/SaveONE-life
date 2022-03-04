package com.wyksofts.saveone.util.HelperClasses;

import android.content.Context;
import android.content.Intent;

public class ContactUs {

    public void contact_us(Context context){
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.EMAIL", new String[] { "wycliffnjenga19@gmail.com" });
        intent.setType("text/html");
        intent.setPackage("com.google.android.gm");
        context.startActivity(Intent.createChooser(intent, "Send mail Wycliff / N"));
    }
}
