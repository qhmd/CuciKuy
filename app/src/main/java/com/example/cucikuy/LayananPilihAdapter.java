package com.example.cucikuy;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.TextWatcher;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class LayananPilihAdapter extends RecyclerView.Adapter<LayananPilihAdapter.ViewHolder> {

    private List<LayananItem> layananList;

    private OnTotalChangeListener totalChangeListener;
    private double[] jumlahKgList;

    public void setOnTotalChangeListener(OnTotalChangeListener listener) {
        this.totalChangeListener = listener;
    }

    public LayananPilihAdapter(List<LayananItem> layananList) {
        this.layananList = layananList;
        this.jumlahKgList = new double[layananList.size()];
        for (int i = 0; i < jumlahKgList.length; i++) {
            jumlahKgList[i] = 0.01;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvHarga, tvDurasi;
        EditText tvJumlahKg;
        ImageView imgIcon;
        ImageButton btnTambah, btnKurang;

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

        holder.tvJumlahKg.setText(String.format(Locale.US, "%.2f kg", item.getJumlahKg()));

        holder.tvJumlahKg.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String clean = s.toString().replace("kg", "").trim();
                try {
                    double value = Double.parseDouble(clean);
                    item.setJumlahKg(value);
                    holder.tvJumlahKg.removeTextChangedListener(this);
                    holder.tvJumlahKg.setText(String.format(Locale.US, "%.2f kg", value));
                    holder.tvJumlahKg.setSelection(holder.tvJumlahKg.getText().toString().length() - 3); // cursor before " kg"
                    holder.tvJumlahKg.addTextChangedListener(this);
                    hitungTotal(); // ⬅️ total berubah di sini
                } catch (NumberFormatException e) {
                    // Invalid input, bisa diabaikan atau ditangani
                }
            }
        });

        holder.btnTambah.setOnClickListener(v -> {
            double current = item.getJumlahKg();
            current += 0.01;
            item.setJumlahKg(current);
            holder.tvJumlahKg.setText(String.format(Locale.US, "%.2f kg", current));
            hitungTotal();
        });

        holder.btnKurang.setOnClickListener(v -> {
            double current = item.getJumlahKg();
            if (current > 0.01) {
                current -= 0.01;
                item.setJumlahKg(current);
                holder.tvJumlahKg.setText(String.format(Locale.US, "%.2f kg", current));
                hitungTotal();
            }
        });
    }

    private void hitungTotal() {
        int total = 0;
        for (LayananItem item : layananList) {
            try {
                int harga = Integer.parseInt(item.getHarga());
                total += item.getJumlahKg() * harga;
            } catch (NumberFormatException e) {
                // skip jika harga tidak valid
            }
        }

        if (totalChangeListener != null) {
            totalChangeListener.onTotalChanged(total);
        }
    }


    public interface OnTotalChangeListener {
        void onTotalChanged(int total);
    }



    @Override
    public int getItemCount() {
        return layananList.size();
    }
}
