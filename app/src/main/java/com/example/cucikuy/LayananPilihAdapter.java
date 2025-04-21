package com.example.cucikuy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class LayananPilihAdapter extends RecyclerView.Adapter<LayananPilihAdapter.ViewHolder> {

    private List<LayananItem> layananList;

    public LayananPilihAdapter(List<LayananItem> layananList) {
        this.layananList = layananList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvHarga, tvDurasi, tvJumlahKg;
        ImageView imgIcon;
        Button btnTambah, btnKurang;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama_layanan);
            tvHarga = itemView.findViewById(R.id.tv_harga_layanan);
            tvDurasi = itemView.findViewById(R.id.tv_durasi_layanan);
            tvJumlahKg = itemView.findViewById(R.id.tv_jumlah_kg);
            imgIcon = itemView.findViewById(R.id.img_icon);
            btnTambah = itemView.findViewById(R.id.btn_tambah);
            btnKurang = itemView.findViewById(R.id.btn_kurang);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layanan_pilih, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LayananItem item = layananList.get(position);
        holder.tvNama.setText(item.getNama());
        holder.tvHarga.setText("Rp " + item.getHarga());
        holder.tvDurasi.setText(item.getDurasi());
        holder.imgIcon.setImageResource(item.getIconLaundry());

        // Set default jumlah kg
        double[] jumlahKg = {0.01}; // Gunakan array supaya bisa diubah dalam inner class
        holder.tvJumlahKg.setText(String.format(Locale.US, "%.2f kg", jumlahKg[0]));

        // Tombol tambah
        holder.btnTambah.setOnClickListener(v -> {
            jumlahKg[0] += 0.01;
            holder.tvJumlahKg.setText(String.format(Locale.US, "%.2f kg", jumlahKg[0]));
        });

        // Tombol kurang
        holder.btnKurang.setOnClickListener(v -> {
            if (jumlahKg[0] > 0.01) {
                jumlahKg[0] -= 0.01;
                holder.tvJumlahKg.setText(String.format(Locale.US, "%.2f kg", jumlahKg[0]));
            }
        });
    }

    @Override
    public int getItemCount() {
        return layananList.size();
    }
}
