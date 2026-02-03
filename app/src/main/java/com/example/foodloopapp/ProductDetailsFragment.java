package com.example.foodloopapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

public class ProductDetailsFragment extends Fragment {

    private static final String ARG_PRODUCT_ID = "product_id";

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

        String productId = null;
        if (getArguments() != null) {
            productId = getArguments().getString(ARG_PRODUCT_ID);
        }

        TextView tvName = root.findViewById(R.id.product_detail_name);
        TextView tvPrice = root.findViewById(R.id.product_detail_price);
        TextView tvUnit = root.findViewById(R.id.product_detail_unit);
        TextView tvCategory = root.findViewById(R.id.product_detail_category);
        TextView tvDescription = root.findViewById(R.id.product_detail_description);

        if (productId != null) {
            loadProduct(productId, tvName, tvPrice, tvUnit, tvCategory, tvDescription);
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
                             TextView tvDescription) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .document(productId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Product p = doc.toObject(Product.class);
                        if (p != null) {
                            tvName.setText(p.getName());
                            tvPrice.setText(p.getPricePerUnit());
                            tvUnit.setText(p.getUnit());
                            tvCategory.setText(p.getCategory());
                            tvDescription.setText(p.getDescription());
                        } else {
                            tvName.setText(getString(R.string.unknown_product_name));
                        }
                    } else {
                        tvName.setText(getString(R.string.unknown_product_name));
                    }
                });
    }
}
