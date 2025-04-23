package com.example.cucikuy;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DetailOrderanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_orderan);

        TextView tvNama = findViewById(R.id.tv_nama_kontak);
        TextView tvNoHp = findViewById(R.id.tv_no_hp);
        TextView tvAlamat = findViewById(R.id.tv_alamat);

        tvNama.setText(getIntent().getStringExtra("nama"));
        tvNoHp.setText(getIntent().getStringExtra("noHp"));
        tvAlamat.setText(getIntent().getStringExtra("alamat"));

        RecyclerView rvLayananDipilih = findViewById(R.id.rv_layanan_dipilih);
        rvLayananDipilih.setLayoutManager(new LinearLayoutManager(this));

    }
}

