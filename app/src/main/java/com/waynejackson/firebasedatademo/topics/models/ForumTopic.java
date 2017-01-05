package com.waynejackson.firebasedatademo.topics.models;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by wayne.jackson on 1/4/17.
 * Model class representing a forum topic.
 */

@IgnoreExtraProperties
public class ForumTopic {
    private String title;
    private String author;
    private String uid;

    public ForumTopic() {
        // Default constructor required for calls to DataSnapshot.getValue(ForumTopic.class)
    }

    public ForumTopic(String title, String author, String uid) {
        this.title = title;
        this.author = author;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getUid() {
        return uid;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        return result;
    }
}
