package com.example.cucikuy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeActivity extends AppCompatActivity {

    Button sign_out_btn;
    FirebaseFirestore db;
    FirebaseUser currentUser;

    TextView tvEmail, tvNamaOutlet, tvAlamat, tvNoHp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sign_out_btn = findViewById(R.id.sign_out_btn);
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Inisialisasi TextView dari layout
        tvEmail = findViewById(R.id.tvEmail);
        tvNamaOutlet = findViewById(R.id.nama_outlet);
        tvAlamat = findViewById(R.id.alamat);
        tvNoHp = findViewById(R.id.no_hp);
        if (currentUser != null) {
            String uid = currentUser.getUid();

            db.collection("users").document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String email = currentUser.getEmail();
                            String namaOutlet = documentSnapshot.getString("nama_outlet");
                            String alamat = documentSnapshot.getString("alamat");
                            String noHp = documentSnapshot.getString("no_hp");
                            // Tampilkan komponen setelah datanya di-set
                            tvEmail.setVisibility(View.VISIBLE);
                            tvNamaOutlet.setVisibility(View.VISIBLE);
                            tvAlamat.setVisibility(View.VISIBLE);
                            tvNoHp.setVisibility(View.VISIBLE);

                            // Tampilkan di TextView
                            tvEmail.setText(email);
                            tvNamaOutlet.setText(namaOutlet);
                            tvAlamat.setText(alamat);
                            tvNoHp.setText(noHp);

                        } else {
                            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Gagal ambil data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
        sign_out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}