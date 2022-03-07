package com.wyksofts.saveone.ui.landingUI.authfrags.general;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.wyksofts.saveone.R;
import com.wyksofts.saveone.models.login_md.LoginUser;
import com.wyksofts.saveone.ui.landingUI.authfrags.ResetPassword.ResetPasswordDialog;
import com.wyksofts.saveone.util.PasswordChecker;

public class Login extends Fragment {

    Button login_btn;
    private EditText UserEmail,UserPassword;
    private TextView forgotPassword;
    private Switch RememberMe;
    private ProgressBar loading_bar;

    public Login() {
        //Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        login_btn = view.findViewById(R.id.login_btn2);

        ViewCompat.setTransitionName(login_btn, "login");

        loading_bar = view.findViewById(R.id.loading_bar);
        loading_bar.setVisibility(View.GONE);


        UserEmail = view.findViewById(R.id.login_name);
        UserPassword = view.findViewById(R.id.login_password);
        forgotPassword = view.findViewById(R.id.forgot_password);
        RememberMe = view.findViewById(R.id.remember_me);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ResetPasswordDialog(getContext()).showResetPasswordDiag();
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyFields();
            }
        });
    }

    private void verifyFields() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        String isValid = UserPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            UserEmail.setError("Enter your email");
        }else if(!email.contains("@gmail.com") && !email.contains("@outlook.com") && !email.contains("@icloud.com")
                && !email.contains("@yahoo.com") && !email.contains("@hotmail.com")){
            UserEmail.setError("Invalid email");
        }else if (TextUtils.isEmpty(password)){
            UserPassword.setError("Enter your password");
        }
        else if (password.length()<6){
            UserPassword.setError("Your password is too short, must be atleast 6 characters.");
        }else if (!new PasswordChecker().isValidPassword(isValid)){
            UserPassword.setError("Must have atleast one upper case and one number.");
        }
        else{
            new LoginUser(getContext()).authUser(email,password,loading_bar);
        }
    }
}