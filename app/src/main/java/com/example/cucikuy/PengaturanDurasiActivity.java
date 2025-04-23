package com.example.cucikuy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        //bikin adapter setelah list terisin
        durasiAdapater = new DurasiAdapater(durasiItemList, DurasiAdapater.LAYOUT_DEFAULT);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(durasiAdapater);

        DurasiData dataLoaeder = new DurasiData();

        dataLoaeder.loadDurasiData(new DurasiData.DataCallback() {

            @Override
            public void onDataLoaded(List<DurasiItem> durasiItemListFirestore) {
                durasiItemList.clear();
                durasiItemList.addAll(durasiItemListFirestore);
                durasiAdapater.notifyDataSetChanged(); // <-- Perbaiki di sini
            }

            @Override
            public void onError(String error) {
                Log.e("Firebase", error);
            }
        });

        btn_add_durasi = findViewById(R.id.btn_add_durasi);
        recyclerView = findViewById(R.id.rv_data_durasi);


        btn_add_durasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PengaturanDurasiActivity.this, TambahDurasi.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, 0);
    }
}
