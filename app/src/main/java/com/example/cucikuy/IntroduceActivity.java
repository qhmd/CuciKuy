package com.example.cucikuy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;


import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;

public class IntroduceActivity extends AppCompatActivity {
    private EditText namaOutlet;
    private EditText alamat;
    ConstraintLayout blurTarget;
    private ProgressBar spinner;
    private View darkOverlay;
    private EditText noHp;
    private FirebaseUser user;
    private FirebaseFirestore db;
    private Button send_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        spinner = findViewById(R.id.googleProgress);
        darkOverlay = findViewById(R.id.darkOverlay);
        blurTarget = findViewById(R.id.constraintUtama);
        namaOutlet = findViewById(R.id.nama_outlet);
        alamat = findViewById(R.id.alamat);
        noHp = findViewById(R.id.no_hp);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        String uid = user.getUid();
        String email = user.getEmail();
        send_btn = findViewById(R.id.send_button);
        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                darkOverlay.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.VISIBLE);
                Blurry.with(IntroduceActivity.this)
                        .radius(5)         // tingkat blur (0–25)
                        .sampling(2)        // pengambilan sampel (semakin besar, semakin ringan proses)
                        .onto(blurTarget);  // layout yang akan diblur
                send_btn.setEnabled(false); // opsional: disable agar tidak ditekan berkali-kali
                if (validateInput()) {
                    sendDataIntroduce();
                } else {
                    spinner.setVisibility(View.GONE);
                    send_btn.setEnabled(true);
                    darkOverlay.setVisibility(View.GONE);
                    Blurry.delete(blurTarget);
                }
            }
            private boolean validateInput() {
                if (namaOutlet.getText().toString().isEmpty()) {
                    namaOutlet.setError("Nama outlet harus diisi");
                    return false;
                }
                if (alamat.getText().toString().isEmpty()) {
                    alamat.setError("Alamat harus diisi");
                    return false;
                }
                if (noHp.getText().toString().isEmpty()) {
                    noHp.setError("Nomor HP harus diisi");
                    return false;
                }
                return true;
            }

            private void sendDataIntroduce () {
                if (!validateInput()) {
                    return;
                }

                String namaOutletStr = namaOutlet.getText().toString();
                String alamatStr = alamat.getText().toString();
                String noHpStr = noHp.getText().toString();

                Map<String, Object> data = new HashMap<>();
                data.put("nama_outlet", namaOutletStr);
                data.put("alamat", alamatStr);
                data.put("no_hp", noHpStr);
                data.put("email", email);

                db.collection("users").document(uid)
                        .set(data)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("Firestore", "Data updated");
                            updateUI();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Firestore", "Error", e);
                            Log.d("FIREBASE_UID", "UID saat ini: " + uid);
                            spinner.setVisibility((View.GONE));
                            send_btn.setEnabled(true);
                            darkOverlay.setVisibility(View.GONE);
                            Blurry.delete(blurTarget);
                            Toast.makeText(IntroduceActivity.this, "Terjadi kesalahan. Coba lagi.", Toast.LENGTH_SHORT).show();
                        });
            }
            private void updateUI () {
                spinner.setVisibility((View.GONE));
                send_btn.setEnabled(true);
                darkOverlay.setVisibility(View.GONE);
                Blurry.delete(blurTarget);
                startActivity(new Intent(IntroduceActivity.this, TambahOrderanActivity.class));
                finish();
            }
        });
    }
}
