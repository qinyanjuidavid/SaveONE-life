package com.wyksofts.saveone.App;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.MainPage.FragmentHolder;

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
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent, this.getTheme()));
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
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
        super.onBackPressed();
    }
}