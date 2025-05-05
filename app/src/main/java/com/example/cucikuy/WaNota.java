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

import java.util.ArrayList;

public class WaNota extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private OrderItem order;
    private ArrayList<LayananItem> selectedLayanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ambil data dari Intent
        order = (OrderItem) getIntent().getSerializableExtra("order");
        selectedLayanan = (ArrayList<LayananItem>) getIntent().getSerializableExtra("selectedLayanan");

        if (order == null || selectedLayanan == null) {
            Toast.makeText(this, "Data order atau layanan tidak tersedia", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadNota(); // Ambil ketentuan dan langsung kirim ke WhatsApp
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
                            kirimKeWhatsApp(order.getNo_hp(), nota);
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
        sb.append(order.getNo_nota()).append("\n");
        sb.append("Pelanggan : ").append(order.getNama_pelanggan()).append("\n");
        sb.append("No Handphone : ").append(order.getNo_hp()).append("\n");
//      sb.append("Alamat : ").append(order.getAlamat()).append("\n");
        sb.append("Layanan : ").append(order.getJenis_durasi()).append("\n");
        sb.append("Masuk : ").append(order.getTanggal()).append("\n");
        sb.append("Est Selesai : ").append(order.getEst_selesai()).append("\n");
        sb.append("----------------------------------------\n");
        sb.append("LAYANAN\n");

        for (LayananItem item : layananList) {
            sb.append(item.getNama()).append(" (").append(item.getDurasi()).append(")\n");
            sb.append(item.getJumlah_kg()).append(" kg x Rp ")
                    .append(FormatIDR.FormatIDR(item.getHarga_per_kg())).append(" = ")
                    .append( "Rp "+ FormatIDR.FormatIDR(item.getTotal_harga())).append("\n");
        }

        sb.append("----------------------------------------\n");
        sb.append("PEMBAYARAN\n");
        sb.append("Total Harga Akhir : Rp ").append(FormatIDR.FormatIDR(order.getTotal_bayar())).append("\n");
        sb.append("Status : ").append(order.isBelum_bayar() ? "Belum Bayar" : "Sudah Bayar").append("\n\n");
        sb.append(ketentuan);

        return sb.toString();
    }

    private void kirimKeWhatsApp(String nomorTujuan, String pesan) {
        nomorTujuan = nomorTujuan.replace("+", "").replace(" ", "");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = "https://api.whatsapp.com/send?phone=62" + nomorTujuan + "&text=" + Uri.encode(pesan);
        intent.setData(Uri.parse(url));
        startActivity(intent);
        finish(); // tutup activity
    }
}
