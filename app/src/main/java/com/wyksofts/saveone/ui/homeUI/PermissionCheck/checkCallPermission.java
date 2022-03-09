package com.wyksofts.saveone.ui.homeUI.PermissionCheck;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import com.wyksofts.saveone.ui.homeUI.DialogsHelperClasses.makeACall;
import com.wyksofts.saveone.util.showAppToast;

public class checkCallPermission extends Activity {

    Context context;
    String number;
    Activity activity;

    public checkCallPermission(Context context, String number, Activity activity) {
        this.context = context;
        this.number = number;
        this.activity = activity;
    }

    //check call permission
    public  boolean isCallPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Permission granted
                return true;
            } else {
                //Permission is deniedd
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else {
            //permission automatically granted
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new showAppToast().showSuccess(context, "Permission granted");
                    new makeACall().callAction(context, number);
                } else {
                    new showAppToast().showSuccess(context, "Permission denied");
                }
                return;
            }
        }
    }
}
