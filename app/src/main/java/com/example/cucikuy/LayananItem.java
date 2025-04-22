package com.example.cucikuy;

public class LayananItem {
    private String nama;
    private String harga;
    private String durasi;
    private int iconLaundry;
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

    private double jumlahKg = 0.01;

    public double getJumlahKg() {
        return jumlahKg;
    }

    public void setJumlahKg(double jumlahKg) {
        this.jumlahKg = jumlahKg;
    }

}