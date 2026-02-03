package com.example.foodloopapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProducerFragment extends Fragment {

    private static final String TAG = "ProducerFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_producer, container, false);

        RecyclerView rv = root.findViewById(R.id.rvProducers);
        if (rv == null) {
            Log.e(TAG, "RecyclerView with ID R.id.rvProducers not found in layout.");
            return root;
        }

        // 1) Create adapter with click → open profile
        ProducerAdapter adapter = new ProducerAdapter(producerId -> {
            if (!isAdded()) {
                Log.w(TAG, "Fragment not attached, cannot perform transaction.");
                return;
            }
            ProducerProfileFragment profile = ProducerProfileFragment.newInstance(producerId);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, profile)
                    .addToBackStack(null)
                    .commit();
        });
        rv.setAdapter(adapter);

        // 2) Load producers from Firestore and push into adapter
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("producers")
                .get()
                .addOnSuccessListener(query -> {
                    List<Producer> list = new ArrayList<>();
                    for (DocumentSnapshot doc : query.getDocuments()) {
                        Producer p = doc.toObject(Producer.class);
                        if (p != null) {
                            p.setId(doc.getId()); // document id as producerId
                            list.add(p);
                        }
                    }
                    adapter.setItems(list);
                })
                .addOnFailureListener(e ->
                        Log.e(TAG, "Failed to load producers", e));

        return root;
    }
}
