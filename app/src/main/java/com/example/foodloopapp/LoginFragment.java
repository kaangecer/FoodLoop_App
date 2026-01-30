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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.example.foodloopapp.R;          // <-- adjust package
import com.google.android.material.button.MaterialButton;


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
    private LinearLayout containerSignUpDetails;
    private TextInputLayout inputLayoutName;
    private TextInputEditText edtName;
    private TextInputLayout inputLayoutSignUpPassword;
    private TextInputEditText edtSignUpPassword;
    private TextInputLayout inputLayoutSignUpPasswordConfirm;
    private TextInputEditText edtSignUpPasswordConfirm;
    private MaterialButton btnConfirmSignUp;

    private MaterialButton btnGoogle;

    // Fake: in a real app you would query backend to see if email exists
    private boolean isExistingUser(String email) {
        // Demo rule: emails ending with "example.com" are treated as existing users
        return email != null && email.endsWith("@example.com");
    }

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
        btnGoogle = view.findViewById(R.id.btnGoogle);

        // Sign-up detail views
        containerSignUpDetails = view.findViewById(R.id.containerSignUpDetails);
        inputLayoutName = view.findViewById(R.id.inputLayoutName);
        edtName = view.findViewById(R.id.edtName);
        inputLayoutSignUpPassword = view.findViewById(R.id.inputLayoutSignUpPassword);
        edtSignUpPassword = view.findViewById(R.id.edtSignUpPassword);
        inputLayoutSignUpPasswordConfirm = view.findViewById(R.id.inputLayoutSignUpPasswordConfirm);
        edtSignUpPasswordConfirm = view.findViewById(R.id.edtSignUpPasswordConfirm);
        btnConfirmSignUp = view.findViewById(R.id.btnConfirmSignUp);

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
        Toast.makeText(getContext(), "Continue clicked", Toast.LENGTH_SHORT).show();  // debug

        String email = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";

        if (!isValidEmail(email)) {
            inputLayoutEmail.setError("Please enter a valid email");
            return;
        } else {
            inputLayoutEmail.setError(null);
        }

        // Decide which UI to show in step 2
        boolean existing = isExistingUser(email);
        showStep2(existing);
    }

    private boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Show password login or sign up content depending on whether this is an existing user.
     */
    private void showStep2(boolean existingUser) {
        containerStep2.setVisibility(View.VISIBLE);

        if (existingUser) {
            // Show password login UI
            inputLayoutPassword.setVisibility(View.VISIBLE);
            txtForgotPassword.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.VISIBLE);

            // Hide signup‑only bits
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

            // Show signup UI
            txtNewAccountInfo.setVisibility(View.VISIBLE);
            btnSignUp.setVisibility(View.VISIBLE);

            Toast.makeText(getContext(),
                    "New here? Let's create your account.",
                    Toast.LENGTH_SHORT).show();
        }
    }

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

        // TODO: call your real login API here
        Toast.makeText(getContext(),
                "Logging in… (fake for now)",
                Toast.LENGTH_SHORT).show();
    }

    private void handleSignUp() {
        String email = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";

        if (!isValidEmail(email)) {
            inputLayoutEmail.setError("Invalid email");
            return;
        } else {
            inputLayoutEmail.setError(null);
        }
        containerSignUpDetails.setVisibility(View.VISIBLE);

        // Optional: Login-spezifische UI ausblenden, damit es übersichtlich bleibt
        inputLayoutPassword.setVisibility(View.GONE);
        txtForgotPassword.setVisibility(View.GONE);
        btnLogin.setVisibility(View.GONE);


        // TODO: navigate to sign‑up details screen or call signup API
        Toast.makeText(getContext(),
                "Starting sign‑up for " + email,
                Toast.LENGTH_SHORT).show();
    }
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

        // TODO: hier echte Sign-up Logik / API Call einbauen
        Toast.makeText(getContext(),
                "Creating account for " + email,
                Toast.LENGTH_SHORT).show();

        // Optional: nach erfolgreichem Sign-up direkt in Login-Flow wechseln
        // oder Navigation zum Home-Screen.
    }

    private void handleForgotPassword() {
        String email = edtEmail.getText() != null ? edtEmail.getText().toString().trim() : "";

        if (!isValidEmail(email)) {
            Toast.makeText(getContext(),
                    "Enter a valid email first",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: trigger password reset flow
        Toast.makeText(getContext(),
                "Password reset link would be sent to " + email,
                Toast.LENGTH_SHORT).show();
    }

    private void handleGoogle() {
        // TODO: plug in your Google sign‑in here
        Toast.makeText(getContext(),
                "Google sign‑in not implemented yet",
                Toast.LENGTH_SHORT).show();
    }
}
