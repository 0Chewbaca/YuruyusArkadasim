package com.erenmeric1.yuruyus_arkadasim.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erenmeric1.yuruyus_arkadasim.ChatActivity;
import com.erenmeric1.yuruyus_arkadasim.Model.Message;
import com.erenmeric1.yuruyus_arkadasim.Model.User;
import com.erenmeric1.yuruyus_arkadasim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder>{

    Context mContext;
    List<User> mUsers;
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

    public ChatUserAdapter(Context mContext, List<User> mUsers) {
        this.mContext = mContext;
        this.mUsers = mUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view  = LayoutInflater.from(mContext).inflate(R.layout.dm_user_item, parent, false);
        return new ChatUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserAdapter.ViewHolder holder, int position) {
        User user = mUsers.get(position);

        holder.username.setText(user.getName());
        Picasso.get().load(user.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.imageProfile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("userId", user.getId());
                mContext.startActivity(intent);
            }
        });

        getPreview( holder, user.getId());

    }

    private void getPreview(ViewHolder holder, String userId) {
        FirebaseDatabase.getInstance().getReference().child("Messages").child(fUser.getUid()).child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()==false){
                    holder.preview.setText("");
                    holder.time.setText("Start chatting");
                    holder.unreadMessages.setText("");
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Messages").child(fUser.getUid()).child(userId)
                            .child("lastMessage").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull  DataSnapshot snapshot) {
                            Message lastMessage = snapshot.getValue(Message.class);
                            holder.preview.setText(lastMessage.getText());
                            if(lastMessage.getText().length()>30)
                                holder.preview.setText(lastMessage.getText().substring(0,29)+"...");
                            holder.time.setText(lastMessage.getTimeToString());
                            if(!lastMessage.getAuthor().equals(fUser.getUid()) && !lastMessage.getSeen())
                                holder.unreadMessages.setVisibility(View.VISIBLE);
                            else
                                holder.unreadMessages.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public CircleImageView imageProfile;
        public TextView username;
        public TextView preview;
        public TextView time;
        public TextView unreadMessages;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageProfile = itemView.findViewById(R.id.image_profile);
            username = itemView.findViewById(R.id.username);
            preview = itemView.findViewById(R.id.preview);
            time = itemView.findViewById(R.id.time);
            unreadMessages = itemView.findViewById(R.id.unread_messages);

        }
    }
}
