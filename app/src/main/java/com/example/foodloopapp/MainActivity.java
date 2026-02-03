package com.example.foodloopapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_nav);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selected = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selected = new HomeFragment();
            } else if (id == R.id.nav_products) {
                selected = new ProductFragment();
            } else if (id == R.id.nav_producers) {
                selected = new ProducerFragment();
            } else if (id == R.id.nav_maps) {
                selected = new MapsFragment();
            } else if (id == R.id.nav_cart) {
                selected = new CartFragment();
            }

            if (selected != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selected)
                        .commit();
            }
            return true;
        });

        seedDemoData();


        // Show Home on first start
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }
    public void navigateTo(int menuItemId) {
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(menuItemId);  // behaves like a user tap
        }
    }

    public void openProducerFromMap(String producerId) {
        ProducerProfileFragment profileFragment = ProducerProfileFragment.newInstance(producerId);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, profileFragment)
                .addToBackStack(null)
                .commit();
    }
    private void seedDemoData() {
        Log.d("MainActivity", "Seeding demo data...");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Generate producer IDs upfront so we can reference them from products
        String meatProducerId   = db.collection("producers").document().getId();
        String eggsProducerId   = db.collection("producers").document().getId();
        String dairyProducerId  = db.collection("producers").document().getId();
        String veggiesProducerId= db.collection("producers").document().getId();
        String fruitProducerId  = db.collection("producers").document().getId();
        // If you strictly want 5, remove one of the above and its products.

        // --- Producers ---

        Map<String, Object> hofFleischmann = new HashMap<>();
        hofFleischmann.put("name", "Hof Fleischmann");
        hofFleischmann.put("city", "Berlin");
        hofFleischmann.put("address", "Weidenweg 10");
        hofFleischmann.put("lat", 52.5205);
        hofFleischmann.put("lng", 13.4095);
        hofFleischmann.put("description", "Regionaler Hof mit Rind-, Schweine- und Geflügelfleisch.");
        hofFleischmann.put("imageUrl", "");
        hofFleischmann.put("websiteUrl", "");
        hofFleischmann.put("phone", "");
        hofFleischmann.put("categoryFocus", "Fleisch");

        Map<String, Object> hofEierglueck = new HashMap<>();
        hofEierglueck.put("name", "Hof Eierglück");
        hofEierglueck.put("city", "Potsdam");
        hofEierglueck.put("address", "Hühnerweg 3");
        hofEierglueck.put("lat", 52.3950);
        hofEierglueck.put("lng", 13.0650);
        hofEierglueck.put("description", "Freilandhof mit frischen Eiern und Eiprodukten.");
        hofEierglueck.put("imageUrl", "");
        hofEierglueck.put("websiteUrl", "");
        hofEierglueck.put("phone", "");
        hofEierglueck.put("categoryFocus", "Eier");

        Map<String, Object> molkereiFrisch = new HashMap<>();
        molkereiFrisch.put("name", "Molkerei Frisch & Fein");
        molkereiFrisch.put("city", "Berlin");
        molkereiFrisch.put("address", "Milchstrasse 7");
        molkereiFrisch.put("lat", 52.5150);
        molkereiFrisch.put("lng", 13.4000);
        molkereiFrisch.put("description", "Familienbetrieb mit Milch, Joghurt und Käse.");
        molkereiFrisch.put("imageUrl", "");
        molkereiFrisch.put("websiteUrl", "");
        molkereiFrisch.put("phone", "");
        molkereiFrisch.put("categoryFocus", "Milchprodukte");

        Map<String, Object> gemuesegartenGrün = new HashMap<>();
        gemuesegartenGrün.put("name", "Gemüsegarten Grün");
        gemuesegartenGrün.put("city", "Berlin");
        gemuesegartenGrün.put("address", "Gartenweg 2");
        gemuesegartenGrün.put("lat", 52.5300);
        gemuesegartenGrün.put("lng", 13.3900);
        gemuesegartenGrün.put("description", "Vielfältiger Gemüsehof mit saisonalem Angebot.");
        gemuesegartenGrün.put("imageUrl", "");
        gemuesegartenGrün.put("websiteUrl", "");
        gemuesegartenGrün.put("phone", "");
        gemuesegartenGrün.put("categoryFocus", "Gemüse");

        Map<String, Object> obsthofSonnengruen = new HashMap<>();
        obsthofSonnengruen.put("name", "Obsthof Sonnengrün");
        obsthofSonnengruen.put("city", "Potsdam");
        obsthofSonnengruen.put("address", "Obstallee 4");
        obsthofSonnengruen.put("lat", 52.4100);
        obsthofSonnengruen.put("lng", 13.0500);
        obsthofSonnengruen.put("description", "Obstplantage mit Äpfeln, Birnen und Beeren.");
        obsthofSonnengruen.put("imageUrl", "");
        obsthofSonnengruen.put("websiteUrl", "");
        obsthofSonnengruen.put("phone", "");
        obsthofSonnengruen.put("categoryFocus", "Obst");

        db.collection("producers").document(meatProducerId).set(hofFleischmann);
        db.collection("producers").document(eggsProducerId).set(hofEierglueck);
        db.collection("producers").document(dairyProducerId).set(molkereiFrisch);
        db.collection("producers").document(veggiesProducerId).set(gemuesegartenGrün);
        db.collection("producers").document(fruitProducerId).set(obsthofSonnengruen);

        // --- Products ---

        // Meat – 6 products
        addProduct(db, "Rinderhackfleisch", "8,99 €", "kg", "Fleisch", meatProducerId,
                "Frisches Rinderhack aus artgerechter Haltung.");
        addProduct(db, "Schweineschnitzel", "10,49 €", "kg", "Fleisch", meatProducerId,
                "Zarte Schweineschnitzel, küchenfertig geschnitten.");
        addProduct(db, "Hähnchenbrustfilet", "9,79 €", "kg", "Fleisch", meatProducerId,
                "Saftige Hähnchenbrustfilets ohne Knochen.");
        addProduct(db, "Rinderbraten", "14,99 €", "kg", "Fleisch", meatProducerId,
                "Magerer Rinderbraten für Schmorgerichte.");
        addProduct(db, "Bratwürste", "6,49 €", "6 Stück", "Fleisch", meatProducerId,
                "Hausgemachte Bratwürste nach Hofrezept.");
        addProduct(db, "Suppenknochen Rind", "2,99 €", "kg", "Fleisch", meatProducerId,
                "Knochen für kräftige Suppen und Fonds.");

        // Eggs – 6 products
        addProduct(db, "Freilandeier Größe M", "3,49 €", "10 Stück", "Eier", eggsProducerId,
                "Freilandeier von glücklichen Hühnern.");
        addProduct(db, "Freilandeier Größe L", "3,79 €", "10 Stück", "Eier", eggsProducerId,
                "Große Eier mit kräftig gelbem Dotter.");
        addProduct(db, "Bunte Ostereier", "2,99 €", "6 Stück", "Eier", eggsProducerId,
                "Bereits gekochte und gefärbte Eier.");
        addProduct(db, "Eierlikör", "7,99 €", "0,5 Liter", "Eier", eggsProducerId,
                "Cremiger Eierlikör aus Hofeiern.");
        addProduct(db, "Eierteigwaren Bandnudeln", "3,59 €", "500 g", "Eier", eggsProducerId,
                "Bandnudeln mit hohem Eigehalt.");
        addProduct(db, "Spiegeleier-Box", "3,99 €", "10 Stück", "Eier", eggsProducerId,
                "Ausgewählte Eier mit perfekter Spiegelei-Form.");

        // Dairy – 6 products
        addProduct(db, "Frische Vollmilch 3,8%", "1,39 €", "Liter", "Milchprodukte", dairyProducerId,
                "Unbehandelte Frischmilch direkt von der Molkerei.");
        addProduct(db, "Joghurt Natur", "0,89 €", "500 g", "Milchprodukte", dairyProducerId,
                "Milder Naturjoghurt ohne Zusatzstoffe.");
        addProduct(db, "Gouda jung", "2,49 €", "200 g", "Milchprodukte", dairyProducerId,
                "Schnittkäse mit mildem Geschmack.");
        addProduct(db, "Butter", "2,29 €", "250 g", "Milchprodukte", dairyProducerId,
                "Traditionell hergestellte Sauerrahmbutter.");
        addProduct(db, "Quark Magerstufe", "1,19 €", "500 g", "Milchprodukte", dairyProducerId,
                "Magerquark für süße und herzhafte Speisen.");
        addProduct(db, "Sahne 30%", "1,09 €", "200 ml", "Milchprodukte", dairyProducerId,
                "Schlagsahne für Desserts und Kuchen.");

        // Veggies – 6 products
        addProduct(db, "Karotten Bund", "1,99 €", "Bund", "Gemüse", veggiesProducerId,
                "Knackige Möhren mit frischem Grün.");
        addProduct(db, "Kartoffeln festkochend", "3,49 €", "2,5 kg", "Gemüse", veggiesProducerId,
                "Festkochende Kartoffeln für Salate und Gratins.");
        addProduct(db, "Zucchini", "2,29 €", "kg", "Gemüse", veggiesProducerId,
                "Zarte Zucchini aus Freilandanbau.");
        addProduct(db, "Salatkopf Mischsalat", "1,79 €", "Stück", "Gemüse", veggiesProducerId,
                "Bunter Blattsalat, erntefrisch.");
        addProduct(db, "Tomaten", "3,29 €", "kg", "Gemüse", veggiesProducerId,
                "Aromatische Strauchtomaten.");
        addProduct(db, "Paprika Mix", "2,99 €", "3 Stück", "Gemüse", veggiesProducerId,
                "Rote, gelbe und grüne Paprika im Mix.");

        // Fruit – 6 products
        addProduct(db, "Tafeläpfel Elstar", "2,49 €", "kg", "Obst", fruitProducerId,
                "Saftige Äpfel mit ausgewogener Säure.");
        addProduct(db, "Birnen Conference", "2,79 €", "kg", "Obst", fruitProducerId,
                "Süße Birnen mit zartem Fruchtfleisch.");
        addProduct(db, "Erdbeeren Schale", "3,99 €", "500 g", "Obst", fruitProducerId,
                "Sonnengereifte Erdbeeren aus Freilandanbau.");
        addProduct(db, "Himbeeren Schale", "2,99 €", "200 g", "Obst", fruitProducerId,
                "Aromatische Himbeeren, handgepflückt.");
        addProduct(db, "Kirschen", "5,49 €", "kg", "Obst", fruitProducerId,
                "Süßkirschen mit intensivem Geschmack.");
        addProduct(db, "Pflaumen", "3,49 €", "kg", "Obst", fruitProducerId,
                "Reife Pflaumen, ideal zum Backen oder Naschen.");

        Log.d("MainActivity", "seedDemoData() dispatched all writes");
    }

    // Helper to avoid repetition
    private void addProduct(FirebaseFirestore db,
                            String name,
                            String pricePerUnit,
                            String unit,
                            String category,
                            String producerId,
                            String description) {
        Map<String, Object> product = new HashMap<>();
        product.put("name", name);
        product.put("pricePerUnit", pricePerUnit);
        product.put("unit", unit);
        product.put("category", category);
        product.put("producerId", producerId);
        product.put("description", description);
        product.put("imageUrl", "");

        db.collection("products").add(product)
                .addOnSuccessListener(ref ->
                        Log.d("MainActivity", "Product " + name + " seeded, id = " + ref.getId()))
                .addOnFailureListener(e ->
                        Log.e("MainActivity", "Failed to seed product " + name, e));
    }


}
