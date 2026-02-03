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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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

    // Inline sign-up section (between email and Continue)
    private LinearLayout containerSignUpDetails;
    private TextInputLayout inputLayoutName;
    private TextInputEditText edtName;
    private TextInputLayout inputLayoutSignUpPassword;
    private TextInputEditText edtSignUpPassword;
    private TextInputLayout inputLayoutSignUpPasswordConfirm;
    private TextInputEditText edtSignUpPasswordConfirm;
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

    // Step 1: user taps "Continue" after entering email
    private void handleContinueWithEmail() {
        String email = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";

        if (!isValidEmail(email)) {
            inputLayoutEmail.setError("Please enter a valid email");
            return;
        } else {
            inputLayoutEmail.setError(null);
        }

        // Check if this email already has a Firebase account
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

    /**
     * Show password login or sign up CTA depending on whether this is an existing user.
     */
    private void showStep2(boolean existingUser) {
        containerStep2.setVisibility(View.VISIBLE);
        containerSignUpDetails.setVisibility(View.GONE); // hidden until user taps "Sign up"

        if (existingUser) {
            // Show password login UI
            inputLayoutPassword.setVisibility(View.VISIBLE);
            txtForgotPassword.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);

            // Hide signup-only bits
            txtNewAccountInfo.setVisibility(View.GONE);
            btnSignUp.setVisibility(View.GONE);

            Toast.makeText(getContext(),
                    "Welcome back! Please enter your password.",
                    Toast.LENGTH_SHORT).show();
        } else {
            // Hide password login bits
            inputLayoutPassword.setVisibility(View.GONE);
            txtForgotPassword.setVisibility(View.GONE);
            btnLogin.setVisibility(View.GONE);

            // Show signup UI CTA
            txtNewAccountInfo.setVisibility(View.VISIBLE);
            btnSignUp.setVisibility(View.VISIBLE);

            Toast.makeText(getContext(),
                    "New here? Let's create your account.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Existing user logs in
    private void handleLogin() {
        String email = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";
        String password = edtPassword.getText() != null ? edtPassword.getText().toString() : "";

        if (!isValidEmail(email)) {
            inputLayoutEmail.setError("Invalid email");
            return;
        } else {
            inputLayoutEmail.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            inputLayoutPassword.setError("Password required");
            return;
        } else {
            inputLayoutPassword.setError(null);
        }

        btnLogin.setEnabled(false);

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    btnLogin.setEnabled(true);

                    if (!task.isSuccessful()) {
                        Toast.makeText(getContext(),
                                "Login failed: " +
                                        (task.getException() != null ? task.getException().getMessage() : ""),
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    Toast.makeText(getContext(),
                            "Logged in!",
                            Toast.LENGTH_SHORT).show();

                    goToHome();
                });
    }

    // User taps "Sign up" CTA under step 2
    private void handleSignUp() {
        String email = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";

        if (!isValidEmail(email)) {
            inputLayoutEmail.setError("Invalid email");
            return;
        } else {
            inputLayoutEmail.setError(null);
        }

        // Show inline sign-up fields between email and Continue
        containerSignUpDetails.setVisibility(View.VISIBLE);

        // Optional: hide password login bits to keep UI clean
        inputLayoutPassword.setVisibility(View.GONE);
        txtForgotPassword.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);
    }

    // User taps "Create account"
    private void handleConfirmSignUp() {
        String email = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";
        String name = edtName.getText() != null ? edtName.getText().toString().trim() : "";
        String password = edtSignUpPassword.getText() != null ? edtSignUpPassword.getText().toString() : "";
        String passwordConfirm = edtSignUpPasswordConfirm.getText() != null ? edtSignUpPasswordConfirm.getText().toString() : "";

        boolean hasError = false;

        if (!isValidEmail(email)) {
            inputLayoutEmail.setError("Invalid email");
            hasError = true;
        } else {
            inputLayoutEmail.setError(null);
        }

        if (TextUtils.isEmpty(name)) {
            inputLayoutName.setError("Name required");
            hasError = true;
        } else {
            inputLayoutName.setError(null);
        }

        if (TextUtils.isEmpty(password)) {
            inputLayoutSignUpPassword.setError("Password required");
            hasError = true;
        } else {
            inputLayoutSignUpPassword.setError(null);
        }

        if (TextUtils.isEmpty(passwordConfirm)) {
            inputLayoutSignUpPasswordConfirm.setError("Please confirm password");
            hasError = true;
        } else if (!password.equals(passwordConfirm)) {
            inputLayoutSignUpPasswordConfirm.setError("Passwords do not match");
            hasError = true;
        } else {
            inputLayoutSignUpPasswordConfirm.setError(null);
        }

        if (hasError) {
            return;
        }

        btnConfirmSignUp.setEnabled(false);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    btnConfirmSignUp.setEnabled(true);

                    if (!task.isSuccessful() || task.getResult() == null) {
                        Toast.makeText(getContext(),
                                "Sign-up failed: " +
                                        (task.getException() != null ? task.getException().getMessage() : ""),
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    FirebaseUser firebaseUser = task.getResult().getUser();
                    if (firebaseUser == null) {
                        Toast.makeText(getContext(),
                                "Sign-up failed: no user returned",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    String uid = firebaseUser.getUid();
                    long now = System.currentTimeMillis();

                    User user = new User(uid, email, name, now);

                    db.collection("users")
                            .document(uid)
                            .set(user)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(),
                                        "Account created!",
                                        Toast.LENGTH_SHORT).show();
                                goToHome();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(),
                                        "Profile save failed: " + e.getMessage(),
                                        Toast.LENGTH_LONG).show();
                            });
                });
    }

    private void handleForgotPassword() {
        String email = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";

        if (!isValidEmail(email)) {
            Toast.makeText(getContext(),
                    "Enter a valid email first",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(),
                                "Reset link sent to " + email,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(),
                                "Failed to send reset link.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleGoogle() {
        // TODO: plug in your Google sign-in here
        Toast.makeText(getContext(),
                "Google sign-in not implemented yet",
                Toast.LENGTH_SHORT).show();
    }

    private void goToHome() {
        if (getActivity() instanceof MainActivity) {
            MainActivity main = (MainActivity) getActivity();
            main.navigateTo(R.id.nav_home);
        }
    }
}
