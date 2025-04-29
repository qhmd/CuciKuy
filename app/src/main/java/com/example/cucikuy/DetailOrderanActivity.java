package com.example.cucikuy;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
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
        String tanggalMasuk = getIntent().getStringExtra("tanggalMasuk");
        String estimasiSelesai = getIntent().getStringExtra("estiminasiSelesai");

        TextView tvNama = findViewById(R.id.tv_nama_kontak);
        TextView tvTotalHarga = findViewById(R.id.total_harga);
        TextView tvNoHp = findViewById(R.id.tv_no_hp);
        TextView tvTanggalMsuk = findViewById(R.id.tanggal_masuk);
        TextView tvEstSelesai = findViewById(R.id.est_seleai);


        tvNama.setText(nama);
        tvTotalHarga.setText(totalHarga);
        tvNoHp.setText(noHp);
        tvTanggalMsuk.setText(tanggalMasuk);
        tvEstSelesai.setText(estimasiSelesai);

        RecyclerView recyclerView = findViewById(R.id.rv_layanan_dipilih);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<LayananItem> selectedLayanan = (ArrayList<LayananItem>) getIntent().getSerializableExtra("selectedLayanan");
        if (selectedLayanan != null) {
            LayananOrderAdapter adapter = new LayananOrderAdapter(selectedLayanan);
            recyclerView.setAdapter(adapter);
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(DetailOrderanActivity.this, HomeActivity.class);
                intent.putExtra("navigateTo", "order");
                startActivity(intent);
                finish();
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}

