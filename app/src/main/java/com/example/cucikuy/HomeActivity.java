package com.example.cucikuy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_fragment);
        setupBottomNav();
        loadFragment(new FragmentBeranda());
    }
    private void setupBottomNav() {
        findViewById(R.id.homeOption).setOnClickListener(v -> {
            loadFragment(new FragmentBeranda());
        });
        findViewById(R.id.orderOption).setOnClickListener(v -> {
            loadFragment(new FragmentOrder());
        });
        findViewById(R.id.reportOption).setOnClickListener(v -> {
            loadFragment(new FragmentReport());
        });
        findViewById(R.id.settingOption).setOnClickListener(v -> {
            loadFragment(new FragmentSetting());
        });
    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

}