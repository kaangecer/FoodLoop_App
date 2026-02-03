package com.example.foodloopapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.foodloopapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_nav);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selected = new HomeFragment();
            } else if (id == R.id.nav_products) {
                selected = new ProductFragment();
            } else if (id == R.id.nav_producers) {
                selected = new ProducerFragment();
            } else if (id == R.id.nav_maps) {
                selected = new MapsFragment();
            } else if (id == R.id.nav_cart) {
                selected = new CartFragment();
            }

            if (selected != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selected)
                        .commit();
            }
            return true;
        });

        // Only seed sample auth users + profiles
        seedSampleUsers();

        // Show Home on first start
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }

    public void navigateTo(int menuItemId) {
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(menuItemId);  // behaves like a user tap
        }
    }

    public void openProducerFromMap(String producerId) {
        ProducerProfileFragment profileFragment = ProducerProfileFragment.newInstance(producerId);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .addToBackStack(null)
                .commit();
    }

    // ---------- Sample user seeding ----------

    public static void seedSampleUsers() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Sample users
        createUserWithProfile(
                auth, db,
                "alice@foodloop.test",
                "password123",
                "Alice Bauer"
        );

        createUserWithProfile(
                auth, db,
                "bob@foodloop.test",
                "password123",
                "Bob Schneider"
        );
    }

    private static void createUserWithProfile(FirebaseAuth auth,
                                              FirebaseFirestore db,
                                              String email,
                                              String password,
                                              String fullName) {

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            String uid = task.getResult().getUser().getUid();
                            long now = System.currentTimeMillis();

                            User user = new User(uid, email, fullName, now);

                            db.collection("users")
                                    .document(uid)
                                    .set(user)
                                    .addOnSuccessListener(aVoid ->
                                            Log.d(TAG, "Seeded user: " + email))
                                    .addOnFailureListener(e ->
                                            Log.e(TAG, "Failed to write profile for " + email, e));
                        } else {
                            Log.e(TAG, "Auth createUser failed for " + email, task.getException());
                        }
                    }
                });
    }
}
