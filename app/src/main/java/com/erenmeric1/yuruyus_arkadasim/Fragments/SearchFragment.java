package com.erenmeric1.yuruyus_arkadasim.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erenmeric1.yuruyus_arkadasim.Adapter.TagAdapter;
import com.erenmeric1.yuruyus_arkadasim.Adapter.UserAdapter;
import com.erenmeric1.yuruyus_arkadasim.Model.User;
import com.erenmeric1.yuruyus_arkadasim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewTags;

    private List<User> mUsers;
    private UserAdapter userAdapter;
    private String currentUsercity;
    String ccity;
    private List<String> mHashTags;
    private List<String> mHashTagCount;
    private SocialAutoCompleteTextView searchBar;
    private TagAdapter tagAdapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerViewTags = view.findViewById(R.id.recycler_view_tags);
        recyclerViewTags.setHasFixedSize(true);
        recyclerViewTags.setLayoutManager(new LinearLayoutManager(getContext()));

        mUsers = new ArrayList<User>();
        userAdapter = new UserAdapter(getContext(), mUsers, true);
        recyclerView.setAdapter(userAdapter);

        mHashTags = new ArrayList<String>();
        mHashTagCount = new ArrayList<String>();
        tagAdapter = new TagAdapter(getContext(), mHashTags, mHashTagCount);
        recyclerViewTags.setAdapter(tagAdapter);

        searchBar = view.findViewById(R.id.search_bar);

        readUsers();
        readTags();



        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString());
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            
            }
        });

        return view;
    }

        private void readTags() {
            DatabaseReference hashTags = FirebaseDatabase.getInstance().getReference().child("Hashtags");
            hashTags.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mHashTags.clear();
                    mHashTagCount.clear();

                    for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                        mHashTags.add(snapshot.getKey());
                        mHashTagCount.add(snapshot.getChildrenCount()+ "");
                    }
                    tagAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void readUsers() {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(searchBar.getText().toString().isEmpty()){
                        mUsers.clear();
                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                            User user = snapshot1.getValue(User.class);

                            FirebaseDatabase.getInstance().getReference().child("Users").child(
                                    FirebaseAuth.getInstance().getCurrentUser().getUid()
                            ).child("city").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ccity = snapshot.getValue().toString();
                                    Log.d("ccity", ccity);
                                    Log.d("ccity", user.getCity());

                                    if (user.getCity().toString().equals(ccity)) {
                                        mUsers.add(user);
                                        Log.d("ccity","here");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        }

                        userAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        private void searchUser(String s) {

            //s = s.toLowerCase(Locale.ROOT);
            Query query = FirebaseDatabase.getInstance().getReference().child("Users")
                    .orderByChild("username").startAt(s).endAt( s+ "\uf8ff");

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot s: snapshot.getChildren()){
                        Log.d("allahhh", s.toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mUsers.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        User user = snapshot1.getValue(User.class);

                        //Log.d("Users","now"+ user.getName());
                        //String ccity = snapshot.getValue().
                        Log.d("cccity", ccity);
                        //Log.d("cccity","here");

                        if (user.getCity().toString().equals(ccity)) {
                            mUsers.add(user);
                            Log.d("cccity","here");
                        }

                    }
                    for(User user: mUsers){
                        Log.d("Users","end" + user.getName());
                    }

                    if(!mUsers.isEmpty())
                        userAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    private void filter (String text){
        List<String> mSearchTags = new ArrayList<>();
        List<String> mSearchTagsCount = new ArrayList<>();

        for(String s: mHashTags){
            if(s.toLowerCase().contains(text.toLowerCase())){
                mSearchTags.add(s);
                mSearchTagsCount.add(mHashTagCount.get(mHashTags.indexOf(s)));
            }
        }

        tagAdapter.filter(mSearchTags, mSearchTagsCount);
    }
}