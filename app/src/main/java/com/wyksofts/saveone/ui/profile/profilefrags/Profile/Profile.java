package com.wyksofts.saveone.ui.profile.profilefrags.Profile;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.showLogOutDialog;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.showMpesaDialog;
import com.wyksofts.saveone.ui.landingUI.authfrags.Orphanage.OtherInfo.SelectLocationMap;
import com.wyksofts.saveone.ui.profile.profilefrags.Donations.Donations;
import com.wyksofts.saveone.util.AlertPopDiag;
import com.wyksofts.saveone.util.Constants.Constants;
import com.wyksofts.saveone.util.showAppToast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Profile extends Fragment {

    ImageView arrow_back, logout_menu, profile_image;
    TextView profile_name, profile_phone_number, profile_email,profile_verified;
    CardView profile_location_card,profile_number_ch_card, profile_in_need_card, not_verified_card;
    TextView prof_location,prof_number_ch, prof_in_need;
    TextView profile_description, profile_country, profile_title;
    Button updateProfileBtn, copyNumber, Update_LocationBtn;
    LinearLayout subscribe_na_mpesa;

    //update profile dialog
    Dialog  LogOutDialog, uploadImageDialog,updateProfileDialog;

    //firebase and storage
    FirebaseFirestore database;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageReference;

    //dialog_view
    ImageView groupPhoto;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 111;

    //shared preference
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    LinearLayout hidden_information;

    FloatingActionButton donations;


    public Profile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogOutDialog = new Dialog(getContext());
        updateProfileDialog = new Dialog(getContext());
        uploadImageDialog = new Dialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_profile, container, false);

        //init firebase
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        hidden_information = view.findViewById(R.id.hidden_information);

        initLoading();

        //toolbar
        arrow_back = view.findViewById(R.id.arrow_back);
        logout_menu = view.findViewById(R.id.logout_menu);
        profile_image = view.findViewById(R.id.profile_image);
        donations = view.findViewById(R.id.donations);
        copyNumber = view.findViewById(R.id.copy_number);

        //text_views
        profile_phone_number = view.findViewById(R.id.profile_phone_number);
        profile_name = view.findViewById(R.id.profile_name);
        profile_email = view.findViewById(R.id.profile_email);
        profile_description = view.findViewById(R.id.profile_description);
        profile_country = view.findViewById(R.id.profile_country);
        profile_verified = view.findViewById(R.id.profile_verified);
        profile_title = view.findViewById(R.id.profile_title);
        subscribe_na_mpesa = view.findViewById(R.id.subscribe_na_mpesa);

        //card_views
        profile_location_card = view.findViewById(R.id.profile_location);
        profile_number_ch_card = view.findViewById(R.id.profile_number_ch);
        profile_in_need_card = view.findViewById(R.id.profile_in_need);
        not_verified_card = view.findViewById(R.id.not_verified_card);

        //txt
        prof_location = view.findViewById(R.id.prof_location);
        prof_number_ch = view.findViewById(R.id.prof_number_ch);
        prof_in_need = view.findViewById(R.id.prof_in_need);

        updateProfileBtn = view.findViewById(R.id.Update_Profile);
        Update_LocationBtn = view.findViewById(R.id.Update_Location);

        user = FirebaseAuth.getInstance().getCurrentUser();

        //shared preference
        pref = getContext().getSharedPreferences("user", 0);
        editor = pref.edit();

        ViewCompat.setTransitionName(arrow_back, "landing");
        ViewCompat.setTransitionName(Update_LocationBtn, "landing2");


        if (user != null) {
            profile_name.setText(user.getDisplayName());
            profile_email.setText(user.getEmail());

            profile_verified.setText("Not Verified");
            Glide.with(getContext())
                    .load(user.getPhotoUrl())
                    .into(profile_image);

            //get profile info
            getProfileInfo(user.getEmail().toString());

            updateProfileBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UpdateProfile();
                }
            });

            donations.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .addToBackStack(null)
                            .setCustomAnimations(R.anim.fade_in,
                                    R.anim.fade_out)
                            .addToBackStack(null)
                            .addSharedElement(arrow_back, "arrow_back")
                            .replace(R.id.profile_root, new Donations())
                            .commit();
                }
            });

        }else {
            //no user don't show
            new showAppToast().showFailure(getContext(), "Failed to get info");
        }

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();//go back
            }
        });

        Update_LocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show update map
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .addToBackStack(null)
                        .setCustomAnimations(R.anim.fade_in,
                                R.anim.fade_out)
                        .addToBackStack(null)
                        .addSharedElement(Update_LocationBtn, "addInfo")
                        .replace(R.id.profile_root, new SelectLocationMap())
                        .commit();

            }
        });

        logout_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show logout dialog
                new showLogOutDialog(getContext()).show();
            }
        });

        copyNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                //copy number to the clipboard
                ClipboardManager clipboard = (ClipboardManager) getActivity()
                        .getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Phone Copied", "+254703285070");
                clipboard.setPrimaryClip(clip);
                new showAppToast().showSuccess(getContext(),"Copied number");
            }
        });

        subscribe_na_mpesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //new showAppToast().showFailure(getContext(),"Service is not available at the moment");
                new showMpesaDialog(getContext()).mpesaDialog(Constants.TILL_NUMBER,true);
            }
        });

    }

    //init data loading loading
    private void initLoading() {
        uploadImageDialog.setContentView(R.layout.upload_image);
        uploadImageDialog.setCancelable(false);
        uploadImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }



    //get profile information
    public void getProfileInfo(String email){

        uploadImageDialog.show();

        database = FirebaseFirestore.getInstance();
        DocumentReference ref = database
                .collection("Orphanage")
                .document(email);//check if user is a donor

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()) {

                        hidden_information.setVisibility(View.VISIBLE);

                        uploadImageDialog.dismiss();

                        String name = document.getString("name");
                        String bank_account = document.getString("bank_account");
                        String bank_account_name = document.getString("bank_name");
                        String till_number = document.getString("till_number");
                        String coordinates = document.getString("coordinates");
                        String country = document.getString("country");
                        String description = document.getString("description");
                        String location = document.getString("location");
                        String number_of_children = document.getString("number_of_children");
                        String phone_number = document.getString("phone_number");
                        String email = document.getString("email");
                        String verified = document.getString("verified");
                        String what_needed = document.getString("in_need_of");
                        String url = document.getString("url");

                        profile_name.setText(name);
                        prof_location.setText("Address:\t"+location);
                        prof_number_ch.setText(number_of_children+"\t\tChildren's");
                        profile_description.setText(description);
                        prof_in_need.setText("In need of,\t"+what_needed);
                        profile_country.setText(country);
                        profile_phone_number.setText(phone_number);
                        profile_email.setText(email);

                        if (verified !=null){
                            profile_verified.setText("Verified");
                            //hide the card
                            not_verified_card.setVisibility(View.GONE);
                        }else{
                            profile_verified.setText("Not Verified");
                            profile_title.setText("Hello,"+user.getDisplayName()+"\tyour orphanage is not verified");
                            not_verified_card.setVisibility(View.VISIBLE);
                        }

                        Glide.with(getContext())
                                .load(url)
                                .placeholder(R.drawable.imggg)
                                .into(profile_image);
                    }
                    else {
                        //user is a doner
                        hidden_information.setVisibility(View.GONE);
                        uploadImageDialog.dismiss();
                        donations.setVisibility(View.GONE);

                        //show message to doner user here
                    }
                }
                else {
                    new AlertPopDiag(getContext()).show("Please make sure you are connected to the internet","Connection Error!!!");
                }
            }
        });
    }


    //update profile
    public void UpdateProfile(){

        EditText profile_description, profile_number_of_children,
                profile_what_needed, profile_phone_number,
                profile_till_number,profile_bank_account, profile_bank_name;

        updateProfileDialog.setContentView(R.layout.update_profile_dialog);

        groupPhoto = updateProfileDialog.findViewById(R.id.update_image);
        groupPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFile();
            }
        });

        //other views
        profile_description = updateProfileDialog.findViewById(R.id.profile_description);
        profile_number_of_children = updateProfileDialog.findViewById(R.id.profile_number_of_children);
        profile_what_needed = updateProfileDialog.findViewById(R.id.profile_what_needed);
        profile_phone_number = updateProfileDialog.findViewById(R.id.profile_phone_number);
        profile_till_number = updateProfileDialog.findViewById(R.id.profile_till_number);
        profile_bank_account = updateProfileDialog.findViewById(R.id.profile_bank_account);
        profile_bank_name = updateProfileDialog.findViewById(R.id.profile_bank_account_name);


        updateProfileDialog.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfileDialog.dismiss();
            }
        });

        updateProfileDialog.findViewById(R.id.update_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open update fuction
                String p_description = profile_description.getText().toString();
                String p_number_of_children = profile_number_of_children.getText().toString();
                String p_what_needed = profile_what_needed.getText().toString();
                String p_phone_number = profile_phone_number.getText().toString();
                String p_till_number = profile_till_number.getText().toString();
                String p_bank_account = profile_bank_account.getText().toString();
                String p_bank_name = profile_bank_name.getText().toString();

                //check if field are empty
                if (TextUtils.isEmpty(p_description) && TextUtils.isEmpty(p_number_of_children) &&
                        TextUtils.isEmpty(p_what_needed) && TextUtils.isEmpty(p_phone_number) &&
                        TextUtils.isEmpty(p_till_number) && TextUtils.isEmpty(p_bank_account) &&
                        TextUtils.isEmpty(p_bank_name)){
                    new showAppToast().showFailure(getContext(), "***Please each field must filled***");
                }else{
                    //if not update profile
                    sendUpdatedData(p_description, p_number_of_children,p_what_needed,
                            p_phone_number, p_till_number, p_bank_account,
                            p_bank_name );

                }
            }
        });

        updateProfileDialog.setCancelable(false);
        updateProfileDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        updateProfileDialog.show();
    }

    //send updated data to databasde
    private void sendUpdatedData(String p_description, String p_number_of_children,String p_what_needed,
                                 String p_phone_number, String p_till_number, String p_bank_account,
                                 String p_bank_name ) {

        String email = user.getEmail();
        String org_name = user.getDisplayName();

        if (user != null) {

            Map<String, Object> data = new HashMap<>();
            data.put("phone_number", p_phone_number);
            data.put("till_number", p_till_number);
            data.put("bank_account", p_bank_account);
            data.put("bank_name", p_bank_name);
            data.put("description", p_description);
            data.put("number_of_children", p_number_of_children);
            data.put("in_need_of",p_what_needed);

            database.collection("Orphanage")
                    .document(email)
                    .set(data, SetOptions.merge())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            uploadImage();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            new AlertPopDiag(getContext()).show(
                                    "Oops we have experienced an error while uploading",
                                    "Connection error");
                        }
                    });

        }else{

        }
    }



    //upload image
    private void uploadImage() {
        if (filePath != null) {

            uploadImageDialog.show();

            // Defining the child of storageReference using user email
            String name = user.getEmail();

            StorageReference ref = storageReference.child("images/" + name);

            // adding listeners on upload
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                            uploadImageDialog.dismiss();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();
                            addImageUrl(downloadUrl);

                            editor.putString("url",downloadUrl.toString());
                            editor.commit();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e){
                            new showAppToast().showFailure(getContext(),"Oops your group image is too large in size");
                            uploadImageDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(
                                UploadTask.TaskSnapshot taskSnapshot){
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            TextView progress_text = uploadImageDialog.findViewById(R.id.text_status);
                            progress_text.setText(
                                    new StringBuilder().append("Updating your profile in\t")
                                            .append((int) progress).append("%")
                                            .toString());
                        }
                    });
        }else{
            new showAppToast().showFailure(getContext(),"Group photo can not be empty");
        }
    }


    //add image url to the database
    private void addImageUrl(Uri downloadUrl) {
        uploadImageDialog.show();
        String email = user.getEmail();

        Map<String, Object> data = new HashMap<>();
        data.put("url", downloadUrl.toString());

        database.collection("Orphanage")
                .document(email)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        uploadImageDialog.dismiss();
                        updateProfileDialog.dismiss();
                        new showAppToast().showSuccess(getContext(),"Profile Updated Successfully");

                        // Reload current fragment
                        Fragment frg = null;
                        frg = getActivity().getSupportFragmentManager().findFragmentByTag("profile");
                        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        new AlertPopDiag(getContext()).show(
                                "Oops we have experienced an error while updating your profile. Please try again.",
                                "Connection error");
                    }
                });
    }


    private void getImageFile(){
        //get mobile gallery intent
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select group photo from here..."),
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                Glide.with(getContext())
                        .load(bitmap)
                        .into(groupPhoto);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}