package com.wyksofts.saveone.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class getBitmap {


    //convert image to bitmap
    public Bitmap getBitmap(String drawableName, int width, int height, Context context){
        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(),
                context.getResources().getIdentifier(drawableName, "drawable",
                        context.getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }
}
