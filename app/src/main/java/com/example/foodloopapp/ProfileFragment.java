package com.example.foodloopapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodloopapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private LinearLayout layoutLogin, layoutUserInfo;
    private TextView tvUserName, tvUserEmail;
    private Button btnConsumerLogin, btnProducerLogin, btnLogout;

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

        setupListeners();
        updateUI();

        return view;
    }

    private void setupListeners() {
        btnConsumerLogin.setOnClickListener(v -> navigateToLogin());
        btnProducerLogin.setOnClickListener(v -> navigateToLogin()); // Can be changed to ProducerLogin later

        btnLogout.setOnClickListener(v -> {
            auth.signOut();
            updateUI();
        });
    }

    private void updateUI() {
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            // User is logged in
            layoutLogin.setVisibility(View.GONE);
            layoutUserInfo.setVisibility(View.VISIBLE);

            // Fetch additional info from Firestore
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            User user = documentSnapshot.toObject(User.class);
                            if (user != null) {
                                tvUserName.setText(user.getFullName());
                                tvUserEmail.setText(user.getEmail());
                            }
                        } else {
                            // Fallback to Firebase Auth display name/email
                            tvUserName.setText(currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Kein Name");
                            tvUserEmail.setText(currentUser.getEmail());
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error fetching user data", e);
                        tvUserEmail.setText(currentUser.getEmail());
                    });

        } else {
            // User is logged out
            layoutLogin.setVisibility(View.VISIBLE);
            layoutUserInfo.setVisibility(View.GONE);
        }
    }

    private void navigateToLogin() {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }
}
