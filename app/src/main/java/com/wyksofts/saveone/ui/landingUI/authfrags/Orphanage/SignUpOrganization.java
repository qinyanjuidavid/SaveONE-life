package com.wyksofts.saveone.ui.landingUI.authfrags.Orphanage;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.models.Donors.CreateUserAccount;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.showTermsDiag;
import com.wyksofts.saveone.util.AgreeWithTerms;
import com.wyksofts.saveone.util.PasswordChecker;
import com.wyksofts.saveone.util.showAppToast;


public class SignUpOrganization extends Fragment {

    Button register_new_orphanage;
    FragmentManager fragmentmanager;
    TextView terms;

    CheckBox agree_with_terms;
    EditText OrgEmail, OrgName, OrgPassword, OrgPasswordConfirm;

    ProgressBar loading_bar;

    SharedPreferences.Editor editor;
    SharedPreferences pref;

    public SignUpOrganization() {
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
        View view = inflater.inflate(R.layout.fragment_sign_up_organisation, container, false);

        FirebaseAuth.getInstance().signOut();

        register_new_orphanage = view.findViewById(R.id.register_new_orphanage);

        pref = getContext().getSharedPreferences("user", 0);
        editor = pref.edit();

        OrgName = view.findViewById(R.id.organisation_name);
        OrgPassword =view.findViewById(R.id.organisation_password);
        OrgPasswordConfirm = view.findViewById(R.id.confirm_organisation_password);
        OrgEmail = view.findViewById(R.id.organisation_email);
        agree_with_terms = view.findViewById(R.id.agree_with_terms);
        loading_bar = view.findViewById(R.id.loading_bar);
        terms = view.findViewById(R.id.terms);

        ViewCompat.setTransitionName(register_new_orphanage, "orphanage");


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        register_new_orphanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifyFields();
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
        String email = OrgEmail.getText().toString();
        String name = OrgName.getText().toString();
        String password = OrgPassword.getText().toString();
        String isValid = OrgPassword.getText().toString().trim();

        String password_confirm = OrgPasswordConfirm.getText().toString();
        String password_confirm_isInvalid = OrgPasswordConfirm.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            OrgName.setError("Enter your user name");
        }else if (TextUtils.isEmpty(email)){
            OrgEmail.setError("Enter your email");
        }else if(
                !email.contains("@gmail.com")
                        && !email.contains("@outlook.com")
                        && !email.contains("@icloud.com")
                        && !email.contains("@yahoo.com")
                        && !email.contains("@hotmail.com")){
            OrgEmail.setError("Invalid email");
        }
        else if (TextUtils.isEmpty(password)){
            OrgPassword.setError("Enter your password");
        }
        else if (password.length()<6){
            OrgPassword.setError("Your password is too short, must be atleast 6 characters.");
        }
        else if (!new PasswordChecker().isValidPassword(isValid)){
            OrgPassword.setError("Must have at least one upper case and one number.");
        }
        else if (password_confirm.length()<6){
            OrgPasswordConfirm.setError("Your password is too short, must be at least 6 characters.");
        }
        else if (!new PasswordChecker().isValidPassword(password_confirm_isInvalid)){
            OrgPasswordConfirm.setError("Must have at least one upper case and one number.");
        }
        else if(!password.equals(password_confirm)){
            OrgPasswordConfirm.setError("Password are not matching");
        }
        else if (!new AgreeWithTerms().agreewithTerms(agree_with_terms)){
            new showAppToast().showFailure(getContext(), "Agree with our terms to continue.");
        }
        else{
            new CreateUserAccount(getContext()).authUser(name, email, password, loading_bar,false);

            Boolean isDonor = pref.getBoolean("isDonor",false);

            editor.putString("email",email);
            editor.putString("name",email);
            editor.commit();

            if (!isDonor){
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.fade_in,
                                R.anim.fade_out)
                        .addToBackStack(null)
                        .addSharedElement(register_new_orphanage, "addInfo")
                        .replace(R.id.root_layout, new AdditionalInfo())
                        .commit();
            }else{
                new showAppToast().showFailure(getContext(),"Failed to Create Account");
            }

        }
    }
}