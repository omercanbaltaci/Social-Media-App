package com.example.socialmediaapp.AdaptersClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.ModelClasses.Model_FavoriteImageStatus;
import com.example.socialmediaapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class GetFavoiteImageStatusAdapter extends FirestoreRecyclerAdapter<Model_FavoriteImageStatus, GetFavoiteImageStatusAdapter.GetFavoriteImageStatusViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public GetFavoiteImageStatusAdapter(@NonNull FirestoreRecyclerOptions<Model_FavoriteImageStatus> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GetFavoriteImageStatusViewHolder getFavoriteImageStatusViewHolder, int i, @NonNull Model_FavoriteImageStatus model_favoriteImageStatus) {
        String linkOfImageStatus = model_favoriteImageStatus.getStatusimageurl();
        String linkOfProfilePic = model_favoriteImageStatus.getProfileurl();
        getFavoriteImageStatusViewHolder.userEmailTV.setText(model_favoriteImageStatus.getUseremail());
        getFavoriteImageStatusViewHolder.dateOFStatusTV.setText(model_favoriteImageStatus.getCurrentdatetime());
        getFavoriteImageStatusViewHolder.statusDescriptionTV.setText(model_favoriteImageStatus.getStatus());

        Glide.with(getFavoriteImageStatusViewHolder.statusIV.getContext())
                .load(linkOfImageStatus).into(getFavoriteImageStatusViewHolder.statusIV);
        Glide.with(getFavoriteImageStatusViewHolder.profilePicIV.getContext())
                .load(linkOfProfilePic).into(getFavoriteImageStatusViewHolder.profilePicIV);

        getFavoriteImageStatusViewHolder.removeStatusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();
                if (objectFirebaseAuth != null) {
                    FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();
                    objectFirebaseFirestore.collection("UserFavorite").document(
                            objectFirebaseAuth.getCurrentUser().getEmail().toString()
                    ).collection("FavoriteImageStatus")
                            .document(getSnapshots().getSnapshot(getFavoriteImageStatusViewHolder.getAdapterPosition()).getId())
                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getFavoriteImageStatusViewHolder.removeStatusIV.getContext(), "Favori statü silindi", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getFavoriteImageStatusViewHolder.removeStatusIV.getContext(), "Favori statü silinemedi", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(getFavoriteImageStatusViewHolder.removeStatusIV.getContext(), "Giriş yapmış bir kullanıcı yok", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public GetFavoriteImageStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetFavoriteImageStatusViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.model_favorite_is, parent, false
        ));
    }

    public class GetFavoriteImageStatusViewHolder extends RecyclerView.ViewHolder {
        ImageView statusIV, profilePicIV, removeStatusIV;
        TextView userEmailTV, dateOFStatusTV, statusDescriptionTV;

        public GetFavoriteImageStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            statusIV = itemView.findViewById(R.id.model_favorite_is_StatusIV);
            profilePicIV = itemView.findViewById(R.id.model_favorite_is_profilePicIV);
            removeStatusIV = itemView.findViewById(R.id.model_favorite_is_removeStatusIV);
            userEmailTV = itemView.findViewById(R.id.model_favorite_is_userEmailTV);
            dateOFStatusTV = itemView.findViewById(R.id.model_favorite_is_dateOfStatusTV);
            statusDescriptionTV = itemView.findViewById(R.id.model_favorite_is_statusDescriptTv);
        }
    }
}
