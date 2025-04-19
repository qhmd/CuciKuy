package com.example.cucikuy;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
public class DetailKontakActivity extends AppCompatActivity {
    private TextView tvNama, tvNoHp, tvAlamat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kontak);

        // Inisialisasi TextView
        tvNama = findViewById(R.id.tvDetailNama);
        tvNoHp = findViewById(R.id.tvDetailNoHp);
        tvAlamat = findViewById(R.id.tvDetailAlamat);

        // Ambil data yang dikirim lewat Intent
        String nama = getIntent().getStringExtra("nama");
        String noTelpon = getIntent().getStringExtra("nomor");
        String alamat = getIntent().getStringExtra("alamat");

        Log.d("datadetail", nama + " - " + noTelpon + " - " + alamat);

        // Set data ke TextView
        tvNama.setText(nama);
        tvNoHp.setText(noTelpon);
        tvAlamat.setText(alamat);
    }

}
