package com.example.socialmediaapp.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmediaapp.R;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterPage extends AppCompatActivity {
    private CircleImageView profileIV;
    private EditText userNameET, userEmailET, userPasswordET, userConfirmPasswordET;
    private Button registerBtn;
    private RadioGroup objectRadioGroup;
    private TextView dateTV;

    private Uri profileImageURL;
    private static int REQUEST_CODE = 1;
    private DatePickerDialog.OnDateSetListener objectOnDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        attachJavaToXMLObjects();
    }

    private void chooseDOB() {
        try {
            Calendar objectCalendar = Calendar.getInstance();
            int year = objectCalendar.get(Calendar.YEAR);
            int month = objectCalendar.get(Calendar.MONTH);
            int day = objectCalendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog objectDatePickerDialog = new DatePickerDialog(this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    objectOnDateSetListener, year, month, day);

            objectDatePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            objectDatePickerDialog.show();

            objectOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    dateTV.setText(dayOfMonth + "-" + month + "-" + year);
                }
            };
        }
        catch (Exception e) {
            Toast.makeText(this, "RegisterPage:" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        }
        catch (Exception e) {
            Toast.makeText(this, "RegisterPage:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseImageFromMobileGallery() {
        try {
            openMobileGallery();
        }
        catch (Exception e) {
            Toast.makeText(this, "RegisterPage:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openMobileGallery() {
        try {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");

            startActivityForResult(galleryIntent, REQUEST_CODE);
        }
        catch (Exception e) {
            Toast.makeText(this, "RegisterPage:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData() != null && data != null) {
            profileImageURL = data.getData();
            profileIV.setImageURI(profileImageURL);
        }
        else {
            Toast.makeText(this, "Fotoğraf seçilmedi!", Toast.LENGTH_SHORT).show();
        }
    }
}