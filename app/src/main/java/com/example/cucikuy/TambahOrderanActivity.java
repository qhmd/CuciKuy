package com.example.cucikuy;

import android.content.Context;
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

        kontakAdapter.setOnItemClickListener(kontakItem -> {
            Log.i("detailkontakbro", "klikk");
            Intent intent = new Intent(TambahOrderanActivity.this, DetailKontakActivity.class);
            intent.putExtra("nama", kontakItem.getNama());
            intent.putExtra("nomor", kontakItem.getNoTelpon());
            intent.putExtra("alamat", kontakItem.getAlamat());

            startActivity(intent);
        });

        btn_add_contact.setOnClickListener(v -> {
            startActivity(new Intent(TambahOrderanActivity.this, TambahKontakAktivitas.class));
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, 0);
    }
}
