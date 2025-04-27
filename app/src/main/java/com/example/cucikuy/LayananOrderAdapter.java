package com.example.cucikuy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LayananOrderAdapter extends RecyclerView.Adapter<LayananOrderAdapter.ViewHolder> {
    private List<LayananItem> layananList;

    public LayananOrderAdapter(List<LayananItem> layananList) {
        this.layananList = layananList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvHarga, tvDurasi, tvJumlahKg, tvHargaLayanan;
        ImageView imgIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvHarga = itemView.findViewById(R.id.tvHarga);
            tvDurasi = itemView.findViewById(R.id.tvDurasi);
            tvJumlahKg = itemView.findViewById(R.id.tvJumlahKg);
            imgIcon = itemView.findViewById(R.id.img_icon);
            tvHargaLayanan = itemView.findViewById(R.id.tvHargaLayanan);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layanan_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LayananItem item = layananList.get(position);
        holder.tvNama.setText(item.getNama());
        holder.tvHarga.setText("Rp " + item.getHarga());
        holder.tvDurasi.setText(item.getDurasi() + " hari");
        holder.tvJumlahKg.setText(item.getJumlahKg() + " Kg");
        holder.imgIcon.setImageResource(item.getIconLaundry());
        holder.tvHargaLayanan.setText("Rp " + item.getTotalHarga());
    }
    @Override
    public int getItemCount() {
        return layananList.size();
    }
}
