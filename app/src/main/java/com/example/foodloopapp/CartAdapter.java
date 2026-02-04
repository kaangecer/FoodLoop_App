package com.example.foodloopapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget .RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH> {

    public interface Listener {
        void onPlus(CartItem item);
        void onMinus(CartItem item);
        void onRemove(CartItem item);
    }

    private final List<CartItem> items;
    private final Listener listener;

    public CartAdapter(List<CartItem> items, Listener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        CartItem item = items.get(position);

        h.tvName.setText(item.name != null ? item.name : "");

        String priceText = item.pricePerUnitText != null ? item.pricePerUnitText : "";
        String unitText = (item.unit != null && !item.unit.isEmpty()) ? (" / " + item.unit) : "";
        h.tvPrice.setText(priceText + unitText);

        h.tvQty.setText(String.valueOf(item.quantity));

        h.btnPlus.setOnClickListener(v -> listener.onPlus(item));
        h.btnMinus.setOnClickListener(v -> listener.onMinus(item));
        h.btnRemove.setOnClickListener(v -> listener.onRemove(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice, tvQty;
        Button btnPlus, btnMinus, btnRemove;

        VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvPrice = itemView.findViewById(R.id.tvItemPrice);
            tvQty = itemView.findViewById(R.id.tvQty);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
