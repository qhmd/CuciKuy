package com.example.cucikuy.Pengaturan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cucikuy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.util.HashMap;
import java.util.Map;

public class PengaturanNotaActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userId;
    private Button btn_save_nota;
    private EditText et_nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_nota);
        overridePendingTransition(R.anim.fade_in, 0);

        btn_save_nota = findViewById(R.id.btn_save_nota);
        et_nota = findViewById(R.id.et_nota);
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getUid();

        // Load isi terakhir dari SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String isiSebelumnya = sharedPreferences.getString("isinota", "");
        et_nota.setText(isiSebelumnya);

        btn_save_nota.setOnClickListener(v -> {
            if (userId == null) {
                Toast.makeText(this, "User belum login!", Toast.LENGTH_SHORT).show();
                return;
            }

            String isiNota = et_nota.getText().toString();

            Map<String, Object> data = new HashMap<>();
            data.put("isi_catatan", isiNota);

            db.collection("users")
                    .document(userId)
                    .collection("nota")
                    .document("catatan_nota")
                    .set(data)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(PengaturanNotaActivity.this,"Berhasil disimpan", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(PengaturanNotaActivity.this, "Gagal menyimpan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("isinota", et_nota.getText().toString());
        editor.apply();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, 0);
    }

}
