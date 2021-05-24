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
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    public interface GetUserInfo {
        public void getUserInfo(String urlOfPP, String userName);
    }

    // Class
    private View parent;
    private Uri newProfilePirURI;
    private static int REQUEST_CODE = 1;
    private boolean checkForChangeDp = false;
    private String extractedURI, extractedName;

    // Firebase
    private FirebaseFirestore objectFirebaseFirestore;
    private FirebaseAuth objectFirebaseAuth;
    private DocumentReference objectDocumentReference;
    private StorageReference objectStorageReference;
    private FirebaseStorage objectFirebaseStorage;

    // XML
    private CircleImageView profileIV;
    private Button updateUserInfoBtn, updateDpBtn;
    private EditText userNameET, userBioET, userCityET, userCountryET;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
        parent = inflater.inflate(R.layout.fragment_setting, container, false);
        initializeObjects();
        loadProfileInformationAtStart(new GetUserInfo() {
            @Override
            public void getUserInfo(String urlOfPP, String userName) {
                extractedName = userName;
                extractedURI = urlOfPP;
            }
        });
        updateDpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        updateUserInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });

        return parent;
    }

    private String getExtension(Uri uri) {
        ContentResolver objectContentResolver = getActivity().getContentResolver();
        MimeTypeMap objectMimeTypeMap = MimeTypeMap.getSingleton();

        return objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(uri));
    }

    private void updateUserInfo() {
        try {
            if (checkForChangeDp) {
                objectStorageReference = FirebaseStorage.getInstance().getReference("ImageFolder");

                if (newProfilePirURI != null) {
                    String imageName = extractedName + "." + getExtension(newProfilePirURI);
                    StorageReference imgRef = objectStorageReference.child(imageName);

                    UploadTask uploadTask = imgRef.putFile(newProfilePirURI);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (task.isSuccessful()) {
                                throw task.getException();
                            }

                            return imgRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Map<String, Object> objectMap = new HashMap<>();
                                objectMap.put("profileimageurl", task.getResult().toString());
                                objectMap.put("username", userNameET.getText().toString());
                                objectMap.put("userbio", userBioET.getText().toString());
                                objectMap.put("usercity", userCityET.getText().toString());
                                objectMap.put("usercountry", userCountryET.getText().toString());

                                objectFirebaseFirestore.collection("UserProfileData")
                                        .document(objectFirebaseAuth.getCurrentUser().getEmail())
                                        .update(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(getContext(), "Profile güncellendi", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getContext(), "Profil güncellenemedi", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else if (!task.isSuccessful())
                                Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(getContext(), "Lütfen bir resim seçin", Toast.LENGTH_SHORT).show();
            } else if (!checkForChangeDp) {
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("username", userNameET.getText().toString());
                objectMap.put("userbio", userBioET.getText().toString());
                objectMap.put("usercity", userCityET.getText().toString());
                objectMap.put("usercountry", userCountryET.getText().toString());

                objectFirebaseFirestore.collection("UserProfileData")
                        .document(objectFirebaseAuth.getCurrentUser().getEmail())
                        .update(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Profile güncellendi", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Profil güncellenemedi", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null && data != null) {
            newProfilePirURI = data.getData();
            profileIV.setImageURI(newProfilePirURI);
            checkForChangeDp = true;
        } else Toast.makeText(getContext(), "Resim seçilmedi", Toast.LENGTH_SHORT).show();
    }

    private void openGallery() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(galleryIntent, REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadProfileInformationAtStart(GetUserInfo objectGetUserInfo) {
        try {
            if (objectFirebaseAuth != null) {
                objectDocumentReference = objectFirebaseFirestore.collection("UserProfileData")
                        .document(objectFirebaseAuth.getCurrentUser().getEmail());
                objectDocumentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        userNameET.setText(documentSnapshot.getString("username"));
                        userCityET.setText(documentSnapshot.getString("userbio"));
                        userBioET.setText(documentSnapshot.getString("usercity"));
                        userCountryET.setText(documentSnapshot.getString("usercountry"));

                        Glide.with(getContext()).load(documentSnapshot.getString("profileimageurl"))
                                .into(profileIV);

                        objectGetUserInfo.getUserInfo(documentSnapshot.getString("profileimageurl")
                        ,documentSnapshot.getString("username"));
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

    private void initializeObjects() {
        try {
            objectFirebaseAuth = FirebaseAuth.getInstance();
            objectFirebaseFirestore = FirebaseFirestore.getInstance();
            objectFirebaseStorage = FirebaseStorage.getInstance();

            profileIV = parent.findViewById(R.id.settingFrag_profilePicIV);
            updateDpBtn = parent.findViewById(R.id.settingFrag_changeDpButton);
            updateUserInfoBtn = parent.findViewById(R.id.settingFrag_updateInfoBtn);
            userNameET = parent.findViewById(R.id.settingFrag_userNameET);
            userBioET = parent.findViewById(R.id.settingFrag_userBioET);
            userCityET = parent.findViewById(R.id.settingFrag_userCityET);
            userCountryET = parent.findViewById(R.id.settingFrag_userCountryET);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}