package com.example.thomas.augmento;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    private List<Post> postList;

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        public ImageButton likeButton, commentButton;
        public TextView likesNumberTextView, commentsNumberTextView;
        int countLikes;
        String currentUserId;
        DatabaseReference likesRef, commentsRef;

        public MyViewHolder(View view)
        {
            super(view);
            mView=itemView;

            likeButton=mView.findViewById(R.id.likeButton);
            commentButton=mView.findViewById(R.id.commentButton);
            likesNumberTextView=mView.findViewById(R.id.likesNumberTextView);
            commentsNumberTextView=mView.findViewById(R.id.commentsNumberTextView);

            likesRef= FirebaseDatabase.getInstance().getReference().child("Likes");
            commentsRef=FirebaseDatabase.getInstance().getReference().child("Posts");
            currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        public void setLikeButtonStatus(final String PostKey)
        {
            likesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(PostKey).hasChild(currentUserId))
                    {
                        countLikes=(int) dataSnapshot.child(PostKey).getChildrenCount();
                        likeButton.setImageResource(R.drawable.ic_purple_heart);
                        likesNumberTextView.setText(Integer.toString(countLikes));
                    }
                    else
                    {
                        countLikes=(int) dataSnapshot.child(PostKey).getChildrenCount();
                        likeButton.setImageResource(R.drawable.ic_heart_normal);
                        likesNumberTextView.setText(Integer.toString(countLikes));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void setCommentButtonNumber(final String PostKey)
        {
            commentsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int commentCount=(int)dataSnapshot.child(PostKey).child("Comments").getChildrenCount();
                    commentsNumberTextView.setText(Integer.toString(commentCount));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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
