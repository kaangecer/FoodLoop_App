package com.example.foodloopapp;

public class Product {

    // Firestore requires a public no-arg constructor
    public Product() { }

    private String id;          // Firestore document id (set manually)
    private String name;
    private String pricePerUnit;
    private String unit;
    private String category;
    private String producerId;
    private String description;
    private String imageUrl;

    // Optional convenience constructor (not used by Firestore)
    public Product(String id,
                   String name,
                   String pricePerUnit,
                   String unit,
                   String category,
                   String producerId,
                   String description,
                   String imageUrl) {
        this.id = id;
        this.name = name;
        this.pricePerUnit = pricePerUnit;
        this.unit = unit;
        this.category = category;
        this.producerId = producerId;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters (Firestore uses these / fields directly)
    public String getId() { return id; }
    public String getName() { return name; }
    public String getPricePerUnit() { return pricePerUnit; }
    public String getUnit() { return unit; }
    public String getCategory() { return category; }
    public String getProducerId() { return producerId; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPricePerUnit(String pricePerUnit) { this.pricePerUnit = pricePerUnit; }
    public void setUnit(String unit) { this.unit = unit; }
    public void setCategory(String category) { this.category = category; }
    public void setProducerId(String producerId) { this.producerId = producerId; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
