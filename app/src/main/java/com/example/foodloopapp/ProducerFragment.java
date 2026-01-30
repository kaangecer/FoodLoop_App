package com.example.foodloopapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProducerFragment extends Fragment {

    private static final String ARG_PRODUCER_ID = "producer_id";

    public static ProducerFragment newInstance(long producerId) {
        ProducerFragment fragment = new ProducerFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PRODUCER_ID, producerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producer, container, false);

        long producerId = -1;
        if (getArguments() != null) {
            producerId = getArguments().getLong(ARG_PRODUCER_ID, -1);
        }

        TextView title = view.findViewById(R.id.producer_title);
        TextView details = view.findViewById(R.id.producer_details);

        String name;
        String description;

        if (producerId == 1L) {
            name = "Hof Müller";
            description = "Regionaler Bauernhof mit frischem Obst und Gemüse.";
        } else if (producerId == 2L) {
            name = "Biohof Schmidt";
            description = "Zertifizierter Biohof mit Milchprodukten und Eiern.";
        } else if (producerId == 3L) {
            name = "Kartoffelhof Meyer";
            description = "Kartoffeln, Wurzelgemüse und saisonale Produkte.";
        } else {
            name = "Unbekannter Produzent (" + producerId + ")";
            description = "Beispieldaten, später aus der Datenbank laden.";
        }

        title.setText(name);
        details.setText(description);

        return view;
    }
}
