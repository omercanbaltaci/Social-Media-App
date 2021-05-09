package com.example.socialmediaapp.AdaptersClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.ModelClasses.Model_GetTextComments;
import com.example.socialmediaapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class GetTextCommentAdapter extends FirestoreRecyclerAdapter <Model_GetTextComments, GetTextCommentAdapter.GetTextCommentsViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public GetTextCommentAdapter(@NonNull FirestoreRecyclerOptions<Model_GetTextComments> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GetTextCommentsViewHolder getTextCommentsViewHolder, int i, @NonNull Model_GetTextComments model_getTextComments) {
        getTextCommentsViewHolder.userEmailTV.setText(model_getTextComments.getCommentperson());
        getTextCommentsViewHolder.commentDateTV.setText(model_getTextComments.getCurrentdatetime());
        getTextCommentsViewHolder.commentTV.setText(model_getTextComments.getComment());
        String profileImageUrl = model_getTextComments.getProfilepicurl();

        Glide.with(getTextCommentsViewHolder.userProfileIV.getContext())
                .load(profileImageUrl).into(getTextCommentsViewHolder.userProfileIV);
    }

    @NonNull
    @Override
    public GetTextCommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetTextCommentsViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.model_text_comments, parent, false));
    }

    public class GetTextCommentsViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfileIV;
        TextView userEmailTV, commentDateTV, commentTV;

        public GetTextCommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfileIV = itemView.findViewById(R.id.model_addcomment_profilePicIV);
            userEmailTV = itemView.findViewById(R.id.model_addcomment_userNameTV);
            commentDateTV = itemView.findViewById(R.id.model_addcomment_currentDateTimeTV);
            commentTV = itemView.findViewById(R.id.model_addcomment_commentTV);
        }
    }
}
