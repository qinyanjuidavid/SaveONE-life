package com.wyksofts.saveone.model.ServerEML;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wyksofts.saveone.App.MainActivity;
import com.wyksofts.saveone.util.ErrorDialog;
import com.wyksofts.saveone.util.LoadingDialog;
import com.wyksofts.saveone.util.showAppToast;


public class CreateUserAccount extends View {

    FirebaseAuth auth;
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    Context mContext;
    FirebaseFirestore db;

    public CreateUserAccount(Context context) {
        super(context);
        mContext = context;
        auth = FirebaseAuth.getInstance();
        pref = getContext().getSharedPreferences("ProductData", 0);
        editor = pref.edit();
    }


    //verify doner details
    public void authUser(String name, String email, String password, ProgressBar bar){

        //show loading diag
        //new LoadingDialog().show(mContext);
        bar.setVisibility(VISIBLE);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //hide loading dialog
                        //new LoadingDialog().dismiss(mContext);
                        bar.setVisibility(GONE);

                        if (!task.isSuccessful()){
                            try{
                                throw task.getException();
                            }
                            // if user enters wrong email.
                            catch (FirebaseAuthWeakPasswordException weakPassword){
                                showErrorDiag(String.valueOf(weakPassword));
                            }
                            // if user enters wrong password.
                            catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                showErrorDiag("Your email is Invalid");
                            }
                            catch (FirebaseAuthUserCollisionException existEmail) {
                                showErrorDiag("Email is registered on another account");
                            }
                            catch (Exception e){
                                Log.d(TAG, "onComplete: " + e.getMessage());
                                showErrorDiag(String.valueOf(e.getMessage()));
                            }
                        }else{

                            addUserNameToDonorAc(task.getResult().getUser(), name);

                            String s = "Hello\t{"+name+"} \t thanks for registering with us";

                            new showAppToast().showSuccess(getContext(),s);

                            mContext.startActivity(new Intent(getContext(), MainActivity.class));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        new showAppToast().showSuccess(getContext(),"Failed to communicate with the server!");
                        //hide loading dialog
                        new LoadingDialog().dismiss(mContext);
                    }
                });

    }


    private void addUserNameToDonorAc(FirebaseUser user, String name) {
        String username = name;

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

    private void showErrorDiag(String errorMessage){
        new ErrorDialog(getContext()).show(errorMessage,"Account Error!");
    }
}
