package com.psteam.foodlocation.activities;

import static com.psteam.foodlocation.ultilities.RetrofitClient.getRetrofitGoogleMapAPI;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.MapRestaurantAdapter;
import com.psteam.foodlocation.databinding.ActivityMapBinding;
import com.psteam.foodlocation.listeners.MapRestaurantListener;
import com.psteam.foodlocation.models.GoogleMapApiModels.DirectionResponses;
import com.psteam.foodlocation.services.LocationService;
import com.psteam.foodlocation.services.ServiceAPI;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;
import com.psteam.foodlocation.ultilities.Para;
import com.psteam.lib.modeluser.RestaurantModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, MapRestaurantListener {

    private final static LatLng currentLocation = new LatLng(Para.latitude, Para.longitude);

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

    private TextView textViewRestaurantName, textViewRestaurantAddress, textViewCloseTime, textViewOpenTime, textViewDistance, textViewDirections, textViewDuration;
    private ImageView imageViewRestaurant;
    private LinearLayout layoutSuggestRestaurant, layoutLocationInfo, layoutProgressBar;

    private ProgressBar progressBar;

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
        restaurantModels = new ArrayList<>();
        initData();
        buttonMyLocation = findViewById(R.id.buttonMyLocation);
        iconLocation = findViewById(R.id.iconLocation);
        layoutSuggestRestaurant = findViewById(R.id.layoutSuggestRestaurant);
        layoutLocationInfo = findViewById(R.id.layoutLocationInfo);

        textViewRestaurantName = findViewById(R.id.textViewRestaurantName);
        textViewRestaurantAddress = findViewById(R.id.textViewRestaurantAddress);
        textViewOpenTime = findViewById(R.id.textViewOpenTime);
        textViewCloseTime = findViewById(R.id.textViewCloseTime);
        textViewDistance = findViewById(R.id.textviewDistance);
        textViewDirections = findViewById(R.id.textViewDirections);
        textViewDuration = findViewById(R.id.textviewDuration);
        imageViewRestaurant=findViewById(R.id.imageViewRestaurant);

        progressBar = findViewById(R.id.progressBarMap);
        layoutProgressBar = findViewById(R.id.layoutProgressBar);

        initBottomSheetRestaurant();


    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        restaurantModels = (ArrayList<RestaurantModel>) bundle.getSerializable("restaurantModels");
    }

    private void initSearchRestaurant() {

        mapRestaurantAdapter = new MapRestaurantAdapter(restaurantModels, this, getApplicationContext());
        recyclerViewSearch.setAdapter(mapRestaurantAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(getDrawable(R.drawable.divider));
        recyclerViewSearch.addItemDecoration(itemDecoration);

    }

    private static int peekHeight;
    private RestaurantModel restaurantModel;

    private void initBottomSheetRestaurant() {
        layoutBottomSheet = findViewById(R.id.bottomSheetContainer);
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        peekHeight = bottomSheetBehavior.getPeekHeight();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        recyclerViewSearch = findViewById(R.id.recycleViewSearchRestaurant);
        initSearchRestaurant();
        // checkSelfPermission();

        layoutLocationInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RestaurantDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("restaurantModel", restaurantModel);
            intent.putExtra("bundle", bundle);
            startActivity(intent);
        });

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

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        setUpClusterer();
    }

    private ClusterManager<RestaurantModel> mClusterManager;

    @SuppressLint("MissingPermission")
    private void setUpClusterer() {

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // Disable button current location
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            locationButton.setVisibility(View.GONE);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Para.latitude, Para.longitude), 15));

        mClusterManager = new ClusterManager<RestaurantModel>(this, mMap);
        mClusterManager.setRenderer(new RestaurantRender(getApplicationContext(), mMap, mClusterManager));

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                mClusterManager.onCameraIdle();
                if (markerSelected) {
                    iconLocation.setVisibility(View.GONE);
                    oldMarkerClicked = markerClicked;
                    markerSelected = false;

                    if (layoutSuggestRestaurant.getVisibility() == View.VISIBLE) {
                        layoutSuggestRestaurant.setVisibility(View.GONE);
                        layoutLocationInfo.setVisibility(View.VISIBLE);
                    }

                    if (bottomSheetBehavior != null) {
                        bottomSheetBehavior.setPeekHeight(layoutLocationInfo.getHeight() + peekHeight);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        bottomSheetBehavior.setDraggable(false);
                        RestaurantModel restaurantModel = (RestaurantModel) markerClicked.getTag();
                        setRestaurantInfo(restaurantModel);
                    }

                } else {
                    layoutLocationInfo.setVisibility(View.GONE);
                    layoutSuggestRestaurant.setVisibility(View.VISIBLE);

                    if (bottomSheetBehavior != null) {
                        bottomSheetBehavior.setPeekHeight(peekHeight);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        bottomSheetBehavior.setDraggable(true);
                    }
                }
            }
        });

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<RestaurantModel>() {
            @Override
            public boolean onClusterClick(Cluster<RestaurantModel> cluster) {
                // Create the builder to collect all essential cluster items for the bounds.
                LatLngBounds.Builder builder = LatLngBounds.builder();
                for (ClusterItem item : cluster.getItems()) {
                    builder.include(item.getPosition());
                }
                // Get the LatLngBounds
                final LatLngBounds bounds = builder.build();

                // Animate camera to the bounds
                try {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100), 500, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<RestaurantModel>() {
            @Override
            public boolean onClusterItemClick(RestaurantModel item) {

                Marker marker = getMarkerFromCluster(item.getPosition());

                // Click buttonGuide In bottom sheet
                if (marker == null && flag) {

                    markerSelected = true;
                    markerClicked = Para.marker;

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(temp, 15);
                    mMap.animateCamera(cameraUpdate, 500, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onFinish() {
                            markerClicked.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            markerClicked.setTitle(item.getName());
                            markerClicked.setTag(item);
                            markerClicked.showInfoWindow();
                            temp = null;
                            flag = false;
                        }
                    });
                }

                if (polyline != null) {
                    if (!item.getPosition().equals(latLngDestination)) {
                        polyline.remove();
                        polyline = null;
                    }
                }

                if (marker != null) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(item.getPosition(), 15);
                    mMap.animateCamera(cameraUpdate, 500, new GoogleMap.CancelableCallback() {
                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onFinish() {
                            markerSelected = true;
                            markerClicked = marker;
                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            marker.setTitle(item.getName());
                            marker.setTag(item);
                            marker.showInfoWindow();
                        }
                    });
                }
                return true;
            }
        });

        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {

                if (bottomSheetBehavior != null) {
                    bottomSheetBehavior.setPeekHeight(0);
                    if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                }

                if (!markerSelected) {
                    if (iconLocation.getVisibility() == View.GONE)
                        iconLocation.setVisibility(View.VISIBLE);

                    if (markerClicked != null) {
                        markerClicked.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        markerClicked.hideInfoWindow();
                        markerClicked = null;
                    }
                }
                if (oldMarkerClicked != null) {
                    oldMarkerClicked.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    oldMarkerClicked.hideInfoWindow();
                    oldMarkerClicked = null;
                }
            }
        });

        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(currentLocation);
        circleOptions.radius(Constants.RADIUS);
        circleOptions.fillColor(Color.TRANSPARENT);
        circleOptions.strokeWidth(6);
        circleOptions.strokeColor(Color.LTGRAY);
        mMap.addCircle(circleOptions);
        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {
        mClusterManager.addItems(restaurantModels);
    }

    private void getMyLocation() {
        LatLng latLng = new LatLng(Para.latitude, Para.longitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
    }

    private void setRestaurantInfo(RestaurantModel restaurantModel) {
        this.restaurantModel=restaurantModel;
        textViewRestaurantName.setText(restaurantModel.getName());
        textViewRestaurantAddress.setText(restaurantModel.getAddress());
        textViewDistance.setText(String.format("%skm", Math.round(Double.parseDouble(restaurantModel.getDistance()))));
        Glide.with(getApplicationContext()).load(restaurantModel.getMainPic()).thumbnail(0.3f).into(imageViewRestaurant);
        textViewDirections.setOnClickListener(v -> {
            if (polyline == null) {
                loadingDirection(true);
                getDirection(currentLocation, restaurantModel.getPosition());
            }
        });
    }

    @Override
    public void onRestaurantGuideClicked(RestaurantModel restaurantModel) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(restaurantModel.getLatLng(), 15);
        mMap.animateCamera(cameraUpdate, 500, null);
        if (getMarkerFromCluster(restaurantModel.getPosition()) != null)
            mClusterManager.getMarkerManager().onMarkerClick(getMarkerFromCluster(restaurantModel.getPosition()));
        else {
            flag = true;
            temp = restaurantModel.getPosition();
        }
    }

    @Override
    public void onRestaurantClicked(RestaurantModel restaurantModel) {
        Intent intent = new Intent(getApplicationContext(), RestaurantDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurantModel", restaurantModel);
        intent.putExtra("bundle", bundle);
        startActivity(intent);

    }

    public static boolean flag = false;

    private static LatLng temp;

    private Marker getMarkerFromCluster(LatLng latLng) {
        for (Marker marker : mClusterManager.getMarkerCollection().getMarkers()) {
            if (marker.getPosition().equals(latLng)) {
                return marker;
            }
        }
        return null;
    }

    private Polyline polyline;

    private void drawPolyline(@NonNull Response<DirectionResponses> response) {
        if (response.body() != null) {
            String shape = response.body().getRoutes().get(0).getOverviewPolyline().getPoints();
            String distance = response.body().getRoutes().get(0).getLegs().get(0).getDistance().getText();
            String duration = response.body().getRoutes().get(0).getLegs().get(0).getDuration().getText();

            polyline = mMap.addPolyline(new PolylineOptions()
                    .addAll(PolyUtil.decode(shape))
                    .width(10f)
                    .color(Color.BLUE));

            /*mMap.addMarker(new MarkerOptions()
                    .position(center)
                    .title(duration)

            ).setIcon(null);*/
            textViewDuration.setText(duration);
            textViewDistance.setText(distance);
            loadingDirection(false);
        }
    }

    private void loadingDirection(boolean Loading) {
        if (Loading) {
            progressBar.setVisibility(View.VISIBLE);
            layoutProgressBar.setVisibility(View.VISIBLE);
            layoutLocationInfo.setVisibility(View.GONE);
            mMap.getUiSettings().setScrollGesturesEnabled(false);

        } else {
            progressBar.setVisibility(View.GONE);
            layoutProgressBar.setVisibility(View.GONE);
            layoutLocationInfo.setVisibility(View.VISIBLE);
            mMap.getUiSettings().setScrollGesturesEnabled(true);
        }
    }

    private static LatLng latLngDestination;

    private void getDirection(LatLng latLngOrigin, LatLng latLngDestination) {
        String origin = String.valueOf(latLngOrigin.latitude) + "," + String.valueOf(latLngOrigin.longitude);
        String destination = String.valueOf(latLngDestination.latitude) + "," + String.valueOf(latLngDestination.longitude);
        this.latLngDestination = latLngDestination;

        ServiceAPI serviceAPI = getRetrofitGoogleMapAPI().create(ServiceAPI.class);
        Call<DirectionResponses> call = serviceAPI.getDirection(origin, destination, getString(R.string.google_map_api_key));
        call.enqueue(new Callback<DirectionResponses>() {
            @Override
            public void onResponse(Call<DirectionResponses> call, Response<DirectionResponses> response) {
                if (response.body() != null && response.body().getStatus().equals("OK")) {
                    drawPolyline(response);
                    Log.d("OKAY", response.message());
                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi khi lấy dữ liệu", Toast.LENGTH_SHORT).show();
                    loadingDirection(false);
                }
            }

            @Override
            public void onFailure(Call<DirectionResponses> call, Throwable t) {
                Log.e("TAG:", t.getLocalizedMessage());
            }
        });
    }

    public class RestaurantRender extends DefaultClusterRenderer<RestaurantModel> {

        private final ClusterManager<RestaurantModel> mClusterManager;

        public RestaurantRender(Context context, GoogleMap map, ClusterManager<RestaurantModel> clusterManager) {
            super(context, map, clusterManager);
            mClusterManager = clusterManager;
        }

        @Override
        protected void onClusterItemRendered(@NonNull RestaurantModel clusterItem, @NonNull Marker marker) {
            super.onClusterItemRendered(clusterItem, marker);
            if (clusterItem.getPosition().equals(temp)) {
                Para.marker = marker;
                mClusterManager.getMarkerManager().onMarkerClick(marker);
            }
        }
    }
}