package com.example.cucikuy.Kontak;

import com.example.cucikuy.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class KontakData {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    public KontakData() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public interface DataCallback {
        void onDataLoaded(List<KontakItem> kontakItemList);
        void onError(String error);
    }

    public void loadKontakData(DataCallback callback) {
        String userId = mAuth.getCurrentUser().getUid();
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
