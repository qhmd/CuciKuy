package com.example.cucikuy;

public class OrderItem {
    private Integer gambar;
    private String  noNota;
    private String jenisDurasi;
    private String namaPengguna;
    private String tanggalMasuk;
    private String estimasiSelesai;
    private int totalPembayaran;
    private boolean belumBayar;

    //Constructor kosong untuk firestore
    public OrderItem () {}

    //getter and setter
    public void setImage(Integer gambar) {this.gambar = gambar;}
    public Integer getImage() {return gambar;}
    public String getNoNota() { return noNota; }
    public void setNoNota(String noNota) { this.noNota = noNota; }
    public String getJenisDurasi() { return jenisDurasi; }
    public void setJenisDurasi(String jenisDurasi) { this.jenisDurasi = jenisDurasi; }
    public String getNamaPengguna() { return namaPengguna; }
    public void setNamaPengguna(String namaPengguna) { this.namaPengguna = namaPengguna; }
    public String getTanggalMasuk() { return tanggalMasuk; }
    public void setTanggalMasuk(String tanggalMasuk) { this.tanggalMasuk = tanggalMasuk; }
    public String getEstimasiSelesai() { return estimasiSelesai; }
    public void setEstimasiSelesai(String estimasiSelesai) { this.estimasiSelesai = estimasiSelesai; }
    public int getTotalPembayaran() { return totalPembayaran; }
    public void setTotalPembayaran(int totalPembayaran) { this.totalPembayaran = totalPembayaran; }
    public boolean isBelumBayar() { return belumBayar; }
    public void setBelumBayar(boolean belumBayar) { this.belumBayar = belumBayar; }

}
