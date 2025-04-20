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
    private List<DurasiItem> durasiItems;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_durasi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DurasiItem durasiItem = durasiItems.get(position);
        holder.tvJam.setText(durasiItem.getJamDurasi());
        holder.tvTextJam.setText(durasiItem.getNameDurasi());
        holder.editDuration.setImageResource(durasiItem.getEditDurasi());;
        holder.deleteDuration.setImageResource(durasiItem.getDeleteDurasi());
    }

    @Override
    public int getItemCount() {
        return durasiItems.size();
    }
    public  class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJam, tvTextJam;
        ImageView editDuration, deleteDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJam = itemView.findViewById(R.id.tvJam);
            tvTextJam = itemView.findViewById(R.id.tvTextJam);
            editDuration = itemView.findViewById(R.id.editDuration);
            deleteDuration = itemView.findViewById(R.id.deleteDuration);
        }

        public interface OnItemClickListener {
            void onItemClick(DurasiItem item);
        }
    }
    public  DurasiAdapater (List<DurasiItem> durasiItems) {
        this.durasiItems = durasiItems;
    }

}
