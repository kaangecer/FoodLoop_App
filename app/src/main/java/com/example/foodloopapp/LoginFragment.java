package com.example.foodloopapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodloopapp.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    private TextInputLayout inputLayoutEmail;
    private TextInputEditText edtEmail;
    private MaterialButton btnContinueEmail;

    private LinearLayout containerStep2;
    private TextInputLayout inputLayoutPassword;
    private TextInputEditText edtPassword;
    private TextView txtForgotPassword;
    private MaterialButton btnLogin;
    private TextView txtNewAccountInfo;
    private MaterialButton btnSignUp;

    // Inline sign-up section
    private LinearLayout containerSignUpDetails;
    private TextInputLayout inputLayoutName;
    private TextInputEditText edtName;
    private TextInputLayout inputLayoutSignUpPassword;
    private TextInputEditText edtSignUpPassword;
    private TextInputLayout inputLayoutSignUpPasswordConfirm;
    private TextInputEditText edtSignUpPasswordConfirm;
    private SwitchMaterial switchIsProducer;
    private MaterialButton btnConfirmSignUp;

    private MaterialButton btnGoogle;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind views
        inputLayoutEmail = view.findViewById(R.id.inputLayoutEmail);
        edtEmail = view.findViewById(R.id.edtEmail);
        btnContinueEmail = view.findViewById(R.id.btnContinueEmail);

        containerStep2 = view.findViewById(R.id.containerStep2);
        inputLayoutPassword = view.findViewById(R.id.inputLayoutPassword);
        edtPassword = view.findViewById(R.id.edtPassword);
        txtForgotPassword = view.findViewById(R.id.txtForgotPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        txtNewAccountInfo = view.findViewById(R.id.txtNewAccountInfo);
        btnSignUp = view.findViewById(R.id.btnSignUp);

        containerSignUpDetails = view.findViewById(R.id.containerSignUpDetails);
        inputLayoutName = view.findViewById(R.id.inputLayoutName);
        edtName = view.findViewById(R.id.edtName);
        inputLayoutSignUpPassword = view.findViewById(R.id.inputLayoutSignUpPassword);
        edtSignUpPassword = view.findViewById(R.id.edtSignUpPassword);
        inputLayoutSignUpPasswordConfirm = view.findViewById(R.id.inputLayoutSignUpPasswordConfirm);
        edtSignUpPasswordConfirm = view.findViewById(R.id.edtSignUpPasswordConfirm);
        switchIsProducer = view.findViewById(R.id.switchIsProducer);
        btnConfirmSignUp = view.findViewById(R.id.btnConfirmSignUp);

        btnGoogle = view.findViewById(R.id.btnGoogle);

        setupListeners();
    }

    private void setupListeners() {
        btnContinueEmail.setOnClickListener(v -> handleContinueWithEmail());
        btnLogin.setOnClickListener(v -> handleLogin());
        btnSignUp.setOnClickListener(v -> handleSignUp());
        btnConfirmSignUp.setOnClickListener(v -> handleConfirmSignUp());
        txtForgotPassword.setOnClickListener(v -> handleForgotPassword());
        btnGoogle.setOnClickListener(v -> handleGoogle());
    }

    private void handleContinueWithEmail() {
        String email = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";

        if (!isValidEmail(email)) {
            inputLayoutEmail.setError("Please enter a valid email");
            return;
        } else {
            inputLayoutEmail.setError(null);
        }

        auth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(getContext(),
                                "Could not check account: " +
                                        (task.getException() != null ? task.getException().getMessage() : ""),
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    boolean existingUser =
                            task.getResult() != null &&
                                    task.getResult().getSignInMethods() != null &&
                                    !task.getResult().getSignInMethods().isEmpty();

                    showStep2(existingUser);
                });
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void showStep2(boolean existingUser) {
        containerStep2.setVisibility(View.VISIBLE);
        containerSignUpDetails.setVisibility(View.GONE);

        if (existingUser) {
            inputLayoutPassword.setVisibility(View.VISIBLE);
            txtForgotPassword.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);
            txtNewAccountInfo.setVisibility(View.GONE);
            btnSignUp.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Welcome back!", Toast.LENGTH_SHORT).show();
        } else {
            inputLayoutPassword.setVisibility(View.GONE);
            txtForgotPassword.setVisibility(View.GONE);
            btnLogin.setVisibility(View.GONE);
            txtNewAccountInfo.setVisibility(View.VISIBLE);
            btnSignUp.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "New here? Let's create your account.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLogin() {
        String email = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";
        String password = edtPassword.getText() != null ? edtPassword.getText().toString() : "";

        if (TextUtils.isEmpty(password)) {
            inputLayoutPassword.setError("Password required");
            return;
        }

        btnLogin.setEnabled(false);
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    btnLogin.setEnabled(true);
                    if (!task.isSuccessful()) {
                        Toast.makeText(getContext(), "Login failed: " +
                                (task.getException() != null ? task.getException().getMessage() : ""),
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    goToHome();
                });
    }

    private void handleSignUp() {
        containerSignUpDetails.setVisibility(View.VISIBLE);
        inputLayoutPassword.setVisibility(View.GONE);
        txtForgotPassword.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);
    }

    private void handleConfirmSignUp() {
        String email = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";
        String name = edtName.getText() != null ? edtName.getText().toString().trim() : "";
        String password = edtSignUpPassword.getText() != null ? edtSignUpPassword.getText().toString() : "";
        String passwordConfirm = edtSignUpPasswordConfirm.getText() != null ? edtSignUpPasswordConfirm.getText().toString() : "";
        boolean isProducer = switchIsProducer.isChecked();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || !password.equals(passwordConfirm)) {
            Toast.makeText(getContext(), "Please check your inputs", Toast.LENGTH_SHORT).show();
            return;
        }

        btnConfirmSignUp.setEnabled(false);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    btnConfirmSignUp.setEnabled(true);

                    if (!task.isSuccessful() || task.getResult() == null) {
                        String error = (task.getException() != null ? task.getException().getMessage() : "Unknown error");
                        if (error != null && error.contains("email address is already in use")) {
                            Toast.makeText(getContext(), "Email already registered.", Toast.LENGTH_LONG).show();
                            showStep2(true);
                        } else {
                            Toast.makeText(getContext(), "Sign-up failed: " + error, Toast.LENGTH_LONG).show();
                        }
                        return;
                    }

                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if (firebaseUser == null) return;

                    String uid = firebaseUser.getUid();
                    User user = new User(uid, email, name, System.currentTimeMillis(), isProducer);

                    // 1. Save to "users"
                    db.collection("users").document(uid).set(user)
                            .addOnSuccessListener(aVoid -> {
                                // 2. If producer, also save to "producers"
                                if (isProducer) {
                                    Map<String, Object> producerData = new HashMap<>();
                                    producerData.put("id", uid); // Explizite ID für Producer-Klasse
                                    producerData.put("name", name);
                                    producerData.put("email", email);
                                    producerData.put("userId", uid);
                                    producerData.put("description", "New FoodLoop Producer");

                                    db.collection("producers").document(uid).set(producerData)
                                            .addOnSuccessListener(v -> {
                                                Toast.makeText(getContext(), "Producer account created!", Toast.LENGTH_SHORT).show();
                                                goToHome();
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(getContext(), "User created, but producer entry failed.", Toast.LENGTH_SHORT).show();
                                                goToHome();
                                            });
                                } else {
                                    Toast.makeText(getContext(), "Account created!", Toast.LENGTH_SHORT).show();
                                    goToHome();
                                }
                            });
                });
    }

    private void handleForgotPassword() {
        String email = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";
        if (isValidEmail(email)) {
            auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) Toast.makeText(getContext(), "Reset link sent.", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void handleGoogle() {
        Toast.makeText(getContext(), "Google sign-in not implemented yet", Toast.LENGTH_SHORT).show();
    }

    private void goToHome() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateTo(R.id.nav_home);
        }
    }
}
