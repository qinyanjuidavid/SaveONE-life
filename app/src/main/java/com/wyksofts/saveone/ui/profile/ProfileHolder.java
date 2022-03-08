package com.wyksofts.saveone.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.MainPage.FragmentHolder;
import com.wyksofts.saveone.ui.profile.profilefrags.Profile;

public class ProfileHolder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_holder);

        getSupportFragmentManager().beginTransaction().add(R.id.profile_root,
                new Profile(), "profile").commit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.orange1, this.getTheme()));
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.orange1));
        }
    }
}