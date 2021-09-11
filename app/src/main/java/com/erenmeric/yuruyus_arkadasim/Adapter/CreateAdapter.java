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
import com.erenmeric.yuruyus_arkadasim.Model.Post;
import com.erenmeric.yuruyus_arkadasim.R;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CreateAdapter extends RecyclerView.Adapter<CreateAdapter.ViewHolder> {

    private Context mContext;
    private List<Post> mList;


    public CreateAdapter(Context mContext, List<Post> mList) {
        this.mContext = mContext;
        this.mList = mList;
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

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit().putString("postid",post.getPostId()).apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction().replace(
                        R.id.fragment_container, new PostDetailFragment()).commit();
            }
        });
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
            comment = itemView.findViewById(R.id.comment);
            save = itemView.findViewById(R.id.save);
            more = itemView.findViewById(R.id.more);
            username = itemView.findViewById(R.id.username);
            numberOfLikes = itemView.findViewById(R.id.number_of_likes);
            numberOfComments = itemView.findViewById(R.id.number_of_comments);
            author = itemView.findViewById(R.id.author);
            description = itemView.findViewById(R.id.description);

        }
    }




}
