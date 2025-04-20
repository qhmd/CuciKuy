package com.example.cucikuy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;

public class TambahDurasi extends AppCompatActivity {
    private ProgressBar spinner;
    ConstraintLayout blurTarget;
    private View darkOverlay;
    private EditText nama_durasi, waktu_durasi;

    Button btn_add_durasi;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_duration);
        overridePendingTransition(R.anim.fade_in, 0);

        btn_add_durasi = findViewById(R.id.btn_add_durasi);
        spinner = findViewById(R.id.googleProgress);
        darkOverlay = findViewById(R.id.darkOverlay);
        blurTarget = findViewById(R.id.constraintUtama);
        nama_durasi = findViewById(R.id.et_input_nameDurasi);
        waktu_durasi = findViewById(R.id.et_input_waktu_durasi);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        String uid = user.getUid();
        btn_add_durasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaDurasi = nama_durasi.getText().toString();
                String waktuDurasi = waktu_durasi.getText().toString();
                Map<String, Object> data = new HashMap<>();
                data.put("nama_durasi", namaDurasi);
                data.put("waktu_durasi", waktuDurasi);
                db.collection("users")
                        .document(uid)
                        .collection("durasi")
                        .document(namaDurasi)
                        .set(data)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("Firestore", "Kontak berhasil disimpan dengan nomor HP sebagai ID!");
                            updateUI();
                        })
                        .addOnFailureListener(e -> {
                            spinner.setVisibility((View.GONE));
                            btn_add_durasi.setEnabled(true);
                            darkOverlay.setVisibility(View.GONE);
                            Blurry.delete(blurTarget);
                            Log.w("Firestore", "Gagal menyimpan kontak", e);
                        });
            }
        });
    }
    private void updateUI () {
        spinner.setVisibility((View.GONE));
        btn_add_durasi.setEnabled(true);
        darkOverlay.setVisibility(View.GONE);
        Blurry.delete(blurTarget);
        startActivity(new Intent(TambahDurasi.this, PengaturanDurasiActivity.class));
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, 0);
    }
}
