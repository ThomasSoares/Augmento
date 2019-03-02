package com.example.thomas.augmento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchNewActivity extends AppCompatActivity {

    EditText searchEditText;
    ImageButton searchButton;
    RecyclerView searchResultList;
    String searchBoxInput;

    private DatabaseReference allUsersDatabaseRef;
    private FirebaseRecyclerAdapter<FindFriends, FindFriendsAdapter.FindFriendsViewHolder> firebaseRecyclerAdapter;


    public void initialize()
    {
        searchBoxInput="mary_jane";
        searchEditText=findViewById(R.id.searchEditText);
        searchButton=findViewById(R.id.searchButton);
        searchResultList=findViewById(R.id.recyclerView);
        allUsersDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void listeners()
    {
        searchResultList.setHasFixedSize(true);
        searchResultList.setLayoutManager(new LinearLayoutManager(this));
        searchButton.setOnClickListener(v->{

            searchBoxInput=searchEditText.getText().toString();
            searchPeopleAndFriends(searchBoxInput);
        });
    }



    private void searchPeopleAndFriends(String searchBoxInput)
    {
        Toast.makeText(this,"Searching....",Toast.LENGTH_SHORT).show();

        Query searchPeopleAndFriendsQuery=allUsersDatabaseRef.orderByChild("Username")
                .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");

        FirebaseRecyclerOptions<FindFriends> users=
                new FirebaseRecyclerOptions.Builder<FindFriends>()
                        .setQuery(searchPeopleAndFriendsQuery, snapshot ->
                                new FindFriends(snapshot.child("ProfileImage").getValue().toString(),
                                snapshot.child("FirstName").getValue().toString(),
                                snapshot.child("LastName").getValue().toString(),
                                snapshot.child("Username").getValue().toString()))
                        .build();

        firebaseRecyclerAdapter=
                new FirebaseRecyclerAdapter<FindFriends, FindFriendsAdapter.FindFriendsViewHolder>(users) {
                    @Override
                    protected void onBindViewHolder(@NonNull FindFriendsAdapter.FindFriendsViewHolder myViewHolder, int i, @NonNull FindFriends findFriends) {

                        myViewHolder.setProfileImage(getApplicationContext(),findFriends.getProfileImage());
                        myViewHolder.setFirstName(findFriends.getFirstName());
                        myViewHolder.setLastName(findFriends.getLastName());
                        myViewHolder.setUsername(findFriends.getUsername());

                    }

                    @NonNull
                    @Override
                    public FindFriendsAdapter.FindFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_user_display_layout, parent, false);

                        return new FindFriendsAdapter.FindFriendsViewHolder(view);
                    }
                };
        firebaseRecyclerAdapter.startListening();
        searchResultList.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();

        firebaseRecyclerAdapter.stopListening();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_new);

        initialize();
        listeners();

        //searchPeopleAndFriends(searchBoxInput);
    }
}
