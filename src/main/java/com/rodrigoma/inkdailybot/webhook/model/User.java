package com.rodrigoma.inkdailybot.webhook.model;

import java.io.Serializable;

public class User implements Serializable {
    private final static long serialVersionUID = 0L;

    private Integer id;
    private boolean is_bot;
    private String first_name;
    private String last_name;
    private String username;
    private String language_code;

    public Integer id() {
        return id;
    }

    public Boolean isBot() {
        return is_bot;
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

    public String languageCode() {
        return language_code;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", is_bot=" + is_bot +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", username='" + username + '\'' +
                ", language_code='" + language_code + '\'' +
                '}';
    }
}