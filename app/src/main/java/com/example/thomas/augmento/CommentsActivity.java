package com.example.thomas.augmento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CommentsActivity extends AppCompatActivity {

    private Button commentButton;
    private EditText commentEditText;
    private RecyclerView commentList;

    private String Post_Key, currentUserID;

    private DatabaseReference userRef, postRef;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerAdapter<Comments, CommentsAdapter.CommentsViewHolder> firebaseRecyclerAdapter;

    public void initialize()
    {
        commentList=findViewById(R.id.commentList);
        commentButton=findViewById(R.id.commentButton);
        commentEditText=findViewById(R.id.commentEditText);

        Post_Key=getIntent().getExtras().get("PostKey").toString();

        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        postRef=FirebaseDatabase.getInstance().getReference().child("Posts").child(Post_Key).child("Comments");
        mAuth=FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
    }

    public void listeners()
    {
        commentList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        commentList.setLayoutManager(linearLayoutManager);

        commentButton.setOnClickListener(v->{
            userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        String userName=dataSnapshot.child("Username").getValue().toString();

                        validateComment(userName);

                        commentEditText.setText("");

                        hideKeyboard();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
    }

    public void hideKeyboard()
    {
        //FUNCTION THAT HIDES THE KEYBOARD

        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText())
        {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }

    }

    private void validateComment(String userName)
    {
        String commentText=commentEditText.getText().toString();
        if(TextUtils.isEmpty(commentText))
        {
            Toast.makeText(this, "Please write a comment",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar calendarDate=Calendar.getInstance();
            SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyyy");
            final String saveCurrentDate=currentDate.format(calendarDate.getTime());

            Calendar calendarTime=Calendar.getInstance();
            SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss");
            final String saveCurrentTime=currentTime.format(calendarTime.getTime());

            final String randomKey=currentUserID+saveCurrentDate+saveCurrentTime;

            HashMap commentsMap=new HashMap();
            commentsMap.put("UID",currentUserID);
            commentsMap.put("Comment",commentText);
            commentsMap.put("Date",saveCurrentDate);
            commentsMap.put("Time",saveCurrentTime);
            commentsMap.put("Username",userName);

            postRef.child(randomKey).updateChildren(commentsMap)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(CommentsActivity.this,"You have commented!",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(CommentsActivity.this,"Error occurred!",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Comments> comments=
                new FirebaseRecyclerOptions.Builder<Comments>()
                        .setQuery(postRef, new SnapshotParser<Comments>() {
                            @NonNull
                            @Override
                            public Comments parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new Comments(snapshot.child("Comment").getValue().toString(),
                                        snapshot.child("Date").getValue().toString(),
                                        snapshot.child("Time").getValue().toString(),
                                        snapshot.child("Username").getValue().toString());
                            }
                        })
                .build();

        firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Comments, CommentsAdapter.CommentsViewHolder>(comments) {
                    @Override
                    protected void onBindViewHolder(@NonNull CommentsAdapter.CommentsViewHolder commentsViewHolder, int i, @NonNull Comments comments) {
                        commentsViewHolder.setUsername(comments.getUsername());
                        commentsViewHolder.setComment(comments.getComment());
                        commentsViewHolder.setDate(comments.getDate());
                        commentsViewHolder.setTime(comments.getTime());
                    }

                    @NonNull
                    @Override
                    public CommentsAdapter.CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_comments_layout, parent, false);

                        return new CommentsAdapter.CommentsViewHolder(view);
                    }
                };

        commentList.setAdapter(firebaseRecyclerAdapter);

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
        setContentView(R.layout.activity_comments);

        initialize();
        listeners();
    }
}
