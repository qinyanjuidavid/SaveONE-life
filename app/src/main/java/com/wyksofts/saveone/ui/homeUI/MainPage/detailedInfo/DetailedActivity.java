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

public class DetailedActivity extends AppCompatActivity implements AuthListener, MpesaListener {

    ProgressBar progressBar;
    Dialog mpesa_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detailed);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent, this.getTheme()));

        Mpesa.with(this, Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.detailed_layout, new DetailedInfo())
                .commit();
    }

    //send money na mpesa
    public void sendMoney(int amount, String phone, ProgressBar loading_bar, Dialog dialog){

        this.progressBar = loading_bar;
        this.mpesa_dialog = dialog;

        progressBar.setVisibility(View.VISIBLE);

        STKPush.Builder builder = new STKPush.Builder("174379", Constants.PASS_KEY,
                amount,"174379", phone);

        STKPush push = builder.build();

        Mpesa.getInstance().pay(this, push);
    }

    @Override
    public void onAuthError(Pair<Integer, String> result) {
        new showAppToast().showFailure(this, "auth"+result.message);
    }

    @Override
    public void onAuthSuccess() {

    }

    @Override
    public void onMpesaError(Pair<Integer, String> result) {
        new showAppToast().showFailure(this, "mpesa"+result.message);
        progressBar.setVisibility(View.GONE);
        mpesa_dialog.dismiss();
    }

    @Override
    public void onMpesaSuccess(String MerchantRequestID, String CheckoutRequestID, String CustomerMessage) {
        new showAppToast().showSuccess(this, "Success");
        progressBar.setVisibility(View.GONE);
        mpesa_dialog.dismiss();
    }

}