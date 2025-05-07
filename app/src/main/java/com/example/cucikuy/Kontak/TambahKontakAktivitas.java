package com.example.cucikuy.Kontak;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cucikuy.R;
import com.example.cucikuy.Order.TambahOrderanActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.blurry.Blurry;

public class TambahKontakAktivitas extends AppCompatActivity {
    TextInputLayout nama_kontak;
    TextInputEditText et_nama_kontak;
    TextInputLayout no_hp;
    TextInputEditText et_no_hp;

    TextInputEditText et_alamat;
    Button btn_contact;
    FirebaseUser user;
    FirebaseFirestore db;
    ConstraintLayout blurTarget;
    private ProgressBar spinner;
    private View darkOverlay;
    private ImageView arrow_back;


    private EditText noHp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        overridePendingTransition(R.anim.fade_in, 0);

        nama_kontak = findViewById(R.id.textFieldName);
        et_nama_kontak = findViewById(R.id.et_input_name);
        arrow_back = findViewById(R.id.arrow_back);

        arrow_back.setOnClickListener(v -> {
            finish();
        });

        if (et_nama_kontak != null) {
            final boolean[] hasTypedBefore = {false};

            et_nama_kontak.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence text, int start, int before, int count) {
                    if (!TextUtils.isEmpty(text)) {
                        hasTypedBefore[0] = true;
                        nama_kontak.setError(null);
                    } else {
                        if (hasTypedBefore[0]) {
                            nama_kontak.setError("Input tidak boleh kosong");
                        } else {
                            nama_kontak.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
        no_hp = findViewById(R.id.textFieldNoHp);
        et_no_hp = findViewById(R.id.et_input_nohp);
        if (et_no_hp != null) {
            final boolean[] hasTypedBefore = {false};

            et_no_hp.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence text, int start, int before, int count) {
                    if (!TextUtils.isEmpty(text)) {
                        hasTypedBefore[0] = true;
                        no_hp.setError(null);
                    } else {
                        if (hasTypedBefore[0]) {
                            no_hp.setError("Input tidak boleh kosong");
                        } else {
                            no_hp.setError(null);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
        et_alamat = findViewById(R.id.et_input_alamat);
        btn_contact = findViewById(R.id.btn_add_contact);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
        spinner = findViewById(R.id.googleProgress);
        darkOverlay = findViewById(R.id.darkOverlay);
        blurTarget = findViewById(R.id.constraint_utama);
        String uid = user.getUid();

        btn_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("pelanggan", "diklik");
                darkOverlay.setVisibility(View.VISIBLE);
                spinner.setVisibility((View.VISIBLE));
                Blurry.with(TambahKontakAktivitas.this)
                        .radius(5)         // tingkat blur (0–25)
                        .sampling(2)        // pengambilan sampel (semakin besar, semakin ringan proses)
                        .onto(blurTarget);  // layout yang akan diblur
                btn_contact.setEnabled(false);
                String nama = et_nama_kontak.getText().toString().trim();
                String nomorHp = et_no_hp.getText().toString().trim();
                String alamat = et_alamat.getText().toString().trim();
                Log.i("pelanggan", nama + nomorHp + alamat);

                Map<String, Object> kontak = new HashMap<>();
                kontak.put("nama", nama);
                kontak.put("nomor", nomorHp);
                if (!alamat.isEmpty()) {
                    kontak.put("alamat", alamat);
                }

                db.collection("users")
                        .document(uid)
                        .collection("kontak")
                        .document(nomorHp)
                        .set(kontak)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("Firestore", "Kontak berhasil disimpan dengan nomor HP sebagai ID!");
                            updateUI();
                        })
                        .addOnFailureListener(e -> {
                            spinner.setVisibility((View.GONE));
                            btn_contact.setEnabled(true);
                            darkOverlay.setVisibility(View.GONE);
                            Blurry.delete(blurTarget);
                            Log.w("Firestore", "Gagal menyimpan kontak", e);
                        });
            }
        });
    }
    private void updateUI () {
        spinner.setVisibility((View.GONE));
        btn_contact.setEnabled(true);
        darkOverlay.setVisibility(View.GONE);
        Blurry.delete(blurTarget);
        startActivity(new Intent(TambahKontakAktivitas.this, TambahOrderanActivity.class));
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, 0);
    }
}
