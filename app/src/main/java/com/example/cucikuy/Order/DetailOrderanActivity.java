    package com.example.cucikuy.Order;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.widget.TextView;

    import androidx.activity.OnBackPressedCallback;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.cucikuy.HomeActivity;
    import com.example.cucikuy.Layanan.LayananItem;
    import com.example.cucikuy.Layanan.LayananOrderAdapter;
    import com.example.cucikuy.R;
    import com.google.gson.Gson;

    import java.util.ArrayList;

    public class DetailOrderanActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail_orderan);
            String nama = getIntent().getStringExtra("nama");
            String totalHarga = getIntent().getStringExtra("totalHarga");
            String noHp = getIntent().getStringExtra("noHp");
            String tanggalMasuk = getIntent().getStringExtra("tanggalMasuk");
            String estimasiSelesai = getIntent().getStringExtra("estiminasiSelesai");

            TextView tvNama = findViewById(R.id.tv_nama_kontak );
            TextView tvTotalHarga = findViewById(R.id.total_harga);
            TextView tvNoHp = findViewById(R.id.tv_no_hp);
            TextView tvTanggalMsuk = findViewById(R.id.tanggal_masuk);
            TextView tvEstSelesai = findViewById(R.id.est_seleai);


            tvNama.setText(nama);
            tvTotalHarga.setText(totalHarga);
            tvNoHp.setText(noHp);
            tvTanggalMsuk.setText(tanggalMasuk);
            tvEstSelesai.setText(estimasiSelesai);

            RecyclerView recyclerView = findViewById(R.id.rv_layanan_dipilih);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            OrderItem order = (OrderItem) getIntent().getSerializableExtra("order");
            ArrayList<LayananItem> selectedLayanan = (ArrayList<LayananItem>) getIntent().getSerializableExtra("selectedLayanan");
            Gson gson = new Gson();
            String isinya = gson.toJson(selectedLayanan);
            Log.d("DetailOrderan", "selectedLayanan: " + isinya);
            if (selectedLayanan != null) {
                LayananOrderAdapter adapter = new LayananOrderAdapter(selectedLayanan);
                recyclerView.setAdapter(adapter);
            }
            OnBackPressedCallback callback = new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    Intent intent = new Intent(DetailOrderanActivity.this, HomeActivity.class);
                    intent.putExtra("navigateTo", "order");
                    startActivity(intent);
                    finish();
                }
            };

            getOnBackPressedDispatcher().addCallback(this, callback);
        }
    }

