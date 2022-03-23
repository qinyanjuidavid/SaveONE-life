package com.wyksofts.saveone.ui.homeUI.HelperClasses;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bdhobare.mpesa.Mpesa;
import com.bdhobare.mpesa.interfaces.AuthListener;
import com.bdhobare.mpesa.interfaces.MpesaListener;
import com.bdhobare.mpesa.models.STKPush;
import com.bdhobare.mpesa.utils.Pair;
import com.google.android.material.textfield.TextInputLayout;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.MainPage.detailedInfo.DetailedActivity;
import com.wyksofts.saveone.util.Constants.Constants;
import com.wyksofts.saveone.util.showAppToast;

public class showMpesaDialog extends Activity implements AuthListener, MpesaListener {

    Dialog mpesa_dialog;
    Context context;
    ProgressBar loading_bar;
    EditText EPhone,EAmount;

    public showMpesaDialog(Context context) {
        this.context = context;
        mpesa_dialog = new Dialog(context);

        Mpesa.with(this, Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
    }

    public void mpesaDialog(String PayBill, Boolean isSubscribed) {
        mpesa_dialog.setContentView(R.layout.mpesa_dialog);

        EPhone = mpesa_dialog.findViewById(R.id.donor_phone_number);
        EAmount = mpesa_dialog.findViewById(R.id.donor_amount);
        loading_bar = mpesa_dialog.findViewById(R.id.loading_bar);
        TextView txt = mpesa_dialog.findViewById(R.id.txt);
        TextView title = mpesa_dialog.findViewById(R.id.title);


        if (isSubscribed){
            EAmount.setVisibility(View.GONE);
            txt.setText(R.string.isSubscribed_tex);
            title.setText(R.string.subscribe_na_mpesa);
        }else{
            txt.setText(R.string.mpesa_description);
        }

        mpesa_dialog.findViewById(R.id.donate_na_mpesa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMoney(PayBill, EAmount, EPhone, isSubscribed);
            }
        });



        mpesa_dialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mpesa_dialog.dismiss();
                loading_bar.setVisibility(View.GONE);
            }
        });
        mpesa_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        mpesa_dialog.setCancelable(false);
        mpesa_dialog.show();
    }




    //send money na mpesa
    public void sendMoney(String PayBill, EditText amount, EditText phone, Boolean isSubscribed){

        String phoneNumber = EPhone.getText().toString();
        String money;

        if (isSubscribed){
            //get subscription_fee
            money = Constants.SubscriptionAmount;
            //set fee
            EAmount.setHint(money);

        }else{
            money = EAmount.getText().toString();
        }

        if (phoneNumber.length() != 10) {
            EPhone.setError("Invalid number");
        }
        else if (amount.length() >=1) {
            EAmount.setError("Amount should be more than 0");
        }

        loading_bar.setVisibility(View.VISIBLE);

        //stk push
        STKPush.Builder builder = new STKPush.Builder(
                PayBill,
                Constants.PASS_KEY,
                Integer.parseInt(money),
                PayBill,
                phoneNumber);

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
        new showAppToast().showFailure(context, "mpesa"+result.message);
        loading_bar.setVisibility(View.GONE);
        mpesa_dialog.dismiss();
    }

    @Override
    public void onMpesaSuccess(String MerchantRequestID, String CheckoutRequestID, String CustomerMessage) {
        new showAppToast().showSuccess(context, "Success");
        loading_bar.setVisibility(View.GONE);
        mpesa_dialog.dismiss();
    }
}
