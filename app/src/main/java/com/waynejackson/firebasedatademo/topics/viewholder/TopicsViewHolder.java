package com.waynejackson.firebasedatademo.topics.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.waynejackson.firebasedatademo.R;
import com.waynejackson.firebasedatademo.topics.models.ForumTopic;

/**
 * Created by wayne.jackson on 1/4/17.
 */

public class TopicsViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;

    public TopicsViewHolder(View itemView) {
        super(itemView);
        titleView  = (TextView) itemView.findViewById(R.id.topic_title);
        authorView = (TextView) itemView.findViewById(R.id.topic_author);
    }

    public void bindToTopic(ForumTopic topic) {
        titleView.setText(topic.getTitle());
        authorView.setText(topic.getAuthor());
    }
}
