package com.example.cucikuy.Kontak;

import com.example.cucikuy.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KontakData {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userId;
    public KontakData() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public interface DataCallback {
        void onDataLoaded(List<KontakItem> kontakItemList);
        void onError(String error);
    }
    public interface DeleteCallback {
        void onDeleteSuccess();
        void onDeleteFailure(String error);
    }
    public interface EditCallback {
        void onEditSuccess();
        void onEditFailure(String error);
    }
    public void editKontak(String noTelpon, String namaBaru, String alamatBaru, EditCallback callback) {
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("nama", namaBaru);
        if (alamatBaru != null ) {
        updateData.put("alamat", alamatBaru);
        }

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(mAuth.getUid())
                .collection("kontak")
                .document(noTelpon)
                .update(updateData)
                .addOnSuccessListener(unused -> callback.onEditSuccess())
                .addOnFailureListener(e -> callback.onEditFailure(e.getMessage()));
    }


    public void hapusKontak(String noTelpon, DeleteCallback callback) {
        db.collection("users")
                .document(mAuth.getUid())
                .collection("kontak")
                .document(noTelpon) // Gunakan noTelpon sebagai ID dokumen
                .delete()
                .addOnSuccessListener(aVoid -> {
                    callback.onDeleteSuccess();
                })
                .addOnFailureListener(e -> {
                    callback.onDeleteFailure(e.getMessage());
                });
    }

    public void loadKontakData(DataCallback callback) {
        userId = mAuth.getCurrentUser().getUid();
        CollectionReference kontakRef = db.collection("users").document(userId).collection("kontak");

        kontakRef.get().addOnCompleteListener(task -> {
            List<KontakItem> kontakItemList = new ArrayList<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String nama = document.getString("nama");
                    String noHp = document.getString("nomor");
                    String alamat = document.getString("alamat");
                    int icon = R.drawable.person_24;

                    // Pastikan data tidak null
                    if (nama != null && noHp != null) {
                        kontakItemList.add(new KontakItem(icon, nama, noHp, alamat));
                    }
                }
                callback.onDataLoaded(kontakItemList);
            } else {
                callback.onError("Gagal ambil data: " + task.getException());
            }
        });
    }
}
