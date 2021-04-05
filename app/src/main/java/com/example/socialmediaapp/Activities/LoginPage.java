package com.example.socialmediaapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.socialmediaapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {
    // XML
    private RelativeLayout objectRelativeLayout;
    private EditText loginPageEmailET, loginPagePasswordET;
    private Button loginPageLogInBtn, loginPageGoToRegisterBtn;

    // Firebase
    private FirebaseAuth objectFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        attachJavaObjectToXML();

        objectFirebaseAuth = FirebaseAuth.getInstance();

        loginPageLogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });
    }

    private void attachJavaObjectToXML() {
        try {
            objectRelativeLayout = findViewById(R.id.loginPage_RL);
            loginPageEmailET = findViewById(R.id.loginPage_emailET);
            loginPagePasswordET = findViewById(R.id.loginPage_passwordET);
            loginPageLogInBtn = findViewById(R.id.loginPage_loginBtn);
            loginPageGoToRegisterBtn = findViewById(R.id.loginPage_moveToRegister);

            loginPageGoToRegisterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    moveToRegisterPage();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, "Giriş Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void signInUser() {
        try {
            if (!loginPageEmailET.getText().toString().isEmpty()
            && !loginPagePasswordET.getText().toString().isEmpty()) {
                if (objectFirebaseAuth.getCurrentUser() == null) {
                    objectFirebaseAuth.signInWithEmailAndPassword(
                            loginPageEmailET.getText().toString(), loginPagePasswordET.getText().toString()
                    ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(LoginPage.this, "Hoş geldin, " + loginPageEmailET.getText().toString(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginPage.this, MainContentPage.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginPage.this, "Giriş Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    objectFirebaseAuth.signOut();
                    Toast.makeText(this, "Başarılı şekilde çıkış yapıldı, tekrar giriş yapınız", Toast.LENGTH_SHORT).show();
                }
            } else Toast.makeText(this, "Lütfen gerekli alanları doldurunuz", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Giriş Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void moveToRegisterPage() {
        try {
            startActivity(new Intent(this, RegisterPage.class));
        } catch (Exception e) {
            Toast.makeText(this, "Giriş Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}