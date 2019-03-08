package com.example.thomas.augmento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.AlertsAdapterViewHolder>{

    private List<Alerts> alertsList;

    public AlertsAdapter(List<Alerts> alertsList)
    {
        this.alertsList=alertsList;
    }



    public static class AlertsAdapterViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public AlertsAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setUsername(String username)
        {
            TextView usernameTextView=mView.findViewById(R.id.usernameTextView);
            usernameTextView.setText(username);
        }
        public void setAlertMessage(String alertMessage)
        {
            TextView alertMessageTextView=mView.findViewById(R.id.statusTextView);
            alertMessageTextView.setText(alertMessage);
        }
        public void setPostImage(Context context,String postImage)
        {
            ImageView postImageView=mView.findViewById(R.id.photoTextView);
            Picasso.with(context).load(postImage).placeholder(R.drawable.ic_person_black_24dp).into(postImageView);
        }
        public void setProfileImage(Context context, String profileImage)
        {
            CircleImageView myImage=mView.findViewById(R.id.alertProfileImage);
            Picasso.with(context).load(profileImage).placeholder(R.drawable.ic_person_black_24dp).into(myImage);
        }
    }

    @NonNull
    @Override
    public AlertsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.alers_layout, parent,false);
        return new AlertsAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertsAdapterViewHolder holder, int position) {
        Alerts alerts=alertsList.get(position);
        holder.setUsername(alerts.getUsername());
        holder.setAlertMessage(alerts.getAlertMessage());
    }

    @Override
    public int getItemCount() {
        return alertsList.size();
    }
}
