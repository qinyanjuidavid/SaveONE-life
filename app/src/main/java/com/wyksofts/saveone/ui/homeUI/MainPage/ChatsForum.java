package com.wyksofts.saveone.ui.homeUI.MainPage;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.inappmessaging.internal.Logging;
import com.google.firebase.messaging.FirebaseMessaging;
import com.thekhaeng.recyclerviewmargin.LayoutMarginDecoration;
import com.wyksofts.saveone.Adapters.ChatsAdapter.ChatAdapter;
import com.wyksofts.saveone.R;
import com.wyksofts.saveone.models.ChatModel.Chats.ChatsModel;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.rMessagesNotifications;
import com.wyksofts.saveone.ui.profile.ProfileHolder;
import com.wyksofts.saveone.ui.homeUI.HelperClasses.NoAccountFound;
import com.wyksofts.saveone.util.showAppToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsForum extends Fragment {

    EditText message;
    ImageView attach_file, open_emoji, record_message, send_message;

    //toolbar
    CircleImageView user_image;
    TextView user_name, user_email;

    //request code for voice recognation
    private final int REQ_CODE = 100;

    //firebase
    FirebaseUser user;
    FirebaseFirestore database;

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

    public ChatsForum() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reviews_view, container, false);

        recyclerView = view.findViewById(R.id.chat_recyclerView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseFirestore.getInstance();

        message = view.findViewById(R.id.write_message);
        attach_file = view.findViewById(R.id.attach_file);
        open_emoji = view.findViewById(R.id.open_emoji);
        record_message = view.findViewById(R.id.record_message);
        send_message = view.findViewById(R.id.send_message);

        user_image = view.findViewById(R.id.user_image);
        user_email = view.findViewById(R.id.user_email);
        user_name = view.findViewById(R.id.user_name);
        user_layout = view.findViewById(R.id.user_layout);

        //shared preference
        pref = getContext().getSharedPreferences("user", 0);
        editor = pref.edit();

        warning_dialog = new Dialog(getContext());

        //initUI
        initUI();

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

            }else if(url !=null){
                //set share preference url
                loadImage(url);
            }else{
                //set placeholder
                String image_url = String.valueOf(getContext().getResources().getDrawable(R.drawable.imgg));
                loadImage(image_url);
            }


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
                            sendMessage(sms);
                        }

                    }else{
                        new showAppToast().showFailure(getContext(),"You can not send void message");
                    }

                }else{
                    new NoAccountFound().showCreateAccountDialog(getContext());
                }
            }
        });
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
        text_status.setText("Hey,\t"+user.getDisplayName()+"\t"+s);

        warning_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        warning_dialog.setCancelable(false);
        warning_dialog.show();
    }


    //user data
    private void initUI() {
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layout);
        recyclerView.addItemDecoration(new LayoutMarginDecoration(2, 2));
        list_data = new ArrayList<ChatsModel>();

        getMessages();
    }

    //listen to data change
    private void getUpdates(){

        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        firebaseMessaging.subscribeToTopic("Chats");

    }



    //get messages
    public void getMessages(){
        database.collection("Chats")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(Logging.TAG, document.getId() + " ===>>>=> " + document.getData());

                                ArrayList< HashMap<String,String>> arrayList =
                                        (ArrayList<HashMap<String,String>>)document.get("messages");
                                for (HashMap<String, String> map : arrayList){
                                    List<String> list = new ArrayList<String>(map.values());

                                    String date = list.get(0);
                                    String name = list.get(1);
                                    String time = list.get(2);
                                    String message = list.get(3);
                                    String email = list.get(4);

                                    //add data to the model
                                    list_data.add(new ChatsModel(date,name,email,message,time));
                                    recyclerView.setAdapter(adapter);

                                    adapter.notifyDataSetChanged();

                                    //scroll to last conversation
                                    recyclerView.scrollToPosition(list_data.size() - 1);

                                }

                            }
                        }
                        else {
                            Log.w(Logging.TAG, "Error getting documents.", task.getException());
                            new showAppToast().showFailure(getContext(),"Failed to get chats");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e){
                new showAppToast().showFailure(getContext(),"Failed to get chats");
            }
        });

    }


    //send message to the database
    private void sendMessage(String sms) {

        String email = user.getEmail();
        String name = user.getDisplayName();

        //get current time

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date time = new Date();
        String currentTime = dateFormat.format(time);

        //get current date
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String date = df.format(c);


        Map<String, Object> data = new HashMap<>();
        data.put("date", date);
        data.put("email", email);
        data.put("message", sms);
        data.put("name", name);
        data.put("time", currentTime);

        //path
        String path = date+currentTime;

        Map<String, Object> docData = new HashMap<>();
        docData.put("messages", Arrays.asList(data));

        database.collection("Chats")
                .document(path)
                .set(docData, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
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
        }
    }

    public void loadImage(String url){
        //load using glide
        Glide.with(getContext())
                .load(url)
                .placeholder(R.drawable.imgg)
                .into(user_image);
    }
}