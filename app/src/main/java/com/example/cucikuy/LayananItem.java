package com.example.cucikuy;

import java.io.Serializable;

public class LayananItem implements Serializable {
    private String nama;
    private String harga;
    private String durasi;
    private int iconLaundry;
    private double jumlahKg;
    private double totalHarga;
    private double totalHargaLayanan;
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


    public double getJumlahKg() {
        return jumlahKg;
    }

    public void setJumlahKg(double jumlahKg) {
        this.jumlahKg = jumlahKg;
    }

    public void setTotalHarga(double totalHargaLayanan) {
        this.totalHargaLayanan = totalHargaLayanan;
    }

    public double getTotalHarga() {
        return totalHargaLayanan;
    }

}