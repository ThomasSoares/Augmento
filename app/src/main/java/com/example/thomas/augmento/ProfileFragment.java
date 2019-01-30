package com.example.thomas.augmento;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;


public class ProfileFragment extends Fragment {

    View parentHolder;
    CircleImageView editImageView, profileImageView;
    TextView nameTextView;

    ProgressBar profilePicProgressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    private StorageReference UserProfileImageRef;
    String currentUserID;
    String downloadUrl=null;

    public void initialize()
    {
        editImageView=parentHolder.findViewById(R.id.editImageView);
        profileImageView=parentHolder.findViewById(R.id.profileImageView1);
        nameTextView=parentHolder.findViewById(R.id.nameTextView);

        profilePicProgressBar=parentHolder.findViewById(R.id.profilePicProgressBar);

        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        UserRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef=FirebaseStorage.getInstance().getReference().child("Profile Images");

        setProfilePhoto();
    }

    public void listeners()
    {
        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getPhoto();
            }
        });
    }

    public void setProfilePhoto()
    {
        profilePicProgressBar.setVisibility(View.VISIBLE);

        StorageReference pathReference=UserProfileImageRef.child(currentUserID+".jpg");
        final long ONE_MEGABYTE=1024*1024;

        pathReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profileImageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, profileImageView.getWidth(), profileImageView.getHeight(), false));
                profilePicProgressBar.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                profilePicProgressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed to load profile image",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getPhoto()
    {
        Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==1)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                getPhoto();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage=data.getData();

        if (requestCode==1 && resultCode==RESULT_OK && data!=null)
        {

                CropImage.activity(selectedImage)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(getContext(), this);

        }

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result=CropImage.getActivityResult(data);

            if(resultCode==RESULT_OK)
            {
                Uri resultUri=result.getUri();
                profilePicProgressBar.setVisibility(View.VISIBLE);

                final StorageReference filePath=UserProfileImageRef.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getContext(),"Profile Image stored in storage",Toast.LENGTH_SHORT).show();


                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl=uri.toString();

                                    UserRef.child("ProfileImage").setValue(downloadUrl)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful())
                                                    {
                                                        profilePicProgressBar.setVisibility(View.GONE);
                                                        Toast.makeText(getContext(),"Image stored in database", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else
                                                    {
                                                        profilePicProgressBar.setVisibility(View.GONE);
                                                        String message=task.getException().getMessage();
                                                        Toast.makeText(getContext(),"Error Occured: "+message, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            });

                        }
                    }
                });

                try {
                    Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),resultUri);
                    profileImageView.setImageBitmap(bitmap);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }


            }
            else
            {
                Toast.makeText(getContext(),"Error Image cant be cropped", Toast.LENGTH_SHORT).show();
            }
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
        /*
        if(checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }*/
        return parentHolder;
    }





}
