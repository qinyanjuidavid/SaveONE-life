package com.wyksofts.saveone.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.wyksofts.saveone.R;

public class LoadingDialog {
    Dialog loadingDialog;


    public void show(Context context){
        loadingDialog = new Dialog(context);
        loadingDialog.setContentView(R.layout.loading_diag);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        loadingDialog.show();
    }

    public void dismiss(Context context){
        loadingDialog = new Dialog(context);
        loadingDialog.setContentView(R.layout.loading_diag);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        loadingDialog.show();
        this.loadingDialog.dismiss();
    }
}
