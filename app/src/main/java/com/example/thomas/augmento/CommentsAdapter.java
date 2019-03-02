package com.example.thomas.augmento;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>
{
    private List<Comments> commentsList;

    public static class CommentsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public CommentsViewHolder(@NonNull View itemView)
        {
            super(itemView);
            mView=itemView;
        }

        public void setUsername(String username)
        {
            TextView myUserName=mView.findViewById(R.id.commentUsernameTextView);
            myUserName.setText(username);
        }

        public void setComment(String comment)
        {
            TextView myComment=mView.findViewById(R.id.commentTextTextView);
            myComment.setText(comment);
        }

        public void setDate(String date)
        {
            TextView myDate=mView.findViewById(R.id.commentDateTextView);
            myDate.setText(date);
        }

        public void setTime(String time)
        {
            TextView myTime=mView.findViewById(R.id.commentTimeTextView);
            myTime.setText(time);
        }
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_comments_layout, parent, false);
        return new CommentsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {
        Comments comments=commentsList.get(position);
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }
}
