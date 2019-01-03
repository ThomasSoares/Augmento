package com.example.thomas.augmento;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    View parentHolder;
    CircleImageView editImageView;
    final static int GALLERY_PICK=1;
    final static int RESULT_OK=-1;

    public void initialize()
    {
        editImageView=parentHolder.findViewById(R.id.editImageView);
    }

    public void listeners()
    {
        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_PICK);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GALLERY_PICK && requestCode==RESULT_OK && data!=null)
        {
            Uri imageUri=data.getData();

        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentHolder= inflater.inflate(R.layout.fragment_profile, container, false);
        initialize();
        listeners();
        return parentHolder;
    }





}
