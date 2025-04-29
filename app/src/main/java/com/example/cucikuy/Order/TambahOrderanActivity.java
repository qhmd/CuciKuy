package com.example.cucikuy.Order;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cucikuy.Durasi.DurasiAdapater;
import com.example.cucikuy.Durasi.DurasiData;
import com.example.cucikuy.Durasi.DurasiItem;
import com.example.cucikuy.Kontak.KontakAdapter;
import com.example.cucikuy.Kontak.KontakData;
import com.example.cucikuy.Kontak.KontakItem;
import com.example.cucikuy.Kontak.TambahKontakAktivitas;
import com.example.cucikuy.R;

import java.util.ArrayList;
import java.util.List;

public class TambahOrderanActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private KontakAdapter kontakAdapter;
    private List<KontakItem> kontakItemList;
    private ImageView btn_add_contact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tambah_order);
        overridePendingTransition(R.anim.fade_in, 0);

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

        kontakAdapter.setOnItemClickListener(this::showDurasiDialog);

        btn_add_contact.setOnClickListener(v -> {
            startActivity(new Intent(TambahOrderanActivity.this, TambahKontakAktivitas.class));
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, 0);
        finish();
    }

    private void showDurasiDialog(KontakItem kontakItem) {
        DurasiData durasiData = new DurasiData();
        durasiData.loadDurasiData(new DurasiData.DataCallback() {
            @Override
            public void onDataLoaded(List<DurasiItem> durasiItemList) {
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_durasi, null);
                RecyclerView rvDurasi = dialogView.findViewById(R.id.recyclerDialogDurasi);
                rvDurasi.setLayoutManager(new LinearLayoutManager(TambahOrderanActivity.this));
                DurasiAdapater adapter = new DurasiAdapater(durasiItemList, DurasiAdapater.LAYOUT_DIALOG);

                rvDurasi.setAdapter(adapter);

                androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(TambahOrderanActivity.this)
                        .setView(dialogView)
                        .create();
                adapter.setOnItemClickListener(selectedDurasi -> {
                    dialog.dismiss();
                    Intent intent = new Intent(TambahOrderanActivity.this, DetailKontakActivity.class);
                    intent.putExtra("nama", kontakItem.getNama());
                    intent.putExtra("nomor", kontakItem.getNoTelpon());
                    intent.putExtra("alamat", kontakItem.getAlamat());
                    intent.putExtra("durasiNama", selectedDurasi.getNameDurasi());
                    intent.putExtra("durasiJam", selectedDurasi.getJamDurasi());
                    startActivity(intent);
                });

                dialog.show();
            }

            @Override
            public void onError(String error) {
                Log.e("DurasiDialog", "Gagal ambil durasi: " + error);
            }
        });
    }
}