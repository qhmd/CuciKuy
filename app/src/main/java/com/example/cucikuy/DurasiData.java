package com.example.cucikuy;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class DurasiData {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public DurasiData() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public interface DataCallback {
        void onDataLoaded(List<DurasiItem> durasiItemList);
        void onError(String error);
    }

    public void loadDurasiData(DataCallback callback) {
        String userId = mAuth.getCurrentUser().getUid();
        CollectionReference durasiRef = db.collection("users").document(userId).collection("durasi");

        durasiRef.get().addOnCompleteListener(task -> {
            List<DurasiItem> durasiItemList = new ArrayList<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String nama_durasi = document.getString("nama_durasi");
                    String waktu_durasi = document.getString("waktu_durasi");
                    int iconEdit = R.drawable.baseline_edit_24;
                    int iconDelete = R.drawable.baseline_delete_24;

                    // Pastikan data tidak null
                    if (nama_durasi != null && waktu_durasi != null) {
                        durasiItemList.add(new DurasiItem(nama_durasi, waktu_durasi, iconEdit, iconDelete));
                    }
                }
                callback.onDataLoaded(durasiItemList);
            } else {
                callback.onError("Gagal ambil data: " + task.getException());
            }
        });
    }
}
