package com.example.multinotes;

import java.io.Serializable;

public class Notes implements Serializable {

    private String title = "";
    private String content = "";
    private String timestamp = "";

    public Notes(String t, String c, String i) {
        title = t;
        content = c;
        timestamp = i;
    }

    public Notes() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return timestamp;
    }

    public void setTime(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNotePrev() {
        if (content.length() > 80) {
            return (content.substring(0, 80) + "...");
        } else {
            return content;
        }
    }

    @Override
    public String toString() {
        return title + content + timestamp;
    }
}
