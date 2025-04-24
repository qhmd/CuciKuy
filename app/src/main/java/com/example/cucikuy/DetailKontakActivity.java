package com.example.cucikuy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DetailKontakActivity extends AppCompatActivity {
    private TextView tvNama, tvNoHp, tvAlamat;
    private Button btn_tambah_order;
    private List<LayananItem> layananList;
    private LayananPilihAdapter adapter; // [✔️ Tambahan agar adapter bisa diakses di luar callback Firestore]
    private String totalHarga = ""; // [✔️ Tambahan untuk menyimpan total harga secara global]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_orderan);

        // Ambil data dari Intent
        String durasi = getIntent().getStringExtra("durasiNama");
        String namaKontak = getIntent().getStringExtra("nama");
        String noHp = getIntent().getStringExtra("nomor");

        // Set TextView
        tvNama = findViewById(R.id.tv_nama_kontak);
        tvNoHp = findViewById(R.id.tv_no_hp);
        tvAlamat = findViewById(R.id.tv_alamat);
        TextView tvTotalHarga = findViewById(R.id.tv_total_harga); // [✔️ Menyimpan referensi TextView total harga]
        btn_tambah_order = findViewById(R.id.btn_add_orderan);

        tvNama.setText(namaKontak);
        tvNoHp.setText("+ " + noHp);

        // Setup RecyclerView
        RecyclerView rvLayanan = findViewById(R.id.rv_layanan_detail_orderan);
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
                    layananList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        String namaLayanan = doc.getId();
                        String harga = doc.getString("harga");
                        String durasiLayanan = doc.getString("durasi");
                        layananList.add(new LayananItem(
                                namaLayanan,
                                harga,
                                durasiLayanan,
                                R.drawable.baseline_dry_cleaning_24
                        ));
                    }

                    // [✔️ Simpan adapter sebagai global]
                    adapter = new LayananPilihAdapter(layananList);

                    // [✔️ Set listener untuk update total harga]
                    adapter.setOnTotalChangeListener(totalFormatted -> {
                        tvTotalHarga.setText("Total: " + totalFormatted);
                        totalHarga = totalFormatted; // [✔️ Simpan total harga di variabel global untuk dikirim]
                    });
                    rvLayanan.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Log.e("DetailKontakActivity", "Gagal mengambil data layanan: " + e.getMessage());
                });


        btn_tambah_order.setOnClickListener(v -> {
            if (adapter != null) { // [✔️ Pastikan adapter tidak null]
                List<LayananItem> selectedLayanan = adapter.getSelectedLayanan();
                Intent intent = new Intent(DetailKontakActivity.this, DetailOrderanActivity.class);

                intent.putExtra("totalHarga", totalHarga); // [✔️ Kirim total harga]
                intent.putExtra("nama", namaKontak);
                intent.putExtra("noHp", noHp);
                intent.putExtra("namaDurasi", durasi);

                intent.putExtra("selectedLayanan", (Serializable) selectedLayanan); // [✔️ Kirim layanan yang dipilih]
                for (LayananItem item : selectedLayanan) {
                    Log.i("apadipilih",
                            "Nama: " + item.getNama() +
                                    ", Harga: " + item.getHarga() +
                                    ", Durasi: " + item.getDurasi() +
                                    ", Total Harga: " + item.getTotalHarga() +
                                    ", JumlahKg: " + item.getJumlahKg());
                }
                Log.i("apadipilih", totalHarga);

                startActivity(intent);
            }
        });
    }
}
