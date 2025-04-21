package com.example.cucikuy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class PengaturanLayananActivity extends AppCompatActivity {
    // Deklarasi RecyclerView untuk menampilkan layanan
    RecyclerView rvLayanan;
    // Deklarasi TabLayout untuk menampilkan nama durasi
    TabLayout tabLayoutDurasi;
    // Deklarasi tombol untuk menambah layanan
    ImageView btn_tambah_layanan;
    // List untuk menyimpan nama durasi
    List<String> durasiNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_layanan);
        overridePendingTransition(R.anim.fade_in, 0);

        // Inisialisasi tampilan elemen UI
        btn_tambah_layanan = findViewById(R.id.tambah_layanan);
        tabLayoutDurasi = findViewById(R.id.tab_layout_durasi);
        rvLayanan = findViewById(R.id.rv_layanan);
        rvLayanan.setLayoutManager(new LinearLayoutManager(this)); // Layout untuk RecyclerView

        // Set listener untuk tombol tambah layanan
        btn_tambah_layanan.setOnClickListener(v -> {
            if (!durasiNames.isEmpty()) {
                // Jika durasiNames tidak kosong, lanjutkan ke halaman TambahLayanan
                Intent intent = new Intent(PengaturanLayananActivity.this, TambahLayanan.class);
                intent.putStringArrayListExtra("durasiList", new ArrayList<>(durasiNames));
                startActivity(intent);
            } else {
                // Jika data durasi kosong, tampilkan pesan kesalahan
                Toast.makeText(this, "Data durasi belum tersedia!", Toast.LENGTH_SHORT).show();
            }
        });

        // Ambil data durasi dari FirebaseFirestore
        DurasiData durasiData = new DurasiData();
        durasiData.loadDurasiData(new DurasiData.DataCallback() {
            @Override
            public void onDataLoaded(List<DurasiItem> durasiItemList) {
                // Bersihkan list durasiNames
                durasiNames.clear();
                tabLayoutDurasi.removeAllTabs(); // Hapus tab yang sudah ada

                // Menambahkan tab untuk setiap nama durasi
                for (DurasiItem item : durasiItemList) {
                    String namaDurasi = item.getNameDurasi();
                    durasiNames.add(namaDurasi);
                    tabLayoutDurasi.addTab(tabLayoutDurasi.newTab().setText(namaDurasi)); // Tambahkan tab baru
                }

                // Jika ada durasi, otomatis pilih tab pertama dan tampilkan layanan
                if (!durasiItemList.isEmpty()) {
                    tabLayoutDurasi.getTabAt(0).select(); // Pilih tab pertama
                    loadLayananForDurasi(durasiItemList.get(0).getNameDurasi()); // Tampilkan layanan dari durasi pertama
                }

                // Set listener ketika tab dipilih
                tabLayoutDurasi.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        // Ketika tab dipilih, ambil nama durasi dan load layanan yang sesuai
                        String selectedDurasi = tab.getText().toString();
                        loadLayananForDurasi(selectedDurasi);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {}

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {}
                });
            }

            @Override
            public void onError(String error) {
                // Jika gagal mengambil data durasi
                Log.e("DurasiDialog", "Gagal ambil durasi: " + error);
            }
        });
    }

    // Fungsi untuk memuat layanan berdasarkan nama durasi yang dipilih
    private void loadLayananForDurasi(String durasiName) {
        // Ambil user ID dari Firebase Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Ambil data layanan dari Firestore berdasarkan nama durasi
        db.collection("users")
                .document(userId)
                .collection("durasi")
                .document(durasiName)
                .collection("layanan")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<LayananItem> layananList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        // Ambil nama layanan dan harga dari setiap document
                        String namaLayanan = doc.getString("nama_layanan");
                        String harga = doc.getString("harga");
                        String durasi = doc.getString("durasi");
                        // Tambahkan layanan ke list
                        layananList.add(new LayananItem(namaLayanan, harga, durasi, R.drawable.baseline_dry_cleaning_24 ));
                    }

                    // Pasang adapter ke RecyclerView untuk menampilkan layanan
                    LayananAdapter adapter = new LayananAdapter(layananList);
                    rvLayanan.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    // Jika gagal mengambil layanan dari Firestore
                    Log.e("LayananError", "Gagal ambil layanan: " + e.getMessage());
                });
    }

    // Fungsi untuk handle tombol back (untuk animasi transisi)
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, 0);
    }
}