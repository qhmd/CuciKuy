package com.example.cucikuy;

import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;

public class FragmentAntrian extends Fragment {
    private RecyclerView recyclerView;
    private DetailKontakActivity.AntrianAdapter adapter;
    private ArrayList<OrderItem> orderList;
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

        recyclerView = view.findViewById(R.id.recyclerViewAntrian);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderList = new ArrayList<>();
        adapter = new DetailKontakActivity.AntrianAdapter(orderList);
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
                            OrderItem orderItem = document.toObject(OrderItem.class);
                            orderList.add(orderItem);
                            Gson gson = new Gson();
                            String json = gson.toJson(document.getData());
                            Log.i("fetchOrders", json);

                            String json1 = gson.toJson(orderItem);
                            Log.i("fetchOrders", json1);

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
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("Antrian", "Gagal ambil antrian", e);
                });
    }
}
