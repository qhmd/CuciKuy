package com.example.cucikuy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class DurasiAdapater extends  RecyclerView.Adapter<DurasiAdapater.ViewHolder> {
    private final List<DurasiItem> durasiItems;
    private int layoutType;

    public static final int LAYOUT_DEFAULT = 0;
    public static final int LAYOUT_DIALOG = 1;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(DurasiItem item);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public  DurasiAdapater (List<DurasiItem> durasiItems, int layoutType) {
        this.durasiItems = durasiItems;
        this.layoutType = layoutType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = (layoutType == LAYOUT_DIALOG) ?
                R.layout.item_durasi_dialog : R.layout.item_durasi;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DurasiItem durasiItem = durasiItems.get(position);
        holder.tvJam.setText(durasiItem.getJamDurasi());
        holder.tvNamaDurasi.setText(durasiItem.getNameDurasi());

        if (layoutType != LAYOUT_DIALOG) {
            holder.editDuration.setImageResource(durasiItem.getEditDurasi());
            holder.deleteDuration.setImageResource(durasiItem.getDeleteDurasi());
            holder.editDuration.setVisibility(View.VISIBLE);
            holder.deleteDuration.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return durasiItems.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJam, tvNamaDurasi;
        String tvTextJam;
        ImageView editDuration, deleteDuration;
        private OnItemClickListener listener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJam = itemView.findViewById(R.id.tvJam);
            tvTextJam = " Jam";
            tvNamaDurasi = itemView.findViewById(R.id.tvNamaDurasi);
            editDuration = itemView.findViewById(R.id.editDuration);
            deleteDuration = itemView.findViewById(R.id.deleteDuration);
        }
    }
}
