package com.wyksofts.saveone.util.HelperClasses;

import android.content.Context;
import android.content.Intent;

import com.wyksofts.saveone.R;

public class ShareApp {

    public void shareApp(Context context){
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT",
                "Download:-\t" + context.getString(R.string.app_name));
        intent.putExtra("android.intent.extra.TEXT",
                "https://wyksoftsinc.web.app");
        context.startActivity(Intent.createChooser(intent, "Share via"));
    }
}
