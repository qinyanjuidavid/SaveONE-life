package com.wyksofts.saveone.ui.homeUI.MainPage.ChatsView;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;
import com.wyksofts.saveone.Adapters.ChatsAdapter.ChatAdapter;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.models.ChatModel.Chats.ChatsModel;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.rMessagesNotifications;
import com.wyksofts.saveone.ui.profile.ProfileHolder;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.NoAccountFound;
import com.wyksofts.saveone.util.CurrentDay;
import com.wyksofts.saveone.util.showAppToast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsForum extends Fragment {

    EditText message;
    ImageView attach_file, add_image, open_emoji, record_message, send_message;

    //toolbar
    CircleImageView user_image;
    TextView user_name, user_email;
    FirebaseUser user;
    FirebaseFirestore database;

    //request code for voice recognation
    private final int REQ_CODE = 100;
    private final int PICK_IMAGE_REQUEST = 111;
    private Uri filePath;
    private Bitmap bitmap;
    private View view;

    PopupWindow popupWindow;
    FloatingActionButton send_message_img;



    //view and chat data
    private List<ChatsModel> list_data;
    private RecyclerView recyclerView;
    private ChatAdapter adapter;

    //user toolbar layout
    private RelativeLayout user_layout;

    //shared preference
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    //warning dialog
    Dialog warning_dialog;

    //image post
    LinearLayout image_post_layout;
    ImageView image_to_post, close;
    CardView card;
    Dialog uploadImageDialog;

    //firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    public ChatsForum() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uploadImageDialog = new Dialog(getContext());

        uploadImageDialog.setContentView(R.layout.upload_image);
        uploadImageDialog.setCancelable(false);
        uploadImageDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        //init firebase
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);

        this.view = view;

        database = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = view.findViewById(R.id.chat_recyclerView);

        message = view.findViewById(R.id.write_message);
        attach_file = view.findViewById(R.id.attach_file);
        add_image = view.findViewById(R.id.add_image);
        open_emoji = view.findViewById(R.id.open_emoji);
        record_message = view.findViewById(R.id.record_message);
        send_message = view.findViewById(R.id.send_message);

        user_image = view.findViewById(R.id.user_image);
        user_email = view.findViewById(R.id.user_email);
        user_name = view.findViewById(R.id.user_name);
        user_layout = view.findViewById(R.id.user_layout);

        //add image post and other variables
        image_post_layout = view.findViewById(R.id.image_post_layout);
        card = view.findViewById(R.id.card);

        //shared preference
        pref = getContext().getSharedPreferences("user", 0);
        editor = pref.edit();

        warning_dialog = new Dialog(getContext());

        //initUI
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layout);
        recyclerView.addItemDecoration(new LayoutMarginDecoration(2, 2));
        list_data = new ArrayList<>();

        getMessages();

        //adapter
        adapter = new ChatAdapter(list_data,getContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //set values
        if (user != null) {
            user_layout.setVisibility(View.VISIBLE);

            //set email and name
            user_email.setText(user.getEmail());
            user_name.setText(user.getDisplayName());

            //get image url from
            String url = pref.getString("url",null);

            if (user.getPhotoUrl() !=null){
                //set google url
                loadImage(user.getPhotoUrl().toString());

            }else if(url != null){
                //set share preference url
                loadImage(url);
            }else{

                //set placeholder
                @SuppressLint("UseCompatLoadingForDrawables") String image_url = String.valueOf(getContext()
                        .getResources().getDrawable(R.drawable.imgg));
                loadImage(image_url);
            }

            image_post_layout.setVisibility(View.GONE);

            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.fall_down);

            attach_file.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //open images
                    if (image_post_layout.getVisibility() != View.VISIBLE){
                        image_post_layout.setVisibility(View.VISIBLE);
                    }else{
                        image_post_layout.setVisibility(View.GONE);
                    }
                    TransitionManager.beginDelayedTransition(card);
                }
            });
        }else{
            user_layout.setVisibility(View.GONE);
        }

        //open profile page when clicked
        user_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ProfileHolder.class));
            }
        });


        //write message
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                checkMessage(editable.toString());
            }
        });

        //speak message
        record_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
                intent.putExtra("android.speech.extra.LANGUAGE", Locale.getDefault());
                intent.putExtra("android.speech.extra.PROMPT", "Speak Now Please");
                try {
                    startActivityForResult(intent, REQ_CODE);
                    return;
                } catch (ActivityNotFoundException activityNotFoundException) {
                    new showAppToast().showSuccess(getContext(),"Sorry your device is not supported");
                    return;
                }
            }
        });

        //send text
        send_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sms = message.getText().toString().trim();

                //check if user is logged in
                if(user != null){

                    //check if message is empty
                    if(!sms.isEmpty()){

                        //check if message is offencive
                        if (sms.contains("fuck") || sms.contains("pussy") || sms.contains("dick")){
                            showWarningDialog();
                        }else {
                            //send message
                            sendMessage(sms,"");
                        }
                    }else{
                        new showAppToast().showFailure(getContext(),"You can not send void message");
                    }

                }else{
                    new NoAccountFound().showCreateAccountDialog(getContext());
                }
            }
        });


        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //get Image file
                getImageFile();
            }
        });
    }



    //upload image and message
    private void uploadImage(String sms) {
        if (filePath != null) {

            popupWindow.dismiss();
            uploadImageDialog.show();

            // Defining the child of storageReference using user email
            String name = user.getEmail();
            String date = new CurrentDay().getCurrentDate();

            StorageReference ref = storageReference.child("images/" + name+date);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 20, bytes);
            String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                    bitmap,name,null);

            Uri uri = Uri.parse(path);

            // adding listeners on upload
            ref.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                            uploadImageDialog.dismiss();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();

                            //add image url to the database if image exists
                            sendMessage(sms, downloadUrl.toString());

                            editor.putString("url",downloadUrl.toString());
                            editor.commit();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e){
                            new showAppToast().showFailure(getContext(),"Oops your message image is too large in size");
                            uploadImageDialog.dismiss();
                            sendMessage(sms, "");
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onProgress(
                                @NonNull UploadTask.TaskSnapshot taskSnapshot){
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() /
                                    taskSnapshot.getTotalByteCount());
                            TextView progress_text = uploadImageDialog.findViewById(R.id.text_status);
                            progress_text.setText("Sending message in\t" +
                                            (int) progress + "%");
                        }
                    });
        }else{
            sendMessage(sms, "");
        }
    }


    //get image file from device
    private void getImageFile(){
        //get mobile gallery intent
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Attach your picture from here..."), PICK_IMAGE_REQUEST);
    }



    //get messages
    public void getMessages(){
        database.collection("Chats")
                .orderBy("timeStamp")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String date = document.getString("date");
                                String name = document.getString("name");
                                String time = document.getString("time");
                                String image = document.getString("image");
                                String message = document.getString("message");
                                String email = document.getString("email");

                                //add data to the model
                                list_data.add(new ChatsModel(date,name,email,message,time,image));
                                recyclerView.setAdapter(adapter);

                                adapter.notifyDataSetChanged();

                                //scroll to last conversation
                                recyclerView.scrollToPosition(list_data.size() - 1);
                            }
                        }
                        else {
                            new showAppToast().showFailure(getContext(),"Failed to get forum data");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e){
                new showAppToast().showFailure(getContext(),"Failed to get forum data");
            }
        });
    }



    //send message
    public void sendMessage(String sms, String url) {

        String email = user.getEmail();
        String name = user.getDisplayName();

        String date = new CurrentDay().getCurrentDate();
        String currentTime = new CurrentDay().getCurrentTime();
        FieldValue timeStamp = FieldValue.serverTimestamp();

        Map<String, Object> data = new HashMap<>();
        data.put("date", date);
        data.put("email", email);
        data.put("image", url.toString());
        data.put("message", sms);
        data.put("name", name);
        data.put("time", currentTime);
        data.put("timeStamp", timeStamp);

        //path
        String path = date+currentTime;

        database.collection("Chats")
                .document(path)
                .set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(Void aVoid) {

                        //ring sound
                        final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.message_sent);
                        mp.start();

                        //clear
                        list_data.clear();

                        getMessages();
                        adapter.notifyDataSetChanged();
                        adapter.notifyItemInserted(-1);

                        //send notification
                        sendNotification();

                        message.setText("");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }



    //get text from speech recognizer...
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    message.setText(result.get(0).toString().toLowerCase());
                }
                break;
            }

            case PICK_IMAGE_REQUEST: {
                if (resultCode == RESULT_OK && data != null && data.getData() != null) {

                    // Get the Uri of data
                    filePath = data.getData();

                    try {

                        //open pop up view
                        //Create a View object yourself through inflater
                        LayoutInflater inflater = (LayoutInflater) view.getContext()
                                .getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
                        View popupView = inflater.inflate(R.layout.upload_picture_fragment, null);

                        //length and width
                        int width = LinearLayout.LayoutParams.MATCH_PARENT;
                        int height = LinearLayout.LayoutParams.MATCH_PARENT;

                        //Inactive Items Outside Of PopupWindow
                        boolean focusable = true;

                        //Create a window
                        popupWindow = new PopupWindow(popupView, width, height, focusable);

                        //Set the location of the window on the screen
                        popupWindow.showAtLocation(view, Gravity.TOP | Gravity.RIGHT, 0, 0);

                        //get varibles

                        close = popupView.findViewById(R.id.close);
                        close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //close view
                                popupWindow.dismiss();
                            }
                        });

                        image_to_post = popupView.findViewById(R.id.image_to_post);
                        image_to_post.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                popupWindow.dismiss();
                                filePath.equals("");
                                //select image
                                getImageFile();
                            }
                        });

                        //send message
                        send_message_img = popupView.findViewById(R.id.send_message_img);

                        send_message_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                //edit text
                                EditText write_message = popupView.findViewById(R.id.message2);
                                String message = write_message.getText().toString().trim();

                                //send message with attached image
                                uploadImage(message);
                            }
                        });


                        // Setting image on image view using Bitmap
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                        Glide.with(getContext())
                                .load(bitmap)
                                .into(image_to_post);

                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
            default:
                break;
        }
    }



    //send notification to all devices a new message was sent
    private void sendNotification() {

        //check whether notification was allowed
        String receive_notifications = pref.getString("receive_notifications", null);

        if (receive_notifications == null){
            //request for permission
            new rMessagesNotifications(getContext()).show();

        }else if(receive_notifications.equals("false")){
            //do nothing
        }else{
            //subscribe
            new rMessagesNotifications(getContext()).subscribeToTopic();
        }
    }

    //check if the field has messages and animate icons
    public void checkMessage(String message){
        if (!message.equals("")){
            //show send
            send_message.setVisibility(View.VISIBLE);
            record_message.setVisibility(View.GONE);

        }else{
            //show mic
            send_message.setVisibility(View.GONE);
            record_message.setVisibility(View.VISIBLE);
            record_message.startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.rotate_anticlockwise));
        }
    }

    @SuppressLint("SetTextI18n")
    private void showWarningDialog() {
        warning_dialog.setContentView(R.layout.warning_dialog);

        warning_dialog.findViewById(R.id.close)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        warning_dialog.dismiss();
                        message.setText("");
                    }
                });

        TextView text_status = warning_dialog.findViewById(R.id.warning_txt);
        String s = getContext().getString(R.string.warning);
        assert user != null;
        text_status.setText("Hey,\t"+user.getDisplayName()+"\t"+s);

        warning_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        warning_dialog.setCancelable(false);
        warning_dialog.show();
    }

    public void loadImage(String url){
        //load using glide
        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.imgg)
                .into(user_image);
    }
}