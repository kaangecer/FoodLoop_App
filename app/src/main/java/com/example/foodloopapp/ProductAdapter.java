package com.example.foodloopapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.VH> {

    public interface OnProductClick {
        void onProductClick(Product product);
    }

    private final List<Product> items;
    private final OnProductClick listener;

    public ProductAdapter(List<Product> items, OnProductClick listener) {
        this.items = items;
        this.listener = listener;
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        View root;

        VH(@NonNull View itemView) {
            super(itemView);
            root = itemView;
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Product p = items.get(position);
        holder.tvName.setText(p.getName());
        holder.tvPrice.setText(p.getPricePerUnit());

        holder.root.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(p);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
