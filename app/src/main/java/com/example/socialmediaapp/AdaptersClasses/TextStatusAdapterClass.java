package com.example.socialmediaapp.AdaptersClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.ModelClasses.Model_TextStatus;
import com.example.socialmediaapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TextStatusAdapterClass extends FirestoreRecyclerAdapter<Model_TextStatus, TextStatusAdapterClass.TextStatusViewHolder> {
    public TextStatusAdapterClass(@NonNull FirestoreRecyclerOptions<Model_TextStatus> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TextStatusViewHolder textStatusViewHolder, int i, @NonNull Model_TextStatus model_textStatus) {
        textStatusViewHolder.userStatusTV.setText(model_textStatus.getUseremail());
        textStatusViewHolder.dateTimeTV.setText(model_textStatus.getCurrentdatetime());
        textStatusViewHolder.userStatusTV.setText(model_textStatus.getStatus());
        textStatusViewHolder.heartCountTV.setText(Integer.toString(model_textStatus.getNooflove()));
        textStatusViewHolder.hahaCountTV.setText(Integer.toString(model_textStatus.getNoofhaha()));
        textStatusViewHolder.sadCountTV.setText(Integer.toString(model_textStatus.getNoofsad()));
        textStatusViewHolder.commentCountTV.setText(Integer.toString(model_textStatus.getNoofcomments()));
        String linkOfProfileImage = model_textStatus.getProfileurl();

        Glide.with(textStatusViewHolder.profileIV.getContext())
                .load(linkOfProfileImage).into(textStatusViewHolder.profileIV);
    }

    @NonNull
    @Override
    public TextStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TextStatusViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.model_text_status, parent, false
        ));
    }

    public class TextStatusViewHolder extends RecyclerView.ViewHolder {
        ImageView profileIV;
        ImageView heartIV, hahaIV, sadIV, deleteIV, commentIV, favoriteIV;
        TextView userEmailTV, dateTimeTV, userStatusTV, heartCountTV, hahaCountTV, sadCountTV, commentCountTV;

        public TextStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            profileIV = itemView.findViewById(R.id.model_textStatus_profileIV);
            heartIV = itemView.findViewById(R.id.model_textStatus_heartIV);
            hahaIV = itemView.findViewById(R.id.model_textStatus_hahaIV);
            sadIV = itemView.findViewById(R.id.model_textStatus_sadIV);
            deleteIV = itemView.findViewById(R.id.model_textStatus_deleteIV);
            commentIV = itemView.findViewById(R.id.model_textStatus_commentIV);
            favoriteIV = itemView.findViewById(R.id.model_textStatus_favoriteTextStatus);
            userEmailTV = itemView.findViewById(R.id.model_textStatus_emailTV);
            dateTimeTV = itemView.findViewById(R.id.model_textStatus_dateTV);
            userStatusTV = itemView.findViewById(R.id.model_textStatus_textStatusTV);
            heartCountTV = itemView.findViewById(R.id.model_textStatus_heartCountTV);
            hahaCountTV = itemView.findViewById(R.id.model_textStatus_hahaCountTV);
            sadCountTV = itemView.findViewById(R.id.model_textStatus_sadCountTV);
            commentCountTV = itemView.findViewById(R.id.model_textStatus_commentCountTV);
        }
    }
}
