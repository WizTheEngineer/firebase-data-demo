package com.waynejackson.firebasedatademo.topics.viewholder;

import com.google.firebase.auth.FirebaseUser;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.waynejackson.firebasedatademo.R;
import com.waynejackson.firebasedatademo.topics.models.ForumTopic;

/**
 * Created by wayne.jackson on 1/4/17.
 */

public class TopicsViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public Button deleteBtn;

    public TopicsViewHolder(View itemView) {
        super(itemView);
        titleView  = (TextView) itemView.findViewById(R.id.topic_title);
        authorView = (TextView) itemView.findViewById(R.id.topic_author);
        deleteBtn = (Button) itemView.findViewById(R.id.delete_btn);
    }

    public void bindToTopic(FirebaseUser currentUser, ForumTopic topic, View.OnClickListener deleteClickListener) {
        titleView.setText(topic.getTitle());
        authorView.setText(topic.getAuthor());

        if (currentUser.getUid().equals(topic.getUid())) {
            // The user can only delete this topic if they are the owner
            deleteBtn.setOnClickListener(deleteClickListener);
            deleteBtn.setVisibility(View.VISIBLE);
        } else {
            deleteBtn.setOnClickListener(null);
            deleteBtn.setVisibility(View.GONE);
        }
    }
}
