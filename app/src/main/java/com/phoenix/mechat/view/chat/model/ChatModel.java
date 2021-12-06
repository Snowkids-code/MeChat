package com.phoenix.mechat.view.chat.model;

public class ChatModel {

    String Sent;
    String Received;
    String ImageSent;
    String ImageReceived;

    public ChatModel(){

    }

    public ChatModel(String sent, String received, String imageSent, String imageReceived) {
        Sent = sent;
        Received = received;
        ImageSent = imageSent;
        ImageReceived = imageReceived;
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

    public String getImageSent() {
        return ImageSent;
    }

    public void setImageSent(String imageSent) {
        ImageSent = imageSent;
    }

    public String getImageReceived() {
        return ImageReceived;
    }

    public void setImageReceived(String imageReceived) {
        ImageReceived = imageReceived;
    }
}
