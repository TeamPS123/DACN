package com.psteam.foodlocation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.ActivityRestaurantMapBinding;
import com.psteam.foodlocation.ultilities.Para;

public class RestaurantMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityRestaurantMapBinding binding;
    private GoogleMap mMap;
    private View mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();
    }

    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(RestaurantMapActivity.this, R.color.white));// set status background white
        }
    }

    private void setListeners() {
        binding.buttonDirection.setOnClickListener(v -> {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?saddr=+"+ Para.currentLatLng() +"&daddr=11.5572771,107.8486486"));
            startActivity(intent);
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(11.5572771, 107.8466486);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.getUiSettings().setMapToolbarEnabled(false);

    }
}