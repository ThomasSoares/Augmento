package com.example.thomas.augmento;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.thomas.augmento.PostAdapter.MyViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    View parentHolder;
    private List<Post> postList=new ArrayList<>();
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    ProgressBar homeProgressBar;
    private DatabaseReference postsRef;
    FirebaseRecyclerAdapter <Posts, MyViewHolder> firebaseRecyclerAdapter;
    int count;

    public void initialize()
    {
        recyclerView=parentHolder.findViewById(R.id.postRecyclerView);
        postAdapter=new PostAdapter(postList);
        homeProgressBar=parentHolder.findViewById(R.id.homeProgressBar);
        homeProgressBar.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(postAdapter);
        postsRef= FirebaseDatabase.getInstance().getReference().child("Posts");
        count=0;
    }

    public void listeners()
    {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void OnClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    private void preparePostData()
    {
        Post post=new Post("johm321","Just clicked a blank purple screen");
        postList.add(post);

        post=new Post("tommy123","Just clicked a blank purple screen");
        postList.add(post);

        post=new Post("tommy123","Just clicked a blank purple screen");
        postList.add(post);
        post=new Post("tommy123","Just clicked a blank purple screen");
        postList.add(post);
        post=new Post("tommy123","Just clicked a blank purple screen");
        postList.add(post);
        post=new Post("tommy123","Just clicked a blank purple screen");
        postList.add(post);
    }

    public void displayAllUserPosts()
    {

        FirebaseRecyclerOptions<Posts> posts=
                new FirebaseRecyclerOptions.Builder<Posts>()
                .setQuery(postsRef, new SnapshotParser<Posts>() {
                    @NonNull
                    @Override
                    public Posts parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Posts(snapshot.child("Date").getValue().toString(),
                                snapshot.child("Description").getValue().toString(),
                                snapshot.child("PostImage").getValue().toString(),
                                snapshot.child("ProfileImage").getValue().toString(),
                                snapshot.child("Time").getValue().toString(),
                                snapshot.child("UserId").getKey().toString(),
                                snapshot.child("Username").getValue().toString());
                    }
                })
                .build();

        firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<Posts, MyViewHolder>(posts) {
                    @Override
                    protected void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i, @NonNull Posts posts) {
                        myViewHolder.setUsername(posts.getUsername());
                        myViewHolder.setDescription(posts.getDescription());
                        myViewHolder.setProfileImage(getContext(), posts.getProfileImage());
                        myViewHolder.setPostImage(getContext(), posts.getPostImage());

                        homeProgressBar.setVisibility(View.GONE);

                    }

                    @NonNull
                    @Override
                    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.post_list_row, parent, false);

                        return new MyViewHolder(view);
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

        parentHolder= inflater.inflate(R.layout.fragment_home, container, false);

        initialize();
        listeners();

        displayAllUserPosts();


        return parentHolder;
    }

}
