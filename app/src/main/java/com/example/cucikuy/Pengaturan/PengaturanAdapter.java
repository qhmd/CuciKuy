package com.example.cucikuy.Pengaturan;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cucikuy.R;

import java.util.List;

public class PengaturanAdapter extends RecyclerView.Adapter<PengaturanAdapter.ViewHolder> {
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pengaturan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PengaturanItem pengaturanItem = pengaturanItems.get(position);
        Log.i("datapengaturan", pengaturanItem.getJudul());
        holder.tvJudul.setText(pengaturanItem.getJudul());
        holder.tvDeskripsi.setText(pengaturanItem.getDeskripsi());
        holder.ivIcon.setImageResource(pengaturanItem.getIconResId());
        holder.itemView.setOnClickListener(v -> {
            String judul = pengaturanItem.getJudul();
            Context context = v.getContext();
            switch (judul) {
                case "Pengaturan Akun":
                    context.startActivity(new Intent(context, PengaturanAkunActivity.class));
                    break;
                case "Pengaturan Toko":
                    context.startActivity(new Intent(context, PengaturanTokoActivity.class));
                    break;
                case "Pengaturan Durasi":
                    context.startActivity(new Intent(context, PengaturanDurasiActivity.class));
                    break;
                case "Pengaturan Layanan":
                    context.startActivity(new Intent(context, PengaturanLayananActivity.class));
                    break;
                case "Pengaturan Parfum":
                    context.startActivity(new Intent(context, PengaturanParfumActivity.class));
                    break;
                case "Pengaturan Diskon":
                    context.startActivity(new Intent(context, PengaturanDiskonActivity.class));
                    break;
                case "Pengaturan Pelanggan":
                    context.startActivity(new Intent(context, PengaturanPelangganActivity.class));
                    break;
                case "Pengaturan nota":
                    context.startActivity(new Intent(context, PengaturanNotaActivity.class));
                    break;
                default:
                    Toast.makeText(context, "Pengaturan belum tersedia", Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return pengaturanItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJudul, tvDeskripsi;
        ImageView ivIcon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJudul = itemView.findViewById(R.id.tvJudul);
            tvDeskripsi = itemView.findViewById(R.id.tvDeskripsi);
            ivIcon = itemView.findViewById(R.id.iconPengaturan);
        }
    }
    private List<PengaturanItem> pengaturanItems ;

    public  PengaturanAdapter (List<PengaturanItem> pengaturanItems) {
        this.pengaturanItems = pengaturanItems;
    }
}
