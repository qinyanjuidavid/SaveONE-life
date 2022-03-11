package com.wyksofts.saveone.ui.homeUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.rMessagesNotifications;
import com.wyksofts.saveone.util.showAppToast;

public class Settings extends AppCompatActivity {

    private Switch new_message, delivery, real_time;

    //shared preference
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    String isNotification;//new messages notificaation

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setings);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.orange1, this.getTheme()));
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.orange1));
        }

        //shared preference
        pref = getApplicationContext().getSharedPreferences("user", 0);
        editor = pref.edit();


        new_message = findViewById(R.id.new_message);

        //get settings from shared preference
        isNotification = pref.getString("receive_notifications", null);

        if (isNotification.equals(null)){
            new_message.setChecked(false);
        }else if(isNotification.equals("false")){
            new_message.setChecked(false);
        }else{
            new_message.setChecked(true);
        }

        initButtons();
    }

    private void initButtons() {

        new_message.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    //switch is on
                    if (isNotification == null || isNotification.equals("false")){
                        //subscribe
                        new rMessagesNotifications(getApplicationContext()).subscribeToTopic();
                    }else{
                        //do nothing
                        new showAppToast().showSuccess(getApplicationContext(), "You will be receiving notification");
                    }

                }else{
                    //switch is off
                    new rMessagesNotifications(getApplicationContext()).unsubscribeToTopic();

                }
            }
        });

    }

}