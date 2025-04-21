package com.example.cucikuy;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DetailKontakActivity extends AppCompatActivity {
    private TextView tvNama, tvNoHp, tvAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kontak);

        // Ambil data dari Intent
        String durasi = getIntent().getStringExtra("durasiNama");
        String namaKontak = getIntent().getStringExtra("nama");
        String noHp = getIntent().getStringExtra("nomor");
        String alamat = getIntent().getStringExtra("alamat");

        // Set TextView
        tvNama = findViewById(R.id.tv_nama_kontak);
        tvNoHp = findViewById(R.id.tv_no_hp);
        tvAlamat = findViewById(R.id.tv_alamat);

        tvNama.setText(namaKontak);
        tvNoHp.setText(noHp);
        tvAlamat.setText(alamat);

        // Setup RecyclerView
        RecyclerView rvLayanan = findViewById(R.id.rv_layanan_detail);
        rvLayanan.setLayoutManager(new LinearLayoutManager(this));

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("durasi")
                .document(durasi)
                .collection("layanan")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<LayananItem> layananList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String namaLayanan = doc.getId();
                        String harga = doc.getString("harga");
                        String durasiLayanan = doc.getString("durasi");
                        Log.d("LayananDebug", "Document ID: " + doc.getId());
                        Log.d("LayananDebug", "Field 'layanan': " + doc.getString("durasi"));
                        layananList.add(new LayananItem(
                                namaLayanan,
                                harga,
                                durasiLayanan,
                                R.drawable.baseline_dry_cleaning_24
                        ));
                    }

                    LayananPilihAdapter adapter = new LayananPilihAdapter(layananList);
                    rvLayanan.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Log.e("DetailKontakActivity", "Gagal mengambil data layanan: " + e.getMessage());
                });
    }
}
