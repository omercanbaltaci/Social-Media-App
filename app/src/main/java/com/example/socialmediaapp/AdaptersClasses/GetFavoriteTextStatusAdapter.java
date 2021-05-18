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
import com.example.socialmediaapp.ModelClasses.Model_FavoriteTextStatus;
import com.example.socialmediaapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class GetFavoriteTextStatusAdapter extends FirestoreRecyclerAdapter<Model_FavoriteTextStatus, GetFavoriteTextStatusAdapter.GetFavoriteTextStatusViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public GetFavoriteTextStatusAdapter(@NonNull FirestoreRecyclerOptions<Model_FavoriteTextStatus> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull GetFavoriteTextStatusViewHolder getFavoriteTextStatusViewHolder, int i, @NonNull Model_FavoriteTextStatus model_favoriteTextStatus) {
        getFavoriteTextStatusViewHolder.userEmailTV.setText(model_favoriteTextStatus.getUseremail());
        getFavoriteTextStatusViewHolder.dateOfStatusTV.setText(model_favoriteTextStatus.getCurrentdatetime());
        getFavoriteTextStatusViewHolder.statusTV.setText(model_favoriteTextStatus.getStatus());
        String linkOfProfilePic = model_favoriteTextStatus.getProfileurl();

        Glide.with(getFavoriteTextStatusViewHolder.profilePicIV.getContext())
                .load(linkOfProfilePic).into(getFavoriteTextStatusViewHolder.profilePicIV);

        getFavoriteTextStatusViewHolder.removeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth objectFirebaseAuth = FirebaseAuth.getInstance();
                if (objectFirebaseAuth != null) {
                    String currentLoggedInUser = objectFirebaseAuth.getCurrentUser().getEmail();
                    FirebaseFirestore objectFirebaseFirestore = FirebaseFirestore.getInstance();

                    objectFirebaseFirestore.collection("UserFavorite").document(currentLoggedInUser)
                            .collection("FavoriteTextStatus")
                            .document(
                                    getSnapshots().getSnapshot(getFavoriteTextStatusViewHolder.getAdapterPosition()).getId()
                            )
                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getFavoriteTextStatusViewHolder.removeIV.getContext(), "Favori statü silindi", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getFavoriteTextStatusViewHolder.removeIV.getContext(), "Favori statü silinemedi", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(getFavoriteTextStatusViewHolder.removeIV.getContext(), "Giriş yapmış bir kullanıcı yok", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NonNull
    @Override
    public GetFavoriteTextStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GetFavoriteTextStatusViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.model_favorite_ts, parent, false
        ));
    }

    public class GetFavoriteTextStatusViewHolder extends RecyclerView.ViewHolder {
        ImageView removeIV, profilePicIV;
        TextView userEmailTV, dateOfStatusTV, statusTV;

        public GetFavoriteTextStatusViewHolder(@NonNull View itemView) {
            super(itemView);

            removeIV = itemView.findViewById(R.id.model_favorite_ts_removeStatusIV);
            profilePicIV = itemView.findViewById(R.id.model_favorite_ts_profilePicIV);
            userEmailTV = itemView.findViewById(R.id.model_favorite_ts_emailTV);
            dateOfStatusTV = itemView.findViewById(R.id.model_favorite_ts_dateTV);
            statusTV = itemView.findViewById(R.id.model_favorite_ts_statusTV);
        }
    }
}
