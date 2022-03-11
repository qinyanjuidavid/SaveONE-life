package com.wyksofts.saveone.ui.homeUI.HelperClasses;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class makeACall {

    public void callAction(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_CALL,
                Uri.parse("tel:" +number ));
        context.startActivity(intent);
    }
}
