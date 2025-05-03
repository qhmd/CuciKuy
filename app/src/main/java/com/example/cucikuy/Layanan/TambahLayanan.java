package com.example.cucikuy.Layanan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cucikuy.Pengaturan.PengaturanLayananActivity;
import com.example.cucikuy.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;

public class TambahLayanan extends AppCompatActivity {
    TextInputEditText nama_layanan, harga_layanan;
    AutoCompleteTextView durasi_layanan;
    Button btn_add_layanan;
    FirebaseUser user;
    FirebaseFirestore db;
    ConstraintLayout blurTarget;
    private ProgressBar spinner;
    private View darkOverlay;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_layanan);
        overridePendingTransition(R.anim.fade_in, 0);

        nama_layanan = findViewById(R.id.et_input_name_layanan);
        durasi_layanan = findViewById(R.id.dropdown_layanan);
        harga_layanan = findViewById(R.id.et_input_harga);

        btn_add_layanan = findViewById(R.id.btn_add_layanan);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        spinner = findViewById(R.id.googleProgress);
        darkOverlay = findViewById(R.id.darkOverlay);
        blurTarget = findViewById(R.id.constraint_utama);
        String uid = user.getUid();

        btn_add_layanan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                darkOverlay.setVisibility(View.VISIBLE);
                spinner.setVisibility((View.VISIBLE));
                Blurry.with(TambahLayanan.this)
                        .radius(5)         // tingkat blur (0–25)
                        .sampling(2)        // pengambilan sampel (semakin besar, semakin ringan proses)
                        .onto(blurTarget);  // layout yang akan diblur
                btn_add_layanan.setEnabled(false);
                String nama = nama_layanan.getText().toString().trim();
                String durasi = durasi_layanan.getText().toString().trim();
                Double harga = Double.parseDouble(harga_layanan.getText().toString());

                Map<String, Object> layanan = new HashMap<>();
                layanan.put("nama_layanan", nama);
                layanan.put("durasi", durasi);
                layanan.put("harga", harga);
                db.collection("users")
                        .document(uid)
                        .collection("durasi")
                        .document(durasi)
                        .collection("layanan")
                        .document(nama)
                        .set(layanan)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("Firestore", "Layanan berhasil di tambah!");
                            updateUI();
                        })
                        .addOnFailureListener(e -> {
                            spinner.setVisibility((View.GONE));
                            btn_add_layanan.setEnabled(true);
                            darkOverlay.setVisibility(View.GONE);
                            Blurry.delete(blurTarget);
                            Log.w("Firestore", "Gagal menyimpan layanan", e);
                        });
            }

            private void updateUI () {
                spinner.setVisibility((View.GONE));
                btn_add_layanan.setEnabled(true);
                darkOverlay.setVisibility(View.GONE);
                Blurry.delete(blurTarget);
                startActivity(new Intent(TambahLayanan.this, PengaturanLayananActivity.class));
                finish();
            }
        });

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.dropdown_layanan);

        ArrayList<String> durasiList = getIntent().getStringArrayListExtra("durasiList");
        if (durasiList != null && !durasiList.isEmpty()) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_dropdown_item_1line,
                    durasiList
            );
            autoCompleteTextView.setAdapter(adapter);
        }
    }
}
