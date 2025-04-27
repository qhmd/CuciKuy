package com.example.cucikuy;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FragmentAntrian extends Fragment {
    private RecyclerView recyclerView;
    private AntrianAdapter adapter;
    private ArrayList<OrderItem> orderList;
    String userId;

    private FirebaseFirestore db;
    db = FirebaseFirestore.getInstance();
    userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_antrian, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewAntrian);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        orderList = new ArrayList<>();
        adapter = new AntrianAdapter(orderList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
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
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        for (QueryDocumentSnapshot doc : querySnapshot) {
                            OrderItem orderItem = doc.toObject(OrderItem.class);
                            orderList.add(orderItem);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e-> {
                    Log.e("Firestore", "Error: " + e.getMessage());
                });
    }
}
