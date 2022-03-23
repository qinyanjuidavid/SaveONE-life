package com.wyksofts.saveone.ui.landingUI.authfrags.others;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.wyksofts.saveone.App.MainActivity;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.models.Donors.CreateUserAccount;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.showTermsDiag;
import com.wyksofts.saveone.util.AgreeWithTerms;
import com.wyksofts.saveone.util.PasswordChecker;
import com.wyksofts.saveone.util.showAppToast;


public class SignUpDonor extends Fragment {

    TextView terms;
    CheckBox agree_with_terms;
    EditText UserEmail, UserName, UserPassword;

    ProgressBar google_loading;
    FirebaseAuth mAuth;

    GoogleSignInAccount googleSignInAccount;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;

    Button register_donor;
    LinearLayout continue_with_google;

    public SignUpDonor() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Transition transition = TransitionInflater.from(requireContext())
                .inflateTransition(R.transition.shared_image);
        setSharedElementEnterTransition(transition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up_donor, container, false);

        register_donor = view.findViewById(R.id.register_donor);
        continue_with_google = view.findViewById(R.id.continue_with_google);

        UserName = view.findViewById(R.id.donor_name);
        UserPassword =view.findViewById(R.id.donor_password);
        UserEmail = view.findViewById(R.id.donor_email);
        agree_with_terms = view.findViewById(R.id.donor_agree_with_terms);
        terms = view.findViewById(R.id.terms);

        google_loading = view.findViewById(R.id.loading_bar);

        mAuth = FirebaseAuth.getInstance();
        initGoogle();

        ViewCompat.setTransitionName(register_donor, "donor");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        register_donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyFields();
            }
        });

        continue_with_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new showTermsDiag(getContext()).showTCDialog();
            }
        });
    }


    private void verifyFields() {
        String email = UserEmail.getText().toString();
        String name = UserName.getText().toString();
        String password = UserPassword.getText().toString();
        String isValid = UserPassword.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            UserName.setError("Enter your user name");
        }else if (TextUtils.isEmpty(email)){
            UserEmail.setError("Enter your email");
        }else if(
                !email.contains("@gmail.com")
                        && !email.contains("@outlook.com")
                        && !email.contains("@icloud.com")
                        && !email.contains("@yahoo.com")
                        && !email.contains("@hotmail.com")){
            UserEmail.setError("Invalid email");
        }else if (TextUtils.isEmpty(password)){
            UserPassword.setError("Enter your password");
        }
        else if (password.length()<6){
            UserPassword.setError("Your password is too short, must be atleast 6 characters.");
        }else if (!new PasswordChecker().isValidPassword(isValid)){
            UserPassword.setError("Must have atleast one upper case and one number.");
        }else if (!new AgreeWithTerms().agreewithTerms(agree_with_terms)){
            new showAppToast().showFailure(getContext(), "Agree with our terms to continue.");
        }
        else{
            new CreateUserAccount(getContext()).authUser(name,email,password, google_loading,true);
        }
    }




    private void initGoogle() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.web_client_ip))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        google_loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed
                Log.w(TAG, "Google sign in failed", e);
                new showAppToast().showFailure(getContext(), "Google sign in failed");
                google_loading.setVisibility(View.GONE);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener((Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        google_loading.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            new showAppToast().showSuccess(getContext(), "Hello! Good to See You Back");

                            startActivity(new Intent(getContext(), MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            new showAppToast().showFailure(getContext(), "Timeout error check you network connection");
                        }
                    }
                });
    }
}