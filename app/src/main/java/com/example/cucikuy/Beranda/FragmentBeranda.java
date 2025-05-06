package com.example.cucikuy.Beranda;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cucikuy.Kontak.TambahKontakAktivitas;
import com.example.cucikuy.Order.TambahOrderanActivity;
import com.example.cucikuy.R;


public class FragmentBeranda extends Fragment {
    LinearLayout tambahPelanggan,tambahOrder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);

        tambahPelanggan = view.findViewById(R.id.tambah_pelanggan);
        tambahOrder = view.findViewById(R.id.tambah_order);

        tambahPelanggan.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TambahKontakAktivitas.class);
            startActivity(intent);
        });

        tambahOrder.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TambahOrderanActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
