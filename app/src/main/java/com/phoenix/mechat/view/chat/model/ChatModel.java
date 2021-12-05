package com.phoenix.mechat.view.chat.model;

public class ChatModel {

    String Sent;
    String Received;

    public ChatModel(){

    }

    public ChatModel(String sent, String received) {
        Sent = sent;
        Received = received;
    }

    public String getSent() {
        return Sent;
    }

    public void setSent(String sent) {
        Sent = sent;
    }

    public String getReceived() {
        return Received;
    }

    public void setReceived(String received) {
        Received = received;
    }
}
