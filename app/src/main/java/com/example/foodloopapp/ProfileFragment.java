package com.example.foodloopapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodloopapp.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private LinearLayout layoutLogin, layoutUserInfo;
    private TextView tvUserName, tvUserEmail;
    private Button btnConsumerLogin, btnProducerLogin, btnLogout, btnAddProduct, btnUpdateProfile;
    
    private TextInputLayout inputLayoutCity, inputLayoutDescription;
    private TextInputEditText edtCity, edtDescription;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // UI Elements
        layoutLogin = view.findViewById(R.id.layoutLogin);
        layoutUserInfo = view.findViewById(R.id.layoutUserInfo);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);
        btnConsumerLogin = view.findViewById(R.id.btnConsumerLogin);
        btnProducerLogin = view.findViewById(R.id.btnProducerLogin);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnAddProduct = view.findViewById(R.id.btnAddProduct);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);
        
        inputLayoutCity = view.findViewById(R.id.inputLayoutProducerCity);
        inputLayoutDescription = view.findViewById(R.id.inputLayoutProducerDescription);
        edtCity = view.findViewById(R.id.edtProducerCity);
        edtDescription = view.findViewById(R.id.edtProducerDescription);

        setupListeners();
        updateUI();

        return view;
    }

    private void setupListeners() {
        btnConsumerLogin.setOnClickListener(v -> navigateToLogin());
        btnProducerLogin.setOnClickListener(v -> navigateToLogin());

        btnAddProduct.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddProductFragment())
                    .addToBackStack(null)
                    .commit();
        });

        btnUpdateProfile.setOnClickListener(v -> updateProducerProfile());

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            updateUI();
        });
    }

    private void updateUI() {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            layoutLogin.setVisibility(View.GONE);
            layoutUserInfo.setVisibility(View.VISIBLE);

            db.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            if (user != null) {
                                tvUserName.setText(user.getFullName());
                                tvUserEmail.setText(user.getEmail());
                                
                                if (user.isProducer()) {
                                    showProducerTools(true);
                                    loadProducerData(currentUser.getUid());
                                } else {
                                    showProducerTools(false);
                                }
                            }
                        }
                    });

        } else {
            layoutLogin.setVisibility(View.VISIBLE);
            layoutUserInfo.setVisibility(View.GONE);
        }
    }

    private void showProducerTools(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        btnAddProduct.setVisibility(visibility);
        btnUpdateProfile.setVisibility(visibility);
        inputLayoutCity.setVisibility(visibility);
        inputLayoutDescription.setVisibility(visibility);
    }

    private void loadProducerData(String uid) {
        db.collection("producers").document(uid).get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String city = doc.getString("city");
                        String desc = doc.getString("description");
                        if (city != null) edtCity.setText(city);
                        if (desc != null) edtDescription.setText(desc);
                    }
                });
    }

    private void updateProducerProfile() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) return;

        String city = edtCity.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();

        Map<String, Object> data = new HashMap<>();
        data.put("city", city);
        data.put("description", description);

        btnUpdateProfile.setEnabled(false);
        db.collection("producers").document(user.getUid()).update(data)
                .addOnSuccessListener(aVoid -> {
                    btnUpdateProfile.setEnabled(true);
                    Toast.makeText(getContext(), "Profil aktualisiert!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    btnUpdateProfile.setEnabled(true);
                    Toast.makeText(getContext(), "Update fehlgeschlagen.", Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToLogin() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }
}
