package com.example.foodloopapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductDetailsFragment extends Fragment {

    private static final String ARG_PRODUCT_ID = "product_id";

    private Product currentProduct;
    private String currentProductId;

    public static ProductDetailsFragment newInstance(String productId) {
        ProductDetailsFragment f = new ProductDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCT_ID, productId);
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_product_details, container, false);

        currentProductId = null;
        if (getArguments() != null) {
            currentProductId = getArguments().getString(ARG_PRODUCT_ID);
        }

        TextView tvName = root.findViewById(R.id.product_detail_name);
        TextView tvPrice = root.findViewById(R.id.product_detail_price);
        TextView tvUnit = root.findViewById(R.id.product_detail_unit);
        TextView tvCategory = root.findViewById(R.id.product_detail_category);
        TextView tvDescription = root.findViewById(R.id.product_detail_description);

        Button btnAddToCart = root.findViewById(R.id.btn_add_to_cart);
        btnAddToCart.setEnabled(false);

        btnAddToCart.setOnClickListener(v -> {
            if (currentProduct == null || currentProductId == null) return;
            addToCart(currentProductId, currentProduct);
        });

        if (currentProductId != null) {
            loadProduct(currentProductId, tvName, tvPrice, tvUnit, tvCategory, tvDescription, btnAddToCart);
        } else {
            tvName.setText(getString(R.string.unknown_product_name));
            tvPrice.setText(getString(R.string.unknown_product_price));
            tvUnit.setText(getString(R.string.unknown_product_unit));
            tvCategory.setText(getString(R.string.unknown_product_category));
            tvDescription.setText(getString(R.string.unknown_product_description));
        }

        return root;
    }

    private void loadProduct(String productId,
                             TextView tvName,
                             TextView tvPrice,
                             TextView tvUnit,
                             TextView tvCategory,
                             TextView tvDescription,
                             Button btnAddToCart) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .document(productId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Product p = doc.toObject(Product.class);
                        if (p != null) {
                            p.setId(doc.getId()); // wichtig
                            currentProduct = p;

                            tvName.setText(p.getName());
                            tvPrice.setText(p.getPricePerUnit());
                            tvUnit.setText(p.getUnit());
                            tvCategory.setText(p.getCategory());
                            tvDescription.setText(p.getDescription());

                            btnAddToCart.setEnabled(true);
                        } else {
                            tvName.setText(getString(R.string.unknown_product_name));
                        }
                    } else {
                        tvName.setText(getString(R.string.unknown_product_name));
                    }
                });
    }

    private void addToCart(String productId, Product p) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // basic: still, kein Popup
            return;
        }

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        double priceValue = parsePrice(p.getPricePerUnit());

        CartItem item = new CartItem(
                productId,
                p.getName(),
                p.getPricePerUnit(),
                priceValue,
                p.getUnit(),
                1,
                p.getImageUrl()
        );

        DocumentReference ref = db.collection("users")
                .document(uid)
                .collection("cart")
                .document(productId);

        // Falls schon drin -> Menge erhöhen, sonst neu setzen
        db.runTransaction(tx -> {
            var snap = tx.get(ref);
            if (snap.exists()) {
                Long q = snap.getLong("quantity");
                long newQ = (q == null ? 1 : q + 1);
                tx.update(ref, "quantity", newQ);
            } else {
                tx.set(ref, item);
            }
            return null;
        }).addOnSuccessListener(v -> {
            // kein Popup, keine Navigation
        }).addOnFailureListener(e -> {
            // optional loggen, sonst still
        });
    }

    private static double parsePrice(String s) {
        if (s == null) return 0;
        String cleaned = s.replace(",", ".");
        Matcher m = Pattern.compile("(\\d+(?:\\.\\d+)?)").matcher(cleaned);
        if (m.find()) {
            try { return Double.parseDouble(m.group(1)); } catch (Exception ignored) {}
        }
        return 0;
    }
}
