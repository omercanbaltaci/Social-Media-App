package com.example.socialmediaapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.socialmediaapp.R;

public class LoginPage extends AppCompatActivity {
    private RelativeLayout objectRelativeLayout;
    private EditText loginPageEmailET, loginPagePasswordET;
    private Button loginPageLogInBtn, loginPageGoToRegisterBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        attachJavaObjectToXML();
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

    private void moveToRegisterPage() {
        try {
            startActivity(new Intent(this, RegisterPage.class));
        } catch (Exception e) {
            Toast.makeText(this, "Giriş Sayfası:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}