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

public class KontakAdapter extends RecyclerView.Adapter<KontakAdapter.ViewHolder> {
    private List<KontakItem> kontakItems ;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kontak, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KontakItem kontakItem = kontakItems.get(position);
        holder.ivIcon.setImageResource(kontakItem.getIconKontak());
        holder.tvNama.setText(kontakItem.getNama());
        holder.tvNoHp.setText("+ " + kontakItem.getNoTelpon());
        holder.tvAlamat.setText(kontakItem.getAlamat());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null ) {
                listener.onItemClick(kontakItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return kontakItems.size();
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvNoHp,tvAlamat;
        ImageView ivIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iconPerson);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvNoHp = itemView.findViewById(R.id.tvNoHp);
            tvAlamat = itemView.findViewById(R.id.tvAlamat);
        }
    }

    public  KontakAdapter (List<KontakItem> kontakItems) {
        this.kontakItems = kontakItems;
    }
    public interface OnItemClickListener {
        void onItemClick(KontakItem item);
    }
}
