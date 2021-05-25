package com.example.socialmediaapp.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.socialmediaapp.Activities.MainContentPage;
import com.example.socialmediaapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    public interface GetBioInfo {
        public void getBioInfo(String bio);
    }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // Class
    private View parent;
    private Typeface objectTypeface;
    private String currentLoggedInUser;
    private Dialog userBioDialog;
    private String extractedBio;

    // Firebase
    private FirebaseFirestore objectFirebaseFirestore;
    private FirebaseAuth objectFirebaseAuth;
    private DocumentReference objectDocumentReference;

    // XML
    private CircleImageView profilePicIV;
    private ImageView backgroundPicIV;
    private TextView userNameTV, userEmailTV, userEmotionsTV, textStatusCountTV, imagesStatusCountTV, genderTV, userCityTV, userCountryTV, userEmailCardTV;
    private Button goBackBtn, bioBtn;
    private TextView crossTV, bioTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_profile, container, false);
        setCustomFont();
        initiliazeObjects();
        loadProfileData(new GetBioInfo() {
            @Override
            public void getBioInfo(String bio) {
                extractedBio = bio;
            }
        });

        userBioDialog = new Dialog(getContext());
        userBioDialog.setContentView(R.layout.user_bio_layout);

        bioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userBioDialog.show();
                bioTV.setText(extractedBio);
            }
        });

        return parent;
    }

    private void loadProfileData(GetBioInfo objectGetBioInfo) {
        try {
            if (objectFirebaseAuth != null) {
                currentLoggedInUser = objectFirebaseAuth.getCurrentUser().getEmail();

                objectDocumentReference = objectFirebaseFirestore.collection("UserProfileData")
                        .document(currentLoggedInUser);
                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String linkOfProfileURL = documentSnapshot.getString("profileimageurl");
                        Glide.with(getContext()).load(linkOfProfileURL).into(profilePicIV);

                        objectGetBioInfo.getBioInfo(documentSnapshot.getString("userbio"));

                        userEmailCardTV.setText(currentLoggedInUser);
                        userEmailTV.setText(currentLoggedInUser);
                        userNameTV.setText(documentSnapshot.getString("username"));
                        userNameTV.setAllCaps(true);
                        userNameTV.setTypeface(objectTypeface);

                        long textStatusCount = documentSnapshot.getLong("nooftextstatus");
                        long imageStatusCount = documentSnapshot.getLong("noofimagestatus");
                        long emotions = textStatusCount + imageStatusCount;

                        String userBio = documentSnapshot.getString("userbio");

                        if (emotions < 10) userEmotionsTV.setText("0" + Long.toString(emotions));
                        else userEmotionsTV.setText(Long.toString(emotions));
                        if (textStatusCount < 10) textStatusCountTV.setText("0" + Long.toString(textStatusCount));
                        else textStatusCountTV.setText(Long.toString(textStatusCount));
                        if (imageStatusCount < 10) imagesStatusCountTV.setText("0" + Long.toString(imageStatusCount));
                        else imagesStatusCountTV.setText(Long.toString(imageStatusCount));

                        genderTV.setText(documentSnapshot.getString("gender"));
                        userCityTV.setText(documentSnapshot.getString("usercity"));
                        userCountryTV.setText(documentSnapshot.getString("usercountry"));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else Toast.makeText(getContext(), "Giriş yapmış bir kullanıcı yok", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setCustomFont() {
        objectTypeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Inter-Regular.ttf");
    }

    private void initiliazeObjects() {
        try {
            objectFirebaseAuth = FirebaseAuth.getInstance();
            objectFirebaseFirestore = FirebaseFirestore.getInstance();

            profilePicIV = parent.findViewById(R.id.profileFragment_miniProfilePicIV);
            userNameTV = parent.findViewById(R.id.profileFragment_userName);
            userEmailTV = parent.findViewById(R.id.profileFragment_userEmail);
            userEmotionsTV = parent.findViewById(R.id.profileFragment_emotionsTV);
            textStatusCountTV = parent.findViewById(R.id.profileFragment_textStatusTV);
            imagesStatusCountTV = parent.findViewById(R.id.profileFragment_imageStatusTV);
            genderTV = parent.findViewById(R.id.profileFragment_genderCardTV);
            userCityTV = parent.findViewById(R.id.profileFragment_addressCardTV);
            userCountryTV = parent.findViewById(R.id.profileFragment_countryCardTV);
            userEmailCardTV = parent.findViewById(R.id.profileFragment_emailCardTV);
            goBackBtn = parent.findViewById(R.id.profileFragment_goBackBtn);
            bioBtn = parent.findViewById(R.id.profileFragment_bioBtn);

            goBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), MainContentPage.class));
                }
            });

            crossTV = userBioDialog.findViewById(R.id.userBio_crossTV);
            crossTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userBioDialog.dismiss();
                }
            });

            bioTV = userBioDialog.findViewById(R.id.userBio_bioTV);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}