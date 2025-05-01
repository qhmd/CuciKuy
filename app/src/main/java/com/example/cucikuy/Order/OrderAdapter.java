package com.example.cucikuy.Order;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cucikuy.Layanan.LayananItem;
import com.example.cucikuy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private Context context;
    private ArrayList<OrderItem> orderList;
    private OnItemClickListener listener;
    private FirebaseFirestore db;

    public interface OnItemClickListener {
        void onItemClick(OrderItem item);
    }

    public OrderAdapter(ArrayList<OrderItem> orderList, OnItemClickListener listener) {
        this.orderList = orderList;
        this.listener = listener;
        this.db = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_order_antrian, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem order = orderList.get(position);
        Gson gson = new Gson();
        Log.i("dataadapter", gson.toJson(order));

        holder.noNota.setText(order.getNo_nota());
        holder.jenisDurasi.setText(order.getJenis_durasi());
        holder.namaPengguna.setText(order.getNama_pelanggan());
        holder.tanggalMasuk.setText("Masuk : " + order.getTanggal());
        holder.estimasiSelesai.setText("Est Sel : " + order.getEst_selesai());
        holder.totalPembayaran.setText(order.getTotal_bayar());
        holder.statusPembayaran.setText(order.isBelum_bayar() ? "Belum Bayar" : "Sudah Bayar");

        holder.itemView.setOnClickListener(v -> {
            Log.i("ketekan", "Item diklik");
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String noNota = order.getNo_nota();
            // Ambil layanan seperti biasa
            db.collection("users")
                    .document(userId)
                    .collection("pesanan")
                    .document("statusPesanan")
                    .collection("antrian")
                    .document(noNota)
                    .collection("layanan")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        ArrayList<LayananItem> selectedLayananList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            LayananItem layanan = doc.toObject(LayananItem.class);
                            Log.d("FirestoreData", "Layanan: " + doc.getData().toString());
                            selectedLayananList.add(layanan);
                            Gson gsonbro = new Gson();
                            String inidia = gsonbro.toJson(layanan);
                            Log.i("browtf", inidia);
                        }
                        // Setelah ambil layanan, ambil data dari dokumen antrian
                        db.collection("users")
                                .document(userId)
                                .collection("pesanan")
                                .document("statusPesanan")
                                .collection("antrian")
                                .document(noNota)
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    if (documentSnapshot.exists()) {
                                        // Menampilkan semua isi dokumen
                                        Map<String, Object> data = documentSnapshot.getData();
                                        if (data != null) {
                                            for (Map.Entry<String, Object> entry : data.entrySet()) {
                                                Log.i("antrianFullData", entry.getKey() + ": " + entry.getValue());
                                            }
                                        }
                                        // Kalau masih tetap mau ambil sebagian data seperti sebelumnya
                                        String catatan = documentSnapshot.getString("catatan");
                                        String metodePembayaran = documentSnapshot.getString("metodePembayaran");
                                        String alamatAntar = documentSnapshot.getString("alamatAntar");
                                        // Kirim ke DetailOrderanActivity
                                        Intent intent = new Intent(context, DetailOrderanActivity.class);
                                        intent.putExtra("selectedLayanan", selectedLayananList);
                                        intent.putExtra("catatan", catatan);
                                        intent.putExtra("metodePembayaran", metodePembayaran);
                                        intent.putExtra("alamatAntar", alamatAntar);

                                        Gson gson5 = new Gson();
                                        String lihatisi = gson5.toJson(selectedLayananList);
                                        Log.i("liatnoss", lihatisi);

                                        context.startActivity(intent);
                                    } else {
                                        Log.w("antrianFullData", "Dokumen tidak ditemukan");
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Firebase", "Gagal mengambil data antrian", e);
                                });
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Firebase", "Gagal mengambil data layanan", e);
                    });
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView gambar;
        TextView noNota, jenisDurasi, namaPengguna, tanggalMasuk, estimasiSelesai, totalPembayaran, statusPembayaran;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            gambar = itemView.findViewById(R.id.iconOrderan);
            noNota = itemView.findViewById(R.id.textNoNota);
            jenisDurasi = itemView.findViewById(R.id.textJenisDurasi);
            namaPengguna = itemView.findViewById(R.id.textNamaPengguna);
            tanggalMasuk = itemView.findViewById(R.id.textTanggalMasuk);
            estimasiSelesai = itemView.findViewById(R.id.textEstimasiSelesai);
            totalPembayaran = itemView.findViewById(R.id.textTotalPembayaran);
            statusPembayaran = itemView.findViewById(R.id.textStatusPembayaran);
        }
    }
}
