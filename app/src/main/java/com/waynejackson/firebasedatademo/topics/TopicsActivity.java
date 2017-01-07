package com.waynejackson.firebasedatademo.topics;

import java.util.List;

import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.waynejackson.firebasedatademo.R;
import com.waynejackson.firebasedatademo.utils.RandomSentenceGenerator;
import com.waynejackson.firebasedatademo.topics.data.FirebaseForumTopicService;
import com.waynejackson.firebasedatademo.topics.models.ForumTopic;
import com.waynejackson.firebasedatademo.topics.viewholder.TopicsViewHolder;
import timber.log.Timber;

public class TopicsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseForumTopicService mTopicService;

    private RecyclerView mTopicRecycler;
    private TopicsAdapter mAdapter;
    private LinearLayoutManager mManager;

    private FloatingActionButton mAddTopicBtn;

    public static void start(Context context, boolean newTask) {
        Intent intent = new Intent(context, TopicsActivity.class);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topics);
        mAuth = FirebaseAuth.getInstance();
        mTopicService = FirebaseForumTopicService.getInstance();

        // RecyclerView setup
        mTopicRecycler = (RecyclerView) findViewById(R.id.topics_list);
        mTopicRecycler.setHasFixedSize(true);
        mManager = new LinearLayoutManager(this);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mTopicRecycler.setLayoutManager(mManager);

        mAddTopicBtn = (FloatingActionButton) findViewById(R.id.add_topic_btn);
        mAddTopicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTopicService.submitForumTopic(mAuth.getCurrentUser(), RandomSentenceGenerator.getRandomSentence());
            }
        });

        mAdapter = new TopicsAdapter(mAuth.getCurrentUser(), this);
        mTopicRecycler.setAdapter(mAdapter);
        mTopicService.registerToTopicUpdates(mTopicsListener);
    }

    @Override
    protected void onDestroy() {
        if (mTopicsListener != null) {
            mTopicService.unregisterFromTopicUpdates(mTopicsListener);
        }
        super.onDestroy();
    }

    private ChildEventListener mTopicsListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
            Timber.d("[onChildAdded] previousChild=" + previousChildName);
            mTopicRecycler.scrollToPosition(mAdapter.getItemCount());

            // A new topic has been added
            ForumTopic topic = dataSnapshot.getValue(ForumTopic.class);
            String key = dataSnapshot.getKey();
            mAdapter.addTopic(key, topic);
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
            Timber.d("[onChildChanged] " + previousChildName);

            // A topic has changed, use the key to determine if we are displaying this
            // topic and if so update the topic
            ForumTopic topic = dataSnapshot.getValue(ForumTopic.class);
            String key = dataSnapshot.getKey();
            mAdapter.updateTopic(key, topic);
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Timber.d("[onChildRemoved] ");

            // A topic has changed, use the key to determine if we are displaying this
            // topic and if so remove the topic
            String key = dataSnapshot.getKey();
            mAdapter.removeTopic(key);
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            Timber.d("[onChildMoved] " + previousChildName);

            // Topic has changed position, use the key to determine if we are
            // displaying this topic and if so move it
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Timber.d("[onCancelled] " + databaseError.toException());
            Toast.makeText(TopicsActivity.this, "Failed to load topics.", Toast.LENGTH_SHORT).show();
        }
    };

    private class TopicsAdapter extends RecyclerView.Adapter<TopicsViewHolder> {

        private FirebaseUser mCurrentUser;
        private LayoutInflater mLayoutInflater;
        private List<String> mKeys;
        private List<ForumTopic> mTopics;

        public TopicsAdapter(FirebaseUser currentUser, Context context) {
            mCurrentUser = currentUser;
            mLayoutInflater = LayoutInflater.from(context);
            mKeys = Lists.newArrayList();
            mTopics = Lists.newArrayList();
        }

        @Override
        public TopicsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = mLayoutInflater.inflate(R.layout.item_topic, parent, false);
            return new TopicsViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(TopicsViewHolder holder, final int position) {
            ForumTopic topic = mTopics.get(position);
            holder.bindToTopic(mCurrentUser, topic, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTopicService.deleteForumTopic(mCurrentUser, mKeys.get(position));
                }
            });
        }

        @Override
        public int getItemCount() {
            return mTopics.size();
        }

        public void addTopic(final String key, final ForumTopic topic) {
            mKeys.add(key);
            mTopics.add(topic);
            notifyItemInserted(mTopics.size());
        }

        public void removeTopic(final String key) {
            // Find the index using the keys list
            int index = mKeys.indexOf(key);

            if (index >= 0) {
                mKeys.remove(index);
                mTopics.remove(index);
                notifyItemRemoved(index);
            }
        }

        public void updateTopic(final String key, final ForumTopic topic) {
            // Find the index using the keys list
            int index = mKeys.indexOf(key);

            if (index >= 0) {
                // TODO: Update the topic at this index
            }
        }
    }
}
