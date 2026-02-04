package com.example.foodloopapp;

public class CartItem {
    public String productId;
    public String name;
    public String pricePerUnitText;
    public double priceValue;
    public String unit;
    public long quantity;
    public String imageUrl;

    public CartItem() {}

    public CartItem(String productId, String name, String pricePerUnitText, double priceValue,
                    String unit, long quantity, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.pricePerUnitText = pricePerUnitText;
        this.priceValue = priceValue;
        this.unit = unit;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }
}
