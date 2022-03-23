package com.wyksofts.saveone.ui.homeUI.HelperClasses;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.MainPage.detailedInfo.DetailedActivity;

public class showMpesaDialog {

    Dialog mpesa_dialog;
    Context context;

    public showMpesaDialog(Context context) {
        this.context = context;
        mpesa_dialog = new Dialog(context);
    }

    public void mpesaDialog() {
        mpesa_dialog.setContentView(R.layout.mpesa_dialog);

        EditText phone = mpesa_dialog.findViewById(R.id.donor_phone_number);
        EditText amount = mpesa_dialog.findViewById(R.id.donor_amount);
        ProgressBar loading_bar = mpesa_dialog.findViewById(R.id.loading_bar);

        mpesa_dialog.findViewById(R.id.donate_na_mpesa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMoney(phone,amount,loading_bar);
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

    private void sendMoney(EditText phone, EditText Amount, ProgressBar loading_bar) {

        String phoneNumber = phone.getText().toString();
        String amount = Amount.getText().toString();


        //check validity of a number
        if (phoneNumber.length() != 10) {
            phone.setError("Invalid number");
            return;
        }
        //check validity of a number
        else if (amount.isEmpty()) {
            Amount.setError("Amount should be more than 0");
            return;
        }

        //onSendMoney(phoneNumber,Amount);
        ((DetailedActivity)context).sendMoney(Integer.parseInt(amount),phoneNumber, loading_bar, mpesa_dialog );

    }
}
