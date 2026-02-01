package com.example.foodloopapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProducerFragment extends BaseFragment {

    private static final String TAG = "ProducerFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_producer, container, false);

        RecyclerView rv = root.findViewById(R.id.rvProducers);

        if (rv == null) {
            Log.e(TAG, "RecyclerView with ID R.id.rvProducers not found in layout.");
            return root; // Early return to prevent NullPointerException
        }

        List<Producer> demo = new ArrayList<>();
        demo.add(new Producer(1L, "Hof Müller", 47.3769, 8.5417));
        demo.add(new Producer(2L, "Biohof Schmidt", 46.9480, 7.4474));
        demo.add(new Producer(3L, "Kartoffelhof Meyer", 47.5596, 7.5886));

        rv.setAdapter(new ProducersAdapter(demo, producerId -> {
            if (!isAdded()) {
                Log.w(TAG, "Fragment not attached, cannot perform transaction.");
                return;
            }
            ProducerProfileFragment profile = ProducerProfileFragment.newInstance(producerId);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, profile) // Container-ID für den Fragmentwechsel
                    .addToBackStack(null)
                    .commit();
        }));

        return root;
    }
}
