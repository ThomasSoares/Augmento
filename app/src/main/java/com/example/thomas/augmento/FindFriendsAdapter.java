package com.example.thomas.augmento;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendsAdapter extends RecyclerView.Adapter<FindFriendsAdapter.FindFriendsViewHolder>{

    private List<FindFriends> findFriendsList;

    public FindFriendsAdapter(List<FindFriends> findFriendsList)
    {
        this.findFriendsList=findFriendsList;
    }

    public static class FindFriendsViewHolder extends RecyclerView.ViewHolder
    {
        String temp;
        View mView;

        public FindFriendsViewHolder(@NonNull View itemView) {
            super(itemView);
            temp="";
            mView = itemView;
        }

        public void setProfileImage(Context context, String profileImage)
        {
            CircleImageView myImage=mView.findViewById(R.id.alertProfileImage);
            Picasso.with(context).load(profileImage).placeholder(R.drawable.ic_person_black_24dp).into(myImage);
        }

        public void setFirstName(String firstName)
        {
            TextView myName=mView.findViewById(R.id.all_users_profile_name);
            temp=temp+firstName+" ";
            myName.setText(temp);
        }
        public void setLastName(String lastName)
        {
            TextView myName=mView.findViewById(R.id.all_users_profile_name);
            temp=temp+lastName+" ";
            myName.setText(temp);
        }

        public void setUsername(String username)
        {
            Log.i("GETUSERNAME",username);
            TextView myStatus=mView.findViewById(R.id.all_users_status);
            myStatus.setText(username);
        }
    }

    @NonNull
    @Override
    public FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.all_user_display_layout, parent,false);
        return new FindFriendsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FindFriendsViewHolder holder, int position) {
        FindFriends findFriends=findFriendsList.get(position);
        holder.setFirstName(findFriends.getFirstName());
        holder.setLastName(findFriends.getLastName());
        holder.setUsername(findFriends.getUsername());
    }

    @Override
    public int getItemCount() {
        return findFriendsList.size();
    }
}
