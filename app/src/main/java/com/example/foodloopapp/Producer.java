package com.example.foodloopapp;

public class Producer {

    private String id;           // Firestore document id
    private String name;         // e.g. "Hof Müller"
    private String city;         // e.g. "Berlin"
    private String address;      // street + house number
    private double lat;          // for map
    private double lng;          // for map
    private String description;  // short profile / story
    private String imageUrl;     // farm logo or photo
    private String websiteUrl;   // optional
    private String phone;        // optional contact

    // Required empty constructor for Firestore
    public Producer() {}
    // Convenience constructor
    public Producer(String name,
                    String city,
                    String address,
                    double lat,
                    double lng,
                    String description,
                    String imageUrl,
                    String websiteUrl,
                    String phone) {
        this.name = name;
        this.city = city;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.description = description;
        this.imageUrl = imageUrl;
        this.websiteUrl = websiteUrl;
        this.phone = phone;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public String getCity() { return city; }
    public String getAddress() { return address; }
    public double getLat() { return lat; }
    public double getLng() { return lng; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public String getWebsiteUrl() { return websiteUrl; }
    public String getPhone() { return phone; }

    public void setName(String name) { this.name = name; }
    public void setCity(String city) { this.city = city; }
    public void setAddress(String address) { this.address = address; }
    public void setLat(double lat) { this.lat = lat; }
    public void setLng(double lng) { this.lng = lng; }
    public void setDescription(String description) { this.description = description; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setWebsiteUrl(String websiteUrl) { this.websiteUrl = websiteUrl; }
    public void setPhone(String phone) { this.phone = phone; }
}
