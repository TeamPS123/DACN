package com.psteam.foodlocation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.MapRestaurantAdapter;
import com.psteam.foodlocation.databinding.ActivityMapBinding;
import com.psteam.foodlocation.listeners.MapRestaurantListener;
import com.psteam.foodlocation.models.RestaurantModel;
import com.psteam.foodlocation.services.LocationService;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.Para;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, MapRestaurantListener {

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private GoogleMap mMap;
    private ActivityMapBinding binding;
    private View mapView;
    private ImageView buttonMyLocation;
    private ImageView iconLocation;

    private CardView layoutBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;

    private RecyclerView recyclerViewSearch;
    private MapRestaurantAdapter mapRestaurantAdapter;
    private ArrayList<RestaurantModel> restaurantModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(MapActivity.this, R.color.white));// set status background white
        }

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        buttonMyLocation = findViewById(R.id.buttonMyLocation);
        iconLocation = findViewById(R.id.iconLocation);
        initBottomSheetRestaurant();
    }

    private void initSearchRestaurant() {
        restaurantModels = new ArrayList<>();
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant, "11.5572771,107.8466486"));
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant, "11.5584221,107.8403592"));
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant, "11.5567423,107.842969"));
        restaurantModels.add(new RestaurantModel("ToCoToCo", "875/22, Đường Nguyễn văn Cừ, Phường Lộc Phát, Tp.Bảo Lộc, Tỉnh Lâm Đồng", "2.5", R.drawable.tocotoco_restaurant, "11.5612465,107.8423469"));

        mapRestaurantAdapter = new MapRestaurantAdapter(restaurantModels, this);
        recyclerViewSearch.setAdapter(mapRestaurantAdapter);

    }

    private int peekHeight;

    private void initBottomSheetRestaurant() {
        layoutBottomSheet = findViewById(R.id.bottomSheetContainer);
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        peekHeight = bottomSheetBehavior.getPeekHeight();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        recyclerViewSearch = findViewById(R.id.recycleViewSearchRestaurant);
        initSearchRestaurant();


    }

    private void checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    MapActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION
            );
        } else {
            startLocationService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                startLocationService();
            else {
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                stopServices();

            }
        }
    }

    private void setListeners() {
        buttonMyLocation.setOnClickListener(v -> {
            if (mMap != null) {
                getMyLocation();
            }
        });
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(serviceInfo.service.getClassName())) {
                    if (serviceInfo.foreground) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(getApplicationContext(), "Location service started", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopServices() {
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(getApplicationContext(), "Location services stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean markerSelected = false;
    private Marker markerClicked;
    private Marker oldMarkerClicked;
    private ArrayList<Marker> markerArrayList;


    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        markerArrayList = new ArrayList<>();
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Disable button current location
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            locationButton.setVisibility(View.GONE);
        }

        // Add a marker in Sydney and move the camera
        LatLng currentLocation = new LatLng(Para.latitude, Para.longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                for (RestaurantModel restaurantModel : restaurantModels) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(restaurantModel.LatLng())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .title(restaurantModel.getName()));
                    marker.setTag(restaurantModel);
                    markerArrayList.add(marker);
                }
            }
        });


        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (bottomSheetBehavior != null) {
                    bottomSheetBehavior.setPeekHeight(0);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

                if (!markerSelected) {
                    if (iconLocation.getVisibility() == View.GONE)
                        iconLocation.setVisibility(View.VISIBLE);

                    if (markerClicked != null) {
                        markerClicked.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        markerClicked.hideInfoWindow();
                    }
                }
                if (oldMarkerClicked != null) {
                    oldMarkerClicked.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    oldMarkerClicked.hideInfoWindow();
                    oldMarkerClicked = null;
                }


            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                if (bottomSheetBehavior != null) {
                    bottomSheetBehavior.setPeekHeight(peekHeight);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

                if (markerSelected) {
                    iconLocation.setVisibility(View.GONE);
                    oldMarkerClicked = markerClicked;
                    markerSelected = false;
                }
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(((RestaurantModel)marker.getTag()).LatLng(), 15);
                mMap.animateCamera(cameraUpdate, 500, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onCancel() {
                        return;
                    }

                    @Override
                    public void onFinish() {
                        markerSelected = true;
                        markerClicked = marker;
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        marker.showInfoWindow();
                    }
                });



                return true;
            }
        });

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(currentLocation);
        circleOptions.radius(Constants.RADIUS);
        circleOptions.fillColor(Color.TRANSPARENT);
        circleOptions.strokeWidth(6);
        circleOptions.strokeColor(Color.LTGRAY);
        googleMap.addCircle(circleOptions);
    }

    private void getMyLocation() {
        LatLng latLng = new LatLng(Para.latitude, Para.longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
    }


    @Override
    public void onRestaurantClicked(RestaurantModel restaurantModel) {

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(restaurantModel.LatLng(), 15);
        mMap.animateCamera(cameraUpdate, 500, new GoogleMap.CancelableCallback() {
            @Override
            public void onCancel() {
                return;
            }

            @Override
            public void onFinish() {
                for (Marker marker : markerArrayList) {
                    if (((RestaurantModel) marker.getTag()) == restaurantModel) {
                        markerClicked = marker;
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                        marker.showInfoWindow();
                        markerSelected = true;
                    }
                }
            }
        });


    }
}