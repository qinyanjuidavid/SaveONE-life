package com.wyksofts.saveone.models.ChatModel.Chats;

public class ChatsModel {

    String date;
    String user_name;
    String user_email;
    String user_text;
    String time;


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_text() {
        return user_text;
    }

    public void setUser_text(String user_text) {
        this.user_text = user_text;
    }

    public ChatsModel(String data, String user_name,
                      String user_email, String user_text,
                      String time) {
        this.date = data;
        this.time = time;
        this.user_name = user_name;
        this.user_email = user_email;
        this.user_text = user_text;
    }
}
