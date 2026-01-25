package com.example.foodloopapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;

public abstract class BaseFragment extends Fragment {
    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Common setup for all fragments goes here
        // e.g. set background color, log screen name, etc.
    }
}
