package com.example.cucikuy.Layanan;

import com.example.cucikuy.R;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;

public class LayananItem implements Serializable {
    private String nama;
    private String durasi;
    private double harga_per_kg;
    private double jumlah_kg;
    private double total_harga;

    private int iconLaundry; // ini tidak dari Firestore

    public LayananItem() {
        // Diperlukan untuk Firebase
    }

    public LayananItem(String nama, double harga_per_kg, String durasi, int iconLaundry) {
        this.nama = nama;
        this.harga_per_kg = harga_per_kg;
        this.durasi = durasi;
        this.iconLaundry = iconLaundry;
    }

    // Getter & Setter wajib sesuai nama field

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDurasi() {
        return durasi;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }
    @PropertyName("harga_per_kg")
    public double getHarga_per_kg() {
        return harga_per_kg;
    }
    @PropertyName("harga_per_kg")
    public void setHarga_per_kg(Object harga_per_kg) {
        try {
            if (harga_per_kg == null) {
                this.harga_per_kg = 0.0;  // Default
            } else if (harga_per_kg instanceof Double) {
                this.harga_per_kg = (Double) harga_per_kg;
            } else if (harga_per_kg instanceof String) {
                this.harga_per_kg = Double.parseDouble((String) harga_per_kg); // Mengonversi String ke Double
            } else {
                this.harga_per_kg = 0.0;  // Default jika tipe tidak sesuai
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.harga_per_kg = 0.0;  // Default
        }
    }

    public double getJumlah_kg() {
        return jumlah_kg;
    }

    public void setJumlah_kg(double jumlah_kg) {
        this.jumlah_kg = jumlah_kg;
    }

    public double getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(double total_harga) {
        this.total_harga = total_harga;
    }

    // Yang tidak ada di Firestore
    public int getIconLaundry() {
        return iconLaundry;
    }

    public void setIconLaundry(int iconLaundry) {
        this.iconLaundry = iconLaundry;
    }
}
