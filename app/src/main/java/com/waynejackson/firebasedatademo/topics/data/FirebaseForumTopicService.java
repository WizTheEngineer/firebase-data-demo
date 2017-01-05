package com.waynejackson.firebasedatademo.topics.data;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.waynejackson.firebasedatademo.topics.models.ForumTopic;

/**
 * Created by wayne.jackson on 1/4/17.
 * Service for performing CRUD operations for {@link ForumTopic} objects
 * inside of the Firebase database.
 */

public class FirebaseForumTopicService {

    private static final String TOPICS_KEY = "topics";
    private static final String USER_TOPICS_KEY = "user-topics";

    public static FirebaseForumTopicService INSTANCE;

    private DatabaseReference mDatabase;

    // Prevent direct instantiation
    private FirebaseForumTopicService() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
    }

    public static FirebaseForumTopicService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FirebaseForumTopicService();
        }
        return INSTANCE;
    }

    public void submitForumTopic(FirebaseUser user, String title) {
        String key = mDatabase.child(TOPICS_KEY).push().getKey();
        ForumTopic topic = new ForumTopic(title, user.getEmail(), user.getUid());
        Map<String, Object> topicValues = topic.toMap();

        // Create new topic at /user-topics/$userid/$topicid and at
        // /topics/$topicid simultaneously
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(String.format("/%s/%s", TOPICS_KEY, key), topicValues);
        childUpdates.put(String.format("/%s/%s/%s", USER_TOPICS_KEY, user.getUid(), key), topicValues);

        mDatabase.updateChildren(childUpdates);
    }

    public void registerToTopicUpdates(ChildEventListener listener) {
        mDatabase.child(TOPICS_KEY).addChildEventListener(listener);
    }

    public void unregisterFromTopicUpdates(ChildEventListener listener) {
        mDatabase.child(TOPICS_KEY).removeEventListener(listener);
    }
}
