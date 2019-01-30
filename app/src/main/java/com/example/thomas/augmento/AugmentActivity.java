package com.example.thomas.augmento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AugmentActivity extends AppCompatActivity {

    ImageView importImageView;
    EditText descriptionEditText;
    Button importButton, uploadButton;
    ProgressBar uploadProgressBar;

    private static final int Gallery_Pick=1;
    private Uri ImageUri;
    String description;

    private StorageReference postImagesReference;
    private DatabaseReference userRef, postRef;
    private FirebaseAuth mAuth;
    String currentUserID;

    private String saveCurrentDate;
    private String saveCurrentTime;
    private String postRandomName;
    private String downloadUrl;

    public void initialize() {
        importImageView = findViewById(R.id.importImageView);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        importButton = findViewById(R.id.importButton);
        uploadButton = findViewById(R.id.uploadButton);
        uploadProgressBar=findViewById(R.id.uploadProgressBar);

        postImagesReference = FirebaseStorage.getInstance().getReference();
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");
        postRef=FirebaseDatabase.getInstance().getReference().child("Posts");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        downloadUrl=null;

    }
    public void listeners()
    {
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatePostInfo();
            }
        });
    }

    public void openGallery()
    {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,Gallery_Pick);
    }

    public void validatePostInfo()
    {
        description=descriptionEditText.getText().toString();

        if(ImageUri==null)
        {
            Toast.makeText(getApplicationContext(),"Please select post image!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description))
        {
            descriptionEditText.setError("Cannot be empty");
        }
        else
        {
            storingImageToFirebaseStorage();
        }
    }

    public void storingImageToFirebaseStorage()
    {
        uploadProgressBar.setVisibility(View.VISIBLE);
        Calendar callForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate=currentDate.format(callForDate.getTime());

        Calendar callForTime=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime=currentTime.format(callForTime.getTime());

        postRandomName=currentUserID+saveCurrentDate+saveCurrentTime;

        final StorageReference filePath=postImagesReference.child("Post Images").child(ImageUri.getLastPathSegment()+postRandomName+".jpg");



        filePath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful())
                {

                    filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadUrl=uri.toString();
                            savingPostInfoToDatabase();
                        }
                    });




                    Toast.makeText(getApplicationContext(),"Image Uploaded Successfully!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    uploadProgressBar.setVisibility(View.GONE);
                    String message=task.getException().getMessage();
                    Toast.makeText(getApplicationContext(),"Error Occured! "+message,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void savingPostInfoToDatabase()
    {
        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String username=dataSnapshot.child("Username").getValue().toString();
                    String userProfileImage=dataSnapshot.child("ProfileImage").getValue().toString();

                    HashMap postsMap=new HashMap();
                    postsMap.put("UserId",currentUserID);
                    postsMap.put("Date",saveCurrentDate);
                    postsMap.put("Time",saveCurrentTime);
                    postsMap.put("Description",description);
                    postsMap.put("PostImage",downloadUrl);
                    postsMap.put("ProfileImage",userProfileImage);
                    postsMap.put("Username",username);

                    postRef.child(postRandomName).updateChildren(postsMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful())
                            {
                                uploadProgressBar.setVisibility(View.GONE);
                                finish();
                                Toast.makeText(getApplicationContext(),"Post is updated successfully",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                uploadProgressBar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(),"Error occured while updating",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            importImageView.setImageURI(ImageUri);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_augment);

        initialize();
        listeners();
    }
}
