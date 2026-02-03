package com.example.foodloopapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProducerAdapter extends RecyclerView.Adapter<ProducerAdapter.VH> {

    public interface OnProducerClick {
        void onOpenProfile(String producerId);
    }

    private final List<Producer> items = new ArrayList<>();
    private final OnProducerClick listener;

    public ProducerAdapter(OnProducerClick listener) {
        this.listener = listener;
    }

    // Call this from ProducerFragment after loading from Firestore
    public void setItems(List<Producer> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title;
        TextView city;
        View root;

        VH(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            title = itemView.findViewById(R.id.producer_title);
            city = itemView.findViewById(R.id.producer_region); // or a dedicated city TextView
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_producer, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Producer p = items.get(position);

        holder.title.setText(p.getName());
        holder.city.setText(p.getCity());

        holder.root.setOnClickListener(v -> {
            if (listener != null && p.getId() != null) {
                listener.onOpenProfile(p.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
