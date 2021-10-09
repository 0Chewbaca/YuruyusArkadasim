package com.erenmeric.yuruyus_arkadasim.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.erenmeric.yuruyus_arkadasim.Fragments.PostDetailFragment;
import com.erenmeric.yuruyus_arkadasim.Fragments.ProfileFragment;
import com.erenmeric.yuruyus_arkadasim.Model.Notification;
import com.erenmeric.yuruyus_arkadasim.Model.Post;
import com.erenmeric.yuruyus_arkadasim.Model.User;
import com.erenmeric.yuruyus_arkadasim.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context mContext;
    private List<Notification> mNotifications;

    public NotificationAdapter(Context mContext, List<Notification> mNotifications) {
        this.mContext = mContext;
        this.mNotifications = mNotifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder viewHolder, int i) {
        Notification notification = mNotifications.get(i);

        getUser(viewHolder.imageProfile, viewHolder.username, notification.getUserid());
        if(notification.isPost()){
            viewHolder.postImage.setVisibility(View.VISIBLE);
            getPostImage(viewHolder.postImage, notification.getPostid());
        } else {
            viewHolder.postImage.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notification.isPost()){
                    mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit().
                            putString("postid",notification.getPostid()).apply();
                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().
                            replace(R.id.fragment_container, new PostDetailFragment()).commit();

                } else {
                    mContext.getSharedPreferences("PROFILE",Context.MODE_PRIVATE).edit().
                            putString("profileId",notification.getUserid()).apply();
                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().
                            replace(R.id.fragment_container, new ProfileFragment()).commit();


                }
            }
        });

        viewHolder.comment.setText(notification.getText());

    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }


    private void getPostImage(ImageView postImage, String postid) {
        FirebaseDatabase.getInstance().getReference().child("Posts").child(postid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Post post = snapshot.getValue(Post.class);
                Picasso.get().load(post.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(postImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUser(ImageView imageProfile, TextView username, String userid) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(userid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(user.getImageUrl().equals("default"))
                    imageProfile.setImageResource(R.mipmap.ic_launcher);
                else
                    Picasso.get().load(user.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(imageProfile);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageProfile;
        public ImageView postImage;
        public TextView username;
        public TextView comment;

        public ViewHolder(@NonNull View view) {
            super(view);

            imageProfile = view.findViewById(R.id.image_profile);
            postImage  = view.findViewById(R.id.post_image);
            username = view.findViewById(R.id.username);
            comment = view.findViewById(R.id.comment);

        }
    }

}
