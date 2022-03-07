package com.wyksofts.saveone.models.login_md;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.wyksofts.saveone.App.MainActivity;
import com.wyksofts.saveone.util.AlertPopDiag;
import com.wyksofts.saveone.util.LoadingDialog;
import com.wyksofts.saveone.util.showAppToast;

import es.dmoral.toasty.Toasty;

public class LoginUser extends View {

    FirebaseAuth auth;
    Context mcontext;

    public LoginUser(Context context) {
        super(context);
        auth = FirebaseAuth.getInstance();
        mcontext = context;
    }

    public void authUser(String email, String password, ProgressBar bar) {

        //show loading diag
        bar.setVisibility(VISIBLE);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //dismiss loading diag on success
                        bar.setVisibility(GONE);

                        if (!task.isSuccessful()){
                            try{
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthUserCollisionException weakPassword){
                                showErrorDiag(String.valueOf(weakPassword));
                            } catch (Exception e) {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                                showErrorDiag(String.valueOf(e.getMessage()));
                            }
                        }else{

                            String s = "Hello!\t"+email+"\tGood to see you back.";
                            new showAppToast().showSuccess(getContext(), s);

                            mcontext.startActivity(new Intent(getContext(), MainActivity.class));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        bar.setVisibility(GONE);
                        Toasty.error(getContext(),"Loading failed!" , Toast.LENGTH_SHORT, true).show();
                    }
                });
    }

    private void showErrorDiag(String errorMessage){
        new AlertPopDiag(getContext()).show(errorMessage,"Account Error!");;
    }
}
