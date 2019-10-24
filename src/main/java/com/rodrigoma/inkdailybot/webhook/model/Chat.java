package com.rodrigoma.inkdailybot.webhook.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Chat implements Serializable {
    private final static long serialVersionUID = 0L;

    public enum Type {
        @SerializedName("private") Private, group, supergroup, channel
    }

    private Long id;
    private Type type;

    //Private
    private String first_name;
    private String last_name;

    //Private and Channel
    private String username;

    //Channel and Group
    private String title;

    public Long id() {
        return id;
    }

    public Type type() {
        return type;
    }

    public String firstName() {
        return first_name;
    }

    public String lastName() {
        return last_name;
    }

    public String username() {
        return username;
    }

    public String title() {
        return title;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "id=" + id +
                ", type=" + type +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", username='" + username + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}