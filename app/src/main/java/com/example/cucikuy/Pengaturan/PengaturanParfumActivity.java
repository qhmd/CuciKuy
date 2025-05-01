package com.example.cucikuy.Pengaturan;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cucikuy.R;

public class PengaturanParfumActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengaturan_parfum);
        overridePendingTransition(R.anim.fade_in, 0);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, 0);
    }
}
