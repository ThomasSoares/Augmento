package com.example.thomas.augmento;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class StickerViewLayoutAdapter extends RecyclerView.Adapter<StickerViewLayoutAdapter.MyViewHolder>{
    private List<StickerViewLayout> stickerViewLayoutList;



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
            CircleImageView image=mView.findViewById(R.id.profileImageView);
            Picasso.with(ctx).load(profileImage).into(image);


        }
        public void setDescription(String description)
        {
            TextView descriptionText=mView.findViewById(R.id.descriptionTextView);
            descriptionText.setText(description);
        }

        public void setSticker(Context ctx,String [] stickerArray, ModelLoader modelLoader)
        {

                modelLoader.loadModel(ctx,R.raw.flyingsacuer);

        }

    }

    public StickerViewLayoutAdapter(List<StickerViewLayout> stickerViewLayoutList)
    {
        this.stickerViewLayoutList=stickerViewLayoutList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
