package com.example.foodloopapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

public class ProducerProfileFragment extends Fragment {

    private static final String ARG_PRODUCER_ID = "producer_id";

    public static ProducerProfileFragment newInstance(String producerId) {
        ProducerProfileFragment fragment = new ProducerProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PRODUCER_ID, producerId);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("StringFormatMatches")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_producer_profile, container, false);

        String producerId = null;
        if (getArguments() != null) {
            producerId = getArguments().getString(ARG_PRODUCER_ID, null);
        }

        TextView title = view.findViewById(R.id.producer_profile_title);
        TextView region = view.findViewById(R.id.producer_profile_region);
        TextView assortment = view.findViewById(R.id.producer_profile_assortment);
        TextView pickup = view.findViewById(R.id.producer_profile_pickup);
        TextView about = view.findViewById(R.id.producer_profile_about);

        if (producerId != null) {
            loadProducer(producerId, title, region, assortment, pickup, about);
        }  else {
            title.setText(getString(R.string.unknown_producer_title));
            region.setText(R.string.unknown_producer_region);
            assortment.setText(R.string.unknown_producer_assortment);
            pickup.setText(R.string.unknown_producer_pickup);
            about.setText(R.string.unknown_producer_about);
        }
        return view;
    }
    private void loadProducer(String producerId,
                              TextView title,
                              TextView region,
                              TextView assortment,
                              TextView pickup,
                              TextView about) {
        if (producerId == null) {
            title.setText(getString(R.string.unknown_producer_title));
            region.setText(R.string.unknown_producer_region);
            assortment.setText(R.string.unknown_producer_assortment);
            pickup.setText(R.string.unknown_producer_pickup);
            about.setText(R.string.unknown_producer_about);
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("producers")
                .document(producerId)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Producer p = doc.toObject(Producer.class);
                        if (p!= null) {
                            title.setText(p.getName());
                            region.setText(p.getCity());
                        } else title.setText(getString(R.string.unknown_producer_title));
                    } else {
                        title.setText(getString(R.string.unknown_producer_title));
                    }
                });
    }

}
