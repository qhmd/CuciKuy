package com.example.cucikuy.Laporan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.cucikuy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FragmentReport extends Fragment {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private TextView saldoKas, jumlahPesananSelesai, belumBayar;
    private SharedPreferences sharedPreferences;
    private LinearLayout lihatUangLaporan;

    private static final String PREF_NAME = "AppPreferences";
    private static final String LAST_RESET_DATE_KEY = "last_reset_date";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_report, container, false);

        sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        saldoKas = view.findViewById(R.id.saldo_kas);
        jumlahPesananSelesai = view.findViewById(R.id.jumlah_pesanan_selesai);
        belumBayar = view.findViewById(R.id.belum_bayar);
        lihatUangLaporan = view.findViewById(R.id.lihat_uang_laporan);

        checkAndResetDataIfNeeded();

        lihatUangLaporan.setOnClickListener(v -> showFilterDialog());

        loadDataFromFirestore(); // default load semua data

        return view;
    }

    private void showFilterDialog() {
        String[] options = {"Hari Ini", "7 Hari Terakhir", "30 Hari Terakhir"};

        new AlertDialog.Builder(getContext())
                .setTitle("Pilih Rentang Waktu")
                .setItems(options, (dialog, which) -> {
                    if (which == 0) {
                        loadDataForToday();
                    } else {
                        int days = (which == 1) ? 7 : 30;
                        loadDataFromFirestore(days);
                    }
                })
                .setNegativeButton("Tampilkan Semua", (dialog, which) -> loadDataFromFirestore())
                .show();
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
        belumBayar.setText("Rp 0");
    }

    private void loadDataForToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date todayStart = calendar.getTime();

        loadDataFromFirestore(todayStart);  // Filter hanya untuk hari ini
    }

    private void loadDataFromFirestore() {
        loadDataFromFirestore((Date) null); // null = tanpa filter waktu
    }

    private void loadDataFromFirestore(int days) {
        if (days <= 0) {
            loadDataFromFirestore(); // jika tidak valid, muat semua
            return;
        }

        long millisNow = System.currentTimeMillis();
        long millisDaysAgo = millisNow - (days * 24L * 60 * 60 * 1000);
        Date filterDate = new Date(millisDaysAgo);

        loadDataFromFirestore(filterDate); // Filter berdasarkan tanggal
    }

    private void loadDataFromFirestore(@Nullable Date filterDate) {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users")
                .document(userId)
                .collection("pesanan")
                .document("statusPesanan")
                .collection("antrian")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int totalSelesai = 0;
                    double totalSaldoKas = 0;
                    double totalBelumBayar = 0;

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Date tanggal = document.getDate("tanggal_selesai");

                        // Filter hanya untuk pesanan yang belum dibayar dan pada hari ini
                        if (filterDate != null && (tanggal == null || tanggal.before(filterDate))) {
                            continue; // Skip jika tanggal tidak sesuai
                        }

                        boolean isBelumBayar = Boolean.TRUE.equals(document.getBoolean("belum_bayar"));
                        boolean isBelumSelesai = Boolean.TRUE.equals(document.getBoolean("belum_selesai"));
                        boolean isBelumSiap = Boolean.TRUE.equals(document.getBoolean("belum_siap"));

                        Double totalBayar = document.getDouble("total_bayar");
                        if (totalBayar == null) totalBayar = 0.0;

                        // Hanya menghitung pesanan yang sudah selesai dan belum dibayar
                        if (!isBelumBayar && !isBelumSelesai && !isBelumSiap) {
                            totalSelesai++;
                            totalSaldoKas += totalBayar;
                        }

                        if (isBelumBayar && isBelumSelesai && !isBelumSiap) {
                            totalBelumBayar += totalBayar;  // Total belum bayar dihitung untuk hari ini
                        }
                    }

                    jumlahPesananSelesai.setText(String.valueOf(totalSelesai));
                    saldoKas.setText("Rp " + String.format("%,.0f", totalSaldoKas));
                    belumBayar.setText("Rp " + String.format("%,.0f", totalBelumBayar));  // Menampilkan hanya untuk yang belum bayar
                })
                .addOnFailureListener(e -> Log.e("ERROR", "Gagal mengambil data: ", e));
    }
}
