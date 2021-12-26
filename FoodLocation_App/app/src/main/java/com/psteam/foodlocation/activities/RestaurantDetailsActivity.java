package com.psteam.foodlocation.activities;

import static com.psteam.foodlocation.ultilities.RetrofitClient.getRetrofitGoogleMapAPI;
import static com.psteam.lib.RetrofitClient.getRetrofit;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.ChooseDateReserveTableAdapter;
import com.psteam.foodlocation.adapters.ChooseNumberPeopleAdapter;
import com.psteam.foodlocation.adapters.RestaurantAddressAdapter;
import com.psteam.foodlocation.adapters.RestaurantPostAdapter;
import com.psteam.foodlocation.adapters.ReviewAdapter;
import com.psteam.foodlocation.adapters.TimeBookTableAdapter;
import com.psteam.foodlocation.databinding.ActivityRestaurantDetailsBinding;
import com.psteam.foodlocation.adapters.RestaurantPhotoAdapter;
import com.psteam.foodlocation.models.GoogleMapApiModels.DirectionResponses;
import com.psteam.foodlocation.services.ServiceAPI;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;
import com.psteam.foodlocation.ultilities.LoadingDialog;
import com.psteam.foodlocation.ultilities.Para;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.lib.modeluser.GetResInfo;
import com.psteam.lib.modeluser.GetRestaurantByDistance;
import com.psteam.lib.modeluser.GetRestaurantModel;
import com.psteam.lib.modeluser.GetReviewModel;
import com.psteam.lib.modeluser.Rate;
import com.psteam.lib.modeluser.RestaurantModel;
import com.psteam.lib.modeluser.UserModel;
import com.psteam.lib.modeluser.message;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, RestaurantPostAdapter.RestaurantPostListeners {

    private ActivityRestaurantDetailsBinding binding;
    private GoogleMap mMap;
    private View viewMap;
    private RestaurantPhotoAdapter restaurantPhotoAdapter;
    private ArrayList<String> photoArrayList;

    private TimeBookTableAdapter timeBookTableAdapter;
    private ArrayList<TimeBookTableAdapter.TimeBook> timeBooks;

    private PreferenceManager preferenceManager;

    private RestaurantPostAdapter restaurantPostAdapter;

    private ArrayList<RestaurantAddressAdapter.AddressRestaurant> addressRestaurants;
    private ArrayList<ChooseNumberPeopleAdapter.NumberPeople> numberPeople;
    private ArrayList<ChooseDateReserveTableAdapter.DateReserveTable> dateReserveTables;

    //Review
    private ArrayList<Rate> rates;
    private ArrayList<Rate> tempRates;
    private ReviewAdapter reviewAdapter;
    private GetReviewModel.CountRating countRating;
    private GetReviewModel getReviewModel;

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
        preferenceManager = new PreferenceManager(getApplicationContext());
        init();
        setListeners();
    }

    private void init() {
        LoadingDialog.show(RestaurantDetailsActivity.this, LoadingDialog.Back);
        rates = new ArrayList<>();
        tempRates = new ArrayList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapRestaurant);
        mapFragment.getMapAsync(this);
        viewMap = mapFragment.getView();
        setFullScreen();
        //initFoodRestaurant();
        getDataIntent();

        preferenceManager.AddRestaurant(restaurantModel);
    }

    private void getReview(String restaurantId, int value, int skip, int take) {
        com.psteam.lib.Services.ServiceAPI serviceAPI = getRetrofit().create(com.psteam.lib.Services.ServiceAPI.class);
        Call<GetReviewModel> call = serviceAPI.getReview(restaurantId, value, skip, take);
        call.enqueue(new Callback<GetReviewModel>() {
            @Override
            public void onResponse(Call<GetReviewModel> call, Response<GetReviewModel> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    if (response.body().getRates().size() > 0) {
                        countRating = response.body().getCountRating();
                        rates.addAll(response.body().getRates());
                        getReviewModel = response.body();
                        binding.ratingBarTotalValue.setRating(Float.valueOf(response.body().getRateTotal()));
                        binding.textTotalRate.setText(response.body().getRateTotal());
                        binding.textViewRatingValue.setText(response.body().getRateTotal());
                        binding.ratingBar.setRating(Float.valueOf(response.body().getRateTotal()));
                        binding.textViewTotalCountReview.setText(String.format("%s đánh giá từ %s người dùng", countRating.getCount(), countRating.getCount()));
                        if (Integer.parseInt(countRating.getCount()) <= 3) {
                            binding.textViewViewMoreReview.setText("Xem chi tiết");
                            tempRates.addAll(rates);
                        } else {
                            binding.textViewViewMoreReview.setText(String.format("Xem thêm %d đánh giá", Integer.parseInt(countRating.getCount()) - 3));
                            tempRates.addAll(rates.subList(0, 3));
                        }
                        binding.textViewReviewCount.setText(String.format("(Xem %s đánh giá)", countRating.getCount()));
                        reviewAdapter.notifyDataSetChanged();
                    } else {
                        binding.layout11.setVisibility(View.GONE);
                        binding.textViewReviewCount.setText("Hãy là người đầu tiên trải nghiệm");
                        binding.ratingBar.setRating(Float.valueOf(response.body().getRateTotal()));
                        binding.textViewRatingValue.setText("");
                    }
                }
            }

            @Override
            public void onFailure(Call<GetReviewModel> call, Throwable t) {
                Log.d("Tag", t.getMessage());
            }
        });
    }

    private void initReviewAdapter() {
        reviewAdapter = new ReviewAdapter(restaurantModel, tempRates);
        binding.recycleViewReview.setAdapter(reviewAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecorator(getDrawable(R.drawable.diveder_review));
        binding.recycleViewReview.addItemDecoration(itemDecoration);
    }

    private RestaurantModel restaurantModel;

    private void getDataIntent() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        Uri uri = getIntent().getData();
        if (bundle != null) {
            restaurantModel = (RestaurantModel) bundle.getSerializable("restaurantModel");
            setData();
        } else if (uri != null) {
            String path = uri.toString();
            String[] parameter = path.split("/");
            GetResInfo(parameter[5].substring(0, 6));
        }


    }

    private void setData() {
        if (restaurantModel == null && restaurantModel.getPromotionRes().size() > 0) {
            binding.textViewRestaurantName.setText(String.format("%s: %s", restaurantModel.getName(), restaurantModel.getPromotionRes().get(0).getName()));
        } else {
            binding.textViewRestaurantName.setText(restaurantModel.getName());

        }
        binding.textViewPromotion.setText(restaurantModel.getPromotion());
        binding.textRestaurantName.setText(restaurantModel.getName());
        binding.textViewCategory.setText(restaurantModel.getCategoryResStr());

        binding.textViewChooseAddress.setText(String.format("%s, %s, %s", restaurantModel.getLine(), restaurantModel.getDistrict(), restaurantModel.getCity()));

        binding.textViewRestaurantNameDetails.setText(restaurantModel.getName());
        binding.textViewAddress.setText(restaurantModel.getAddress());
        binding.textViewCategoryRestaurant.setText(restaurantModel.getCategoryResStr());

        AddressRestaurantReserve = restaurantModel.getAddress();
        NumberReserve = 1;

        getDistance(new LatLng(Para.latitude, Para.longitude), restaurantModel.getLatLng());

        if (!restaurantModel.isStatus() && restaurantModel.getStatusCO() == null) {
            binding.layout1.setVisibility(View.GONE);
            binding.buttonReserve.setText("Nhà hàng tạm đóng cửa");
            binding.buttonReserve.setEnabled(false);
            binding.buttonReserve.setTextColor(getResources().getColor(R.color.white));
        }

        initTimeBookTable();
        initSliderPhotoRestaurant();
        initReviewAdapter();
        getReview(restaurantModel.getRestaurantId(), -1, 0, 10);
        GetRestaurantByDistance(new GetRestaurantByDistance("20", "10.803312745723506", "106.71158641576767"));

        LocalDate today;

        // status true: open close day, status false: close open day
        if (restaurantModel.getStatusCO() != null && restaurantModel.isStatus()) {
            Date closeDay = restaurantModel.getStatusOpen();
            today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        } else if (restaurantModel.getStatusCO() != null && !restaurantModel.isStatus()) {
            Date openDay = restaurantModel.getStatusOpen();
            today = openDay.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } else if (restaurantModel.getStatusCO() == null && !restaurantModel.isStatus()) {
            return;
        } else {
            today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        }
        DateReserve = today;
        if (today.getDayOfMonth() == LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).getDayOfMonth()) {
            binding.textViewChooseDate.setText(String.format("Hôm nay, %s", today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("vi", "VN")))));
        } else if (today.plusDays(1).getDayOfMonth() == LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh")).plusDays(1).getDayOfMonth()) {
            binding.textViewChooseDate.setText(String.format("Ngày mai, %s", today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale("vi", "VN")))));
        } else {
            binding.textViewChooseDate.setText(String.format("%s", today.format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy", new Locale("vi", "VN")))));
        }
        getCountReserveTable(restaurantModel.getRestaurantId());

    }

    private void GetResInfo(String resId) {
        com.psteam.lib.Services.ServiceAPI serviceAPI = getRetrofit().create(com.psteam.lib.Services.ServiceAPI.class);
        Call<GetResInfo> call = serviceAPI.GetResInfo(resId);
        call.enqueue(new Callback<GetResInfo>() {
            @Override
            public void onResponse(Call<GetResInfo> call, Response<GetResInfo> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    restaurantModel = response.body().getRestaurant();
                    setData();
                    LatLng latLng = restaurantModel.getLatLng();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    mMap.getUiSettings().setScrollGesturesEnabled(false);
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    mMap.getUiSettings().setMapToolbarEnabled(false);

                }
            }

            @Override
            public void onFailure(Call<GetResInfo> call, Throwable t) {
                Log.d("Tag", t.getMessage());
            }
        });
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
            Bundle bundle = new Bundle();
            bundle.putString("getLongLat", restaurantModel.getLongLat());
            intent.putExtras(bundle);
            startActivity(intent);

        });

        binding.textViewClose.setOnClickListener(v -> {
            finish();
        });

        binding.textViewViewMoreReview.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ReviewRestaurantActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("restaurantModel", restaurantModel);
            bundle.putSerializable("getReviewModel", getReviewModel);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        binding.layoutShareOther.setOnClickListener(v -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Tasty");
                String shareMessage = String.format("%s, %s", restaurantModel.getName(), restaurantModel.getAddress());
                shareMessage = shareMessage + " https://ps.covid21tsp.space/Share/Code/" + restaurantModel.getRestaurantId();
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        });

        binding.layoutCopyShareLink.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Link Share", "https://ps.covid21tsp.space/Share/Code/" + restaurantModel.getRestaurantId());
            clipboard.setPrimaryClip(clip);
            CustomToast.makeText(getApplicationContext(), "Đã lưu vào Clipboard", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
        });

        binding.reportRes.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), ReportRestaurantActivity.class));
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
        LocalDate today;
        int countDay = 7;

        // status true: open close day, status false: close open day
        if (restaurantModel.getStatusCO() != null && restaurantModel.isStatus()) {
            Date closeDay = restaurantModel.getStatusOpen();
            today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            countDay = closeDay.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate().getDayOfMonth() - today.getDayOfMonth();
        } else if (restaurantModel.getStatusCO() != null && !restaurantModel.isStatus()) {
            Date openDay = restaurantModel.getStatusOpen();
            today = openDay.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } else if (restaurantModel.getStatusCO() == null && !restaurantModel.isStatus()) {
            return;
        } else {
            today = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        }


        for (int i = 0; i < countDay; i++) {
            LocalDate localDate = today.plusDays(i);
            LocalDate now = LocalDate.now(ZoneId.of("Asia/Ho_Chi_Minh"));
            if (today.getDayOfMonth() == localDate.getDayOfMonth() && today.getDayOfMonth() == now.getDayOfMonth()) {
                formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                formattedString = "Hôm nay, " + localDate.format(formatter);
            } else if (today.plusDays(1).getDayOfMonth() == localDate.getDayOfMonth() && today.plusDays(1).getDayOfMonth() == now.plusDays(1).getDayOfMonth()) {
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
        photoArrayList.addAll(restaurantModel.getPic());

        restaurantPhotoAdapter = new RestaurantPhotoAdapter(photoArrayList, getApplicationContext());
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

        LocalTime openTime = LocalTime.parse(restaurantModel.getOpenTime(), DateTimeFormatter.ofPattern("hh:mm a", new Locale("vi", "VN")));
        LocalTime closeTime = LocalTime.parse(restaurantModel.getCloseTime(), DateTimeFormatter.ofPattern("hh:mm a", new Locale("vi", "VN")));
        Duration duration = Duration.between(openTime, closeTime);
        Long timeLoop = (duration.toMinutes() - 60);
        LocalTime tempTime = openTime;
        int step = 15;
        int i = 0;
        while (i <= timeLoop) {
            timeBooks.add(new TimeBookTableAdapter.TimeBook(tempTime.plusMinutes(i).format(DateTimeFormatter.ofPattern("hh:mm a", new Locale("vi", "VN"))),
                    restaurantModel.getPromotion()));
            i += step;
        }

        Gson gson = new Gson();
        String s = gson.toJson(timeBooks);

        Log.d("Time", s.toString());

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

    private GetRestaurantModel getRestaurantModels;

    private void GetRestaurantByDistance(GetRestaurantByDistance getRestaurantByDistance) {

        com.psteam.lib.Services.ServiceAPI serviceAPI = getRetrofit().create(com.psteam.lib.Services.ServiceAPI.class);
        Call<GetRestaurantModel> call = serviceAPI.GetResByDistance(getRestaurantByDistance);
        call.enqueue(new Callback<GetRestaurantModel>() {
            @Override
            public void onResponse(Call<GetRestaurantModel> call, Response<GetRestaurantModel> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    if (response.body().getResList().size() > 0) {
                        getRestaurantModels = response.body();
                        restaurantPostAdapter = new RestaurantPostAdapter(getRestaurantModels.getResList(), RestaurantDetailsActivity.this::onRestaurantPostClicked, getApplicationContext());
                        binding.recycleViewPostFoodRestaurant.setAdapter(restaurantPostAdapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetRestaurantModel> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }

    @Override
    public void onRestaurantPostClicked(RestaurantModel restaurantModel) {
        Intent intent = new Intent(getApplicationContext(), RestaurantDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("restaurantModel", restaurantModel);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
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
        if (restaurantModel == null) return;
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
                if (response.body() != null && response.body().getStatus().equals("OK")) {

                    distance = response.body().getRoutes().get(0).getLegs().get(0).getDistance().getText();
                    time = response.body().getRoutes().get(0).getLegs().get(0).getDuration().getText();

                    binding.textViewChooseDistance.setText(String.format("(%s)", distance));
                    binding.textViewDistance.setText(distance);

                    Log.d("OKAY", response.message());
                } else {
                    CustomToast.makeText(getApplicationContext(), "Lỗi khi lấy dữ liệu bản đồ", CustomToast.LENGTH_SHORT, CustomToast.ERROR).show();
                }
                LoadingDialog.dismiss(500l);
            }

            @Override
            public void onFailure(Call<DirectionResponses> call, Throwable t) {
                Log.e("TAG:", t.getLocalizedMessage());
            }
        });
    }


    private void getCountReserveTable(String restaurantId) {

        com.psteam.lib.Services.ServiceAPI serviceAPI = getRetrofit().create(com.psteam.lib.Services.ServiceAPI.class);
        Call<message> call = serviceAPI.getCountReserveTable(restaurantId);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    if (!response.body().getId().equals("0"))
                        binding.textViewCountBookTable.setText(response.body().getId());
                    else {
                        binding.textViewCountBookTableTextBefore.setText("Trở thành người đầu tiên đặt bàn hôm nay");
                        binding.textViewCountBookTable.setVisibility(View.GONE);
                        binding.textViewCountBookTableTextAfter.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }
}
