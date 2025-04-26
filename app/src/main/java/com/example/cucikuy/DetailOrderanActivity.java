package com.example.cucikuy;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailOrderanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_orderan);
        String nama = getIntent().getStringExtra("nama");
        String totalHarga = getIntent().getStringExtra("totalHarga");
        String noHp = getIntent().getStringExtra("noHp");

        TextView tvNama = findViewById(R.id.tv_nama_kontak);
        TextView tvTotalHarga = findViewById(R.id.total_harga);
        TextView tvNoHp = findViewById(R.id.tv_no_hp);

        tvNama.setText(nama);
        tvTotalHarga.setText(totalHarga);
        tvNoHp.setText(noHp);
        RecyclerView recyclerView = findViewById(R.id.rv_layanan_dipilih);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<LayananItem> selectedLayanan = (ArrayList<LayananItem>) getIntent().getSerializableExtra("selectedLayanan");
        if (selectedLayanan != null) {
            LayananOrderAdapter adapter = new LayananOrderAdapter(selectedLayanan);
            recyclerView.setAdapter(adapter);
        }
    }
}

