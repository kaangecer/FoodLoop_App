package com.example.foodloopapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_products, container, false);

        RecyclerView rv = root.findViewById(R.id.rvProducts);

        // Testdaten (damit du sofort was siehst)
        List<Product> products = new ArrayList<>();
        products.add(new Product("Apfel", "1,29 €"));
        products.add(new Product("Brot", "2,49 €"));
        products.add(new Product("Milch", "0,99 €"));

        ProductsAdapter adapter = new ProductsAdapter(products);
        rv.setAdapter(adapter);

        return root;
    }
}
