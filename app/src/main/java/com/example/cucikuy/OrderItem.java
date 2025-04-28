package com.example.cucikuy;

import android.util.Log;

public class OrderItem {
    private Integer gambar;
    private String  no_nota;
    private String  jenis_durasi;
    private String nama_pelanggan;
    private String tanggal_masuk;
    private String estimiasi_selesai;
    private int total_pembayaran;
    private boolean belum_bayar;
    private String tanggal;
    private String no_hp;

    //Constructor kosong untuk firestore
    public OrderItem () {}

    //getter and setter
    public void setImage(Integer gambar) {this.gambar = gambar;}
    public Integer getImage() {return gambar;}
    public String getNoNota() { return no_nota; }
    public void setNoNota(String no_nota) { this.no_nota = no_nota; }
    public String getJenisDurasi() { return jenis_durasi; }
    public void setJenisDurasi(String jenis_durasi) { this.jenis_durasi = jenis_durasi; }
    public String getNamaPengguna() { return nama_pelanggan; }
    public void setNamaPengguna(String nama_pelanggan) { this.nama_pelanggan = nama_pelanggan; }
    public String getTanggalMasuk() { return tanggal_masuk; }
    public void setTanggalMasuk(String tanggalMasuk) { this.tanggal_masuk = tanggalMasuk; }
    public String getEstimasiSelesai() { return estimiasi_selesai; }
    public void setEstimasiSelesai(String estimiasi_selesai) { this.estimiasi_selesai = estimiasi_selesai; }
    public int getTotalPembayaran() { return total_pembayaran; }
    public void setTotalPembayaran(int totalPembayaran) { this.total_pembayaran = totalPembayaran; }
    public boolean isBelumBayar() { return belum_bayar; }
    public void setBelumBayar(boolean belumBayar) { this.belum_bayar = belumBayar; }
    public String getTanggal() {
        return tanggal;
    }
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getNoHp() {
        return no_hp;
    }
    public void setNoHp(String no_hp) {
        this.no_hp = no_hp;
    }

    public void printLog() {
        Log.d("OrderItemLog",
                "gambar=" + gambar + ", " +
                        "no_nota=" + no_nota + ", " +
                        "jenis_durasi=" + jenis_durasi + ", " +
                        "nama_pelanggan=" + nama_pelanggan + ", " +
                        "tanggal_masuk=" + tanggal_masuk + ", " +
                        "estimiasi_selesai=" + estimiasi_selesai + ", " +
                        "total_pembayaran=" + total_pembayaran + ", " +
                        "belum_bayar=" + belum_bayar
        );
    }

}
