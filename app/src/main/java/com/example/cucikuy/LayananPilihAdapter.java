package com.example.cucikuy;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LayananPilihAdapter extends RecyclerView.Adapter<LayananPilihAdapter.ViewHolder> {

    private final List<LayananItem> layananList;
    private final double[] jumlahKgArray;
    private OnTotalChangeListener onTotalChangeListener;

    public LayananPilihAdapter(List<LayananItem> layananList) {
        this.layananList = layananList;
        this.jumlahKgArray = new double[layananList.size()];
        for (int i = 0; i < jumlahKgArray.length; i++) {
            jumlahKgArray[i] = 0.00;
        }
    }

    public interface OnTotalChangeListener {
        void onTotalChangedFormatted(String formattedTotal);
    }

    public void setOnTotalChangeListener(OnTotalChangeListener listener) {
        this.onTotalChangeListener = listener;
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layanan_pilih, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LayananItem item = layananList.get(position);

        // Menampilkan data layanan
        holder.tvNama.setText(item.getNama());
        holder.tvDurasi.setText(item.getDurasi());
        holder.imgIcon.setImageResource(item.getIconLaundry());

        // Menentukan harga per layanan berdasarkan berat (kg)
        double jumlahKg = jumlahKgArray[position];
        double hargaPerKg = Double.parseDouble(item.getHarga()); // harga per kg
        double totalHarga = jumlahKg * hargaPerKg; // total harga

        // Menampilkan total harga untuk layanan ini
        holder.tvHarga.setText("Rp " + NumberFormat.getInstance(new Locale("in", "ID")).format(totalHarga));

        holder.tvJumlahKg.setText(removeTrailingZeros(jumlahKg));

        // Jika ada TextWatcher yang sebelumnya ditambahkan, hapus terlebih dahulu
        if (holder.tvJumlahKg.getTag() instanceof TextWatcher) {
            holder.tvJumlahKg.removeTextChangedListener((TextWatcher) holder.tvJumlahKg.getTag());
        }

        // Menambahkan TextWatcher untuk edit jumlahKg
        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                int pos = holder.getAdapterPosition();
                if (pos == RecyclerView.NO_POSITION) return;

                try {
                    double value = Double.parseDouble(s.toString());
                    jumlahKgArray[pos] = value;
                } catch (NumberFormatException e) {
                    jumlahKgArray[pos] = 0;
                }
                updateTotalHarga();
            }
        };

        holder.tvJumlahKg.addTextChangedListener(watcher);
        holder.tvJumlahKg.setTag(watcher);

        // Tombol tambah dan kurang untuk mengubah jumlahKg
        holder.btnTambah.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                jumlahKgArray[pos] += 0.01;
                holder.tvJumlahKg.setText(removeTrailingZeros(jumlahKgArray[pos]));
            }
        });

        holder.btnKurang.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && jumlahKgArray[pos] > 0.00) {
                jumlahKgArray[pos] -= 0.01;
                holder.tvJumlahKg.setText(removeTrailingZeros(jumlahKgArray[pos]));
            }
        });
    }


    private void updateTotalHarga() {
        int total = 0;
        for (int i = 0; i < layananList.size(); i++) {
            try {
                int harga = Integer.parseInt(layananList.get(i).getHarga());
                total += (int) (harga * jumlahKgArray[i]);
            } catch (NumberFormatException ignored) {}
        }

        if (onTotalChangeListener != null) {
            NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("in", "ID"));
            String formattedTotal = rupiahFormat.format(total);
            onTotalChangeListener.onTotalChangedFormatted(formattedTotal);
        }
    }

    public List<LayananItem> getSelectedLayanan() {
        List<LayananItem> selected = new ArrayList<>();
        for (int i = 0; i < layananList.size(); i++) {
            if (jumlahKgArray[i] > 0) {
                LayananItem item = layananList.get(i);
                item.setJumlahKg(jumlahKgArray[i]);
                selected.add(item);
            }
        }
        return selected;
    }

    @Override
    public int getItemCount() {
        return layananList.size();
    }

    private String removeTrailingZeros(double value) {
        if (value == (long) value) {
            return String.format(Locale.US, "%d", (long) value);
        } else {
            return String.format(Locale.US, "%.2f", value);
        }
    }
    // Menambahkan metode untuk mengambil harga total berdasarkan jumlah kg yang dimasukkan
    public double getTotalHargaPerLayanan(int position) {
        if (position < 0 || position >= layananList.size()) {
            return 0.0; // Menghindari IndexOutOfBoundsException
        }

        double jumlahKg = jumlahKgArray[position];
        double hargaPerKg = Double.parseDouble(layananList.get(position).getHarga()); // Harga per kg
        return jumlahKg * hargaPerKg; // Mengembalikan total harga per layanan
    }

}