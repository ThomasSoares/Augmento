package com.example.thomas.augmento;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private List<Post> postList;

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public MyViewHolder(View view)
        {
            super(view);
            mView=itemView;
        }

        public void setUsername(String username)
        {
            TextView usernameTextView=mView.findViewById(R.id.usernameTextView);
            usernameTextView.setText(username);
        }

        public void setProfileImage(Context ctx, String profileImage)
        {
            CircleImageView image=mView.findViewById(R.id.profileImageView1);
            Picasso.with(ctx).load(profileImage).into(image);


        }

        public void setDescription(String description)
        {
            TextView descriptionText=mView.findViewById(R.id.descriptionTextView);
            descriptionText.setText(description);
        }

        public void setPostImage(Context ctx, String postImage)
        {
            ImageView image=mView.findViewById(R.id.postImageView);
            Picasso.with(ctx).load(postImage).into(image);

        }

    }

    public PostAdapter(List<Post> postList)
    {
        this.postList=postList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_list_row, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Post post=postList.get(i);

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }



}
