package com.example.cucikuy.Durasi;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cucikuy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.gson.Gson;

import java.util.Map;

public class EditDurasi extends AppCompatActivity {

    private EditText etJamDurasi, etNameDurasi;
    private Button btnUpdate;
    private String namaDurasi; // Menggunakan nama durasi sebagai referensi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_durasi);

        // Menghubungkan komponen UI
        etJamDurasi = findViewById(R.id.etJamDurasi);
        etNameDurasi = findViewById(R.id.etNameDurasi);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Mendapatkan nama durasi yang dipilih dari Intent
        namaDurasi = getIntent().getStringExtra("nameDurasi");

        if (namaDurasi == null || namaDurasi.isEmpty()) {
            Toast.makeText(this, "Nama durasi tidak valid", Toast.LENGTH_SHORT).show();
            finish(); // Mengakhiri activity jika namaDurasi tidak ada
            return;
        }
        FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
        String userId = mAuth.getUid(); // pastikan order menyimpan userId

        // Ambil data dari Firestore berdasarkan nama durasi
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("durasi")
                .whereEqualTo("nama_durasi", namaDurasi)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d("FirestoreDebug", "Jumlah dokumen ditemukan: " + queryDocumentSnapshots.size()); // LOG DEBUG
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                        Log.d("FirestoreData", "Data: " + documentSnapshot.getData()); // LOG DEBUG

                        DurasiItem item = documentSnapshot.toObject(DurasiItem.class);
                        if (item != null) {
                            Log.d("FirestoreData", "Jam: " + item.getJamDurasi()); // LOG DEBUG
                            Log.d("FirestoreData", "Nama: " + item.getNameDurasi()); // LOG DEBUG
                            etJamDurasi.setText(item.getJamDurasi());
                            etNameDurasi.setText(item.getNameDurasi());
                        }
                    } else {
                        Log.w("FirestoreData", "Tidak ada durasi dengan nama: " + namaDurasi); // LOG DEBUG
                        Toast.makeText(EditDurasi.this, "Durasi tidak ditemukan", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Gagal mengambil data durasi", e); // LOG DEBUG
                    Toast.makeText(EditDurasi.this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                    finish();
                });

        // Update durasi berdasarkan nama durasi
        btnUpdate.setOnClickListener(v -> {
            String jamDurasi = etJamDurasi.getText().toString().trim();
            String nameDurasi = etNameDurasi.getText().toString().trim();

            Log.d("UpdateDurasi", "Input jam: " + jamDurasi + ", nama: " + nameDurasi); // LOG DEBUG

            if (!jamDurasi.isEmpty() && !nameDurasi.isEmpty()) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String oldNameDurasi = namaDurasi; // Nama durasi lama yang digunakan sebagai ID dokumen
                String newNameDurasi = nameDurasi; // Nama durasi baru (akan menjadi ID dokumen baru)

                DocumentReference oldRef = db.collection("users")
                        .document(userId)
                        .collection("durasi")
                        .document(oldNameDurasi);  // Nama dokumen lama

                DocumentReference newRef = db.collection("users")
                        .document(userId)
                        .collection("durasi")
                        .document(newNameDurasi);  // Nama dokumen baru

                oldRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Ambil data dari dokumen lama
                        Map<String, Object> data = documentSnapshot.getData();
                        if (data != null) {
                            // Update data dengan nama dan waktu durasi baru
                            data.put("nama_durasi", newNameDurasi);
                            data.put("waktu_durasi", jamDurasi);

                            // Tulis data ke dokumen baru
                            newRef.set(data).addOnSuccessListener(aVoid -> {
                                // Hapus dokumen lama setelah data berhasil dipindahkan
                                oldRef.delete().addOnSuccessListener(aVoid1 -> {
                                    Log.d("UpdateDurasi", "Berhasil mengganti nama dokumen dan memperbarui data");
                                    Toast.makeText(EditDurasi.this, "Durasi diperbarui!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }).addOnFailureListener(e -> {
                                    Log.e("UpdateDurasi", "Gagal menghapus dokumen lama", e);
                                    Toast.makeText(EditDurasi.this, "Update gagal", Toast.LENGTH_SHORT).show();
                                });
                            }).addOnFailureListener(e -> {
                                Log.e("UpdateDurasi", "Gagal menulis dokumen baru", e);
                                Toast.makeText(EditDurasi.this, "Update gagal", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } else {
                        Log.e("UpdateDurasi", "Dokumen lama tidak ditemukan");
                        Toast.makeText(EditDurasi.this, "Durasi tidak ditemukan", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Log.e("UpdateDurasi", "Gagal membaca dokumen lama", e);
                    Toast.makeText(EditDurasi.this, "Update gagal", Toast.LENGTH_SHORT).show();
                });
            } else {
                Log.w("UpdateDurasi", "Input kosong"); // LOG DEBUG
                Toast.makeText(EditDurasi.this, "Harap isi semua kolom", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
