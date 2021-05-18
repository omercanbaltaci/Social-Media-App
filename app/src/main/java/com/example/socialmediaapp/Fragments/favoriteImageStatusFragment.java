package com.example.socialmediaapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.socialmediaapp.AdaptersClasses.GetFavoiteImageStatusAdapter;
import com.example.socialmediaapp.ModelClasses.Model_FavoriteImageStatus;
import com.example.socialmediaapp.ModelClasses.Model_FavoriteTextStatus;
import com.example.socialmediaapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link favoriteImageStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class favoriteImageStatusFragment extends Fragment {
    // XML
    private RecyclerView objectRecyclerView;

    // Class
    private View parent;
    private GetFavoiteImageStatusAdapter objectGetFavoriteImageStatusAdapter;

    // Firebase
    FirebaseFirestore objectFirebaseFirestore;
    FirebaseAuth objectFirebaseAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public favoriteImageStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment favoriteImageStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static favoriteImageStatusFragment newInstance(String param1, String param2) {
        favoriteImageStatusFragment fragment = new favoriteImageStatusFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        parent = inflater.inflate(R.layout.fragment_favorite_image_status, container, false);
        initializeJavaObjects();
        getStatusIntoRV();

        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        objectGetFavoriteImageStatusAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        objectGetFavoriteImageStatusAdapter.stopListening();
    }

    private void getStatusIntoRV() {
        try {
            objectFirebaseAuth = FirebaseAuth.getInstance();
            if (objectFirebaseAuth != null) {
                String currentLoggedInUser = objectFirebaseAuth.getCurrentUser().getEmail().toString();
                Query objectQuery = objectFirebaseFirestore.collection("UserFavorite")
                        .document(currentLoggedInUser)
                        .collection("FavoriteImageStatus")
                        .orderBy("currentdatetime", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Model_FavoriteImageStatus> objectOptions =
                        new FirestoreRecyclerOptions.Builder<Model_FavoriteImageStatus>()
                        .setQuery(objectQuery, Model_FavoriteImageStatus.class).build();
                objectGetFavoriteImageStatusAdapter = new GetFavoiteImageStatusAdapter(objectOptions);
                objectRecyclerView.setAdapter(objectGetFavoriteImageStatusAdapter);
                objectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            } else Toast.makeText(getContext(), "Giriş yapmış bir kullanıcı yok", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeJavaObjects() {
        try {
            objectRecyclerView = parent.findViewById(R.id.favorite_ImageStatus_RV);
            objectFirebaseFirestore = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}