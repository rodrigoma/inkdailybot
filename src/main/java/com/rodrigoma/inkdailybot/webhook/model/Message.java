package com.rodrigoma.inkdailybot.webhook.model;

import java.io.Serializable;
import java.util.Arrays;

public class Message implements Serializable {
    private final static long serialVersionUID = 0L;

    private Integer message_id;
    private User from;
    private Integer date;
    private Chat chat;
    private PhotoSize[] photo;

    public Integer messageId() {
        return message_id;
    }

    public User from() {
        return from;
    }

    public Integer date() {
        return date;
    }

    public Chat chat() {
        return chat;
    }

    public PhotoSize[] photo() {
        return photo;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message_id=" + message_id +
                ", from=" + from +
                ", date=" + date +
                ", chat=" + chat +
                ", photo=" + Arrays.toString(photo) +
                '}';
    }
}