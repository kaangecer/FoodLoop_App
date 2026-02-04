package com.example.foodloopapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartFragment extends Fragment {

    private final List<CartItem> cartItems = new ArrayList<>();
    private CartAdapter adapter;

    private TextView tvTotal;
    private TextView tvEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cart, container, false);

        RecyclerView rv = root.findViewById(R.id.rvCart);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        tvTotal = root.findViewById(R.id.tvCartTotal);
        tvEmpty = root.findViewById(R.id.tvCartEmpty);

        adapter = new CartAdapter(cartItems, new CartAdapter.Listener() {
            @Override public void onPlus(CartItem item) { changeQty(item, +1); }
            @Override public void onMinus(CartItem item) { changeQty(item, -1); }
            @Override public void onRemove(CartItem item) { removeItem(item); }
        });

        rv.setAdapter(adapter);

        loadCartLive();

        return root;
    }

    private void loadCartLive() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            tvEmpty.setVisibility(View.VISIBLE);
            tvTotal.setText("Summe: 0,00 €");
            return;
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("cart")
                .addSnapshotListener((value, error) -> {
                    cartItems.clear();

                    if (value != null) {
                        for (var doc : value.getDocuments()) {
                            CartItem item = doc.toObject(CartItem.class);
                            if (item != null) {
                                if (item.productId == null) item.productId = doc.getId();
                                cartItems.add(item);
                            }
                        }
                    }

                    adapter.notifyDataSetChanged();
                    updateUI();
                });
    }

    private void updateUI() {
        tvEmpty.setVisibility(cartItems.isEmpty() ? View.VISIBLE : View.GONE);

        double total = 0;
        for (CartItem i : cartItems) {
            total += (i.priceValue * i.quantity);
        }

        tvTotal.setText(String.format(Locale.GERMANY, "Summe: %.2f €", total));
    }

    private void changeQty(CartItem item, int delta) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
        if (item == null || item.productId == null) return;

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference ref = FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("cart")
                .document(item.productId);

        long newQty = item.quantity + delta;

        // Wenn 0 oder weniger: löschen
        if (newQty <= 0) {
            ref.delete();
        } else {
            ref.update("quantity", newQty);
        }
    }

    private void removeItem(CartItem item) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
        if (item == null || item.productId == null) return;

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("cart")
                .document(item.productId)
                .delete();
    }
}
