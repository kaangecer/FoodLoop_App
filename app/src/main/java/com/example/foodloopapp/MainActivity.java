package com.example.foodloopapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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
            } else if (id == R.id.nav_explore) {
                selected = new ExploreFragment();
            } else if (id == R.id.nav_maps) {
                selected = new MapsFragment();
            } else if (id == R.id.nav_cart) {
                selected = new CartFragment();
            } else if (id == R.id.nav_profile) {
                selected = new ProfileFragment();
            }

            if (selected != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selected)
                        .commit();
            }
            return true;
        });


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
        // Switch to the Explore tab (where products/producers live)
        bottomNav.setSelectedItemId(R.id.nav_explore);

        // Open the specific producer profile
        ProducerProfileFragment profileFragment = ProducerProfileFragment.newInstance(producerId);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .addToBackStack(null)
                .commit();
    }
}
