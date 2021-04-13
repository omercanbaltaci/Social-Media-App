package com.example.socialmediaapp.Fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.socialmediaapp.Activities.MainContentPage;
import com.example.socialmediaapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addImageThoughtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addImageThoughtFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addImageThoughtFragment() {
        // Required empty public constructor
    }

    // Class
    private View objectView;
    private int PreCode = 1000;
    private Uri selectedImageWithURI;
    private Date currentDate;
    private SimpleDateFormat objectSimpleDateFormat;
    private String currentLoggedInUserEmail;
    private String getCurrentLoggedInUserProfilePicUrl;

    // XML
    private ImageView statusImageView;
    private EditText statusET;
    private Button publishStatusBtn, goBackBtn;
    private ProgressBar objectProgressBar;

    // Firebase
    private FirebaseAuth objectFirebaseAuth;
    private FirebaseFirestore objectFirebaseFirestore;
    private DocumentReference objectDocumentReference;
    private StorageReference objectStorageReference;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addImageThoughtFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addImageThoughtFragment newInstance(String param1, String param2) {
        addImageThoughtFragment fragment = new addImageThoughtFragment();
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
        objectView = inflater.inflate(R.layout.fragment_add_image_thought, container, false);
        ConnectJavaViewsToXMLViews();
        objectStorageReference = FirebaseStorage.getInstance().getReference("ImageStatusFolder");
        statusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMobileGallery();
            }
        });
        publishStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishStatus();
            }
        });
        return objectView;
    }

    private void publishStatus() {
        try {
            if (selectedImageWithURI != null) {
                objectFirebaseAuth = FirebaseAuth.getInstance();
                objectFirebaseFirestore = FirebaseFirestore.getInstance();
                if (objectFirebaseAuth != null && !statusET.getText().toString().isEmpty()) {
                    objectProgressBar.setVisibility(View.VISIBLE);
                    publishStatusBtn.setEnabled(false);
                    currentLoggedInUserEmail = objectFirebaseAuth.getCurrentUser().getEmail();
                    objectDocumentReference = objectFirebaseFirestore.collection("UserProfileData")
                            .document(currentLoggedInUserEmail);
                    objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            getCurrentLoggedInUserProfilePicUrl = documentSnapshot.getString("profileimageurl");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Resim paylaşılamadı", Toast.LENGTH_SHORT).show();
                            objectProgressBar.setVisibility(View.INVISIBLE);
                            publishStatusBtn.setEnabled(true);
                        }
                    });
                    String statusImageToBeStored = System.currentTimeMillis() + "." + getExtension(selectedImageWithURI);
                    StorageReference imgRef = objectStorageReference.child(statusImageToBeStored);
                    UploadTask objectUploadTask = imgRef.putFile(selectedImageWithURI);
                    objectUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) throw task.getException();
                            return imgRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Map<String, Object> statusMap = new HashMap<>();
                                statusMap.put("currentdatetime", getCurrentDate());
                                statusMap.put("useremail", currentLoggedInUserEmail);
                                statusMap.put("profileurl", getCurrentLoggedInUserProfilePicUrl);
                                statusMap.put("status", statusET.getText().toString());
                                statusMap.put("noofhaha", 0);
                                statusMap.put("nooflove", 0);
                                statusMap.put("noofsad", 0);
                                statusMap.put("noofcomments", 0);
                                statusMap.put("currentflag", "none");
                                statusMap.put("statusimageurl", task.getResult().toString());
                                objectFirebaseFirestore.collection("ImageStatus")
                                        .document(String.valueOf(System.currentTimeMillis()))
                                        .set(statusMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                objectDocumentReference = objectFirebaseFirestore.collection("UserProfileData")
                                                        .document(currentLoggedInUserEmail);
                                                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                        long totalNumberOfImageStatus = documentSnapshot.getLong("noofimagestatus");
                                                        totalNumberOfImageStatus++;

                                                        Map<String, Object> updatedDataMap = new HashMap<>();
                                                        updatedDataMap.put("noofimagestatus", totalNumberOfImageStatus);
                                                        objectFirebaseFirestore.collection("UserProfileData")
                                                                .document(currentLoggedInUserEmail)
                                                                .update(updatedDataMap);

                                                        Toast.makeText(getContext(), "Statü güncellendi", Toast.LENGTH_SHORT).show();
                                                        objectProgressBar.setVisibility(View.INVISIBLE);
                                                        publishStatusBtn.setEnabled(true);
                                                        startActivity(new Intent(getContext(), MainContentPage.class));
                                                        getActivity().finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(getContext(), "Resim statüsü güncellenemedi", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Resim statüsü güncellenemedi", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Lütfen bir resim statüsü giriniz", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Lütfen bir resim seçiniz", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Resimler:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getExtension(Uri selectedImageWithURI) {
        try {
            ContentResolver objectContentResolver = getActivity().getContentResolver();
            MimeTypeMap objectMimeTypeMap = MimeTypeMap.getSingleton();
            return objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(selectedImageWithURI));
        } catch (Exception e) {
            Toast.makeText(getContext(), "Resimler:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return "No Extension";
        }
    }

    private String getCurrentDate() {
        try {
            currentDate = Calendar.getInstance().getTime();
            objectSimpleDateFormat = new SimpleDateFormat("HH:mm:ss dd-MMM-yyyy");
            return objectSimpleDateFormat.format(currentDate);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Resimler:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return "No date";
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.getData() != null) {
            selectedImageWithURI = data.getData();
            statusImageView.setImageURI(selectedImageWithURI);
        } else {
            Toast.makeText(getContext(), "Resim seçilmedi", Toast.LENGTH_SHORT).show();
        }
    }

    private void openMobileGallery() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(galleryIntent, PreCode);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Resimler:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void ConnectJavaViewsToXMLViews() {
        try {
            statusImageView = objectView.findViewById(R.id.Frag_AddImageThought_getImageIV);
            statusET = objectView.findViewById(R.id.Frag_AddImageThought_descriptionET);
            publishStatusBtn = objectView.findViewById(R.id.Frag_AddImageThought_publishThoughtBtn);
            goBackBtn = objectView.findViewById(R.id.Frag_AddImageThought_goBackBtn);
            objectProgressBar = objectView.findViewById(R.id.Frag_AddImageThought_progressBar);
            goBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getContext(), MainContentPage.class));
                    getActivity().finish();
                }
            });
        } catch (Exception e) {
            Toast.makeText(getContext(), "Resimler:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}