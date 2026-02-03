package com.example.foodloopapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;



import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment {

    private final List<Product> products = new ArrayList<>();
    private ProductAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_products, container, false);

        RecyclerView rv = root.findViewById(R.id.rvProducts);

        adapter = new ProductAdapter(products, this::openProductDetail);
        rv.setAdapter(adapter);

        loadProductsFromFirestore();

        return root;
    }
    private void loadProductsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    products.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Product p = doc.toObject(Product.class);
                        if (p != null) {
                            p.setId(doc.getId());
                            products.add(p);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load products", e);
                });
    }
    private void openProductDetail(Product product) {
        ProductDetailsFragment f = ProductDetailsFragment.newInstance(
                product.getId()
                );
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, f)
                .addToBackStack(null)
                .commit();
    }

}
