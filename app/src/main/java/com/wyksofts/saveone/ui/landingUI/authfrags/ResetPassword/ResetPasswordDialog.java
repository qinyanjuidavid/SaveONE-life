package com.wyksofts.saveone.ui.landingUI.authfrags.ResetPassword;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.wyksofts.saveone.R;
import com.wyksofts.saveone.util.showAppToast;

import es.dmoral.toasty.Toasty;

public class ResetPasswordDialog extends View{
    
    Context mContext;
    Dialog resetPasswordDialog;
    private FirebaseAuth auth;

    public ResetPasswordDialog(Context context) {
        super(context);
        mContext = context;
        resetPasswordDialog = new Dialog(mContext);
        auth = FirebaseAuth.getInstance();
    }

    public void showResetPasswordDiag() {
        resetPasswordDialog.setContentView(R.layout.forgot_password_dialog);
        final EditText UserEmail = (EditText) resetPasswordDialog.findViewById(R.id.forgot_password_Email);
        final ImageView reset_password_btn = (ImageView) resetPasswordDialog.findViewById(R.id.reset_password_btn);
        final ProgressBar progress_bar = (ProgressBar) resetPasswordDialog.findViewById(R.id.reset_password_progress_bar);
        TextView dismiss = (TextView)resetPasswordDialog.findViewById(R.id.close_diag_btn);
        TextView message = resetPasswordDialog.findViewById(R.id.message);

        reset_password_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View param1View) {
                progress_bar.setVisibility(View.VISIBLE);
                reset_password_btn.setVisibility(View.GONE);
                checkValidation(UserEmail, progress_bar, reset_password_btn);
            }
        });
        dismiss.setOnClickListener(new OnClickListener() {
            public void onClick(View param1View) {
                resetPasswordDialog.dismiss();
            }
        });
        resetPasswordDialog.setCancelable(false);
        resetPasswordDialog.getWindow().setBackgroundDrawable((Drawable)new ColorDrawable(0));
        resetPasswordDialog.show();
    }

    private void checkValidation(EditText email, final ProgressBar progressBar, final ImageView sendBtn) {
        String str = email.getText().toString();

        if (TextUtils.isEmpty(str)) {
            email.setError("Enter email");
            return;
        }
        sendBtn.setVisibility(View.VISIBLE);
        if (!str.contains("@gmail.com") && !str.contains("@outlook.com") && !str.contains("@icloud.com")
                && !str.contains("@yahoo.com") && !str.contains("@hotmail.com") ) {
            email.setError("Invalid email!");
            return;
        }
        else {
            sendBtn.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            auth.sendPasswordResetEmail(str).addOnCompleteListener(new OnCompleteListener<Void>() {
                public void onComplete(Task<Void> param1Task) {
                    sendBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    if (!param1Task.isSuccessful()) {
                        new showAppToast().showFailure(getContext(),
                                "Invalid Email!");
                    } else {
                        new showAppToast().showSuccess(getContext(),
                                "Password reset successfully!.Check your email and follow instructions");
                    }
                }
            });
        }
    }

}
