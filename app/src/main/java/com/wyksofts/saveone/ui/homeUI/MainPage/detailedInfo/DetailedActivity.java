package com.wyksofts.saveone.ui.homeUI.MainPage.detailedInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.bdhobare.mpesa.Mode;
import com.bdhobare.mpesa.Mpesa;
import com.bdhobare.mpesa.interfaces.AuthListener;
import com.bdhobare.mpesa.interfaces.MpesaListener;
import com.bdhobare.mpesa.models.STKPush;
import com.bdhobare.mpesa.utils.Pair;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.MainPage.FragmentHolder;
import com.wyksofts.saveone.util.Constants.Constants;
import com.wyksofts.saveone.util.showAppToast;

public class DetailedActivity extends AppCompatActivity{

    ProgressBar progressBar;
    Dialog mpesa_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detailed);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent, this.getTheme()));



        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.detailed_layout, new DetailedInfo())
                .commit();
    }



}