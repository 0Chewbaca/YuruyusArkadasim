package com.erenmeric.yuruyus_arkadasim;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.erenmeric.yuruyus_arkadasim.Adapter.ChatUserAdapter;
import com.erenmeric.yuruyus_arkadasim.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class DirectMessageActivity extends AppCompatActivity {

    private RecyclerView recyclerViewChats;
    private ChatUserAdapter chatUserAdapter;
    private List<User> usersForChat, userForGood;
    List<String> userKeys;
    int a;

    private SocialAutoCompleteTextView searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_message);

        searchBar = findViewById(R.id.search_bar);
        recyclerViewChats = findViewById(R.id.recycler_view_chats);
        recyclerViewChats.setHasFixedSize(true);
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));
        usersForChat = new ArrayList<>();
        userForGood = new ArrayList<>();
        userKeys = new ArrayList<>();
        chatUserAdapter = new ChatUserAdapter(this, usersForChat);
        recyclerViewChats.setAdapter(chatUserAdapter);

        getUsersForChat();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        getSupportActionBar().setTitle(user.getName()+"'s Inbox");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void searchUser(String s){


        if (s.isEmpty()){
            usersForChat.clear();
            userKeys.clear();
            getUsersForChat();
        } else {
            usersForChat.clear();
            userKeys.clear();
            for (User user: userForGood){
                if (user.getUsername().length() >= s.length() && user.getUsername().substring(0, s.length()).equals(s)) {
                    usersForChat.add(user);

                }
            }

            for(User users: usersForChat) {
                //Log.d("eren850", users.getName());
                //Log.d("eren850", users.getId());
                if (users.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    //Log.d("eren850", "erennnn");
                    usersForChat.remove(users);
                }
            }

            chatUserAdapter.notifyDataSetChanged();

        }



    }

    private void getUsersForChat() {
        userKeys.clear();
        usersForChat.clear();

        FirebaseDatabase.getInstance().getReference().child("Messages").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                            userKeys.add(dataSnapshot.getKey());

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersForChat.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    for(String key: userKeys){
                        if(dataSnapshot.getKey().equals(key)){
                            User user = dataSnapshot.getValue(User.class);
                            usersForChat.add(user);
                            //userForGood.add(user);
                        }
                    }
                }



                chatUserAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }


        });


        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userForGood.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    User user = dataSnapshot.getValue(User.class);
                    userForGood.add(user);
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }


        });


    }
}