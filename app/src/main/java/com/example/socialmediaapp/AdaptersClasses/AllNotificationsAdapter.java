package com.example.socialmediaapp.AdaptersClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.ModelClasses.Model_AllNotifications;
import com.example.socialmediaapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AllNotificationsAdapter extends FirestoreRecyclerAdapter<Model_AllNotifications, AllNotificationsAdapter.AllNotificationsViewHolder> {
    FirebaseFirestore  objectFirebaseFirestore = FirebaseFirestore.getInstance();
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public AllNotificationsAdapter(@NonNull FirestoreRecyclerOptions<Model_AllNotifications> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AllNotificationsViewHolder allNotificationsViewHolder, int i, @NonNull Model_AllNotifications model_allNotifications) {
        allNotificationsViewHolder.userEmailTV.setText(model_allNotifications.getEmail());
        String actionOfUser = model_allNotifications.getAction();
        String type = model_allNotifications.getType();
        String finalStatus = "Statünüze" + actionOfUser;
        allNotificationsViewHolder.userActionTV.setText(finalStatus);
        objectFirebaseFirestore.collection("UserProfileData")
                .document(model_allNotifications.getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String linkOfProfilePic = documentSnapshot.getString("profileimageurl");
                        Glide.with(allNotificationsViewHolder.userProfilePicIV.getContext())
                                .load(linkOfProfilePic).into(allNotificationsViewHolder.userProfilePicIV);
                    }
                });
    }

    @NonNull
    @Override
    public AllNotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AllNotificationsViewHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.model_all_notifications, parent, false));
    }

    public class AllNotificationsViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfilePicIV;
        TextView userEmailTV, userActionTV;

        public AllNotificationsViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfilePicIV = itemView.findViewById(R.id.model_allnotifications_profilePicIV);
            userEmailTV = itemView.findViewById(R.id.model_allnotifications_userEmail);
            userActionTV = itemView.findViewById(R.id.model_allnotifications_action);
        }
    }
}
