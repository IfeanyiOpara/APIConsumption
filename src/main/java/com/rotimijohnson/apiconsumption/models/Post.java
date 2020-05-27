package com.rotimijohnson.apiconsumption.models;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class Post {
    private int UserId;
    private int Id;
    private String title;

    @SerializedName("body")
    private String content;

    public Post() {
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
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

    @NonNull
    @Override
    public String toString() {
        return "Id: " + getId() + "Title: " + getTitle() + "Content: " + getContent();
    }
}
