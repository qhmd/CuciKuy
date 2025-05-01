package com.example.cucikuy.Pengaturan;

import com.example.cucikuy.R;

import java.util.ArrayList;
import java.util.List;

public class PengaturanData {
    private static String[] juduls = {
            "Pengaturan Akun",
            "Pengaturan Toko",
            "Pengaturan Durasi",
            "Pengaturan Layanan",
            "Pengaturan Parfum",
            "Pengaturan Diskon",
            "Pengaturan Pelanggan",
            "Pengaturan nota",
    };
    private static String[] deskripsis = {
            "Ubah password akun anda",
            "Ubah data toko anda",
            "Ubah durasi paket",
            "Ubah layanan paket anda",
            "Ubah jenis parfum anda",
            "Atur Diskon laundry anda",
            "Atur kontak pelanggan anda",
            "Atur detail nota",
    };
    private static int[] iconResIds = {
            R.drawable.person_24,
            R.drawable.baseline_store_24,
            R.drawable.baseline_store_24,
            R.drawable.import_export_24,
            R.drawable._spa_24,
            R.drawable.baseline_local_offer_24,
            R.drawable.baseline_contacts_24,
            R.drawable.baseline_note_24,
    };
    public static List<PengaturanItem> getDatas() {
        ArrayList<PengaturanItem> pengaturanitems = new ArrayList<>();
        for (int i = 0; i < juduls.length; i++) {
            PengaturanItem pengaturanitem = new PengaturanItem(iconResIds[i] , juduls[i], deskripsis[i] );
            pengaturanitems.add(pengaturanitem);
        }
        return pengaturanitems;
    }
}
