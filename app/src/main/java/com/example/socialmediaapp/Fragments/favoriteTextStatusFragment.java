package com.example.socialmediaapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaapp.AdaptersClasses.GetFavoriteTextStatusAdapter;
import com.example.socialmediaapp.ModelClasses.Model_FavoriteTextStatus;
import com.example.socialmediaapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link favoriteTextStatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class favoriteTextStatusFragment extends Fragment {
    // XML


    // Class
    private View parent;
    private RecyclerView objectRecyclerView;
    GetFavoriteTextStatusAdapter objectGetFavoriteTextStatusAdapter;

    // Firebase
    FirebaseAuth objectFirebaseAuth;
    FirebaseFirestore objectFirebaseFirestore;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public favoriteTextStatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment favoriteTextStatusFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static favoriteTextStatusFragment newInstance(String param1, String param2) {
        favoriteTextStatusFragment fragment = new favoriteTextStatusFragment();
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
        parent = inflater.inflate(R.layout.fragment_favorite_text_status, container, false);
        initializeVariables();
        getStatusIntoRV();

        return parent;
    }

    @Override
    public void onStart() {
        super.onStart();
        objectGetFavoriteTextStatusAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        objectGetFavoriteTextStatusAdapter.stopListening();
    }

    private void getStatusIntoRV() {
        try {
            objectFirebaseAuth = FirebaseAuth.getInstance();
            if (objectFirebaseAuth != null) {
                String currentLoggedInUser = objectFirebaseAuth.getCurrentUser().getEmail();
                Query objectQuery = objectFirebaseFirestore.collection("UserFavorite")
                        .document(currentLoggedInUser).collection("FavoriteTextStatus")
                        .orderBy("currentdatetime", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<Model_FavoriteTextStatus> objectOptions =
                        new FirestoreRecyclerOptions.Builder<Model_FavoriteTextStatus>()
                                .setQuery(objectQuery, Model_FavoriteTextStatus.class).build();
                objectGetFavoriteTextStatusAdapter = new GetFavoriteTextStatusAdapter(objectOptions);
                objectRecyclerView.setAdapter(objectGetFavoriteTextStatusAdapter);
                objectRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            } else Toast.makeText(getContext(), "Giriş yapmış bir kullanıcı yok", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeVariables() {
        try {
            objectRecyclerView = parent.findViewById(R.id.favoriteTs_RV);
            objectFirebaseFirestore = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}