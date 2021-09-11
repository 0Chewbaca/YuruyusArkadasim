package com.erenmeric.yuruyus_arkadasim.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erenmeric.yuruyus_arkadasim.Adapter.CreateAdapter;
import com.erenmeric.yuruyus_arkadasim.Adapter.PhotoAdapter;
import com.erenmeric.yuruyus_arkadasim.EditProfileActivity;
import com.erenmeric.yuruyus_arkadasim.Model.Post;
import com.erenmeric.yuruyus_arkadasim.Model.User;
import com.erenmeric.yuruyus_arkadasim.OptionsActivity;
import com.erenmeric.yuruyus_arkadasim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private RecyclerView recyclerViewSaves;
    private CreateAdapter postAdapterSaves;
    private List<Post> mySavedPosts;

    private RecyclerView recyclerView;
    private CreateAdapter photoAdapter;
    private List<Post> myPhotoList;

    private CircleImageView imageProfile;
    private ImageView options;
    private TextView posts;
    private TextView followers;
    private TextView following;
    private TextView fullName;
    private TextView bio;
    private TextView username;

    private ImageButton myPictures;
    private ImageButton savedPictures;
    private Button editProfile;

    private LinearLayout bottom;

    private FirebaseUser fUser;
    String profileId;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        String data = getContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE).getString("profileId", "none");
        Log.d("eren10253", data);
        if(data.equals("none")){
            profileId = fUser.getUid();
        } else {
            profileId = data;
        }


        imageProfile = view.findViewById(R.id.image_profile);
        options = view.findViewById(R.id.options);
        posts = view.findViewById(R.id.posts);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        fullName = view.findViewById(R.id.full_name);
        bio = view.findViewById(R.id.bio);
        username = view.findViewById(R.id.username);
        myPictures = view.findViewById(R.id.my_pictures);
        savedPictures = view.findViewById(R.id.saved_pictures);
        editProfile = view.findViewById(R.id.edit_profile);

        recyclerView= view.findViewById(R.id.recycler_view_pictures);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        myPhotoList = new ArrayList<>();
        photoAdapter = new CreateAdapter(getContext(), myPhotoList);
        recyclerView.setAdapter(photoAdapter);

        recyclerViewSaves = view.findViewById(R.id.recycler_view_saved);
        recyclerViewSaves.setHasFixedSize(true);
        recyclerViewSaves.setLayoutManager(new GridLayoutManager(getContext(), 3));

        mySavedPosts = new ArrayList<>();
        postAdapterSaves = new CreateAdapter(getContext(), mySavedPosts);
        recyclerViewSaves.setAdapter(postAdapterSaves);

        bottom = view.findViewById(R.id.bottom);

        getUserInfo();
        getFollowersAndFollowingInfo();
        getPostCount();
        getMyPhotos();
        getSavedPosts();

        if(profileId.equals(fUser.getUid())){
            editProfile.setText("Edit Profile");
            bottom.setVisibility(View.VISIBLE);
        } else {
            checkFollowingStatus();
            bottom.setVisibility(View.GONE);
        }

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OptionsActivity.class));
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText = editProfile.getText().toString();

                if(btnText.equals("Edit Profile")){
                    startActivity(new Intent(getContext(), EditProfileActivity.class));
                } else {
                    if(btnText.equals("Follow")) {
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid())
                                .child("following").child(profileId).setValue(true);
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                                .child("followers").child(fUser.getUid()).setValue(true);
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid())
                                .child("following").child(profileId).removeValue();
                        FirebaseDatabase.getInstance().getReference().child("Follow").child(profileId)
                                .child("followers").child(fUser.getUid()).removeValue();
                    }

                }

            }
        });

        recyclerView.setVisibility(View.VISIBLE);
        recyclerViewSaves.setVisibility(View.GONE);

        myPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerViewSaves.setVisibility(View.GONE);
            }
        });

        savedPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.GONE);
                recyclerViewSaves.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    private void getSavedPosts() {
        List<String> savedIds = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Saves").child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s: snapshot.getChildren()){
                    savedIds.add(s.getKey());
                }

                FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                        mySavedPosts.clear();
                        for (DataSnapshot s1: snapshot1.getChildren() ){
                            Post post = s1.getValue(Post.class);

                            for(String id: savedIds){
                                if(post.getPostId().equals(id)){
                                    mySavedPosts.add(post);
                                }
                            }
                        }

                        postAdapterSaves.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void getMyPhotos() {

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myPhotoList.clear();
                for(DataSnapshot s: snapshot.getChildren()){
                    Post post = s.getValue(Post.class);
                    Log.d("erenege145", post.toString());

                    if(post.getPublisher().equals(profileId)){
                        myPhotoList.add(post);
                    }
                }

                Collections.reverse(myPhotoList);
                photoAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    private void checkFollowingStatus() {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(fUser.getUid())
                .child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                if (snapshot.child(profileId).exists()){
                    editProfile.setText("Following");
                }
                else{
                    editProfile.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    private void getPostCount() {

        FirebaseDatabase.getInstance().getReference("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int counter = 0;
                for(DataSnapshot sp: snapshot.getChildren()){
                    Post post = sp.getValue(Post.class);
                    if(post.getPublisher().equals(profileId))
                        counter++;
                }

                posts.setText(String.valueOf(counter));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getFollowersAndFollowingInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Follow").child(profileId);

        ref.child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followers.setText(snapshot.getChildrenCount() +"") ;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                following.setText(snapshot.getChildrenCount() +"") ;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getUserInfo() {

        FirebaseDatabase.getInstance().getReference().child("Users").child(profileId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                Picasso.get().load(user.getImageUrl()).into(imageProfile);
                username.setText(user.getUsername());
                fullName.setText(user.getName());
                bio.setText(user.getBio());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}