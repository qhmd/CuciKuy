package com.example.cucikuy.Beranda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import java.util.concurrent.CountDownLatch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cucikuy.Kontak.TambahKontakAktivitas;
import com.example.cucikuy.Order.TambahOrderanActivity;
import com.example.cucikuy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragmentBeranda extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView saldoKas, jumlahPesananSelesai, jumlahKgToday;
    private SharedPreferences sharedPreferences;

    private LinearLayout tambahPelanggan, tambahOrder;

    private static final String PREF_NAME = "AppPreferences";
    private static final String LAST_RESET_DATE_KEY = "last_reset_date";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home, container, false);

        // Inisialisasi SharedPreferences
        sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Inisialisasi Views
        saldoKas = view.findViewById(R.id.pendapatan_today);
        jumlahPesananSelesai = view.findViewById(R.id.jumlah_pesanan);
        jumlahKgToday = view.findViewById(R.id.jumlah_kg_today);
        tambahPelanggan = view.findViewById(R.id.tambah_pelanggan);
        tambahOrder = view.findViewById(R.id.tambah_order);

        // Mengecek apakah reset data dibutuhkan
        checkAndResetDataIfNeeded();

        // Memuat data hari ini
        loadDataForToday();

        // Menambahkan event listeners untuk tombol
        tambahPelanggan.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TambahKontakAktivitas.class);
            startActivity(intent);
        });

        tambahOrder.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TambahOrderanActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void checkAndResetDataIfNeeded() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String lastResetDate = sharedPreferences.getString(LAST_RESET_DATE_KEY, "");

        if (!currentDate.equals(lastResetDate)) {
            resetData();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(LAST_RESET_DATE_KEY, currentDate);
            editor.apply();
        }
    }

    private void resetData() {
        saldoKas.setText("Rp 0");
        jumlahPesananSelesai.setText("0");
        jumlahKgToday.setText("0 kg");
    }


    private void loadDataForToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date todayStart = calendar.getTime();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Log.d("FragmentBeranda", "Memulai load data untuk hari ini...");

        db.collection("users")
                .document(userId)
                .collection("pesanan")
                .document("statusPesanan")
                .collection("antrian")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Membuat thread untuk memproses data di background
                    new Thread(() -> {
                        final int[] totalSelesai = {0};
                        final double[] totalSaldoKas = {0};
                        final double[] totalKg = {0};

                        Log.d("FragmentBeranda", "Data pesanan berhasil diambil, memulai proses data...");

                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Date tanggal = document.getDate("tanggal_selesai");

                            if (tanggal != null && !tanggal.before(todayStart)) {
                                Log.d("FragmentBeranda", "Memproses pesanan dengan ID: " + document.getId());

                                boolean isBelumBayar = Boolean.TRUE.equals(document.getBoolean("belum_bayar"));
                                boolean isBelumSelesai = Boolean.TRUE.equals(document.getBoolean("belum_selesai"));
                                boolean isBelumSiap = Boolean.TRUE.equals(document.getBoolean("belum_siap"));

                                Double totalBayar = document.getDouble("total_bayar");
                                if (totalBayar == null) totalBayar = 0.0;

                                    Log.d("FragmentBeranda", "Mencari layanan untuk pesanan ID: " + document.getId());

                                    db.collection("users")
                                            .document(userId)
                                            .collection("pesanan")
                                            .document("statusPesanan")
                                            .collection("antrian")
                                            .document(document.getId())
                                            .collection("layanan")
                                            .get()
                                            .addOnSuccessListener(layananDocs -> {
                                                double beratKg = 0;
                                                for (DocumentSnapshot layananDoc : layananDocs) {
                                                    Double layananKg = layananDoc.getDouble("jumlah_kg");
                                                    if (layananKg != null) {
                                                        beratKg += layananKg;
                                                    }
                                                }

                                                Log.d("FragmentBeranda", "Total berat layanan untuk pesanan ID " + document.getId() + ": " + beratKg + " kg");

                                                // Accumulate totalKg
                                                totalKg[0] += beratKg;

                                                // Memastikan UI diupdate di main thread
                                                getActivity().runOnUiThread(() -> {
                                                    saldoKas.setText("Rp " + String.format("%,.0f", totalSaldoKas[0]));
                                                    jumlahPesananSelesai.setText(String.valueOf(totalSelesai[0]));
                                                    jumlahKgToday.setText(String.format(Locale.getDefault(), "%.2f kg", totalKg[0]));
                                                    Log.d("FragmentBeranda", "UI terupdate: saldoKas = Rp " + totalSaldoKas[0] + ", jumlahPesananSelesai = " + totalSelesai[0] + ", jumlahKgToday = " + totalKg[0]);
                                                });
                                            })
                                            .addOnFailureListener(e -> Log.e("FragmentBeranda", "Gagal mengambil data layanan", e));
                                if (!isBelumBayar && !isBelumSelesai && !isBelumSiap) {
                                    totalSelesai[0]++;
                                    totalSaldoKas[0] += totalBayar;
                                    Log.d("FragmentBeranda", "Pesanan ID " + document.getId() + " sudah selesai, totalSelesai: " + totalSelesai[0] + ", totalSaldoKas: " + totalSaldoKas[0]);
                                }
                            }
                        }
                    }).start(); // Mulai thread baru untuk proses data
                })
                .addOnFailureListener(e -> Log.e("FragmentBeranda", "Gagal mengambil data pesanan", e));
    }


}
