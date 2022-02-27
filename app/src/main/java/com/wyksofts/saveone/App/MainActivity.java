package com.wyksofts.saveone.App;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.mikepenz.materialdrawer.Drawer;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.MainPage.FragmentHolder;
import com.wyksofts.saveone.ui.homeUI.MainPage.HomePage;

public class MainActivity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    Drawer mainDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showHomePage();

        pref = getApplicationContext().getSharedPreferences("homepage", 0);
        editor = pref.edit();
    }

    private void showHomePage() {
        getSupportFragmentManager().beginTransaction().add(R.id.main_layout,
                new FragmentHolder()).commit();
    }
}