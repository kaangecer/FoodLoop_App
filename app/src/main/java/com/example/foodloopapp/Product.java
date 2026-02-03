package com.example.foodloopapp;

public class Product {
    public final String name;
    public final String price;

    public Product(String name, String price) {
        this.name = name;
        this.price = price;
    }

    // id is usually set from Firestore document id
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public String getPricePerUnit() { return pricePerUnit; }
    public String getUnit() { return unit; }
    public String getCategory() { return category; }
    public String getProducerId() { return producerId; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }

    public void setName(String name) { this.name = name; }
    public void setPricePerUnit(String pricePerUnit) { this.pricePerUnit = pricePerUnit; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setCategory(String category) { this.category = category; }
    public void setProducerId(String producerId) { this.producerId = producerId; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
