package com.example.cucikuy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TambahOrderanActivity extends AppCompatActivity {
    ImageView btn_add_contact;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tambah_order);
        overridePendingTransition(R.anim.fade_in, 0);
        btn_add_contact = findViewById(R.id.tambah_kontak);
        btn_add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TambahOrderanActivity.this, TambahKontakAktivitas.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Hilangkan animasi saat tombol back ditekan
        overridePendingTransition(R.anim.fade_in, 0);
    }

}
