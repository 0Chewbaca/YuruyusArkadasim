package com.erenmeric.yuruyus_arkadasim.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erenmeric.yuruyus_arkadasim.Adapter.CreateAdapter;
import com.erenmeric.yuruyus_arkadasim.Model.Post;
import com.erenmeric.yuruyus_arkadasim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CreateAdapter adapter;
    private List<Post> mPosts;
    private List<String> followingLists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.recyclerview_posts);
        mPosts = new ArrayList<>();
        adapter = new CreateAdapter(getContext(), mPosts);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);

        followingLists = new ArrayList<>();
        checkFollowingUsers();
        //return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private void checkFollowingUsers() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().
                getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followingLists.clear();
                for(DataSnapshot s: snapshot.getChildren()){
                    followingLists.add(s.getKey());
                }
                followingLists.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                getPhotos();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getPhotos() {
        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPosts.clear();

                for (DataSnapshot s: snapshot.getChildren()){
                    Post post = s.getValue(Post.class);
                    for (String id: followingLists){
                        if(post.getPublisher().equals(id))
                            mPosts.add(post);
                    }

                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}