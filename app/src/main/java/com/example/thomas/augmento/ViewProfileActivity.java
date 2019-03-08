package com.example.thomas.augmento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ViewProfileActivity extends AppCompatActivity {

    View parentHolder;
    CircleImageView editImageView, profileImageView;
    TextView nameTextView, descriptionTextView;
    Button followButton;


    RecyclerView recyclerView;

    FlexboxLayoutManager layoutManager;
    FirebaseRecyclerAdapter<ProfilePosts, ProfilePostAdapter.ProfilePostsViewHolder> firebaseRecyclerAdapter;

    private DatabaseReference profileUserRef, userRef,postsRef, connRef;
    private FirebaseAuth mAuth;
    private String sendUserID, receiveUserID, CURRENT_STATE;
    private TextView followerCountTextView, followingCountTextView, postCountTextView;

    LocalStorage localStorage;
    int count=0;

    public void initialize()
    {
        editImageView=findViewById(R.id.editImageView);
        profileImageView=findViewById(R.id.profileImageView1);
        nameTextView=findViewById(R.id.nameTextView);
        descriptionTextView=findViewById(R.id.descriptionTextView);
        followButton=findViewById(R.id.followButton);
        followerCountTextView=findViewById(R.id.followerCountTextView);
        followingCountTextView=findViewById(R.id.followingCountTextView);
        postCountTextView=findViewById(R.id.postCountTextView);



        mAuth=FirebaseAuth.getInstance();
        sendUserID=mAuth.getCurrentUser().getUid();
        receiveUserID=getIntent().getExtras().get("UserID").toString();
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");
        postsRef= FirebaseDatabase.getInstance().getReference().child("Posts");
        connRef=FirebaseDatabase.getInstance().getReference().child("Connection");

        recyclerView=findViewById(R.id.profileRecyclerView);
        layoutManager=new FlexboxLayoutManager(getApplicationContext());
        //layoutManager.canScrollHorizontally();
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        CURRENT_STATE="not_friends";
        localStorage=new LocalStorage(getApplicationContext());

    }

    public void listeners()
    {
        userRef.child(receiveUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String profileImage=dataSnapshot.child("ProfileImage").getValue().toString();
                    String username=dataSnapshot.child("Username").getValue().toString();
                    String fullName=dataSnapshot.child("FirstName").getValue().toString() +" "+ dataSnapshot.child("LastName").getValue().toString();
                    String description=dataSnapshot.child("Description").getValue().toString();

                    Picasso.with(getApplicationContext()).load(profileImage).placeholder(R.drawable.ic_person_black_24dp).into(profileImageView);
                    nameTextView.setText(fullName);
                    descriptionTextView.setText(description);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        connRef.child(sendUserID).child("Following").child(receiveUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    followButton.setText("Following");
                }
                else
                {
                    followButton.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (!sendUserID.equals(receiveUserID))
        {
            followButton.setOnClickListener(v -> {


                if(CURRENT_STATE.equals("not_friends"))
                {
                    startFollowing();
                }
            });
        }
        else
        {
            followButton.setVisibility(View.INVISIBLE);
        }

        setFollowersNumber();
        setFollowingNumber();
    }

    public void setFollowersNumber()
    {
        connRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int followersCount=(int) dataSnapshot.child(receiveUserID).child("Followers").getChildrenCount();
                followerCountTextView.setText(String.valueOf(followersCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setFollowingNumber()
    {
        connRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int followingCount=(int) dataSnapshot.child(receiveUserID).child("Following").getChildrenCount();
                followingCountTextView.setText(String.valueOf(followingCount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startFollowing()
    {
        Calendar callForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyyy");
        String saveCurrentDate=currentDate.format(callForDate.getTime());

        Calendar callForTime=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss");
        String saveCurrentTime=currentTime.format(callForTime.getTime());

        HashMap connHashMap=new HashMap();
        connHashMap.put("Date",saveCurrentDate);
        connHashMap.put("Time",saveCurrentTime);

        if(followButton.getText().toString().equalsIgnoreCase("follow"))
        {
            connRef.child(receiveUserID).child("Followers").child(sendUserID).updateChildren(connHashMap).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    followButton.setText("Following");
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_SHORT).show();
                }
            });

            connRef.child(sendUserID).child("Following").child(receiveUserID).updateChildren(connHashMap).addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    followButton.setText("Following");
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_SHORT).show();
                }
            });
        }

        else
        {
            connRef.child(receiveUserID).child("Followers").child(sendUserID).removeValue().addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    followButton.setText("Follow");
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Error OccurearFragment.d",Toast.LENGTH_SHORT).show();
                }
            });

            connRef.child(sendUserID).child("Following").child(receiveUserID).removeValue().addOnCompleteListener(task -> {
                if(task.isSuccessful())
                {
                    followButton.setText("Follow");
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void displayPosts()
    {
        FirebaseRecyclerOptions<ProfilePosts> profilePosts=
                new FirebaseRecyclerOptions.Builder<ProfilePosts>()
                        .setQuery(postsRef, snapshot -> {
                            if(snapshot.child("UserId").getValue().toString().equalsIgnoreCase(receiveUserID))
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
                        profilePostsViewHolder.setPostImageView(getApplicationContext(),profilePosts.getPostImage());

                    }

                    @NonNull
                    @Override
                    public ProfilePostAdapter.ProfilePostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_posts, parent, false);

                        return new ProfilePostAdapter.ProfilePostsViewHolder(view);
                    }
                };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        initialize();
        listeners();
        displayPosts();
        postCountTextView.setText(localStorage.getStorage("PostsCount"));
    }
}
