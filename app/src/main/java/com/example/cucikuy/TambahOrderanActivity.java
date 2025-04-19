package com.example.cucikuy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TambahOrderanActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private KontakAdapter kontakAdapter;
    private List<KontakItem> kontakItemList;
    private ImageView btn_add_contact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tambah_order);
        overridePendingTransition(R.anim.fade_in, 0);

        btn_add_contact = findViewById(R.id.tambah_kontak);
        recyclerView = findViewById(R.id.recyclerViewKontak);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setHasFixedSize(true);

        // ✅ Inisialisasi list dulu
        kontakItemList = new ArrayList<>();

        // ✅ Baru bikin adapter setelah list terisi
        kontakAdapter = new KontakAdapter(kontakItemList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(kontakAdapter);

        // Inisialisasi Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        loadDataFromFirestore();

        kontakAdapter.setOnItemClickListener(kontakItem -> {
            Log.i("detailkontakbro", "klikk");
            Intent intent = new Intent(TambahOrderanActivity.this, DetailKontakActivity.class);
            intent.putExtra("nama", kontakItem.getNama());
            intent.putExtra("nomor", kontakItem.getNoTelpon());
            intent.putExtra("alamat", kontakItem.getAlamat());

            startActivity(intent);
        });

        btn_add_contact.setOnClickListener(v -> {
            startActivity(new Intent(TambahOrderanActivity.this, TambahKontakAktivitas.class));
        });
    }


    private void loadDataFromFirestore() {
        String userId = mAuth.getCurrentUser().getUid();
        Log.i("datauser", userId);
        CollectionReference kontakRef = db.collection("users").document(userId).collection("kontak");

        kontakRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                kontakItemList.clear();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String nama = document.getString("nama");
                    String noHp = document.getString("nomor");
                    String alamat = document.getString("alamat");
                    int icon = R.drawable.person_24;

                    // Pastikan data tidak null
                    if (nama != null && noHp != null ) {
                        kontakItemList.add(new KontakItem(icon, nama, noHp, alamat));
                    } else {
                        Log.w("Firebase", "Data kontak tidak lengkap.");
                    }
                }
                kontakAdapter.notifyDataSetChanged();
            } else {
                Log.e("Firebase", "Gagal ambil data: " + task.getException());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in, 0);
    }
}
