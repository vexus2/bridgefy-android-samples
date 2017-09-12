package com.bridgefy.samples.twitter.entities;

import com.bridgefy.sdk.client.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.UUID;

/**
 * @author dekaru on 5/9/17.
 */

public class Tweet {

    // protocol
    private String id;
    private long date;
    private String content;
    private String sender;
    private boolean posted;


    public Tweet(String content, String sender) {
        this.id = UUID.randomUUID().toString().substring(0, 5);
        this.content = content;
        this.sender = sender;
        this.posted = false;
        this.date = System.currentTimeMillis()/1000;
    }

    public static Tweet create(Message message) {
        Tweet tweet = new Gson().fromJson(
                new Gson().toJson(message.getContent()),
                Tweet.class);
        return tweet;
    }

    public HashMap<String, Object> toHashMap() {
        Gson gson = new Gson();
        String s = gson.toJson(this);
        Type type = new TypeToken<HashMap<String, Object>>() {}.getType();
        return gson.fromJson(s, type);
    }


    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPosted() {
        return posted;
    }

    public void setPosted(boolean posted) {
        this.posted = posted;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj.getClass().equals(this.getClass()) &&
                this.getId().equals(((Tweet) obj).getId()));
    }
}
