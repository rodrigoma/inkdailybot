package com.rodrigoma.inkdailybot.webhook.model;

import java.io.Serializable;

public class Update implements Serializable {
    private final static long serialVersionUID = 0L;

    private Integer update_id;
    private Message message;

    public Integer updateId() {
        return update_id;
    }

    public Message message() {
        return message;
    }

    @Override
    public String toString() {
        return "Update{" +
                "update_id=" + update_id +
                ", message=" + message +
                '}';
    }
}