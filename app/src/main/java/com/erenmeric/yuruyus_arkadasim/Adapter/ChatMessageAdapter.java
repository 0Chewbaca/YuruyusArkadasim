package com.erenmeric.yuruyus_arkadasim.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.erenmeric.yuruyus_arkadasim.Model.Message;
import com.erenmeric.yuruyus_arkadasim.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context mContext;
    List<Message> mMessages;
    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();

    public ChatMessageAdapter(Context mContext, List<Message> mMessages) {
        this.mContext = mContext;
        this.mMessages = mMessages;
    }

    public class ViewHolder0 extends RecyclerView.ViewHolder{

        public TextView timeYour;
        public TextView messageYour;
        public ImageView seenInfo;

        public ViewHolder0(@NonNull View itemView) {
            super(itemView);

            timeYour = itemView.findViewById(R.id.time_your);
            messageYour = itemView.findViewById(R.id.message_your);
            seenInfo = itemView.findViewById(R.id.seen);

        }
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder{

        public TextView timeOther;
        public TextView messageOther;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

            timeOther = itemView.findViewById(R.id.time_other);
            messageOther = itemView.findViewById(R.id.message_other);

        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = mMessages.get(position);
        if(message.getAuthor().equals(fUser.getUid())){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                View view0  = LayoutInflater.from(mContext).inflate(R.layout.your_message_item, parent, false);
                return new ChatMessageAdapter.ViewHolder0(view0);
            case 1:
                View view1  = LayoutInflater.from(mContext).inflate(R.layout.other_person_message_item, parent, false);
                return new ChatMessageAdapter.ViewHolder1(view1);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Message message = mMessages.get(position);
        switch (holder.getItemViewType()) {
            case 0:
                ViewHolder0 viewHolder0 = (ViewHolder0)holder;
                String timeText0 = message.getTimeToString();
                viewHolder0.timeYour.setText(timeText0);
                viewHolder0.messageYour.setText(message.getText());

                if(message.getSeen()==true){
                    viewHolder0.seenInfo.setVisibility(View.VISIBLE);
                }
                else
                    viewHolder0.seenInfo.setVisibility(View.GONE);
                break;

            case 1:
                ViewHolder1 viewHolder1 = (ViewHolder1)holder;
                String timeText1 = message.getTimeToString();
                viewHolder1.timeOther.setText(timeText1);
                viewHolder1.messageOther.setText(message.getText());
                break;
        }
    }

}
