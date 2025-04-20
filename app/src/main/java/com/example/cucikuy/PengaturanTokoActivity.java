package com.example.cucikuy;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class PengaturanTokoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_toko);
        overridePendingTransition(R.anim.fade_in, 0);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, 0);
    }
}
