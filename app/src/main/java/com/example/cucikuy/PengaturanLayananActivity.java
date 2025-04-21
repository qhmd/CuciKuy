package com.example.cucikuy;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PengaturanLayananActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_layanan);
        overridePendingTransition(R.anim.fade_in, 0);

        RecyclerView rvDurasiLayanan = findViewById(R.id.rv_data_durasi_layanan);
        rvDurasiLayanan.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        DurasiData durasiData = new DurasiData();
        durasiData.loadDurasiData(new DurasiData.DataCallback() {
            @Override
            public void onDataLoaded(List<DurasiItem> durasiItemList) {
                DurasiAdapater adapter = new DurasiAdapater(durasiItemList, DurasiAdapater.LAYOUT_FOR_LAYANAN);
                rvDurasiLayanan.setAdapter(adapter);
            }

            @Override
            public void onError(String error) {
                Log.e("DurasiDialog", "Gagal ambil durasi: " + error);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, 0);
    }
}
