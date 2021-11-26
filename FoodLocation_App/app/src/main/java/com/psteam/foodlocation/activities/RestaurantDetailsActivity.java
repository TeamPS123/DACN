package com.psteam.foodlocation.activities;

import static com.psteam.foodlocation.ultilities.RetrofitClient.getRetrofitGoogleMapAPI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.ChooseDateReserveTableAdapter;
import com.psteam.foodlocation.adapters.ChooseNumberPeopleAdapter;
import com.psteam.foodlocation.adapters.RestaurantAddressAdapter;
import com.psteam.foodlocation.adapters.RestaurantPostAdapter;
import com.psteam.foodlocation.adapters.TimeBookTableAdapter;
import com.psteam.foodlocation.databinding.ActivityRestaurantDetailsBinding;
import com.psteam.foodlocation.adapters.RestaurantPhotoAdapter;
import com.psteam.foodlocation.models.GoogleMapApiModels.DirectionResponses;
import com.psteam.foodlocation.services.ServiceAPI;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.Para;
import com.psteam.lib.modeluser.RestaurantModel;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityRestaurantDetailsBinding binding;
    private GoogleMap mMap;
    private View viewMap;
    private RestaurantPhotoAdapter restaurantPhotoAdapter;
    private ArrayList<RestaurantPhotoAdapter.Photo> photoArrayList;

    private TimeBookTableAdapter timeBookTableAdapter;
    private ArrayList<TimeBookTableAdapter.TimeBook> timeBooks;

    private RestaurantPostAdapter restaurantPostAdapter;

    private ArrayList<RestaurantAddressAdapter.AddressRestaurant> addressRestaurants;
    private ArrayList<ChooseNumberPeopleAdapter.NumberPeople> numberPeople;
    private ArrayList<ChooseDateReserveTableAdapter.DateReserveTable> dateReserveTables;

    private String AddressRestaurantReserve;
    private LocalDate DateReserve;
    private int NumberReserve;

    private Handler sliderHandler = new Handler();

    private int currentItem;
    private int totalItem;

    private String distance;
    private String time;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapRestaurant);
        mapFragment.getMapAsync(this);
        viewMap = mapFragment.getView();
        setFullScreen();
        initSliderPhotoRestaurant();
        initTimeBookTable();
        //initFoodRestaurant();

        getDataIntent();
    }

    private RestaurantModel restaurantModel;

    private void getDataIntent() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        restaurantModel = (RestaurantModel) bundle.getSerializable("restaurantModel");
        if (restaurantModel.getPromotionRes().size() > 0) {
            binding.textViewRestaurantName.setText(String.format("%s: %s", restaurantModel.getName(), restaurantModel.getPromotionRes().get(0).getName()));
            binding.textViewPromotion.setText(String.format("-%s%%", restaurantModel.getPromotionRes().get(0).getValue()));
            binding.textViewPromotion.setVisibility(View.VISIBLE);
        } else {
            binding.textViewRestaurantName.setText(restaurantModel.getName());
            binding.textViewPromotion.setVisibility(View.GONE);
        }
        binding.textRestaurantName.setText(restaurantModel.getName());
        binding.textViewCategory.setText(restaurantModel.getCategoryResStr());

        if (restaurantModel.getPic().size() > 0) {

        } else {

        }

        binding.textViewChooseAddress.setText(String.format("%s, %s, %s", restaurantModel.getLine(), restaurantModel.getDistrict(), restaurantModel.getCity()));

        binding.textViewRestaurantNameDetails.setText(restaurantModel.getName());
        binding.textViewAddress.setText(restaurantModel.getAddress());
        binding.textViewCategoryRestaurant.setText(restaurantModel.getCategoryResStr());

        AddressRestaurantReserve = restaurantModel.getAddress();
        NumberReserve = 1;
        DateReserve = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        getDistance(new LatLng(Para.latitude,Para.longitude),restaurantModel.getLatLng());
    }

    private void setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setListeners() {
        binding.textViewReadMore.setOnClickListener(v -> {
            if (binding.textViewReadMore.getText().equals(getText(R.string.read_more))) {
                binding.textViewPromotionForm.setMaxLines(Integer.MAX_VALUE);
                binding.textViewPromotionForm.setEllipsize(null);
                binding.textViewReadMore.setText(R.string.read_less);
                binding.textViewReadMore.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_round_keyboard_arrow_up_24, 0);
            } else {
                binding.textViewPromotionForm.setMaxLines(3);
                binding.textViewPromotionForm.setEllipsize(TextUtils.TruncateAt.END);
                binding.textViewReadMore.setText(R.string.read_more);
                binding.textViewReadMore.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_round_keyboard_arrow_down_24, 0);
            }
        });

        binding.textViewReadMoreRestaurantsText.setOnClickListener(v -> {
            if (binding.textViewReadMoreRestaurantsText.getText().equals(getText(R.string.read_more))) {
                binding.textViewRestaurantDetails.setMaxLines(Integer.MAX_VALUE);
                binding.textViewRestaurantDetails.setEllipsize(null);
                binding.textViewReadMoreRestaurantsText.setText(getText(R.string.read_less));
                binding.textViewReadMoreRestaurantsText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_round_keyboard_arrow_up_24, 0);
            } else {
                binding.textViewRestaurantDetails.setMaxLines(5);
                binding.textViewRestaurantDetails.setEllipsize(TextUtils.TruncateAt.END);
                binding.textViewReadMoreRestaurantsText.setText(R.string.read_more);
                binding.textViewReadMoreRestaurantsText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_round_keyboard_arrow_down_24, 0);
            }
        });

        binding.layoutAddress.setOnClickListener(v -> {
            clickOpenBottomSheetChooseAddressFragment();
        });

        binding.textViewChoosePersonNumber.setOnClickListener(v -> {
            clickOpenBottomSheetChooseNumberPeopleFragment();
        });

        binding.textViewChooseDate.setOnClickListener(v -> {
            clickOpenBottomSheetChooseDateReserveTableFragment();
        });

        binding.buttonReserve.setOnClickListener(v -> {
            int scrollY = binding.recycleViewTimeBookTable.getScrollY();
            int scrollX = binding.recycleViewTimeBookTable.getScrollX();
            binding.nestedScrollView.smoothScrollTo(scrollX, scrollY, 1000);
        });

        binding.textViewCall.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        RestaurantDetailsActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        REQUEST_CODE_PHONE_PERMISSION
                );
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + restaurantModel.getPhoneRes()));
                startActivity(intent);
            }
        });

        binding.textViewDirections.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), RestaurantMapActivity.class);
            intent.putExtra("restaurantModel", restaurantModel);
            startActivity(intent);
        });
    }

    private static final int REQUEST_CODE_PHONE_PERMISSION = 9;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PHONE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + restaurantModel.getPhoneRes()));
                startActivity(intent);
            } else {
                CustomToast.makeText(getApplicationContext(), "Permission denied", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            }
        }
    }

    ChooseDateReserveTableFragment chooseDateReserveTableFragment;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void clickOpenBottomSheetChooseDateReserveTableFragment() {
        dateReserveTables = new ArrayList<>();
        DateTimeFormatter formatter;
        String formattedString = "";
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        for (int i = 0; i < 7; i++) {
            LocalDate localDate = today.plusDays(i);
            if (today.equals(localDate)) {
                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedString = "Hôm nay, " + localDate.format(formatter);
            } else if (today.plusDays(1).equals(localDate)) {
                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedString = "Ngày mai, " + localDate.format(formatter);
            } else {
                formatter = DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy");
                formattedString = localDate.format(formatter);
            }

            dateReserveTables.add(new ChooseDateReserveTableAdapter.DateReserveTable(formattedString, localDate));
        }

        chooseDateReserveTableFragment = new ChooseDateReserveTableFragment(dateReserveTables, new ChooseDateReserveTableAdapter.ChooseDateReserveTableListener() {
            @Override
            public void onChooseDateReserveTableClicked(ChooseDateReserveTableAdapter.DateReserveTable dateReserveTable) {
                binding.textViewChooseDate.setText(dateReserveTable.getDay());
                DateReserve = dateReserveTable.getDate();
                chooseDateReserveTableFragment.dismiss();
            }
        });
        chooseDateReserveTableFragment.show(getSupportFragmentManager(), chooseDateReserveTableFragment.getTag());


    }

    private ChooseNumberPeopleBottomSheetFragment chooseNumberPeopleBottomSheetFragment;

    private void clickOpenBottomSheetChooseNumberPeopleFragment() {
        numberPeople = new ArrayList<>();
        for (int i = 1; i <= 21; i++) {
            numberPeople.add(new ChooseNumberPeopleAdapter.NumberPeople(i));
        }

        chooseNumberPeopleBottomSheetFragment = new ChooseNumberPeopleBottomSheetFragment(numberPeople, new ChooseNumberPeopleAdapter.ChooseNumberPeopleListener() {
            @Override
            public void ChooseNumberPeopleClicked(ChooseNumberPeopleAdapter.NumberPeople numberPeople) {
                binding.textViewChoosePersonNumber.setText(String.format("%d Khách", numberPeople.getNumber()));
                NumberReserve = numberPeople.getNumber();
                chooseNumberPeopleBottomSheetFragment.dismiss();
            }
        });

        chooseNumberPeopleBottomSheetFragment.show(getSupportFragmentManager(), chooseNumberPeopleBottomSheetFragment.getTag());

    }

    private ChooseAddressBottomSheetFragment chooseAddressBottomSheetFragment;

    private void clickOpenBottomSheetChooseAddressFragment() {

        addressRestaurants = new ArrayList<>();
        addressRestaurants.add(new RestaurantAddressAdapter.AddressRestaurant(restaurantModel.getLine(), restaurantModel.getAddress(), distance));

        chooseAddressBottomSheetFragment = new ChooseAddressBottomSheetFragment(addressRestaurants, new RestaurantAddressAdapter.RestaurantAddressListener() {
            @Override
            public void onRestaurantAddressClicked(RestaurantAddressAdapter.AddressRestaurant addressRestaurant) {
                binding.textViewChooseAddress.setText(addressRestaurant.getAddress());
                binding.textViewChooseDistance.setText(addressRestaurant.getDistance());
                chooseAddressBottomSheetFragment.dismiss();

            }
        });
        chooseAddressBottomSheetFragment.show(getSupportFragmentManager(), chooseAddressBottomSheetFragment.getTag());
    }


    private void initSliderPhotoRestaurant() {
        photoArrayList = new ArrayList<>();
        photoArrayList.add(new RestaurantPhotoAdapter.Photo(R.drawable.p1));
        photoArrayList.add(new RestaurantPhotoAdapter.Photo(R.drawable.p2));
        photoArrayList.add(new RestaurantPhotoAdapter.Photo(R.drawable.p3));
        photoArrayList.add(new RestaurantPhotoAdapter.Photo(R.drawable.p4));
        photoArrayList.add(new RestaurantPhotoAdapter.Photo(R.drawable.p5));
        photoArrayList.add(new RestaurantPhotoAdapter.Photo(R.drawable.p6));

        restaurantPhotoAdapter = new RestaurantPhotoAdapter(photoArrayList);
        binding.viewPagerSlideImageRestaurant.setAdapter(restaurantPhotoAdapter);
        binding.circleIndicator.setViewPager(binding.viewPagerSlideImageRestaurant);
        restaurantPhotoAdapter.registerAdapterDataObserver(binding.circleIndicator.getAdapterDataObserver());
        binding.viewPagerSlideImageRestaurant.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });
    }


    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            currentItem = binding.viewPagerSlideImageRestaurant.getCurrentItem();
            totalItem = photoArrayList.size() - 1;
            if (currentItem < totalItem) {
                currentItem++;
                binding.viewPagerSlideImageRestaurant.setCurrentItem(currentItem);
            } else {
                binding.viewPagerSlideImageRestaurant.setCurrentItem(0);
            }


        }
    };

    private void initTimeBookTable() {
        timeBooks = new ArrayList<>();
        timeBooks.add(new TimeBookTableAdapter.TimeBook("05:00 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("05:15 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("05:30 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("05:45 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("06:00 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("06:15 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("06:30 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("06:45 CH", "-15% *"));
        timeBooks.add(new TimeBookTableAdapter.TimeBook("07:00 CH", "-15% *"));

        timeBookTableAdapter = new TimeBookTableAdapter(timeBooks, new TimeBookTableAdapter.TimeBookTableListener() {
            @Override
            public void onTimeBookTableClicked(TimeBookTableAdapter.TimeBook timeBook) {
                Intent intent = new Intent(getApplicationContext(), ReserveTableActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("DateReserve", DateReserve);
                bundle.putString("AddressRestaurantReserve", AddressRestaurantReserve);
                bundle.putInt("NumberReserve", NumberReserve);
                bundle.putString("TimeReserve", timeBook.getTime());
                bundle.putSerializable("restaurantModel", restaurantModel);
                intent.putExtra("bundle", bundle);
                startActivity(intent);
            }
        });
        binding.recycleViewTimeBookTable.setAdapter(timeBookTableAdapter);
    }

    private void initFoodRestaurant() {

       /* restaurantPostAdapter = new RestaurantPostAdapter(foodRestaurants, new RestaurantPostAdapter.RestaurantPostListeners() {
            @Override
            public void onRestaurantPostClicked(RestaurantModel foodRestaurant) {
                startActivity(new Intent(getApplicationContext(), RestaurantDetailsActivity.class));
            }
        }, getApplicationContext());
        binding.recycleViewPostFoodRestaurant.setAdapter(restaurantPostAdapter);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = restaurantModel.getLatLng();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        mMap.getUiSettings().setScrollGesturesEnabled(false);
        mMap.addMarker(new MarkerOptions().position(latLng));
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
            }
        });
    }



    private void getDistance(LatLng latLngOrigin, LatLng latLngDestination) {
        String origin = String.valueOf(latLngOrigin.latitude) + "," + String.valueOf(latLngOrigin.longitude);
        String destination = String.valueOf(latLngDestination.latitude) + "," + String.valueOf(latLngDestination.longitude);

        ServiceAPI serviceAPI = getRetrofitGoogleMapAPI().create(ServiceAPI.class);
        Call<DirectionResponses> call = serviceAPI.getDirection(origin, destination, getString(R.string.google_map_api_key));
        call.enqueue(new Callback<DirectionResponses>() {
            @Override
            public void onResponse(Call<DirectionResponses> call, Response<DirectionResponses> response) {
                if (response.body()!=null && response.body().getStatus().equals("OK")) {

                    distance = response.body().getRoutes().get(0).getLegs().get(0).getDistance().getText();
                    time = response.body().getRoutes().get(0).getLegs().get(0).getDuration().getText();

                    binding.textViewChooseDistance.setText(String.format("(%s)",distance));
                    binding.textViewDistance.setText(distance);

                    Log.d("OKAY", response.message());
                } else {
                    CustomToast.makeText(getApplicationContext(), "Lỗi khi lấy dữ liệu bản đồ", CustomToast.LENGTH_SHORT,CustomToast.ERROR).show();
                }
            }

            @Override
            public void onFailure(Call<DirectionResponses> call, Throwable t) {
                Log.e("TAG:", t.getLocalizedMessage());
            }
        });
    }
}
