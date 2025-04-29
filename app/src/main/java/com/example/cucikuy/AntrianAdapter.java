package com.example.cucikuy;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

public class AntrianAdapter extends RecyclerView.Adapter<AntrianAdapter.AntrianViewHolder> {

    private ArrayList<OrderItem> orderList;

    public AntrianAdapter(ArrayList<OrderItem> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public AntrianViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_antrian, parent, false); // item_order.xml adalah layout untuk 1 data
        return new AntrianViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AntrianViewHolder holder, int position) {
        OrderItem order = orderList.get(position);
        Gson gson = new Gson();
        String json = gson.toJson(order);
        Log.i("dataadapter", json);
        holder.noNota.setText(order.getNo_nota());
        holder.jenisDurasi.setText(order.getJenis_durasi());
        holder.namaPengguna.setText(order.getNama_pelanggan());
        holder.tanggalMasuk.setText(order.getTanggal());
        holder.estimasiSelesai.setText(order.getEst_selesai());
        holder.totalPembayaran.setText("Rp " + order.getTotal_bayar());
        holder.statusPembayaran.setText(order.isBelum_bayar() ? "Belum Bayar" : "Sudah Bayar");
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // INI YANG BELUM ADA: Tambahkan ViewHolder di sini
    static class AntrianViewHolder extends RecyclerView.ViewHolder {
        ImageView gambar;
        TextView noNota, jenisDurasi, namaPengguna, tanggalMasuk, estimasiSelesai, totalPembayaran, statusPembayaran;

        public AntrianViewHolder(@NonNull View itemView) {
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
