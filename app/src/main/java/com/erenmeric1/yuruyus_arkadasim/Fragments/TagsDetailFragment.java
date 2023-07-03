package com.erenmeric1.yuruyus_arkadasim.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.erenmeric1.yuruyus_arkadasim.Adapter.PostAdapter;
import com.erenmeric1.yuruyus_arkadasim.Model.Post;
import com.erenmeric1.yuruyus_arkadasim.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TagsDetailFragment extends Fragment {

    private String tag;
    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> postList;
    private List<String> postIds;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tags_detail, container, false);

        tag = getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getString("tagId","none");

        Log.d("hashh", "onCreateView: "+ tag);

        recyclerView = view.findViewById(R.id.recycler_view_tags);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        postIds = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        FirebaseDatabase.getInstance().getReference().child("Hashtags").child(tag).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                postIds.clear();
                for (DataSnapshot ds: snapshot.getChildren()){
                    String key = ds.getKey();
                    postIds.add(key);
                    Log.d("hashh", "onCreateView: "+ key);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    Post post = ds.getValue(Post.class);
                    for(String key: postIds){
                        if(ds.getKey().equals(key)){
                            postList.add(post);
                            Log.d("hashh", "onCreateView: "+ post.toString());
                        }
                    }

                }
                Collections.reverse(postList);
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

        return view;
    }


}