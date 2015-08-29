package com.mitr.mitr;

/**
 * Created by Nafey on 8/29/2015.
 */
public class ChatItem {
    private String sender;
    private String message;

    public ChatItem(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
