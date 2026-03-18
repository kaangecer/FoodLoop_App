package com.example.foodloopapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class AddProductFragment extends Fragment {

    private TextInputEditText edtName, edtPrice, edtUnit, edtCategory, edtDescription;
    private Button btnSave;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        edtName = view.findViewById(R.id.edtProductName);
        edtPrice = view.findViewById(R.id.edtProductPrice);
        edtUnit = view.findViewById(R.id.edtProductUnit);
        edtCategory = view.findViewById(R.id.edtProductCategory);
        edtDescription = view.findViewById(R.id.edtProductDescription);
        btnSave = view.findViewById(R.id.btnSaveProduct);

        btnSave.setOnClickListener(v -> saveProduct());

        return view;
    }

    private void saveProduct() {
        String name = edtName.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String unit = edtUnit.getText().toString().trim();
        String category = edtCategory.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price)) {
            Toast.makeText(getContext(), "Bitte Name und Preis ausfüllen", Toast.LENGTH_SHORT).show();
            return;
        }

        if (auth.getCurrentUser() == null) return;

        String producerId = auth.getCurrentUser().getUid();
        String productId = UUID.randomUUID().toString();

        Product product = new Product();
        product.setId(productId);
        product.setName(name);
        product.setPricePerUnit(price + " €");
        product.setUnit(unit);
        product.setCategory(category);
        product.setDescription(description);
        product.setProducerId(producerId);
        // Default placeholder image
        product.setImageUrl("https://via.placeholder.com/150");

        btnSave.setEnabled(false);
        db.collection("products").document(productId).set(product)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Produkt erfolgreich hinzugefügt!", Toast.LENGTH_SHORT).show();
                    getParentFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> {
                    btnSave.setEnabled(true);
                    Toast.makeText(getContext(), "Fehler: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
