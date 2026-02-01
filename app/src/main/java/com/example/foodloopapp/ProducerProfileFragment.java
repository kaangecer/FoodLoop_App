package com.example.foodloopapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ProducerProfileFragment extends BaseFragment {

    private static final String ARG_PRODUCER_ID = "producer_id";

    public static ProducerProfileFragment newInstance(long producerId) {
        ProducerProfileFragment fragment = new ProducerProfileFragment();
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

        View view = inflater.inflate(R.layout.fragment_producer_profile, container, false);

        long producerId = -1L;
        if (getArguments() != null) {
            producerId = getArguments().getLong(ARG_PRODUCER_ID, -1L);
        }

        TextView title = view.findViewById(R.id.producer_profile_title);
        TextView region = view.findViewById(R.id.producer_profile_region);
        TextView assortment = view.findViewById(R.id.producer_profile_assortment);
        TextView pickup = view.findViewById(R.id.producer_profile_pickup);
        TextView about = view.findViewById(R.id.producer_profile_about);

        if (producerId == 1L) {
            title.setText(R.string.hof_mueller_name);
            region.setText(R.string.hof_mueller_region);
            assortment.setText(R.string.hof_mueller_assortment);
            pickup.setText(R.string.hof_mueller_pickup);
            about.setText(R.string.hof_mueller_about);
        } else if (producerId == 2L) {
            title.setText(R.string.biohof_schmidt_name);
            region.setText(R.string.biohof_schmidt_region);
            assortment.setText(R.string.biohof_schmidt_assortment);
            pickup.setText(R.string.biohof_schmidt_pickup);
            about.setText(R.string.biohof_schmidt_about);
        } else if (producerId == 3L) {
            title.setText(R.string.kartoffelhof_meyer_name);
            region.setText(R.string.kartoffelhof_meyer_region);
            assortment.setText(R.string.kartoffelhof_meyer_assortment);
            pickup.setText(R.string.kartoffelhof_meyer_pickup);
            about.setText(R.string.kartoffelhof_meyer_about);
        } else {
            title.setText(getString(R.string.unknown_producer_title, producerId));
            region.setText(R.string.unknown_producer_region);
            assortment.setText(R.string.unknown_producer_assortment);
            pickup.setText(R.string.unknown_producer_pickup);
            about.setText(R.string.unknown_producer_about);
        }

        return view;
    }
}
