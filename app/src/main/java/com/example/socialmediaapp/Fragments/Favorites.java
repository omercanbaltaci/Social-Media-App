package com.example.socialmediaapp.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.socialmediaapp.AdaptersClasses.FavoriteStatusTabAdapter;
import com.example.socialmediaapp.R;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Favorites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favorites extends Fragment {
    // XML
    private View parent;
    private TabLayout objectTabLayout;
    private ViewPager objectViewPager;

    // Class
    FavoriteStatusTabAdapter objectFavoriteStatusTabAdapter;
    favoriteImageStatusFragment objectFavoriteImageStatusFragment;
    favoriteTextStatusFragment objectFavoriteTextStatusFragment;
    private int[] tabIcons = {
            R.drawable.ic_text_thoughts, R.drawable.ic_image_thoughts
    };

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Favorites() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Favorites.
     */
    // TODO: Rename and change types and number of parameters
    public static Favorites newInstance(String param1, String param2) {
        Favorites fragment = new Favorites();
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
        parent = inflater.inflate(R.layout.fragment_favorites, container, false);
        initializeVariables();
        addFragmentsToTabLayout();

        return parent;
    }

    private void setUpTabIcons() {
        try {
            objectTabLayout.getTabAt(0).setIcon(tabIcons[0]);
            objectTabLayout.getTabAt(1).setIcon(tabIcons[1]);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addFragmentsToTabLayout() {
        try {
            objectFavoriteStatusTabAdapter = new FavoriteStatusTabAdapter(getChildFragmentManager());
            objectFavoriteStatusTabAdapter.addFragment(objectFavoriteTextStatusFragment);
            objectFavoriteStatusTabAdapter.addFragment(objectFavoriteImageStatusFragment);
            objectViewPager.setAdapter(objectFavoriteStatusTabAdapter);
            objectTabLayout.setupWithViewPager(objectViewPager);
            objectViewPager.setSaveFromParentEnabled(false);
            setUpTabIcons();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initializeVariables() {
        try {
            objectFavoriteTextStatusFragment = new favoriteTextStatusFragment();
            objectFavoriteImageStatusFragment = new favoriteImageStatusFragment();
            objectTabLayout = parent.findViewById(R.id.favoriteFragment_tabLayout);
            objectViewPager = parent.findViewById(R.id.favoriteFragment_viewPager);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}