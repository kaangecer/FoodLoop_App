package com.example.foodloopapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final List<Producer> producers = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        loadDummyProducers();

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map_container);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        return view;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add markers for producers
        for (Producer p : producers) {
            LatLng pos = new LatLng(p.getLat(), p.getLng());
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(p.getName()));
            if (marker != null) {
                marker.setTag(p.getId()); // store producer id
            }
        }

        // Move camera to first producer
        if (!producers.isEmpty()) {
            Producer first = producers.get(0);
            LatLng pos = new LatLng(first.getLat(), first.getLng());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 10f));
        }

        mMap.setOnMarkerClickListener(marker -> {
            Object tag = marker.getTag();
            if (tag instanceof Long) {
                long producerId = (Long) tag;
                navigateToProducerProfile(producerId);
            }
            return true; // we handled the click
        });
    }

    private void navigateToProducerProfile(long producerId) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).openProducerFromMap(producerId);
        }
    }

    private void loadDummyProducers() {
        producers.clear();
        producers.add(new Producer(1L, "Hof Müller", 52.5200, 13.4050));
        producers.add(new Producer(2L, "Biohof Schmidt", 52.4500, 13.3000));
        producers.add(new Producer(3L, "Kartoffelhof Meyer", 52.5500, 13.3500));
    }
}
