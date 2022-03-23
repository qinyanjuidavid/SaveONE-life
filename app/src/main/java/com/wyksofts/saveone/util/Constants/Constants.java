package com.wyksofts.saveone.util.Constants;

import static com.wyksofts.saveone.R.drawable.*;

import android.annotation.SuppressLint;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wyksofts.saveone.R;

public interface Constants {

    String CHANNEL_ID = "messages_notification";
    String EMAIL = "wycliffnjenga19@gmail.com";
    String ANDROID_GM= "com.google.android.gm";

    //icons
    int home_icon = baseline_home_24;
    int map_icon = baseline_map_24;
    int chat_icon = baseline_chat_24;

    String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{4,}$";

    String ALLOWED_CHARACTERS ="yuiopqwertasdfghjklzxcvbnm0123456789";

    int NOTIFICATION_ID = 1;

    String CONSUMER_KEY = "ToqAzOaRJzxUvC8pf1PbRwpYF8j2Aqbk";
    String CONSUMER_SECRET = "d8g9DsbcwniXu6Fj";
    String PASS_KEY = "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919";

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @SuppressLint("StaticFieldLeak")
    FirebaseFirestore database = FirebaseFirestore.getInstance();


}
