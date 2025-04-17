package com.example.cucikuy;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class FragmentOrder extends Fragment {
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate layout untuk fragment parent
        View view = inflater.inflate(R.layout.activity_order, container, false);

        // Inisialisasi TabLayout dan ViewPager2
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        // Inisialisasi ViewPagerAdapter dan set adapter
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        // Menghubungkan TabLayout dan ViewPager2
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0: tab.setText("Antrian"); break;
                        case 1: tab.setText("Siap Ambil"); break;
                        case 2: tab.setText("Belum Bayar"); break;
                    }
                }).attach();

        return view; // Mengembalikan tampilan fragment
    }
}
