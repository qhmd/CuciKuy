package com.example.cucikuy.Order;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cucikuy.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private ArrayList<OrderItem> orderList;

    public OrderAdapter(ArrayList<OrderItem> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_antrian, parent, false); // item_order.xml adalah layout untuk 1 data
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderItem order = orderList.get(position);
        Gson gson = new Gson();
        String json = gson.toJson(order);
        Log.i("dataadapter", json);
        holder.noNota.setText(order.getNo_nota());
        holder.jenisDurasi.setText(order.getJenis_durasi());
        holder.namaPengguna.setText(order.getNama_pelanggan());
        holder.tanggalMasuk.setText("Masuk : " + order.getTanggal());
        holder.estimasiSelesai.setText("Est Sel : "+ order.getEst_selesai());
        holder.totalPembayaran.setText(order.getTotal_bayar());
        holder.statusPembayaran.setText(order.isBelum_bayar() ? "Belum Bayar" : "Sudah Bayar");
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // INI YANG BELUM ADA: Tambahkan ViewHolder di sini
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