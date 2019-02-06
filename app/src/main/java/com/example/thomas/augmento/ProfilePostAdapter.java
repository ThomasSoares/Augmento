package com.example.thomas.augmento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfilePostAdapter extends RecyclerView.Adapter<ProfilePostAdapter.ProfilePostsViewHolder>
{
    private List<ProfilePosts> profilePostsList;

    public static class ProfilePostsViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public ProfilePostsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setPostImageView(Context context, String profilePostImage)
        {
            ImageView postImage=mView.findViewById(R.id.postImageView);
            Picasso.with(context).load(profilePostImage).into(postImage);
        }

        public ImageView getImageView()
        {
            return (ImageView) mView.findViewById(R.id.postImageView);
        }
    }

    @NonNull
    @Override
    public ProfilePostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_posts, parent, false);
        return new ProfilePostAdapter.ProfilePostsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePostsViewHolder holder, int position) {
        ProfilePosts profilePosts=profilePostsList.get(position);
    }

    @Override
    public int getItemCount() {
        return profilePostsList.size();
    }


}
