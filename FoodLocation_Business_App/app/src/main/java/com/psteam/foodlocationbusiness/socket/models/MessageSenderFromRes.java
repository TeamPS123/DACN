package com.psteam.foodlocationbusiness.socket.models;

public class MessageSenderFromRes {
    public MessageSenderFromRes(String sender, String receiver, String title, String body) {
        this.sender = sender;
        this.receiver = receiver;
        this.title = title;
        this.body = body;
    }

    private String sender;
    private String receiver;
    private String title;
    private String body;
}
