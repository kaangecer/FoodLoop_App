package com.example.foodloopapp;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnBrowse = view.findViewById(R.id.btnBrowseProducts);
        Button btnLogin  = view.findViewById(R.id.btnLogin);

        btnBrowse.setOnClickListener(v ->
                ((MainActivity) requireActivity()).navigateTo(R.id.nav_products));

        btnLogin.setOnClickListener(v -> {
            ((MainActivity)  requireActivity())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new LoginFragment())
                    .commit();
        });

        return view;
    }
}