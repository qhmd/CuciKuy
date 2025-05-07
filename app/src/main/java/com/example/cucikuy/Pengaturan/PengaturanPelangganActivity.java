package com.example.cucikuy.Pengaturan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cucikuy.Kontak.EditKontakActivity;
import com.example.cucikuy.Kontak.KontakAdapter;
import com.example.cucikuy.Kontak.KontakData;
import com.example.cucikuy.Kontak.KontakItem;
import com.example.cucikuy.Kontak.TambahKontakAktivitas;
import com.example.cucikuy.Order.TambahOrderanActivity;
import com.example.cucikuy.R;

import java.util.ArrayList;
import java.util.List;

public class PengaturanPelangganActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private KontakAdapter kontakAdapter;
    private List<KontakItem> kontakItemList;
    private Button btn_add_contact;
    private ImageView arrow_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_pelanggan);
        overridePendingTransition(R.anim.fade_in, 0);
        arrow_back = findViewById(R.id.arrow_back);
        btn_add_contact = findViewById(R.id.tambah_kontak);
        recyclerView = findViewById(R.id.recyclerViewKontak);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setHasFixedSize(true);

        // Inisialisasi list dulu
        kontakItemList = new ArrayList<>();

        // Baru bikin adapter setelah list terisi
        kontakAdapter = new KontakAdapter(kontakItemList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(kontakAdapter);

        arrow_back.setOnClickListener(v -> {
            finish();
        });

        // Panggil FirestoreDataLoader untuk mengambil data kontak
        KontakData dataLoader = new KontakData();
        dataLoader.loadKontakData(new KontakData.DataCallback() {
            @Override
            public void onDataLoaded(List<KontakItem> kontakItemListFromFirestore) {
                kontakItemList.clear();
                kontakItemList.addAll(kontakItemListFromFirestore);
                kontakAdapter.notifyDataSetChanged();
            }
            @Override
            public void onError(String error) {
                Log.e("Firebase", error);
            }
        });
        kontakAdapter.setOnItemClickListener(item -> {
            new AlertDialog.Builder(PengaturanPelangganActivity.this)
                    .setTitle("Pilihan")
                    .setMessage("Apa yang ingin Anda lakukan?")
                    .setPositiveButton("Edit", (dialog, which) -> {
                        // Intent ke halaman edit
                        Intent intent = new Intent(PengaturanPelangganActivity.this, EditKontakActivity.class);
                        intent.putExtra("no_telpon", item.getNoTelpon()); // Ganti dengan no_telpon
                        startActivity(intent);
                        finish();
                    })
                    .setNegativeButton("Hapus", (dialog, which) -> {
                        // Fungsi hapus kontak
                        hapusKontak(item);
                    })
                    .setNeutralButton("Batal", null)
                    .show();
        });


//        kontakAdapter.setOnItemClickListener(this::showDurasiDialog);

        btn_add_contact.setOnClickListener(v -> {
            startActivity(new Intent(PengaturanPelangganActivity.this, TambahKontakAktivitas.class));
            finish();
        });

    }
    private void hapusKontak(KontakItem item) {
        KontakData dataLoader = new KontakData();
        dataLoader.hapusKontak(item.getNoTelpon(), new KontakData.DeleteCallback() {
            @Override
            public void onDeleteSuccess() {
                kontakItemList.remove(item);
                kontakAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDeleteFailure(String error) {
                Log.e("HapusKontak", "Gagal menghapus: " + error);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, 0);
    }
}
