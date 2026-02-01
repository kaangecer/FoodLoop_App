package com.example.foodloopapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProducersAdapter extends RecyclerView.Adapter<ProducersAdapter.VH> {

    public interface OnProducerProfileClick {
        void onOpenProfile(long producerId);
    }

    private final List<Producer> items;
    private final OnProducerProfileClick listener;

    public ProducersAdapter(List<Producer> items, OnProducerProfileClick listener) {
        this.items = items;
        this.listener = listener;
    }

    public static class VH extends RecyclerView.ViewHolder {
        TextView title, region, assortment, pickup;
        Button btnProfile;

        VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.producer_title);
            region = itemView.findViewById(R.id.producer_region);
            assortment = itemView.findViewById(R.id.producer_assortment);
            pickup = itemView.findViewById(R.id.producer_pickup);
            btnProfile = itemView.findViewById(R.id.btnProducerProfile);
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
        Context context = holder.itemView.getContext();

        holder.title.setText(p.getName());

        ProducerInfo info = getInfoForProducer(p.getId(), context);
        holder.region.setText(info.region);
        holder.assortment.setText(info.assortment);
        holder.pickup.setText(info.pickupTimes);

        holder.btnProfile.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOpenProfile(p.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private static class ProducerInfo {
        final String region;
        final String assortment;
        final String pickupTimes;

        ProducerInfo(String region, String assortment, String pickupTimes) {
            this.region = region;
            this.assortment = assortment;
            this.pickupTimes = pickupTimes;
        }
    }

    private ProducerInfo getInfoForProducer(long id, Context context) {
        if (id == 1L) {
            return new ProducerInfo(
                    context.getString(R.string.adapter_region_label, "Zürich, Schweiz"),
                    context.getString(R.string.adapter_assortment_label, "Obst & Gemüse"),
                    context.getString(R.string.adapter_pickup_label, "Mo–Fr 08–18 Uhr")
            );
        } else if (id == 2L) {
            return new ProducerInfo(
                    context.getString(R.string.adapter_region_label, "Bern, Schweiz"),
                    context.getString(R.string.adapter_assortment_label, "Milchprodukte & Eier (Bio)"),
                    context.getString(R.string.adapter_pickup_label, "Di–Sa 09–17 Uhr")
            );
        } else if (id == 3L) {
            return new ProducerInfo(
                    context.getString(R.string.adapter_region_label, "Basel, Schweiz"),
                    context.getString(R.string.adapter_assortment_label, "Kartoffeln & Wurzelgemüse"),
                    context.getString(R.string.adapter_pickup_label, "Do–So 10–16 Uhr")
            );
        }
        return new ProducerInfo(
                context.getString(R.string.adapter_region_label, context.getString(R.string.adapter_unknown_region)),
                context.getString(R.string.adapter_assortment_label, context.getString(R.string.adapter_unknown_assortment)),
                context.getString(R.string.adapter_pickup_label, context.getString(R.string.adapter_unknown_pickup))
        );
    }
}
