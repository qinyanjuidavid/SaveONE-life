package com.wyksofts.saveone.App;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mikepenz.materialdrawer.Drawer;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.MainPage.FragmentHolder;
import com.wyksofts.saveone.ui.homeUI.MainPage.HomePage;
import com.wyksofts.saveone.ui.homeUI.MainPage.detailedInfo.DetailedInfo;

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
        // remove title
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.pink, this.getTheme()));
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.pink));
        }

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

    @Override
    public void onBackPressed() {
        if(getSupportFragmentManager().getBackStackEntryCount()>0) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.fade_in,
                            R.anim.fade_out)
                    .replace(R.id.home_layout, new FragmentHolder())
                    .commit();
        }else {
            closeDiag.setContentView(R.layout.close_diag);
            closeDiag.findViewById(R.id.yes_exit).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finishAffinity();
                }
            });
            closeDiag.findViewById(R.id.undo).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    closeDiag.dismiss();
                }
            });
            closeDiag.setCancelable(false);
            closeDiag.getWindow().setBackgroundDrawable((Drawable) new ColorDrawable(0));
            closeDiag.show();
        }
    }
}