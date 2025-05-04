package com.example.cucikuy.Layanan;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.cucikuy.FormatIDR;
import com.example.cucikuy.R;

import java.util.List;

public class LayananAdapter extends RecyclerView.Adapter<LayananAdapter.ViewHolder> {
    private List<LayananItem> layananList;

    public LayananAdapter(List<LayananItem> layananList) {
        this.layananList = layananList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvHarga, tvDurasi;
        ImageView imgIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama_layanan);
            tvHarga = itemView.findViewById(R.id.tv_harga_layanan);
            tvDurasi = itemView.findViewById(R.id.tv_nama_durasi);
            imgIcon = itemView.findViewById(R.id.img_icon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layanan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LayananItem item = layananList.get(position);
//        Log.i("tessbro", String.valueOf(item.getHarga_per_kg()));
        holder.tvNama.setText(item.getNama());
        holder.tvHarga.setText("Rp " + FormatIDR.FormatIDR(item.getHarga_per_kg()));
        holder.tvDurasi.setText(item.getDurasi());
        holder.imgIcon.setImageResource(item.getIconLaundry());
        // Kalau kamu mau set gambar dinamis, bisa di sini juga
        // holder.imgIcon.setImageResource(item.getIconRes());
    }

    @Override
    public int getItemCount() {
        return layananList.size();
    }
}