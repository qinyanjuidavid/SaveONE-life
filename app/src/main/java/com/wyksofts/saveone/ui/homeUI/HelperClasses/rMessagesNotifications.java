package com.wyksofts.saveone.ui.homeUI.HelperClasses;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.util.showAppToast;

public class rMessagesNotifications extends View {

    //context
    Context mcontext;
    Dialog notificationDialog;

    //shared preference
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    public rMessagesNotifications(Context context) {
        super(context);
        this.mcontext = context;
        notificationDialog = new Dialog(mcontext);

        //shared preference
        pref = getContext().getSharedPreferences("user", 0);
        editor = pref.edit();
    }

    public void show() {
        notificationDialog.setContentView(R.layout.receive_notification);

        notificationDialog.findViewById(R.id.yes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationDialog.dismiss();

                //subscribe
                subscribeToTopic();
            }
        });
        notificationDialog.findViewById(R.id.no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationDialog.dismiss();
                //unsubscribe
                unsubscribeToTopic();
            }
        });

        notificationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        notificationDialog.setCancelable(true);
        notificationDialog.show();

    }

    public void subscribeToTopic(){
        FirebaseMessaging.getInstance().subscribeToTopic("chats")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (!task.isSuccessful()) {
                            new showAppToast().showSuccess(mcontext,"We encountered an error");
                        }else{
                            new showAppToast().showSuccess(mcontext,"You will receive notifications");

                            //save to shared preference
                            editor.putString("receive_notifications","true");
                            editor.commit();
                        }

                    }
                });
    }

    public void unsubscribeToTopic(){
        FirebaseMessaging.getInstance().unsubscribeFromTopic("chats");
        new showAppToast().showFailure(mcontext,"You will not receive notifications");

        //save to shared preference
        editor.putString("receive_notifications","false");
        editor.commit();
    }
}
