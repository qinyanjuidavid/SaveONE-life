package com.wyksofts.saveone.models.ChatModel.DeleteMessage;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.wyksofts.saveone.util.showAppToast;

public class DeleteMessage {

    FirebaseFirestore docRef;

    public void deleteMessage(Context context, String time){
        docRef = FirebaseFirestore.getInstance();
        docRef.collection("Chats").document(time)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        new showAppToast().showSuccess(context,"Message is deleted");
                    }})
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        new showAppToast().showSuccess(context,"Message is not deleted");
                    }
                });
    }

}
