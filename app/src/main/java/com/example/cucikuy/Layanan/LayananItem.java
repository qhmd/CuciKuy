package com.example.cucikuy.Layanan;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

public class LayananItem implements Serializable {
    private String harga;
    private String hargaPerKg;
    private int iconLaundry;
    private double totalHargaLayanan;

private String durasi;
private double jumlahKg;
private String nama;


    public LayananItem() {
        // Diperlukan untuk Firebase (default constructor)
    }

    public LayananItem(String nama, String harga, String durasi, int iconLaundry) {
        this.nama = nama;
        this.harga = harga;
        this.durasi = durasi;
        this.iconLaundry = iconLaundry;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }
    public String getDurasi() {
        return durasi;
    }

    public void  setIconLaundry(int iconLaundry) {
        this.iconLaundry = iconLaundry;
    }

    public int getIconLaundry () {
        return  iconLaundry;
    }

    @PropertyName("jumlah_kg")
    public double getJumlahKg() {
        return jumlahKg;
    }
    @PropertyName("jumlah_kg")
    public void setJumlahKg(double jumlahKg) {
        this.jumlahKg = jumlahKg;
    }
    @PropertyName("total_harga")
    public void setTotalHarga(double totalHargaLayanan) {
        this.totalHargaLayanan = totalHargaLayanan;
    }
    @PropertyName("total_harga")
    public double getTotalHarga() {
        return totalHargaLayanan;
    }
    @PropertyName("harga_per_kg")
    public String getHargaPerKg() {
        return hargaPerKg;
    }

    @PropertyName("harga_per_kg")
    public void setHargaPerKg(String hargaPerKg) {
        this.hargaPerKg = hargaPerKg;
    }

    // totalHargaLayanan (tidak ada di Firestore → default)
    public double getTotalHargaLayanan() {
        return totalHargaLayanan;
    }

    public void setTotalHargaLayanan(double totalHargaLayanan) {
        this.totalHargaLayanan = totalHargaLayanan;
    }
}