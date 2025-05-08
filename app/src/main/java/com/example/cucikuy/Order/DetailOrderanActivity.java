    package com.example.cucikuy.Order;

    import android.content.Intent;
    import android.os.Bundle;
    import android.util.Log;
    import android.view.View;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.activity.OnBackPressedCallback;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.content.ContextCompat;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.cucikuy.FormatIDR;
    import com.example.cucikuy.HomeActivity;
    import com.example.cucikuy.Layanan.LayananItem;
    import com.example.cucikuy.Layanan.LayananOrderAdapter;
    import com.example.cucikuy.R;
    import com.example.cucikuy.WaNota;
    import com.google.android.material.dialog.MaterialAlertDialogBuilder;
    import com.google.firebase.auth.FirebaseAuth;
    import com.google.firebase.auth.FirebaseUser;
    import com.google.firebase.firestore.FieldValue;
    import com.google.firebase.firestore.FirebaseFirestore;
    import com.google.gson.Gson;

    import java.util.ArrayList;
    import java.util.Formatter;
    import java.util.HashMap;
    import java.util.Map;

    public class DetailOrderanActivity extends AppCompatActivity {
        private String orderId;
        private ImageView arrow_back;
        private String alamat;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail_orderan);
            String nama = getIntent().getStringExtra("nama");
            Double totalHarga = getIntent().getDoubleExtra("totalHarga", 0.0);
            String noHp = getIntent().getStringExtra("noHp");
            alamat = getIntent().getStringExtra("alamat");
            String tanggalMasuk = getIntent().getStringExtra("tanggalMasuk");
            String estimasiSelesai = getIntent().getStringExtra("estiminasiSelesai");
            String nota = getIntent().getStringExtra("nota");
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                for (String key : extras.keySet()) {
                    Object value = extras.get(key);
                    Log.d("IntentExtraBro", key + " : " + value);
                }
            } else {
                Log.d("IntentExtra", "No extras in intent");
            }



            TextView tvNama = findViewById(R.id.tv_nama_kontak );
            TextView tvTotalHarga = findViewById(R.id.total_harga);
            TextView tvNoHp = findViewById(R.id.tv_no_hp);
            TextView tvTanggalMsuk = findViewById(R.id.tanggal_masuk);
            TextView tvEstSelesai = findViewById(R.id.est_seleai);
            TextView tvStatusPesanan = findViewById(R.id.statusPesanan);
            TextView tvAlamat = findViewById(R.id.tv_alamat);
            arrow_back = findViewById(R.id.arrow_back);


            Button btnKirimWa = findViewById(R.id.kirimWa);
            Button btnPembayaran = findViewById(R.id.pembayran_selesai);
            Button btnPesananSiap = findViewById(R.id.pesanan_siap);
            Button btnPesananSelesai = findViewById(R.id.pesanan_selesai);

            tvNama.setText(nama);
            tvTotalHarga.setText("Rp " +FormatIDR.FormatIDR(totalHarga));
            tvNoHp.setText(noHp);
            tvTanggalMsuk.setText(tanggalMasuk);
            tvEstSelesai.setText(estimasiSelesai);
            tvAlamat.setText(alamat);

            RecyclerView recyclerView = findViewById(R.id.rv_layanan_dipilih);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            OrderItem order = (OrderItem) getIntent().getSerializableExtra("order");
            ArrayList<LayananItem> selectedLayanan = (ArrayList<LayananItem>) getIntent().getSerializableExtra("selectedLayanan");
            Log.i("gson",new Gson().toJson(order));

            arrow_back.setOnClickListener(v -> {
                finish();
            });

            if (order == null || selectedLayanan == null) {
                Toast.makeText(this, "Data pesanan tidak tersedia", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            TextView statusPembayaran = findViewById(R.id.belum_bayar);
            statusPembayaran.setText(order.isBelum_bayar() ? "Belum Bayar " : "Sudah Bayar");

            FirebaseUser mAuth = FirebaseAuth.getInstance().getCurrentUser();
            String userId = mAuth.getUid(); // pastikan order menyimpan userId
            orderId = order.getNo_nota(); // misalnya: "CUCI-202505045"

            if (orderId == null ) {
                if (nota != null) {
                    Log.i("isinota", nota);
                    orderId = nota;
                }
            }
            Gson gson = new Gson();
            String isinya = gson.toJson(selectedLayanan);
            Log.d("DetailOrderan", "selectedLayanan: " + isinya);
            Log.d("DetailOrderan", "order : " + new Gson().toJson(order));
            if (selectedLayanan != null) {
                LayananOrderAdapter adapter = new LayananOrderAdapter(selectedLayanan);
                recyclerView.setAdapter(adapter);
            }

            Log.d("tesss", "isBelum_siap: " + order.isBelum_siap());

            tvStatusPesanan.setText(order.isBelum_siap() ? "Antrian" : "Siap Ambil");

            if (!order.isBelum_bayar() && !order.isBelum_siap()) {
                btnPesananSelesai.setVisibility(View.VISIBLE);
                btnPesananSiap.setVisibility(View.GONE);
            }

            btnKirimWa.setOnClickListener(v -> {
                Log.i("isiorder", new Gson().toJson(order));
                Log.i("isiorder", new Gson().toJson(selectedLayanan));
                Intent intent = new Intent(DetailOrderanActivity.this, WaNota.class);
                intent.putExtra("status_kirim_nota", true);
                intent.putExtra("order", order);
                intent.putExtra("noHp", noHp);
                Log.i("liatnohp","tes" + noHp);
                intent.putExtra("selectedLayanan", selectedLayanan);
                startActivity(intent);
                finish();
            });

            TextView batalkanPesanan = findViewById(R.id.batalkan_pesanan);
            batalkanPesanan.setOnClickListener(v -> {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah Anda yakin ingin membatalkan pesanan ini?")
                        .setPositiveButton("Ya", (dialog, which) -> {
                            if (order != null && orderId != null && !orderId.isEmpty()) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("users")
                                        .document(userId)
                                        .collection("pesanan")
                                        .document("statusPesanan")
                                        .collection("antrian")
                                        .document(orderId)
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(this, "Pesanan berhasil dibatalkan", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(DetailOrderanActivity.this, HomeActivity.class);
                                            intent.putExtra("navigateTo", "order");
                                            startActivity(intent);
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("BatalkanPesanan", "Gagal membatalkan pesanan", e);
                                            Toast.makeText(this, "Gagal membatalkan pesanan", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Toast.makeText(this, "Data pesanan tidak valid", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Tidak", (dialog, which) -> {});
                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(d -> {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.black));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.black));
                });
                dialog.show();
            });


            btnPembayaran.setOnClickListener(v -> {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                        .setTitle("Konfirmasi")
                        .setMessage("Pelanggan telah membayar")
                        .setPositiveButton("Ya", (dialog, which) -> {
                            if (order != null) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("users")
                                        .document(userId)
                                        .collection("pesanan")
                                        .document("statusPesanan")
                                        .collection("antrian")
                                        .document(orderId)
                                        .update("belum_bayar", false)
                                        .addOnSuccessListener(aVoid ->{
                                            Log.d("UpdateStatus", "Status berhasil diupdate");

                                            // Kirim ke WhatsApp melalui WaNota
                                            Intent intent = new Intent(this, WaNota.class);
                                            intent.putExtra("from_pembayaran", true); // flag agar tahu ini dari konfirmasi pembayaran
                                            intent.putExtra("noNota", order.getNo_nota());
                                            intent.putExtra("nama", order.getNama_pelanggan());
                                            intent.putExtra("totalHarga", order.getTotal_bayar());
                                            intent.putExtra("noHp", order.getNo_hp());
                                            Log.i("liatnohp",order.getNo_hp());
                                            startActivity(intent);
                                            }
                                        )
                                        .addOnFailureListener(e -> Log.e("UpdateStatus", "Gagal update status", e));
                            }
                        })

                        .setNegativeButton("Tidak", (dialog, which) -> {});
                        AlertDialog dialog = builder.create();
                        dialog.setOnShowListener(d -> {
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,R.color.black));
                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this,R.color.black));
                        });
                        dialog.show();
            });


            btnPesananSiap.setOnClickListener(v -> {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                        .setTitle("Konfirmasi")
                        .setMessage("Pesanan telah Siap ?")
                        .setPositiveButton("Ya", (dialog, which) -> {
                            if (order != null) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();

                                db.collection("users")
                                        .document(userId)
                                        .collection("pesanan")
                                        .document("statusPesanan")
                                        .collection("antrian")
                                        .document(orderId)
                                        .update("belum_siap", false)
                                        .addOnSuccessListener(aVoid -> {
                                            Log.d("UpdateStatus", "Status berhasil diupdate");
                                            // Kirim WhatsApp lewat WaNota

                                            Intent intent = new Intent(this, WaNota.class);
                                            intent.putExtra("from_status_siap", true); // Tambahkan flag ini
                                            intent.putExtra("selectedLayanan", selectedLayanan);
                                            intent.putExtra("order", order); // Pastikan OrderItem implements Serializable
                                            startActivity(intent);
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("UpdateStatus", "Gagal update status", e);
                                            Toast.makeText(this, "Gagal memperbarui status pesanan", Toast.LENGTH_SHORT).show();
                                        });
                            } else {
                                Toast.makeText(this, "Data order tidak tersedia", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Tidak", (dialog, which) -> {});
                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(d -> {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,R.color.black));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this,R.color.black));
                });
                dialog.show();
            });

            btnPesananSelesai.setOnClickListener(v -> {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                        .setTitle("Konfirmasi")
                        .setMessage("Pesanan telah selesai ?")
                        .setPositiveButton("Ya", (dialog, which) -> {
                            if (order != null) {
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                Map<String, Object> updateData = new HashMap<>();
                                updateData.put("belum_selesai", false);
                                updateData.put("tanggal_selesai", FieldValue.serverTimestamp()); // <-- Tambahkan ini

                                db.collection("users")
                                        .document(userId)
                                        .collection("pesanan")
                                        .document("statusPesanan")
                                        .collection("antrian")
                                        .document(orderId)
                                        .update(updateData)
                                        .addOnSuccessListener(aVoid ->
                                                Toast.makeText(this, "Pesanan Telah Selesai", Toast.LENGTH_SHORT).show()
                                        )
                                        .addOnFailureListener(e -> Log.e("UpdateStatus", "Gagal update status", e));
                            }

                        })

                        .setNegativeButton("Tidak", (dialog, which) -> {});
                AlertDialog dialog = builder.create();
                dialog.setOnShowListener(d -> {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,R.color.black));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this,R.color.black));
                });
                dialog.show();
            });


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

