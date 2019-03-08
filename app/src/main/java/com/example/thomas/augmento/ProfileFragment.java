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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.appcompat.app.AppCompatActivity.RESULT_OK;


public class ProfileFragment extends Fragment {

    View parentHolder;
    CircleImageView editImageView, profileImageView;
    TextView nameTextView, descriptionTextView, followerCountTextView,followingCountTextView,postCountTextView;

    ProgressBar profilePicProgressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, postsRef, connRef;
    private StorageReference UserProfileImageRef;
    String currentUserID, receiverUserID;
    String downloadUrl=null;
    RecyclerView recyclerView;


    LocalStorage localStorage;

    FlexboxLayoutManager layoutManager=new FlexboxLayoutManager(getContext());
    FirebaseRecyclerAdapter<ProfilePosts, ProfilePostAdapter.ProfilePostsViewHolder> firebaseRecyclerAdapter;


    int count=0;

    public void initialize()
    {
        editImageView=parentHolder.findViewById(R.id.editImageView);
        profileImageView=parentHolder.findViewById(R.id.profileImageView1);
        nameTextView=parentHolder.findViewById(R.id.nameTextView);
        descriptionTextView=parentHolder.findViewById(R.id.descriptionTextView);
        followerCountTextView=parentHolder.findViewById(R.id.followerCountTextView);
        followingCountTextView=parentHolder.findViewById(R.id.followingCountTextView);
        postCountTextView=parentHolder.findViewById(R.id.postCountTextView);

        localStorage=new LocalStorage(getContext());
        profilePicProgressBar=parentHolder.findViewById(R.id.profilePicProgressBar);

        mAuth=FirebaseAuth.getInstance();
        receiverUserID=localStorage.getStorage("ProfileID");
        currentUserID=mAuth.getCurrentUser().getUid();
        UserRef=FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef=FirebaseStorage.getInstance().getReference().child("Profile Images");
        postsRef= FirebaseDatabase.getInstance().getReference().child("Posts");
        connRef=FirebaseDatabase.getInstance().getReference().child("Connection").child(currentUserID);


        recyclerView=parentHolder.findViewById(R.id.profileRecyclerView);
        layoutManager=new FlexboxLayoutManager(getContext());
        //layoutManager.canScrollHorizontally();
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        setFollowerCount();
        setFollowingCount();
    }

    public void listeners()
    {
        editImageView.setOnClickListener(v -> getPhoto());

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String profileImage=dataSnapshot.child("ProfileImage").getValue().toString();
                    String username=dataSnapshot.child("Username").getValue().toString();
                    String fullName=dataSnapshot.child("FirstName").getValue().toString() +" "+ dataSnapshot.child("LastName").getValue().toString();
                    String description=dataSnapshot.child("Description").getValue().toString();

                    Picasso.with(getContext()).load(profileImage).placeholder(R.drawable.ic_person_black_24dp).into(profileImageView);
                    nameTextView.setText(fullName);
                    descriptionTextView.setText(description);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setFollowerCount()
    {
        connRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int followersCount=(int) dataSnapshot.child("Followers").getChildrenCount();
                followerCountTextView.setText(String.valueOf(followersCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setFollowingCount()
    {
        connRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int followersCount=(int) dataSnapshot.child("Following").getChildrenCount();
                followingCountTextView.setText(String.valueOf(followersCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

                filePath.putFile(resultUri).addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getContext(),"Profile Image stored in storage",Toast.LENGTH_SHORT).show();


                        filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                            downloadUrl = uri.toString();

                            UserRef.child("ProfileImage").setValue(downloadUrl)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            profilePicProgressBar.setVisibility(View.GONE);
                                            Toast.makeText(getContext(), "Image stored in database", Toast.LENGTH_SHORT).show();
                                        } else {
                                            profilePicProgressBar.setVisibility(View.GONE);
                                            String message = task1.getException().getMessage();
                                            Toast.makeText(getContext(), "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        });

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

    public void displayPosts()
    {

        FirebaseRecyclerOptions<ProfilePosts> profilePosts=
                new FirebaseRecyclerOptions.Builder<ProfilePosts>()
                .setQuery(postsRef, snapshot -> {
                    if(snapshot.child("UserId").getValue().toString().equalsIgnoreCase(currentUserID))
                    {
                        ++count;
                        localStorage.addStorage("PostsCount",String.valueOf(count));
                    }


                    return new ProfilePosts(snapshot.child("PostImage").getValue().toString(), snapshot.child("UserId").getValue().toString());
                }).build();

        firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<ProfilePosts, ProfilePostAdapter.ProfilePostsViewHolder>(profilePosts) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProfilePostAdapter.ProfilePostsViewHolder profilePostsViewHolder, int i, @NonNull ProfilePosts profilePosts) {

                        profilePostsViewHolder.setPostImageView(getContext(),profilePosts.getPostImage());


                    }

                    @NonNull
                    @Override
                    public ProfilePostAdapter.ProfilePostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_posts, parent, false);

                        return new ProfilePostAdapter.ProfilePostsViewHolder(view);
                    }


                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        firebaseRecyclerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        firebaseRecyclerAdapter.stopListening();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentHolder= inflater.inflate(R.layout.fragment_profile, container, false);
        initialize();
        listeners();

        displayPosts();
        postCountTextView.setText(localStorage.getStorage("PostsCount"));

        return parentHolder;
    }





}
