package com.example.cucikuy.Pengaturan;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cucikuy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class PengaturanTokoActivity extends AppCompatActivity {

    private static final String TAG = "PengaturanToko";

    private EditText etNamaOutlet, etAlamat, etNoHp;
    private Button btnSimpan;
    private ImageView arrow_back;

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private DocumentReference docRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_toko);
        overridePendingTransition(R.anim.fade_in, 0);

        etNamaOutlet = findViewById(R.id.etNamaOutlet);
        etAlamat = findViewById(R.id.etAlamat);
        etNoHp = findViewById(R.id.etNoHp);
        btnSimpan = findViewById(R.id.btnSimpan);
        arrow_back = findViewById(R.id.arrow_back);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        arrow_back.setOnClickListener(v -> {
            finish();
        });

        if (auth.getCurrentUser() != null) {
            String uid = auth.getCurrentUser().getUid();
            Log.d(TAG, "User UID: " + uid);

            docRef = db.collection("users").document(uid);

            docRef.get().addOnSuccessListener(snapshot -> {
                if (snapshot.exists()) {
                    Log.d(TAG, "Data ditemukan: " + snapshot.getData());

                    etNamaOutlet.setText(snapshot.getString("nama_outlet"));
                    etAlamat.setText(snapshot.getString("alamat"));
                    etNoHp.setText(snapshot.getString("no_hp"));
                } else {
                    Log.d(TAG, "Dokumen tidak ditemukan.");
                    Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                Log.e(TAG, "Gagal mengambil data", e);
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
            });

            btnSimpan.setOnClickListener(v -> {
                String namaOutlet = etNamaOutlet.getText().toString().trim();
                String alamat = etAlamat.getText().toString().trim();
                String noHp = etNoHp.getText().toString().trim();

                Log.d(TAG, "Data yang akan disimpan: " +
                        "nama_outlet=" + namaOutlet +
                        ", alamat=" + alamat +
                        ", no_hp=" + noHp);

                if (namaOutlet.isEmpty() || alamat.isEmpty() || noHp.isEmpty()) {
                    Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
                    return;
                }

                docRef.update("nama_outlet", namaOutlet,
                                "alamat", alamat,
                                "no_hp", noHp)
                        .addOnSuccessListener(unused -> {
                            Log.d(TAG, "Data berhasil disimpan");
                            Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.e(TAG, "Gagal menyimpan data", e);
                            Toast.makeText(this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                        });
            });

        } else {
            Log.e(TAG, "User belum login.");
            Toast.makeText(this, "User belum login", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, 0);
    }
}
