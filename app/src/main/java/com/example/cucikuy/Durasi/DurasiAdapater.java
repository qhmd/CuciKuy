package com.example.cucikuy.Durasi;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cucikuy.R;

import java.util.List;

public class DurasiAdapater extends RecyclerView.Adapter<DurasiAdapater.ViewHolder> {
    private final List<DurasiItem> durasiItems;
    private int layoutType;

    public static final int LAYOUT_DEFAULT = 0;
    public static final int LAYOUT_DIALOG = 1;
    public static final int LAYOUT_FOR_LAYANAN = 2;

    private OnItemClickListener listener;
    private OnEditClickListener editClickListener;
    private OnDeleteClickListener deleteClickListener;

    // Interface untuk klik item
    public interface OnItemClickListener {
        void onItemClick(DurasiItem item);
    }

    // Interface untuk edit
    public interface OnEditClickListener {

        void onEditClick(DurasiItem item);
    }

    // Interface untuk hapus
    public interface OnDeleteClickListener {
        void onDeleteClick(DurasiItem item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public void setOnEditClickListener(OnEditClickListener listener) {
        this.editClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.deleteClickListener = listener;
    }

    public DurasiAdapater(List<DurasiItem> durasiItems, int layoutType) {
        this.durasiItems = durasiItems;
        this.layoutType = layoutType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId;
        switch (layoutType) {
            case LAYOUT_DIALOG:
                layoutId = R.layout.item_durasi_dialog;
                break;
            case LAYOUT_FOR_LAYANAN:
                layoutId = R.layout.item_durasi_layanan;
                break;
            default:
                layoutId = R.layout.item_durasi;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DurasiItem durasiItem = durasiItems.get(position);
        Log.d("AdapterCheck", "tvJam: " + holder.tvJam);
        Log.d("AdapterCheck", "tvNamaDurasi: " + holder.tvNamaDurasi);

        if (layoutType == LAYOUT_FOR_LAYANAN) {
            if (holder.tvNamaDurasi != null) {
                holder.tvNamaDurasi.setText(durasiItem.getNameDurasi());
            }
        } else {
            if (holder.tvJam != null) {
                holder.tvJam.setText(durasiItem.getJamDurasi());
            }
            if (holder.tvNamaDurasi != null) {
                holder.tvNamaDurasi.setText(durasiItem.getNameDurasi());
            }
        }

        // Tampilkan tombol edit/hapus hanya pada layout default
        if (layoutType != LAYOUT_DIALOG && layoutType != LAYOUT_FOR_LAYANAN) {
            if (holder.editDuration != null && holder.deleteDuration != null) {
                holder.editDuration.setImageResource(durasiItem.getEditDurasi());
                holder.deleteDuration.setImageResource(durasiItem.getDeleteDurasi());

                holder.editDuration.setVisibility(View.VISIBLE);
                holder.deleteDuration.setVisibility(View.VISIBLE);

                holder.editDuration.setOnClickListener(v -> {
                    if (editClickListener != null) {
                        Log.i("editDurasi", "ini di klik");
                        editClickListener.onEditClick(durasiItem);
                    }
                });

                holder.deleteDuration.setOnClickListener(v -> {
                    if (deleteClickListener != null) {
                        // Menampilkan dialog konfirmasi
                        new AlertDialog.Builder(v.getContext())
                                .setTitle("Konfirmasi")
                                .setMessage("Apakah Anda yakin ingin menghapus item ini?")
                                .setPositiveButton("Ya", (dialog, which) -> {
                                    // Jika Ya, panggil listener untuk hapus
                                    deleteClickListener.onDeleteClick(durasiItem);
                                })
                                .setNegativeButton("Tidak", (dialog, which) -> {
                                    dialog.dismiss(); // Menutup dialog jika tidak ingin menghapus
                                })
                                .show();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return durasiItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJam, tvNamaDurasi;
        ImageView editDuration, deleteDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJam = itemView.findViewById(R.id.tvJam);
            tvNamaDurasi = itemView.findViewById(R.id.tvNamaDurasi);
            editDuration = itemView.findViewById(R.id.editDuration);
            deleteDuration = itemView.findViewById(R.id.deleteDuration);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(durasiItems.get(position));
                }
            });
        }
    }
}
