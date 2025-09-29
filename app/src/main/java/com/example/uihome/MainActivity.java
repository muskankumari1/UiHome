package com.example.uihome;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uihome.Adapter.FeaturedAdapter;
import com.example.uihome.Adapter.RecommendedAdapter;
import com.example.uihome.Model.Property;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerFeatured, recyclerRecommended;
    FeaturedAdapter featuredAdapter;
    RecommendedAdapter recommendedAdapter;
    BottomNavigationView bottomNavigation;

    Button btnSort, btnPrice, btnType;

    List<Property> originalFeatured = new ArrayList<>();
    List<Property> originalRecommended = new ArrayList<>();

    List<Property> featuredList = new ArrayList<>();
    List<Property> recommendedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerFeatured = findViewById(R.id.recyclerFeatured);
        recyclerRecommended = findViewById(R.id.recyclerRecommended);
        bottomNavigation = findViewById(R.id.bottomNavigationView);

        btnSort = findViewById(R.id.btnsort);
        btnPrice = findViewById(R.id.btnPrice);
        btnType = findViewById(R.id.btnType);

        loadInitialData();
        setupFeaturedRecycler();
        setupRecommendedRecycler();

        btnSort.setOnClickListener(v -> showSortBottomSheet());
        btnPrice.setOnClickListener(v -> showPriceBottomSheet());
        btnType.setOnClickListener(v -> showTypeBottomSheet());
    }

    private void loadInitialData() {
        originalFeatured.add(new Property("$2,950/mo", "New York", "2 Bed • 2 Bath • 1200 sqft", R.drawable.ic_apartments));
        originalFeatured.add(new Property("$4,500/mo", "Los Angeles", "5 Bed • 6 Bath • 7000 sqft", R.drawable.home));
        originalFeatured.add(new Property("$3,200/mo", "Chicago", "Commercial • 4500 sqft", R.drawable.frontvilla));

        originalRecommended.add(new Property("$3,500/mo", "Miami", "4 Bed • 3 Bath", R.drawable.beachhouse));
        originalRecommended.add(new Property("$5,200/mo", "San Francisco", "3 Bed • 4 Bath", R.drawable.penthouse));
        originalRecommended.add(new Property("$2,100/mo", "Texas", "2 Bed • 1 Bath", R.drawable.cottage));

        featuredList = new ArrayList<>(originalFeatured);
        recommendedList = new ArrayList<>(originalRecommended);
    }

    private void setupFeaturedRecycler() {
        recyclerFeatured.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        featuredAdapter = new FeaturedAdapter(featuredList, this);
        recyclerFeatured.setAdapter(featuredAdapter);
    }

    private void setupRecommendedRecycler() {
        recyclerRecommended.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recommendedAdapter = new RecommendedAdapter(recommendedList, this);
        recyclerRecommended.setAdapter(recommendedAdapter);
    }

    // ==================== BottomSheets =====================

    private void showSortBottomSheet() {
        View sheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_sort, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(sheetView);

        RadioGroup rgSort = sheetView.findViewById(R.id.rgSortOptions);
        RadioButton rbLowToHigh = sheetView.findViewById(R.id.rbLowToHigh);
        RadioButton rbHighToLow = sheetView.findViewById(R.id.rbHighToLow);
        Button btnApply = sheetView.findViewById(R.id.btnApplySort);

        btnApply.setOnClickListener(v -> {
            int selectedId = rgSort.getCheckedRadioButtonId();
            if (selectedId == rbLowToHigh.getId()) {
                sortListLowToHigh(featuredList);
                sortListLowToHigh(recommendedList);
            } else if (selectedId == rbHighToLow.getId()) {
                sortListHighToLow(featuredList);
                sortListHighToLow(recommendedList);
            }

            featuredAdapter.updateList(featuredList);
            recommendedAdapter.updateList(recommendedList);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showPriceBottomSheet() {
        View sheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_price, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(sheetView);

        EditText etMin = sheetView.findViewById(R.id.etMin);
        EditText etMax = sheetView.findViewById(R.id.etMax);
        Button btnApply = sheetView.findViewById(R.id.btnApplyPrice);

        btnApply.setOnClickListener(v -> {
            String minStr = etMin.getText().toString().trim();
            String maxStr = etMax.getText().toString().trim();
            Integer minVal = null, maxVal = null;
            if (!TextUtils.isEmpty(minStr)) minVal = Integer.parseInt(minStr);
            if (!TextUtils.isEmpty(maxStr)) maxVal = Integer.parseInt(maxStr);

            featuredList = filterByPrice(originalFeatured, minVal, maxVal);
            recommendedList = filterByPrice(originalRecommended, minVal, maxVal);

            featuredAdapter.updateList(featuredList);
            recommendedAdapter.updateList(recommendedList);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showTypeBottomSheet() {
        View sheetView = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_type, null);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(sheetView);

        CheckBox cbApartment = sheetView.findViewById(R.id.cbApartment);
        CheckBox cbVilla = sheetView.findViewById(R.id.cbVilla);
        CheckBox cbCommercial = sheetView.findViewById(R.id.cbCommercial);
        CheckBox cbPlots = sheetView.findViewById(R.id.cbPlots);
        Button btnApply = sheetView.findViewById(R.id.btnApplyType);

        btnApply.setOnClickListener(v -> {
            List<String> types = new ArrayList<>();
            if (cbApartment.isChecked()) types.add("Apartment");
            if (cbVilla.isChecked()) types.add("Villa");
            if (cbCommercial.isChecked()) types.add("Commercial");
            if (cbPlots.isChecked()) types.add("Plots");

            if (!types.isEmpty()) {
                featuredList = filterByType(originalFeatured, types);
                recommendedList = filterByType(originalRecommended, types);
            } else {
                featuredList = new ArrayList<>(originalFeatured);
                recommendedList = new ArrayList<>(originalRecommended);
            }

            featuredAdapter.updateList(featuredList);
            recommendedAdapter.updateList(recommendedList);
            dialog.dismiss();
        });

        dialog.show();
    }

    // ==================== Helpers =====================

    private void sortListLowToHigh(List<Property> list) {
        Collections.sort(list, Comparator.comparingInt(p -> parsePrice(p.getPrice())));
    }

    private void sortListHighToLow(List<Property> list) {
        Collections.sort(list, (p1, p2) -> parsePrice(p2.getPrice()) - parsePrice(p1.getPrice()));
    }

    private List<Property> filterByPrice(List<Property> list, Integer min, Integer max) {
        List<Property> filtered = new ArrayList<>();
        for (Property p : list) {
            int price = parsePrice(p.getPrice());
            if ((min == null || price >= min) && (max == null || price <= max)) {
                filtered.add(p);
            }
        }
        return filtered;
    }

    private List<Property> filterByType(List<Property> list, List<String> types) {
        List<Property> filtered = new ArrayList<>();
        for (Property p : list) {
            String detailsLower = p.getDetails().toLowerCase();
            for (String t : types) {
                if (detailsLower.contains(t.toLowerCase())) {
                    filtered.add(p);
                    break;
                }
            }
        }
        return filtered;
    }

    private int parsePrice(String price) {
        try {
            String cleaned = price.replaceAll("[^0-9]", "");
            if (cleaned.isEmpty()) return 0;
            return Integer.parseInt(cleaned);
        } catch (Exception e) {
            return 0;
        }
    }
}
