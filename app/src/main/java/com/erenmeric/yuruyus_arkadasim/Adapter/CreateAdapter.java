package com.erenmeric.yuruyus_arkadasim.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.erenmeric.yuruyus_arkadasim.CommentActivity;
import com.erenmeric.yuruyus_arkadasim.FollowersActivity;
import com.erenmeric.yuruyus_arkadasim.Fragments.PostDetailFragment;
import com.erenmeric.yuruyus_arkadasim.Fragments.ProfileFragment;
import com.erenmeric.yuruyus_arkadasim.Fragments.TagsDetailFragment;
import com.erenmeric.yuruyus_arkadasim.Model.Post;
import com.erenmeric.yuruyus_arkadasim.Model.User;
import com.erenmeric.yuruyus_arkadasim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.hendraanggrian.appcompat.widget.SocialView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class CreateAdapter extends RecyclerView.Adapter<CreateAdapter.ViewHolder> {

    private Context mContext;
    private List<Post> mList;
    private FirebaseUser firebaseUser;


    public CreateAdapter(Context mContext, List<Post> mList) {
        this.mContext = mContext;
        this.mList = mList;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new CreateAdapter.ViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = mList.get(position);
        Picasso.get().load(post.getImageUrl()).placeholder(R.mipmap.ic_launcher).into(holder.postImage);

        isLiked(post.getPostId(), holder.like);
        noOfLikes( post.getPostId(), holder.numberOfLikes);
        isSaved(post.getPostId(), holder.save);
        getComments(post.getPostId(), holder.numberOfComments );

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit().putString("postid",post.getPostId()).apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(
                        R.id.fragment_container, new PostDetailFragment()).commit();
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(post.getPublisher())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if( user.getImageUrl().equals("default") ){
                            holder.imageProfile.setImageResource(R.mipmap.ic_launcher);
                        } else
                            Picasso.get().load(user.getImageUrl()).into(holder.imageProfile);
                        holder.username.setText(user.getUsername());
                        holder.author.setText(user.getName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        holder.description.setText(post.getDescription());
        //holder.description.setEnabled(false);



        holder.description.setOnMentionClickListener(new SocialView.OnClickListener() {
            @Override
            public void onClick(@NonNull SocialView socialView, @NonNull CharSequence charSequence) {
                //Log.d("createadapter", charSequence.toString() + " eren1111");

                FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            User user = ds.getValue(User.class);

                            //Log.d("createadapter", charSequence.toString() + " eren");

                            if (user.getUsername().equals(String.valueOf(charSequence))) {
                                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().
                                        putString("profileId", user.getId()).apply();

                                //Log.d("createadapter", charSequence.toString());

                                ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().
                                        replace(R.id.fragment_container, new ProfileFragment()).commit();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.description.setOnHashtagClickListener(new SocialView.OnClickListener() {
            @Override
            public void onClick(@NonNull SocialView view, @NonNull CharSequence text) {

                Log.d("tagIdd", text.toString());
                mContext.getSharedPreferences("TAG",Context.MODE_PRIVATE).edit().putString("tagId", text.toString()).apply();

                Log.d("tagIdd", text.toString());
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(
                        R.id.fragment_container, new TagsDetailFragment()).commit();

                Log.d("tagIdd", text.toString());
            }
        });




        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.like.getTag().equals("like")){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).setValue(true);
                    addNotification(post.getPostId(), post.getPublisher());
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostId())
                            .child(firebaseUser.getUid()).removeValue();
                }
            }
        });

        holder.author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().
                        putString("profileId",post.getPublisher()).apply();
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,new ProfileFragment()).commit();
            }
        });


        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostId() );
                intent.putExtra("publisherId", post.getPublisher() );
                mContext.startActivity(intent);
                Log.d("eren12345", "Okey??");
            }
        });

        holder.numberOfLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, FollowersActivity.class);
                intent.putExtra("id", post.getPostId()   );
                intent.putExtra("title", "likes");
                mContext.startActivity(intent);
            }
        });

        holder.numberOfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("postId", post.getPostId());
                intent.putExtra("authorId", post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.save.getTag().equals("save")){
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                            .child(post.getPostId()).setValue(true);
                } else {
                    FirebaseDatabase.getInstance().getReference().child("Saves").child(firebaseUser.getUid())
                            .child(post.getPostId()).removeValue();
                }
            }
        });

        holder.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.getSharedPreferences("PROFILE", Context.MODE_PRIVATE).edit().
                        putString("profileId",post.getPublisher()).apply();
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,new ProfileFragment()).commit();
            }
        });



    }

    private void addNotification(String postId, String publisher) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userid", firebaseUser.getUid());
        map.put("text"," liked your post");
        map.put("postid", postId);
        map.put("isPost", true);

        FirebaseDatabase.getInstance().getReference().child("Notifications").child(publisher).
                push().setValue(map);
    }



    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView postImage;
        public ImageView imageProfile;

        public ImageView like;
        public ImageView comment;
        public ImageView save;
        public ImageView more;
        public TextView username;
        public TextView numberOfLikes;
        public TextView author;
        public TextView numberOfComments;
        SocialAutoCompleteTextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            postImage = itemView.findViewById(R.id.post_image);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.commentItem);
            save = itemView.findViewById(R.id.save);
            more = itemView.findViewById(R.id.more);
            username = itemView.findViewById(R.id.username);
            numberOfLikes = itemView.findViewById(R.id.number_of_likes);
            numberOfComments = itemView.findViewById(R.id.number_of_comments);
            author = itemView.findViewById(R.id.author);
            description = itemView.findViewById(R.id.description);
            imageProfile = itemView.findViewById(R.id.profile_image);
        }
    }

    private void isSaved(String postId, ImageView image) {
        FirebaseDatabase.getInstance().getReference().child("Saves").child(FirebaseAuth.getInstance().
                getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postId).exists()){
                    image.setImageResource(R.drawable.ic_save_black);
                    image.setTag("saved");
                } else{
                    image.setImageResource(R.drawable.ic_save);
                    image.setTag("save");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void isLiked(String postId, ImageView imageView ){
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                            imageView.setImageResource(R.drawable.ic_liked);
                            imageView.setTag("liked");
                        } else {
                            imageView.setImageResource(R.drawable.ic_like);
                            imageView.setTag("like");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void noOfLikes(String postId, TextView text){
        FirebaseDatabase.getInstance().getReference().child("Likes").child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        text.setText(snapshot.getChildrenCount() + " likes");
                    }

                    @Override
                    public void onCancelled(@NonNull  DatabaseError error) {

                    }
                });
    }

    private void getComments(String postId, TextView text){

        FirebaseDatabase.getInstance().getReference().child("Comments").child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                text.setText("View all " + snapshot.getChildrenCount() + " comments ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}
