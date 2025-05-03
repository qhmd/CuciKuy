package com.example.cucikuy.Order;

import com.example.cucikuy.Layanan.LayananItem;

import java.io.Serializable;
import java.util.ArrayList;

public class OrderItem implements Serializable {
    private String nama_pelanggan;
    private String no_hp;
    private String est_selesai;
    private String no_nota;
    private boolean belum_bayar;
    private String tanggal;
    private double total_bayar;
    private String jenis_durasi;

    public OrderItem() {
        // WAJIB constructor kosong
    }

    public String getNama_pelanggan() {
        return nama_pelanggan;
    }

    public void setNama_pelanggan(String nama_pelanggan) {
        this.nama_pelanggan = nama_pelanggan;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getEst_selesai() {
        return est_selesai;
    }

    public void setEst_selesai(String est_selesai) {
        this.est_selesai = est_selesai;
    }

    public String getNo_nota() {
        return no_nota;
    }

    public void setNo_nota(String no_nota) {
        this.no_nota = no_nota;
    }

    public boolean isBelum_bayar() {
        return belum_bayar;
    }

    public void setBelum_bayar(boolean belum_bayar) {
        this.belum_bayar = belum_bayar;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public double getTotal_bayar() {
        return total_bayar;
    }

    public void setTotal_bayar(double total_pembayaran) {
        this.total_bayar = total_pembayaran;
    }

    public void setJenis_durasi (String jenis_durasi) {
        this.jenis_durasi = jenis_durasi;
    }

    public String getJenis_durasi () {
        return jenis_durasi;
    }
    private ArrayList<LayananItem> selectedLayanan;

    public  ArrayList<LayananItem> getSelectedLayanan() {
        return selectedLayanan;
    }

    public void setSelectedLayanan(ArrayList<LayananItem> selectedLayanan) {
        this.selectedLayanan = selectedLayanan;
    }
}