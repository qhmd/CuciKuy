package com.example.cucikuy.Order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cucikuy.Layanan.LayananItem;
import com.example.cucikuy.Layanan.LayananPilihAdapter;
import com.example.cucikuy.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DetailKontakActivity extends AppCompatActivity {
    private TextView tvNama, tvNoHp, tvAlamat;
    String namaKontak, durasi, noHp, durasiJam, estiminasiSelesai, tanggal;
    String userId, alamat;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Button btn_tambah_order;
    private List<LayananItem> layananList;
    private Boolean belum_bayar, belum_siap;

    private ArrayList<OrderItem> orderList = new ArrayList<>();

    private LayananPilihAdapter adapter; // [✔️ Tambahan agar adapter bisa diakses di luar callback Firestore]
    private double totalHarga = 0.0;// [✔️ Tambahan untuk menyimpan total harga secara global]
    private String noNota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_orderan);

        belum_bayar = true;
        belum_siap = true;
        // Ambil data dari Intent
        durasi = getIntent().getStringExtra("durasiNama");
        namaKontak = getIntent().getStringExtra("nama");
        noHp = getIntent().getStringExtra("nomor");
        durasiJam = getIntent().getStringExtra("durasiJam");
        alamat = getIntent().getStringExtra("alamat");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                Log.d("IntentExtrates", key + " : " + value);
            }
        } else {
            Log.d("IntentExtra", "No extras in intent");
        }

        // Set TextView
        tvNama = findViewById(R.id.tv_nama_kontak);
        tvNoHp = findViewById(R.id.tv_no_hp);
        TextView tvTotalHarga = findViewById(R.id.tv_total_harga); // [✔️ Menyimpan referensi TextView total harga]
        btn_tambah_order = findViewById(R.id.btn_add_orderan);

        tvNama.setText(namaKontak);
        tvNoHp.setText("+ " + noHp);

        // Setup RecyclerView
        RecyclerView rvLayanan = findViewById(R.id.rv_layanan_detail_orderan);
        rvLayanan.setLayoutManager(new LinearLayoutManager(this));
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("durasi")
                .document(durasi)
                .collection("layanan")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    layananList = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Log.d("Firestore", "Isi dokumen: " + doc.getData());
                        String namaLayanan = doc.getId();
                        Double harga_per_kg = doc.getDouble("harga");
                        String durasiLayanan = doc.getString("durasi");
                        layananList.add(new LayananItem(
                                namaLayanan,
                                harga_per_kg,
                                durasiLayanan,
                                R.drawable.baseline_dry_cleaning_24
                        ));
                    }

                    // [✔️ Simpan adapter sebagai global]
                    adapter = new LayananPilihAdapter(layananList);

                    // [✔️ Set listener untuk update total harga]
                    adapter.setOnTotalChangeListener(total -> {
                        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
                        String formattedTotal = rupiahFormat.format(total);
                        Log.i("totalharga", formattedTotal);
                        tvTotalHarga.setText("Total: " + formattedTotal);
                        totalHarga = total; // simpan sebagai double
                    });

                    rvLayanan.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Log.e("DetailKontakActivity", "Gagal mengambil data layanan: " + e.getMessage());
                });
        btn_tambah_order.setOnClickListener(v -> showBasicDialog());
        }
    private void showBasicDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin melanjutkan?")
                .setPositiveButton("Ya", (dialog, which) -> {
                    if (adapter != null) { // [✔️ Pastikan adapter tidak null]
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss", Locale.getDefault());
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8")); // atau "Asia/Makassar" juga bisa
                        tanggal = sdf.format(new Date());

                        Calendar calendar = Calendar.getInstance();
                        calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(durasiJam));
                        estiminasiSelesai = sdf.format(calendar.getTime());
                        List<LayananItem> selectedLayanan = adapter.getSelectedLayanan();
                        Intent intent = new Intent(DetailKontakActivity.this, DetailOrderanActivity.class);

                        OrderItem orderItem = new OrderItem();
                        orderItem.setBelum_bayar(belum_bayar); // ✅ set nilai dari variabel kamu
                        orderItem.setBelum_siap(belum_siap);
                        sendDetailOrder();

                        orderItem.setNama_pelanggan(namaKontak);
                        orderItem.setNo_hp(noHp);
                        orderItem.setEst_selesai(estiminasiSelesai);
                        orderItem.setNo_nota(noNota);
                        orderItem.setBelum_bayar(belum_bayar);
                        orderItem.setTanggal(tanggal);
                        orderItem.setJenis_durasi(durasi);

                        Object total = totalHarga;
                        double totalBayar = 0.0;
                        Log.w("fetchOrders", String.valueOf(total));

//                                Log.w("fetchOrders", (String) total);
                        if (total instanceof Number) {
                            totalBayar = ((Number) total).doubleValue();
                        } else if (total instanceof String) {
                            try {
                                totalBayar = Double.parseDouble((String) total);
                            } catch (NumberFormatException e) {
                                Log.w("fetchOrders", "Format total_bayar salah di dokumen: ");
                            }
                        }

                        orderItem.setTotal_bayar(totalBayar);

                        // Tambahkan ke list jika semua aman
                        Gson gson = new Gson();
                        String json = gson.toJson(orderItem);
                        Log.i("fetchOrders", json);
                        orderList.add(orderItem);

                        intent.putExtra("order", orderItem); // ✅ kirim order-nya
                        intent.putExtra("selectedLayanan", (Serializable) selectedLayanan);
                        intent.putExtra("totalHarga", totalHarga); // [✔️ Kirim total harga]
                        intent.putExtra("nama", namaKontak);
                        intent.putExtra("noHp", noHp);
                        intent.putExtra("alamat", alamat);
                        intent.putExtra("namaDurasi", durasi);
                        intent.putExtra("tanggalMasuk", tanggal);
                        intent.putExtra("estiminasiSelesai",estiminasiSelesai);
                        intent.putExtra("nota", noNota);
//                        intent.putExtra("no_hp", tvNoHp.getText().toString());
                        intent.putExtra("selectedLayanan", (Serializable) selectedLayanan); // [✔️ Kirim layanan yang dipilih]
                        for (LayananItem item : selectedLayanan) {
                            Log.i("apadipilih",
                                    "Nama: " + item.getNama() +
                                            ", Harga: " + item.getJumlah_kg() +
                                            ", Durasi: " + item.getDurasi() +
                                            ", Total Harga: " + item.getTotal_harga() +
                                            ", JumlahKg: " + item.getJumlah_kg());
                        }
                        Log.i("apadipilih", new Gson().toJson(selectedLayanan));
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Tidak", (dialog, which) -> {});
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(d -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this,R.color.black));
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this,R.color.black));
        });
        dialog.show();
    }
    public void sendDetailOrder () {
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        // Ambil tanggal hari ini
        String today = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        // Ambil tanggal terakhir disimpan
        String lastDate = prefs.getString("lastOrderDate", "");
        // Cek apakah hari ini berbeda dari terakhir
        if (!today.equals(lastDate)) {
            // Kalau beda tanggal, reset jmlOrder ke 1
            editor.putInt("jmlOrder", 1);
            editor.putString("lastOrderDate", today);
            editor.apply();
        }
        // Ambil jmlOrder sekarang
        int jmlOrder = prefs.getInt("jmlOrder", 1);
        Log.i("jmlhorder", String.valueOf(jmlOrder));
        noNota = "CUCI-" + new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date()) + jmlOrder;
        editor.putInt("jmlOrder", jmlOrder + 1);
        editor.apply();
        //estiminasi selesai
        Map<String, Object> pesanan = new HashMap<>();
        pesanan.put("no_nota", noNota);
        pesanan.put("tanggal", tanggal);
        pesanan.put("jenis_durasi", durasi);
        pesanan.put("total_bayar", totalHarga);
        pesanan.put("est_selesai", estiminasiSelesai);
        pesanan.put("nama_pelanggan", tvNama.getText().toString());
        pesanan.put("no_hp", tvNoHp.getText().toString());
        pesanan.put("belum_bayar", belum_bayar);
        pesanan.put("belum_siap", belum_siap);
        pesanan.put("belum_selesai", belum_bayar);
        if (alamat != null) {
//            alamat = tvAlamat.getText().toString().trim();
//            if (!alamat.isEmpty()) {
                pesanan.put("alamat", alamat);
//            }
        }
        db.collection("users")
                .document(userId)
                .collection("pesanan")
                .document("statusPesanan")
                .collection("antrian")
                .document(noNota)
                .set(pesanan)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", durasiJam);
                    Log.d("Firestore", "Pesanan utama berhasil dikirim");
                    // Setelah pesanan disimpan, simpan layanan-layanan
                    List<LayananItem> layananTerpilih = adapter.getSelectedLayanan();
                    CollectionReference layananRef = db.collection("users")
                            .document(userId)
                            .collection("pesanan")
                            .document("statusPesanan")
                            .collection("antrian")
                            .document(noNota)
                            .collection("layanan");
                    for (LayananItem item : layananTerpilih) {
                        Map<String, Object> layananData = new HashMap<>();
                        layananData.put("nama", item.getNama());
                        layananData.put("durasi", item.getDurasi());
                        layananData.put("harga_per_kg", (item.getHarga_per_kg()));
                        layananData.put("jumlah_kg", item.getJumlah_kg());
                        layananData.put("total_harga", (item.getHarga_per_kg()) * item.getJumlah_kg());
                        Log.d("Firestore", item.getTotal_harga()+ "\n"+ item.getJumlah_kg());
                        layananRef.add(layananData)
                                .addOnSuccessListener(docRef -> {
                                    Log.d("Firestore", "Layanan berhasil ditambahkan");
                                    Log.d("FirestorePath", "Path: users/" + userId + "/pesanan/" + noNota + "/layanan");
                                })
                                .addOnFailureListener(e -> Log.e("Firestore", "Gagal menambahkan layanan", e));
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Gagal mengirim pesanan", e));
    }
}

