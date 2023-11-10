package com.base.retrofitbasics;

import com.google.gson.annotations.SerializedName;

public class Post {

    private int userId;


    private String content;

    public String getContent() {
        return content;
    }

    private int id;

    private String title;

    @SerializedName("body")
    private String text;


}