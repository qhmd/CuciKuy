package com.example.cucikuy.Kontak;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cucikuy.Pengaturan.PengaturanPelangganActivity;
import com.example.cucikuy.R;

public class EditKontakActivity extends AppCompatActivity {

    private EditText editNama, editAlamat;
    private TextView editNoTelp;
    private Button btnSimpan;
    private ImageView arrow_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_kontak);

        editNama = findViewById(R.id.edit_nama);
        editAlamat = findViewById(R.id.edit_alamat);
        editNoTelp = findViewById(R.id.edit_notelp);
        btnSimpan = findViewById(R.id.btn_simpan);
        arrow_back = findViewById(R.id.arrow_back);

        arrow_back.setOnClickListener(v -> {
            finish();
        });

        Intent intent = getIntent();
        String noTelp = intent.getStringExtra("no_telpon");
        String nama = intent.getStringExtra("nama");
        String alamat = intent.getStringExtra("alamat");

        editNama.setText(nama);
        editAlamat.setText(alamat);
        editNoTelp.setText("No. Telp: " + noTelp);

        btnSimpan.setOnClickListener(v -> {
            String namaBaru = editNama.getText().toString();
            String alamatBaru = editAlamat.getText().toString();

            new KontakData().editKontak(noTelp, namaBaru, alamatBaru, new KontakData.EditCallback() {
                @Override
                public void onEditSuccess() {
                    Toast.makeText(EditKontakActivity.this, "Berhasil diubah", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditKontakActivity.this, PengaturanPelangganActivity.class);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onEditFailure(String error) {
                    Toast.makeText(EditKontakActivity.this, "Gagal: " + error, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
