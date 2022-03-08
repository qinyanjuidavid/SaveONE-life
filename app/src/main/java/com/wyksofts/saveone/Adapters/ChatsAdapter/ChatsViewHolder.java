package com.wyksofts.saveone.Adapters.ChatsAdapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.wyksofts.saveone.R;

public class ChatsViewHolder extends RecyclerView.ViewHolder {

    CardView chats_bg;
    TextView chat_name, chat_txt, chat_time;
    ImageView more;

    public ChatsViewHolder(@NonNull View itemView) {
        super(itemView);

        chats_bg = itemView.findViewById(R.id.chat_background);
        chat_name = itemView.findViewById(R.id.chat_name);
        chat_txt = itemView.findViewById(R.id.chat_txt);
        chat_time = itemView.findViewById(R.id.chat_time);
        more = itemView.findViewById(R.id.chat_menu);
    }
}