package com.example.cucikuy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cucikuy.Layanan.LayananItem;
import com.example.cucikuy.Order.OrderItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;

public class WaNota extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private OrderItem order;
    private ArrayList<LayananItem> selectedLayanan;
    private String namaLaundry = "", alamatLaundry = "", noHpLaundry = "";

    private boolean fromPembayaran = false;
    private String noNotaPembayaran, namaPembayaran, noHpPembayaran;
    private double totalHargaPembayaran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fromPembayaran = getIntent().getBooleanExtra("from_pembayaran", false);


        if (fromPembayaran) {
            // Ambil data dari intent
            noNotaPembayaran = getIntent().getStringExtra("noNota");
            namaPembayaran = getIntent().getStringExtra("nama");
            totalHargaPembayaran = getIntent().getDoubleExtra("totalHarga", 0.0);
            noHpPembayaran = getIntent().getStringExtra("noHp");

            loadProfilLaundryAndSendPayment();
        } else {
            // Ambil data dari Intent untuk pengiriman nota lengkap
            order = (OrderItem) getIntent().getSerializableExtra("order");
            selectedLayanan = (ArrayList<LayananItem>) getIntent().getSerializableExtra("selectedLayanan");
            noHpPembayaran = getIntent().getStringExtra("noHp");
            Log.i("inihp", noHpPembayaran);
            if (order == null || selectedLayanan == null) {
                Toast.makeText(this, "Data order atau layanan tidak tersedia", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            loadProfilLaundry();
        }
    }

    private void loadProfilLaundry() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        namaLaundry = documentSnapshot.getString("nama_outlet");
                        alamatLaundry = documentSnapshot.getString("alamat");
                        noHpLaundry = documentSnapshot.getString("no_hp");
                    }
                    Log.i("lilbro",namaLaundry );
                    Log.i("lilbro",alamatLaundry );
                    Log.i("lilbro",noHpLaundry );
                    loadNota(); // Lanjut setelah dapat profil
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal memuat profil laundry", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void loadProfilLaundryAndSendPayment() {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        namaLaundry = documentSnapshot.getString("nama_outlet");
                        alamatLaundry = documentSnapshot.getString("alamat");
                        noHpLaundry = documentSnapshot.getString("no_hp");

                        String pesan = "Terima kasih atas pembayaran anda!\n\n"
                                + namaLaundry + "\n"
                                + "Alamat : " + alamatLaundry + "\n"
                                + "No HP : " + noHpLaundry + "\n\n"
                                + noNotaPembayaran + "\n"
                                + "Pelanggan : " + namaPembayaran + "\n"
                                + "Harga Total : Rp " + FormatIDR.FormatIDR(totalHargaPembayaran) + "\n"
                                + "Status : Lunas";
                        Log.i("tesbro", pesan + noHpLaundry );
                        kirimKeWhatsApp(noHpPembayaran, pesan);
                    } else {
                        Toast.makeText(this, "Data laundry tidak ditemukan", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal memuat profil laundry", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private void loadNota() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users")
                .document(userId)
                .collection("nota")
                .document("catatan_nota")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String isiCatatan = documentSnapshot.getString("isi_catatan");
                        if (isiCatatan != null) {
                            String nota = buildNotaWhatsApp(order, selectedLayanan, isiCatatan);
                            if (order.getNo_hp() != null) {
                                kirimKeWhatsApp(order.getNo_hp(), nota);
                            } else {
                                kirimKeWhatsApp(noHpPembayaran, nota);
                            }

                        } else {
                            Toast.makeText(this, "Isi catatan kosong", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    } else {
                        Toast.makeText(this, "Catatan belum tersedia", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal memuat catatan: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });
    }

    private String buildNotaWhatsApp(OrderItem order, ArrayList<LayananItem> layananList, String ketentuan) {
        StringBuilder sb = new StringBuilder();
        sb.append(namaLaundry).append("\n");
        sb.append("Alamat : ").append(alamatLaundry).append("\n");
        sb.append("No HP : ").append(noHpLaundry).append("\n\n");

        sb.append(order.getNo_nota()).append("\n");
        sb.append("Pelanggan : ").append(order.getNama_pelanggan()).append("\n");
        sb.append("No Handphone : ").append(order.getNo_hp()).append("\n");
        sb.append("Layanan : ").append(order.getJenis_durasi()).append("\n");
        sb.append("Masuk : ").append(order.getTanggal()).append("\n");
        sb.append("Est Selesai : ").append(order.getEst_selesai()).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("LAYANAN\n");

        for (LayananItem item : layananList) {
            sb.append(item.getNama()).append(" (").append(item.getDurasi()).append(")\n");
            sb.append(item.getJumlah_kg()).append(" kg x Rp ")
                    .append(FormatIDR.FormatIDR(item.getHarga_per_kg())).append(" = ")
                    .append("Rp ").append(FormatIDR.FormatIDR(item.getTotal_harga())).append("\n");
        }

        sb.append("----------------------------------------\n");
        sb.append("PEMBAYARAN\n");
        sb.append("Total Harga Akhir : Rp ").append(FormatIDR.FormatIDR(order.getTotal_bayar())).append("\n");
        sb.append("Status : ").append(order.isBelum_bayar() ? "Belum Bayar" : "Sudah Bayar").append("\n\n");
        sb.append(ketentuan);

        return sb.toString();
    }

    private void kirimKeWhatsApp(String nomorTujuan, String pesan) {
        Log.i("lilbro", nomorTujuan);
//        Log.i("lilbro", pesan);
        nomorTujuan = nomorTujuan.replace("+", "").replace(" ", "");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = "https://api.whatsapp.com/send?phone=62" + nomorTujuan + "&text=" + Uri.encode(pesan);
        intent.setData(Uri.parse(url));
        startActivity(intent);
        finish(); // tutup activity setelah kirim
    }
}
