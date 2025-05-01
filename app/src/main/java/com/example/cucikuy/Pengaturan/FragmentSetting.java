package com.example.cucikuy.Pengaturan;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cucikuy.R;

import java.util.ArrayList;


public class FragmentSetting extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private RecyclerView recyclerView;
    private PengaturanAdapter pengaturanAdapter;
    private ArrayList<PengaturanItem> pengaturanItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting, container, false);
        recyclerView = view.findViewById(R.id.recyclerView); // pakai `view.findViewById`
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // pakai getContext()

        pengaturanItems = new ArrayList<>();
        pengaturanItems.addAll(PengaturanData.getDatas());
        Log.d("RecyclerView", "Data Size: " + pengaturanItems.size());
        pengaturanAdapter = new PengaturanAdapter(pengaturanItems);
        recyclerView.setAdapter(pengaturanAdapter);

        return view;
    }
}
