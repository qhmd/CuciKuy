package com.example.cucikuy.Order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cucikuy.R;
import com.example.cucikuy.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class FragmentOrder extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private ImageView tambah_order_btn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_order, container, false);

        // Button tambah order
        tambah_order_btn = view.findViewById(R.id.tambah_order);
        tambah_order_btn.setOnClickListener(v -> {
            Log.d("tambahorder", "Di klik");
            startActivity(new Intent(requireContext(), TambahOrderanActivity.class));
        });

        // Inisialisasi TabLayout, ViewPager, dan Adapter
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);

        // Sambungkan TabLayout dengan ViewPager
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Antrian");
                            break;
                        case 1:
                            tab.setText("Siap Ambil");
                            break;
                        case 2:
                            tab.setText("Belum Bayar");
                            break;
                    }
                }).attach();

        return view;
    }
}
