package com.wyksofts.saveone.ui.landingUI.authfrags.Organisation;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.util.AlertPopDiag;
import com.wyksofts.saveone.util.showAppToast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AdditionalInfo extends Fragment {

    Button btn;
    FirebaseUser user;
    FirebaseFirestore database;
    Dialog uploadImageDialog;

    // view for groupPhoto
    private ImageView groupPhoto;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 111;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    EditText org_phone_number,org_bank_name, org_till_number, org_country, org_bank_account;

    public AdditionalInfo() {
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
        View view = inflater.inflate(R.layout.fragment_additional_info, container, false);

        btn = view.findViewById(R.id.register_new_orphanage_info);
        groupPhoto = view.findViewById(R.id.select_organization_image);

        org_phone_number = view.findViewById(R.id.organisation_phone_number);
        org_bank_account =view.findViewById(R.id.organisation_bank_account);
        org_till_number = view.findViewById(R.id.organisation_till_number);
        org_country = view.findViewById(R.id.organisation_country);
        org_bank_name = view.findViewById(R.id.organisation_bank_account_name);

        ViewCompat.setTransitionName(btn, "addInfo");

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        uploadImageDialog = new Dialog(getContext());

        groupPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFile();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verifyFields();
            }
        });
    }


    //get text info from fields and continue
    private void verifyFields() {

        String phone_number = org_phone_number.getText().toString();
        String till_number = org_till_number.getText().toString();
        String bank_account = org_bank_account.getText().toString();
        String bank_account_name = org_bank_name.getText().toString();
        String country = org_country.getText().toString().trim();

        if(TextUtils.isEmpty(phone_number)){
            org_phone_number.setError("Enter Phone");
        }else if (phone_number.length()<13){
            org_phone_number.setError("Please Include Your Country Code (+254)");
        }else if (TextUtils.isEmpty(till_number)){
            org_till_number.setError("Enter Till / Paybill Number");
        }else if (TextUtils.isEmpty(bank_account)){
            org_bank_account.setError("Enter bank Account Number");
        }else if (TextUtils.isEmpty(bank_account_name)){
            org_bank_name.setError("Enter Bank Name");
        }else if (TextUtils.isEmpty(country)){
            org_country.setError("Enter Country");
        }else{

            addInfoToDataBase(phone_number,
                    till_number, bank_account,
                    bank_account_name,country);
        }
    }


    //add data to the data base
    private void addInfoToDataBase(String phone_number, String till_number,
                                   String bank_account, String bank_name,String country) {

        database = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();


        String email = user.getEmail();
        String org_name = user.getDisplayName();


        Map<String, Object> data = new HashMap<>();
        data.put("phone_number", phone_number);
        data.put("till_number", till_number);
        data.put("bank_account", bank_account);
        data.put("bank_name", bank_name);
        data.put("country", country);
        data.put("email", email);
        data.put("name", org_name);

        //Map<String, Object> docData = new HashMap<>();
        //docData.put(pName, Arrays.asList(data));
        //docData.put(org_name, data);

        database.collection("Orphanage")
                 .document(email)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        uploadImage();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        new AlertPopDiag(getContext()).show(
                                "Oops we have experienced an error while uploading",
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
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        getActivity().getContentResolver(),
                                filePath);
                groupPhoto.setImageBitmap(bitmap);
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage() {
        if (filePath != null) {

            uploadImageDialog.setContentView(R.layout.upload_image);
            uploadImageDialog.setCancelable(false);
            uploadImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            uploadImageDialog.show();


            // Defining the child of storageReference using user email
            String name = user.getEmail();

            StorageReference ref = storageReference.child("images/" + name);

            // adding listeners on upload
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot){
                                    uploadImageDialog.dismiss();

                                    //go next
                                    getActivity().getSupportFragmentManager()
                                            .beginTransaction()
                                            .addToBackStack(null)
                                            .setCustomAnimations(R.anim.fade_in,
                                                    R.anim.fade_out)
                                            .addSharedElement(btn, "otherInfo")
                                            .replace(R.id.root_layout, new OtherInfo())
                                            .commit();
                                }
                            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e){
                            new showAppToast().showFailure(getContext(),"Image is too large in size");
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
                                    progress_text.setText("Uploaded " + (int)progress + "%");
                                }
                            });
        }
    }
}