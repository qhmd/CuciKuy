package com.example.cucikuy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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
        holder.tvJudul.setText(pengaturanItem.getJudul());
        holder.tvDeskripsi.setText(pengaturanItem.getDeskripsi());
        holder.ivIcon.setImageResource(pengaturanItem.getIconResId());
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
