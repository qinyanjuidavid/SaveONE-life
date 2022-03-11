package com.wyksofts.saveone.notifications;

import static android.content.ContentValues.TAG;

import static com.wyksofts.saveone.util.Constants.Constants.NOTIFICATION_ID;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.splashscreen.SplashScreen;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.util.Constants.Constants;

public class FirebaseChatNotification extends FirebaseMessagingService {

    FirebaseUser user;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        //get current user
        user = FirebaseAuth.getInstance().getCurrentUser();

        //check for user
        if (user != null){

            String name = user.getDisplayName();

            String title = remoteMessage.getNotification().getTitle();
            String body =  "Hello,\t"+name+"\t"+remoteMessage.getNotification().getBody();


            //Pending intents
            Intent intent = new Intent(this, SplashScreen.class);//open app
            PendingIntent launchIntent = PendingIntent.getActivity(this, 0, intent, 0);

            Intent buttonIntent = new Intent(getBaseContext(), FirebaseChatNotification.class);//this .dismiss notification
            buttonIntent.putExtra("notificationId", NOTIFICATION_ID);
            PendingIntent dismissIntent = PendingIntent.getBroadcast(
                    getBaseContext(), 0, buttonIntent, PendingIntent.FLAG_CANCEL_CURRENT);

            //show notification
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel  = new NotificationChannel(
                        Constants.CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_HIGH);

                getSystemService(NotificationManager.class).createNotificationChannel(channel);

                Notification.Builder notification = new Notification.Builder(this, Constants.CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setAutoCancel(true)
                        .setContentIntent(launchIntent)
                        .addAction(android.R.drawable.ic_menu_view, "View Message ", launchIntent)
                        .addAction(android.R.drawable.ic_delete, "Dismiss", dismissIntent);


                NotificationManagerCompat.from(this).notify(NOTIFICATION_ID,
                        notification.build());
            }

        }else{
            //remind user to donate
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationChannel channel  = new NotificationChannel(
                        Constants.CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_HIGH);

                getSystemService(NotificationManager.class).createNotificationChannel(channel);

                Notification.Builder notification = new Notification.Builder(this, Constants.CHANNEL_ID)
                        .setContentTitle("Hey there buddy")
                        .setContentText("Fight poverty right now together with us by making a donation.")
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setAutoCancel(true);


                NotificationManagerCompat.from(this).notify(NOTIFICATION_ID,
                        notification.build());
            }
        }

    }

}

