package com.example.socialmediaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.socialmediaapp.Activities.LoginPage;

public class MainActivity extends AppCompatActivity {

    // XML
    Button moveToLoginPageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        attachJavaObjectToXML();

        moveToLoginPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLoginPage();
            }
        });
    }

    private void attachJavaObjectToXML() {
        try {
            moveToLoginPageBtn = findViewById(R.id.mainActivity_MoveBtn);
        } catch (Exception e) {
            Toast.makeText(this, "MainAction:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void goToLoginPage() {
        try {
            startActivity(new Intent(this, LoginPage.class));
        } catch (Exception e) {
            Toast.makeText(this, "MainActivity:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}