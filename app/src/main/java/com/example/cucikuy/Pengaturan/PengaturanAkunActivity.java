package com.example.cucikuy.Pengaturan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cucikuy.HomeActivity;
import com.example.cucikuy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class PengaturanAkunActivity extends AppCompatActivity {

    private static final String TAG = "PengaturanAkun";

    private TextView tvEmail;
    private Button btnLogout, btnHapusAkun;

    private ImageView arrow_back;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_akun);
        overridePendingTransition(R.anim.fade_in, 0);

        tvEmail = findViewById(R.id.tvEmail);
        btnLogout = findViewById(R.id.btnLogout);
        btnHapusAkun = findViewById(R.id.btnHapusAkun);
        arrow_back = findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(v -> {
            finish();
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (user != null) {
            String email = user.getEmail();
            tvEmail.setText(email != null ? email : "Tidak diketahui");
        } else {
            Toast.makeText(this, "User tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Logout
        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            Toast.makeText(this, "Berhasil logout", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

        // Hapus akun
        btnHapusAkun.setOnClickListener(v -> {
            String uid = user.getUid();

            // Hapus data dari Firestore
            db.collection("users").document(uid)
                    .delete()
                    .addOnSuccessListener(unused -> {
                        Log.d(TAG, "Dokumen Firestore dihapus.");

                        // Hapus akun dari FirebaseAuth
                        user.delete().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Akun Firebase dihapus.");
                                Toast.makeText(this, "Akun berhasil dihapus", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(this, HomeActivity.class));
                                finish();
                            } else {
                                Log.e(TAG, "Gagal menghapus akun Firebase", task.getException());
                                Toast.makeText(this, "Gagal menghapus akun", Toast.LENGTH_SHORT).show();
                            }
                        });

                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Gagal menghapus data Firestore", e);
                        Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, 0);
    }
}
