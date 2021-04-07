package com.example.socialmediaapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterPage extends AppCompatActivity {
    // XML
    private CircleImageView profileIV;
    private EditText userNameET, userEmailET, userPasswordET, userConfirmPasswordET;
    private Button registerBtn;
    private RadioGroup objectRadioGroup;
    private RadioButton objectRadioButton;
    private TextView dateTV;

    // Class
    private Uri profileImageURL;
    private static int REQUEST_CODE = 1;
    private String finalPassword;
    private int radioID;
    private DatePickerDialog.OnDateSetListener objectOnDateSetListener;

    // Firebase
    private FirebaseFirestore objectFirebaseFirestore;
    private FirebaseAuth objectFirebaseAuth;
    private StorageReference objectStorageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        attachJavaToXMLObjects();

        objectFirebaseFirestore = FirebaseFirestore.getInstance();
        objectFirebaseAuth = FirebaseAuth.getInstance();
        objectStorageReference = FirebaseStorage.getInstance().getReference("ImageFolder");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserAccount();
            }
        });
    }

    private void createUserAccount() {
        try {
            if (objectFirebaseAuth.getCurrentUser() != null) objectFirebaseAuth.signOut();
            else if (objectFirebaseAuth.getCurrentUser() == null
            && !userNameET.getText().toString().isEmpty()
            && !userEmailET.getText().toString().isEmpty()
            && !userPasswordET.getText().toString().isEmpty()) {
                if (userPasswordET.getText().toString().equals(userConfirmPasswordET.getText().toString())) {
                    Toast.makeText(this, "Kullanıcı kaydediliyor", Toast.LENGTH_SHORT).show();
                    finalPassword = userPasswordET.getText().toString();
                    objectFirebaseAuth.createUserWithEmailAndPassword(
                            userEmailET.getText().toString(), finalPassword
                    ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            uploadUserDataToFirebase();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterPage.this, "Üyelik oluşturulamadı:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else Toast.makeText(this, "Şifreler eşleşmiyor", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(this, "Lütfen kullanıcı verilerini kontrol ediniz", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Kayıt Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadUserDataToFirebase() {
        try {
            if (profileImageURL != null) {
                String imageName = userNameET.getText().toString() + "." + getExtension(profileImageURL);
                final StorageReference imageRef = objectStorageReference.child(imageName);

                Toast.makeText(this, "Kullanıcı profil resmi yükleniyor", Toast.LENGTH_SHORT).show();
                UploadTask objectUploadTask = imageRef.putFile(profileImageURL);
                objectUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) throw task.getException();
                        return imageRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterPage.this, "Kullanıcı verileri yükleniyor", Toast.LENGTH_SHORT).show();
                            Map<String, Object> objectMap = new HashMap<>();
                            objectMap.put("profileimageurl", task.getResult().toString());
                            objectMap.put("username", userNameET.getText().toString());
                            objectMap.put("useremail", userEmailET.getText().toString());
                            objectMap.put("dob", dateTV.getText().toString());
                            objectMap.put("userpassword", finalPassword);

                            radioID = objectRadioGroup.getCheckedRadioButtonId();
                            objectRadioButton = findViewById(radioID);

                            objectMap.put("noonemotions", 0);
                            objectMap.put("gender", objectRadioButton.getText().toString());
                            objectMap.put("noofimagestatus", 0);
                            objectMap.put("nooftextstatus", 0);
                            objectMap.put("usercity", "NA");
                            objectMap.put("usercountry", "NA");
                            objectMap.put("userbio", "NA");

                            objectFirebaseFirestore.collection("UserProfileData")
                                    .document(userEmailET.getText().toString())
                                    .set(objectMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegisterPage.this, "Kullanıcı oluşturuldu", Toast.LENGTH_SHORT).show();

                                            if (objectFirebaseAuth != null) objectFirebaseAuth.signOut();
                                            startActivity(new Intent(RegisterPage.this, LoginPage.class));
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterPage.this, "Kullanıcı oluşturulamadı:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else if (!task.isSuccessful())
                            Toast.makeText(RegisterPage.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else Toast.makeText(this, "Lütfen bir profil resmi seçin", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Kayıt Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private String getExtension(Uri uri) {
        try {
            ContentResolver objectContentResolver = getContentResolver();
            MimeTypeMap objectMimeTypeMap = MimeTypeMap.getSingleton();
            return objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(uri));
        } catch (Exception e) {
            Toast.makeText(this, "Kayıt Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void chooseDOB() {
        try {
            // Uygulamanın kurulu olduğu sistem local'i
            Locale locale = getResources().getConfiguration().locale;
            Locale.setDefault(locale);

            // Günün tarihi
            Calendar objectCalendar = Calendar.getInstance();
            int year = objectCalendar.get(Calendar.YEAR);
            int month = objectCalendar.get(Calendar.MONTH);
            int day = objectCalendar.get(Calendar.DAY_OF_MONTH);

            // Doğum tarihi girdisi için ekrana çıkarılacak dialog
            DatePickerDialog objectDatePickerDialog = new DatePickerDialog(this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    objectOnDateSetListener, year, month, day);
            objectDatePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            objectDatePickerDialog.show();

            // Girdi alındıktan sonra date textview'ına girilen değer verilir
            objectOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    dateTV.setText(dayOfMonth + "-" + month + "-" + year);
                }
            };
        } catch (Exception e) {
            Toast.makeText(this, "Kayıt Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void attachJavaToXMLObjects() {
        try {
            profileIV = findViewById(R.id.RegisterPage_userProfileIV);
            userNameET = findViewById(R.id.RegisterPage_userNameET);
            userEmailET = findViewById(R.id.RegisterPage_emailET);
            userPasswordET = findViewById(R.id.RegisterPage_passwordET);
            userConfirmPasswordET = findViewById(R.id.RegisterPage_confirmPasswordET);
            dateTV = findViewById(R.id.RegisterPage_userDOBTV);
            registerBtn = findViewById(R.id.RegisterPage_registerBtn);
            objectRadioGroup = findViewById(R.id.RegisterPage_radioGroup);

            profileIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseImageFromMobileGallery();
                }
            });
            dateTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chooseDOB();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "Kayıt Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseImageFromMobileGallery() {
        try {
            openMobileGallery();
        } catch (Exception e) {
            Toast.makeText(this, "Kayıt Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openMobileGallery() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(galleryIntent, REQUEST_CODE);
        } catch (Exception e) {
            Toast.makeText(this, "Kayıt Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null && data != null) {
            profileImageURL = data.getData();
            profileIV.setImageURI(profileImageURL);
        } else Toast.makeText(this, "Fotoğraf seçilmedi", Toast.LENGTH_SHORT).show();
    }
}