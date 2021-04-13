package com.example.socialmediaapp.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.socialmediaapp.Activities.MainContentPage;
import com.example.socialmediaapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addTextThoughtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addTextThoughtFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public interface GetURLInterface {
        void getProfileURL(String profileUrlValue);
    }

    // Class
    private View objectView;
    private Date currentDate;
    private SimpleDateFormat objectSimpleDateFormat;
    private String profileURL;

    // XML
    private EditText statusET;
    private Button publishStatusBtn, editStatusBtn, goBackBtn;
    private ProgressBar objectProgressBar;

    // Firebase
    private FirebaseAuth objectFirebaseAuth;
    private FirebaseFirestore objectFirebaseFirestore;
    private DocumentReference objectDocumentReference;

    public addTextThoughtFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addTextThoughtFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addTextThoughtFragment newInstance(String param1, String param2) {
        addTextThoughtFragment fragment = new addTextThoughtFragment();
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
        objectView = inflater.inflate(R.layout.fragment_add_text_thought, container, false);
        ConnectJavaViewsToXMLViews();
        publishStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStatus(new GetURLInterface() {
                    @Override
                    public void getProfileURL(String profileUrlValue) {
                        profileURL = profileUrlValue;
                        publishStatus();
                    }
                });
            }
        });
        return objectView;
    }

    private void publishStatus() {
        try {
            if (!statusET.getText().toString().isEmpty()) {
                String currentLoggedInUser = objectFirebaseAuth.getCurrentUser().getEmail();

                Map<String, Object> statusData = new HashMap<>();
                statusData.put("currentdatetime", getCurrentDate());
                statusData.put("useremail", currentLoggedInUser);
                statusData.put("profileurl", profileURL);
                statusData.put("status", statusET.getText().toString());
                statusData.put("noofhaha", 0);
                statusData.put("nooflove", 0);
                statusData.put("noofsad", 0);
                statusData.put("noofcomments", 0);
                statusData.put("currentflag", "none");
                objectFirebaseFirestore.collection("TextStatus")
                        .document(String.valueOf(System.currentTimeMillis()))
                        .set(statusData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), "Statü paylaşıldı", Toast.LENGTH_SHORT).show();
                                objectProgressBar.setVisibility(View.INVISIBLE);

                                objectDocumentReference = objectFirebaseFirestore.collection("UserProfileData")
                                        .document(currentLoggedInUser);
                                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        long noOfTextStatuses = documentSnapshot.getLong("nooftextstatus");
                                        noOfTextStatuses++;

                                        Map<String, Object> objectMap = new HashMap<>();
                                        objectMap.put("nooftextstatus", noOfTextStatuses);
                                        objectFirebaseFirestore.collection("UserProfileData")
                                                .document(currentLoggedInUser).update(objectMap);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Statü paylaşılamadı", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                publishStatusBtn.setEnabled(true);
                                getActivity().finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            } else Toast.makeText(getContext(), "Lütfen bir statü giriniz", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Statüler:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addStatus(final GetURLInterface objectGetURLInterface) {
        try {
            objectFirebaseAuth = FirebaseAuth.getInstance();
            objectFirebaseFirestore =FirebaseFirestore.getInstance();

            if (objectFirebaseAuth != null && !statusET.getText().toString().isEmpty()) {
                objectProgressBar.setVisibility(View.VISIBLE);
                publishStatusBtn.setEnabled(false);

                String currentUserEmail = objectFirebaseAuth.getCurrentUser().getEmail();
                objectDocumentReference = objectFirebaseFirestore.collection("UserProfileData")
                        .document(currentUserEmail);
                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        objectGetURLInterface.getProfileURL(documentSnapshot.getString("profileimageurl"));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Profil bilgisi alınamadı", Toast.LENGTH_SHORT).show();
                        objectProgressBar.setVisibility(View.INVISIBLE);
                        publishStatusBtn.setEnabled(true);
                    }
                });
            } else {
                Toast.makeText(getContext(), "Lütfen bir statü giriniz", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Statüler:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentDate() {
        try {
            currentDate = Calendar.getInstance().getTime();
            objectSimpleDateFormat = new SimpleDateFormat("HH:mm:ss dd-MMM-yyyy");
            return objectSimpleDateFormat.format(currentDate);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Statüler:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return "No date";
        }
    }

    private void ConnectJavaViewsToXMLViews() {
        try {
            statusET = objectView.findViewById(R.id.FragaddTextThought_textThoughtET);
            publishStatusBtn = objectView.findViewById(R.id.FragaddTextThought_publishThoughtBtn);
            editStatusBtn = objectView.findViewById(R.id.FragaddTextThought_editThoughtBtn);
            goBackBtn = objectView.findViewById(R.id.FragaddTextThought_goBackBtn);
            objectProgressBar = objectView.findViewById(R.id.FragaddTextThought_progressBar);

            editStatusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    statusET.setText("");
                }
            });
            goBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), MainContentPage.class));
                    getActivity().finish();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "Statüler:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}