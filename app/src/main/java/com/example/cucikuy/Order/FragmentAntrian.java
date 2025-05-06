package com.example.cucikuy.Order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cucikuy.FormatIDR;
import com.example.cucikuy.Layanan.LayananItem;
import com.example.cucikuy.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentAntrian extends Fragment {
    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private ArrayList<ArrayList<LayananItem>> layananListPerOrder = new ArrayList<>(); // List layanan untuk setiap order
    private ArrayList<OrderItem> orderList = new ArrayList<>();
    String userId;

    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_antrian, container, false);
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Gson gson = new Gson();
        String datalist = gson.toJson(orderList);
        Log.i("isilist", datalist);//isinya kosong

        recyclerView = view.findViewById(R.id.recyclerViewAntrian);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OrderAdapter(orderList, item -> {
            int index = orderList.indexOf(item);
            ArrayList<LayananItem> layanan = layananListPerOrder.get(index);
            Intent intent = new Intent(getContext(), DetailOrderanActivity.class);
            intent.putExtra("order", item);
            Gson gson2 = new Gson();
            String tesini = gson2.toJson(layanan);
            Log.i("tesdlbos", tesini);
            intent.putExtra("selectedLayanan", layanan);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);
        fetchOrders();
        return view;
    }

    private void fetchOrders() {
        db.collection("users")
                .document(userId)
                .collection("pesanan")
                .document("statusPesanan")
                .collection("antrian")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            try {
                                Boolean belumSiap = document.getBoolean("belum_siap");
                                Boolean belumSelesai = document.getBoolean("belum_selesai");
                                if (belumSiap != null && belumSiap == true && belumSelesai != null & belumSelesai == true) {
                                    // lanjut proses seperti biasa
                                    OrderItem orderItem = new OrderItem();
                                    orderItem.setNama_pelanggan(document.getString("nama_pelanggan"));
                                    orderItem.setNo_hp(document.getString("no_hp"));
                                    orderItem.setEst_selesai(document.getString("est_selesai"));
                                    orderItem.setNo_nota(document.getString("no_nota"));
                                    orderItem.setBelum_bayar(Boolean.TRUE.equals(document.getBoolean("belum_bayar")));
                                    orderItem.setTanggal(document.getString("tanggal"));
                                    orderItem.setJenis_durasi(document.getString("jenis_durasi"));

                                    // proses total_bayar seperti sebelumnya
                                    Object total = document.get("total_bayar");
                                    double totalBayar = 0.0;
                                    if (total instanceof Number) {
                                        totalBayar = ((Number) total).doubleValue();
                                    } else if (total instanceof String) {
                                        try {
                                            totalBayar = Double.parseDouble((String) total);
                                        } catch (NumberFormatException e) {
                                            Log.w("fetchOrders", "Format total_bayar salah di dokumen: " + document.getId());
                                        }
                                    }
                                    orderItem.setTotal_bayar(totalBayar);

                                    // Tambahkan ke list
                                    orderList.add(orderItem);
                                    // Ambil layanan
                                    document.getReference().collection("layanan").get().addOnSuccessListener(layananSnapshots -> {
                                        ArrayList<LayananItem> layananItems = new ArrayList<>();
                                        for (DocumentSnapshot layananDoc : layananSnapshots) {
                                            LayananItem layananItem = layananDoc.toObject(LayananItem.class);
                                            layananItems.add(layananItem);
                                        }
                                        layananListPerOrder.add(layananItems);

                                        if (layananListPerOrder.size() == orderList.size()) {
                                            adapter.notifyDataSetChanged();
                                        }
                                    });
                                }

                            } catch (Exception e) {
                                Log.e("fetchOrders", "Gagal parsing dokumen: " + document.getId(), e);
                            }


                            document.getReference().collection("layanan").get().addOnSuccessListener(layananSnapshots -> {
                                ArrayList<LayananItem> layananItems = new ArrayList<>();
                                for (DocumentSnapshot layananDoc : layananSnapshots) {
                                    LayananItem layananItem = layananDoc.toObject(LayananItem.class);
                                    Gson gson1 = new Gson();
                                    String ini = gson1.toJson(layananItem);
                                            Log.i("iniyaa", ini);
                                    layananItems.add(layananItem);
                                }
                                layananListPerOrder.add(layananItems);

                                // Update adapter hanya kalau semua layanan sudah terambil
                                if (layananListPerOrder.size() == orderList.size()) {
                                    adapter.notifyDataSetChanged();
                                }
                            });
//                            orderItem.printLog();
                            // Ambil data field dari nota
                            String namaPelanggan = document.getString("nama_pelanggan");
                            String noHp = document.getString("no_hp");
                            String noNota = document.getString("no_nota");
                            String tanggal = document.getString("tanggal");
                            Log.d("Nota", "Nama: " + namaPelanggan + ", No HP: " + noHp + ", No Nota: " + noNota + ", Tanggal: " + tanggal);
                            // Ambil data dari subkoleksi "layanan"
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Antrian", "Gagal ambil antrian", e);
                });
    }
}
