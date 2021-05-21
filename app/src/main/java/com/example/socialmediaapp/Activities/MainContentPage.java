package com.example.socialmediaapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.DrawableWrapper;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.Fragments.Favorites;
import com.example.socialmediaapp.Fragments.Images;
import com.example.socialmediaapp.Fragments.Statuses;
import com.example.socialmediaapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainContentPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // XML
    private Toolbar objectToolbar;
    private NavigationView objectNavigationView;
    private DrawerLayout objectDrawerLayout;
    private ImageView header_backgroundProfile;
    private CircleImageView header_profilePic;
    private TextView header_userName, header_userEmail;
    private ProgressBar header_progressBar;
    private BottomNavigationView objectBottomNavigationView;
    private FloatingActionButton objectFloatingActionButton;
    private TextView notificationTV;

    // Firebase
    private FirebaseAuth objectFirebaseAuth;
    private FirebaseFirestore objectFirebaseFirestore;
    private DocumentReference objectDocumentReference;
    private CollectionReference objectCollectionReference;
    private FirebaseFirestore notificationsFirebaseFirestore;

    // Class
    private String currentUserEmail;

    // Fragment
    private Favorites objectFavorites;
    private Images objectImages;
    private Statuses objectStatuses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_content_page);

        objectFirebaseAuth = FirebaseAuth.getInstance();
        objectFirebaseFirestore = FirebaseFirestore.getInstance();

        notificationsFirebaseFirestore = FirebaseFirestore.getInstance();
        objectCollectionReference = notificationsFirebaseFirestore.collection("UserProfileData")
                .document(objectFirebaseAuth.getCurrentUser().getEmail().toString())
                .collection("Notifications");

        attachJavaObjectToXMLObject();
        setUpDrawerMenu();
        getCurrentUserDetails();
        objectNavigationView.setNavigationItemSelectedListener(this);

        objectStatuses = new Statuses();
        objectFavorites = new Favorites();
        objectImages = new Images();
        changeFragment(objectStatuses);
        objectBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                try {
                    switch (item.getItemId()) {
                        case R.id.item_textThoughts:
                            changeFragment(objectStatuses);
                            return true;
                        case R.id.item_imageThoughts:
                            changeFragment(objectImages);
                            return true;
                        case R.id.item_favoriteThoughts:
                            changeFragment(objectFavorites);
                            return true;
                        default:
                            return false;
                    }
                } catch (Exception e) {
                    Toast.makeText(MainContentPage.this, "İçerik Sayfası" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        objectFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainContentPage.this, AddThoughtsPage.class));
            }
        });
    }

    private void changeFragment(Fragment objectFragment) {
        try {
            FragmentTransaction objectFragmentTransaction = getSupportFragmentManager().beginTransaction();
            objectFragmentTransaction.replace(R.id.container, objectFragment);
            objectFragmentTransaction.commit();
        } catch (Exception e) {
            Toast.makeText(this, "İçerik Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void attachJavaObjectToXMLObject() {
        try {
            objectToolbar = findViewById(R.id.toolBar);
            objectDrawerLayout = findViewById(R.id.drawerLayout);
            objectNavigationView = findViewById(R.id.navigationView);
            View headerXMLFile = objectNavigationView.getHeaderView(0);
            header_backgroundProfile = headerXMLFile.findViewById(R.id.header_profilePicBack);
            header_profilePic = headerXMLFile.findViewById(R.id.header_profilePic);
            header_userName = headerXMLFile.findViewById(R.id.header_userName);
            header_userEmail = headerXMLFile.findViewById(R.id.header_userEmail);
            header_progressBar = headerXMLFile.findViewById(R.id.header_progressBar);
            objectBottomNavigationView = findViewById(R.id.bottomNavigationViewBar);
            objectFloatingActionButton = findViewById(R.id.mainContentPage_addStatusFloatingButton);

            objectToolbar.inflateMenu(R.menu.main_content_menu_bar);
            objectToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.mainContentPage_item_notificationicon:
                            startActivity(new Intent(MainContentPage.this, AllNotifications.class));
                            return true;
                    }

                    return false;
                }
            });

            notificationTV = (TextView) MenuItemCompat.getActionView(objectNavigationView.getMenu().findItem(R.id.item_notifications));
        } catch (Exception e) {
            Toast.makeText(this, "İçerik Sayfası" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentUserDetails() {
        try {
            currentUserEmail = getCurrentLoggedInUser();
            if (currentUserEmail.equals("Giriş yapılmış bir hesap yok")) {
                Toast.makeText(this, "Giriş yapılmış bir hesap yok", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                header_progressBar.setVisibility(View.VISIBLE);
                objectDocumentReference = objectFirebaseFirestore.collection("UserProfileData")
                        .document(currentUserEmail);
                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        header_userName.setText(documentSnapshot.getString("username"));
                        header_userName.setAllCaps(true);
                        header_userEmail.setText(currentUserEmail);
                        String linkOfProfileImage = documentSnapshot.getString("profileimageurl");

                        Glide.with(MainContentPage.this).load(linkOfProfileImage).into(header_profilePic);
                        Glide.with(MainContentPage.this).load(linkOfProfileImage).into(header_backgroundProfile);

                        header_progressBar.setVisibility(View.INVISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainContentPage.this, "Kullanıcı verileri yüklenemedi:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(MainContentPage.this, "Kullanıcı verileri yüklenemedi:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentLoggedInUser() {
        try {
            if (objectFirebaseAuth != null) return objectFirebaseAuth.getCurrentUser().getEmail().toString();
            else return "Giriş yapılmış bir hesap yok";
        } catch (Exception e) {
            System.out.println("buraya geliyo mu acaba");
            Toast.makeText(this, "İçerik Sayfası" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void setUpDrawerMenu() {
        try {
            ActionBarDrawerToggle objectActionBarDrawerToggle = new ActionBarDrawerToggle(
                    this, objectDrawerLayout, objectToolbar, R.string.open, R.string.close);
            objectDrawerLayout.addDrawerListener(objectActionBarDrawerToggle);
            objectActionBarDrawerToggle.syncState();
        } catch (Exception e) {
            Toast.makeText(this, "İçerik Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openDrawer() {
        try {
            objectDrawerLayout.openDrawer(GravityCompat.START);
        } catch (Exception e) {
            Toast.makeText(this, "İçerik Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void closeDrawer() {
        try {
            objectDrawerLayout.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            Toast.makeText(this, "İçerik Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.item_profile:
                    Toast.makeText(this, "Profile gidiliyor", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item_notifications:
                    Toast.makeText(this, "Bildirimlere gidiliyor", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item_settings:
                    Toast.makeText(this, "Ayarlara gidiliyor", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item_favorite:
                    Toast.makeText(this, "Favorilere gidiliyor", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item_textstatus:
                    Toast.makeText(this, "Statülere gidiliyor", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.item_signOut:
                    signOutUser();
                    return true;
                default:
                    return false;
            }
        } catch (Exception e) {
            Toast.makeText(this, "İçerik Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void signOutUser() {
        try {
            if (objectFirebaseAuth != null) {
                objectFirebaseAuth.signOut();
                Toast.makeText(this, "Çıkış yapıldı", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginPage.class));
                closeDrawer();
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, "İçerik Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private ListenerRegistration objectListenerRegistration;
    @Override
    protected void onStart() {
        super.onStart();
        try {
            objectListenerRegistration = objectCollectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null) Toast.makeText(MainContentPage.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    else if (!value.isEmpty()) {
                        notificationTV.setGravity(Gravity.CENTER_VERTICAL);
                        notificationTV.setTypeface(null, Typeface.BOLD);
                        notificationTV.setTextColor(getResources().getColor(R.color.design_default_color_on_primary));

                        int size = value.size();
                        notificationTV.setText(Integer.toString(size) + " ");
                        Toast.makeText(MainContentPage.this, size + " adet bildiriminiz var", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        objectListenerRegistration.remove();
    }
}