package com.rodrigoma.inkdailybot.webhook.model;

import java.io.Serializable;

public class PhotoSize implements Serializable {
    private final static long serialVersionUID = 0L;

    private String file_id;
    private Integer width;
    private Integer height;
    private Integer file_size;

    public String fileId() {
        return file_id;
    }

    public Integer width() {
        return width;
    }

    public Integer height() {
        return height;
    }

    public Integer fileSize() {
        return file_size;
    }

    @Override
    public String toString() {
        return "PhotoSize{" +
                "file_id='" + file_id + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", file_size=" + file_size +
                '}';
    }
}