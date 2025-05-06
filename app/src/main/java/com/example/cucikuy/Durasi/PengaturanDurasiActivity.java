package com.example.cucikuy.Durasi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cucikuy.R;

import java.util.ArrayList;
import java.util.List;

public class PengaturanDurasiActivity extends AppCompatActivity {
    Button btn_add_durasi;
    private RecyclerView recyclerView;
    private DurasiAdapater durasiAdapater;
    private List<DurasiItem> durasiItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_durasi);
        overridePendingTransition(R.anim.fade_in, 0);
        recyclerView = findViewById(R.id.rv_data_durasi);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setHasFixedSize(true);

        // Inisialisasi list dulu
        durasiItemList = new ArrayList<>();

        // Bikin adapter setelah list terisin
        durasiAdapater = new DurasiAdapater(durasiItemList, DurasiAdapater.LAYOUT_DEFAULT);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(durasiAdapater);

        DurasiData dataLoader = new DurasiData();

        // Ambil data dari Firestore
        dataLoader.loadDurasiData(new DurasiData.DataCallback() {
            @Override
            public void onDataLoaded(List<DurasiItem> durasiItemListFirestore) {
                durasiItemList.clear();
                durasiItemList.addAll(durasiItemListFirestore);
                durasiAdapater.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                Log.e("Firebase", error);
            }
        });

        // Tambah Durasi
        btn_add_durasi = findViewById(R.id.btn_add_durasi);
        btn_add_durasi.setOnClickListener(v -> {
            Intent intent = new Intent(PengaturanDurasiActivity.this, TambahDurasi.class);
            startActivity(intent);
            finish();
        });

        // Set Edit dan Delete listener di adapter
        durasiAdapater.setOnEditClickListener(new DurasiAdapater.OnEditClickListener() {
            @Override
            public void onEditClick(DurasiItem item) {
                Intent intent = new Intent(PengaturanDurasiActivity.this, EditDurasi.class);
                Log.i("namadurasi", item.getNameDurasi());
                intent.putExtra("nameDurasi", item.getNameDurasi());
                startActivity(intent);
            }
        });

        durasiAdapater.setOnDeleteClickListener(new DurasiAdapater.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(DurasiItem item) {
                DurasiData dataHandler = new DurasiData();
                dataHandler.deleteDurasi(item.getNameDurasi(), new DurasiData.DeleteCallback() {
                    @Override
                    public void onSuccess() {
                        durasiItemList.remove(item);
                        durasiAdapater.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String error) {
                        Log.e("DeleteError", error);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, 0);
    }
}
