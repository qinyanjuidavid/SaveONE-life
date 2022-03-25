package com.wyksofts.saveone.ui.homeUI.HelperClasses;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wyksofts.saveone.R;

import java.io.IOException;

public class showImageDialog{

    Context context;
    Dialog dialog;

    public showImageDialog(Context context) {
        this.context = context;
        this.dialog = new Dialog(context);
    }

    public void showDialog(String url, String message){

        dialog.setContentView(R.layout.detailed_image_dialog);

        Glide.with(context)
                .load(url)
                .into((ImageView) dialog.findViewById(R.id.image));

        TextView txt = (TextView) dialog.findViewById(R.id.message);
        txt.setText(message);


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        dialog.show();

    }
}
