package com.example.thomas.augmento;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class NotificationsFragment extends Fragment {

    View parentHolder;
    RecyclerView recyclerView;
    private List<FindFriends> notiList=new ArrayList<>();
    private FindFriendsAdapter mAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parentHolder= inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView=parentHolder.findViewById(R.id.notiRecylerView);
        mAdapter=new FindFriendsAdapter(notiList);

        RecyclerView.LayoutManager mLayoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        prepareNoti();

        return parentHolder;
    }

    public void prepareNoti()
    {
        FindFriends findFriends=new FindFriends("https://firebasestorage.googleapis.com/v0/b/augmento-84ddb.appspot.com/o/Profile%20Images%2FbASZAkYDAuN5iZN6wSpSyp4X7MD2.jpg?alt=media&token=bce0c2b8-7e09-4c41-bfaf-b63d4cdaa208","Mary","Jane","Likes Your Post");
        notiList.add(findFriends);

        findFriends=new FindFriends("https://firebasestorage.googleapis.com/v0/b/augmento-84ddb.appspot.com/o/Profile%20Images%2FbASZAkYDAuN5iZN6wSpSyp4X7MD2.jpg?alt=media&token=bce0c2b8-7e09-4c41-bfaf-b63d4cdaa208","Mary","Jane","Likes Your Post");
        notiList.add(findFriends);
        findFriends=new FindFriends("https://firebasestorage.googleapis.com/v0/b/augmento-84ddb.appspot.com/o/Profile%20Images%2FbASZAkYDAuN5iZN6wSpSyp4X7MD2.jpg?alt=media&token=bce0c2b8-7e09-4c41-bfaf-b63d4cdaa208","Mary","Jane","Likes Your Post");
        notiList.add(findFriends);

        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

}
