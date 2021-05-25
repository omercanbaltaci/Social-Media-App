package com.example.socialmediaapp.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmediaapp.AdaptersClasses.AllNotificationsAdapter;
import com.example.socialmediaapp.ModelClasses.Model_AllNotifications;
import com.example.socialmediaapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;

public class AllNotifications extends AppCompatActivity {
    private FirebaseFirestore objectFirebaseFirestore;
    private RecyclerView objectRecyclerView;
    private AllNotificationsAdapter objectAllNotificationsAdapter;
    private Toolbar objectToolbar;
    private FirebaseAuth objectFirebaseAuth;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notifications);

        objectFirebaseFirestore = FirebaseFirestore.getInstance();
        objectFirebaseAuth = FirebaseAuth.getInstance();

        attachJAVAToXML();
        getAllNotificationsIntoRV();
    }

    @Override
    protected void onStart() {
        super.onStart();
        objectAllNotificationsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        objectAllNotificationsAdapter.stopListening();
    }

    private void getAllNotificationsIntoRV() {
        try {
            if (objectFirebaseAuth != null) {
                String currentLoggedInUser = objectFirebaseAuth.getCurrentUser().getEmail();

                Query objectQuery = objectFirebaseFirestore.collection("UserProfileData")
                        .document(currentLoggedInUser).collection("Notifications");
                FirestoreRecyclerOptions<Model_AllNotifications> objectOptions =
                        new FirestoreRecyclerOptions.Builder<Model_AllNotifications>()
                        .setQuery(objectQuery, Model_AllNotifications.class).build();

                objectAllNotificationsAdapter = new AllNotificationsAdapter(objectOptions);
                objectRecyclerView.setAdapter(objectAllNotificationsAdapter);
                objectRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            } else Toast.makeText(this, "Giriş yapmış bir kullanıcı yok", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    ArrayList<String> objectStringArrayList = new ArrayList<>();
    private void clearAllNotifications() {
        try {
            if (objectFirebaseAuth != null) {
                String currentLoggedInUser = objectFirebaseAuth.getCurrentUser().getEmail();

                objectFirebaseFirestore.collection("UserProfileData")
                        .document(currentLoggedInUser)
                        .collection("Notifications")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot objectQueryDocumentSnapshot:task.getResult()) {
                                        objectStringArrayList.add(objectQueryDocumentSnapshot.getId());
                                        WriteBatch objectWriteBatch = objectFirebaseFirestore.batch();

                                        for (int count = 0; count < objectStringArrayList.size(); count++) {
                                            objectWriteBatch.delete(
                                                    objectFirebaseFirestore.collection("UserProfileData")
                                                    .document(currentLoggedInUser)
                                                    .collection("Notifications")
                                                    .document(objectStringArrayList.get(count))
                                            );
                                        }

                                        objectWriteBatch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) Toast.makeText(AllNotifications.this, "Bildirimler temizlendi", Toast.LENGTH_SHORT).show();
                                                else if (!task.isSuccessful()) Toast.makeText(AllNotifications.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }
                        });
            } else Toast.makeText(this, "Giriş yapmış bir kullanıcı yok", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void attachJAVAToXML() {
        try {
            objectToolbar = findViewById(R.id.allNotifications_toolBar);
            objectRecyclerView = findViewById(R.id.allNotifications_RV);

            objectToolbar.inflateMenu(R.menu.all_notifications_menu);
            objectToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.allNotifications_item_clear:
                            clearAllNotifications();
                            return true;
                        case R.id.allNotifications_item_goback:
                            startActivity(new Intent(AllNotifications.this, MainContentPage.class));
                            return true;
                    }

                    return false;
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}