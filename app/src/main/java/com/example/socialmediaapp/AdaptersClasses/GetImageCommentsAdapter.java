package com.example.socialmediaapp.AdaptersClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.ModelClasses.Model_GetImageComments;
import com.example.socialmediaapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class GetImageCommentsAdapter extends FirestoreRecyclerAdapter<Model_GetImageComments, GetImageCommentsAdapter.GetImageCommentsViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public GetImageCommentsAdapter(@NonNull FirestoreRecyclerOptions<Model_GetImageComments> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GetImageCommentsViewHolder getImageCommentsViewHolder, int i, @NonNull Model_GetImageComments model_getImageComments) {
        getImageCommentsViewHolder.userEmailTV.setText(model_getImageComments.getCommentperson());
        getImageCommentsViewHolder.commentDateTV.setText(model_getImageComments.getCurrentdatetime());
        getImageCommentsViewHolder.commentTV.setText(model_getImageComments.getComment());
        String profileImageUrl = model_getImageComments.getProfilepicurl();

        Glide.with(getImageCommentsViewHolder.userProfileIV.getContext())
                .load(profileImageUrl).into(getImageCommentsViewHolder.userProfileIV);
    }

    @NonNull
    @Override
    public GetImageCommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetImageCommentsViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.model_image_comments, parent, false
        ));
    }

    public class GetImageCommentsViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfileIV;
        TextView userEmailTV, commentDateTV, commentTV;

        public GetImageCommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            userProfileIV = itemView.findViewById(R.id.model_addImagecomment_profilePicIV);
            userEmailTV = itemView.findViewById(R.id.model_addImagecomment_userNameTV);
            commentDateTV = itemView.findViewById(R.id.model_addImagecomment_currentDateTimeTV);
            commentTV = itemView.findViewById(R.id.model_addImagecomment_commentTV);
        }
    }
}
