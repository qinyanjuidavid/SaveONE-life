package com.wyksofts.saveone.App;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.LandingPageDialog;
import com.wyksofts.saveone.ui.homeUI.MainPage.FragmentHolder;
import com.wyksofts.saveone.ui.landingUI.LandingHomePage;

public class MainActivity extends AppCompatActivity {

    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Dialog closeDiag;

    public FragmentTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(FragmentTransaction transaction) {
        this.transaction = transaction;
    }

    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setStatusBarColor(getResources().getColor(R.color.orange1, this.getTheme()));

        setContentView(R.layout.activity_main);

        closeDiag = new Dialog(this);

        showHomePage();

        pref = getApplicationContext().getSharedPreferences("homepage", 0);
        editor = pref.edit();

    }

    private void showHomePage() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.home_layout, new FragmentHolder())
                .commit();
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    ViewPager viewPager;



    @Override
    public void onBackPressed() {
        viewPager = findViewById(R.id.pager);
        if (viewPager.getCurrentItem() == 2){
            viewPager.setCurrentItem(0);
        }else if (viewPager.getCurrentItem() == 1){
            viewPager.setCurrentItem(0);
        }
        else{
            new LandingPageDialog(this).show();
        }
    }
}