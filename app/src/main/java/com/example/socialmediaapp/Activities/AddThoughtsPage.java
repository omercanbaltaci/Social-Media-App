package com.example.socialmediaapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.socialmediaapp.AdaptersClasses.AddThoughtsTabAdapter;
import com.example.socialmediaapp.Fragments.addImageThoughtFragment;
import com.example.socialmediaapp.Fragments.addTextThoughtFragment;
import com.example.socialmediaapp.R;
import com.google.android.material.tabs.TabLayout;

public class AddThoughtsPage extends AppCompatActivity {
    private AddThoughtsTabAdapter objectAddThoughtsTabAdapter;

    // XML
    private TabLayout objectTabLayout;
    private ViewPager objectViewPager;

    // Class
    private int [] tabIcons = {
            R.drawable.ic_text_thoughts, R.drawable.ic_image_thoughts
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_thoughts_page);

        addJavaObjectsToXMLObjects();
        objectAddThoughtsTabAdapter = new AddThoughtsTabAdapter(getSupportFragmentManager());

        objectAddThoughtsTabAdapter.addFragment(new addTextThoughtFragment(), "Statü");
        objectAddThoughtsTabAdapter.addFragment(new addImageThoughtFragment(), "Resim");

        objectViewPager.setAdapter(objectAddThoughtsTabAdapter);
        objectTabLayout.setupWithViewPager(objectViewPager);
        setUpIcons();
    }

    private void setUpIcons() {
        try {
            objectTabLayout.getTabAt(0).setIcon(tabIcons[0]);
            objectTabLayout.getTabAt(1).setIcon(tabIcons[1]);
        } catch (Exception e) {
            Toast.makeText(this, "Statü Oluştur:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addJavaObjectsToXMLObjects() {
        try {
            objectTabLayout = findViewById(R.id.AddThoughts_tabsLayout);
            objectViewPager = findViewById(R.id.AddThoughts_PageViewer);
        } catch (Exception e) {
            Toast.makeText(this, "Statü Oluştur:" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}