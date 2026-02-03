package com.example.foodloopapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

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
        TextView assortment;
        TextView categoryTag;

        VH(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.producer_title);
            city = itemView.findViewById(R.id.producer_region);
            assortment = itemView.findViewById(R.id.producer_assortment);
            categoryTag = itemView.findViewById(R.id.producer_category_tag);
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
        holder.assortment.setText(p.getDescription()); // or a short field

        String cat = p.getCategoryFocus();
        if (cat != null && !cat.isEmpty()) {
            holder.categoryTag.setText(cat);
            holder.categoryTag.setVisibility(View.VISIBLE);
        } else {
            holder.categoryTag.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOpenProfile(p.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
