package com.example.thomas.augmento;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private List<Post> postList;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView username, description;
        //public ImageView image, profile;

        public MyViewHolder(View view)
        {
            super(view);
            username=view.findViewById(R.id.usernameTextView);
            description=view.findViewById(R.id.descriptionTextView);
            //image=view.findViewById(R.id.postImageView);
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
        myViewHolder.username.setText(post.getUsername());
        myViewHolder.description.setText(post.getDescription());
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }



}
