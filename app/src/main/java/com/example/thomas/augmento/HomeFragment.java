package com.example.thomas.augmento;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    View parentHolder;
    private List<Post> postList=new ArrayList<>();
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;

    public void initialize()
    {
        recyclerView=parentHolder.findViewById(R.id.postRecyclerView);
        postAdapter=new PostAdapter(postList);
        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(postAdapter);
    }

    public void listeners()
    {
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Post post=postList.get(position);

                Toast.makeText(getContext(), post.getUsername()+" is selected!",Toast.LENGTH_SHORT).show();
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        parentHolder= inflater.inflate(R.layout.fragment_home, container, false);

        initialize();
        listeners();

        preparePostData();

        return parentHolder;
    }

}
