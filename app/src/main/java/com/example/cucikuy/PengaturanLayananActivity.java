package com.example.cucikuy;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;



public class PengaturanLayananActivity extends AppCompatActivity {
    ImageView btn_tambah_layanan;
    String namaDurasi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_layanan);
        overridePendingTransition(R.anim.fade_in, 0);
        btn_tambah_layanan = findViewById(R.id.tambah_layanan);
        List<String> durasiNames = new ArrayList<>();
        btn_tambah_layanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (durasiNames != null && !durasiNames.isEmpty()) {
                    Intent intent = new Intent(PengaturanLayananActivity.this, TambahLayanan.class);
                    intent.putStringArrayListExtra("durasiList", new ArrayList<>(durasiNames));
                    startActivity(intent);
                } else {
                    Toast.makeText(PengaturanLayananActivity.this, "Data durasi belum tersedia!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DurasiData durasiData = new DurasiData();
        TabLayout tabLayoutDurasi = findViewById(R.id.tab_layout_durasi);

        durasiData.loadDurasiData(new DurasiData.DataCallback() {
            @Override
            public void onDataLoaded(List<DurasiItem> durasiItemList) {
                durasiNames.clear();

                // Tambahkan tab untuk setiap nama_durasi
                for (DurasiItem item : durasiItemList) {
                    namaDurasi = item.getNameDurasi();
                    durasiNames.add(namaDurasi);
                    tabLayoutDurasi.addTab(tabLayoutDurasi.newTab().setText(namaDurasi));
                }

                tabLayoutDurasi.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        String selectedDurasi = tab.getText().toString();
                        Toast.makeText(PengaturanLayananActivity.this, "Tab: " + selectedDurasi, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
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

    List<String> durasiNames = new ArrayList<>();
}
