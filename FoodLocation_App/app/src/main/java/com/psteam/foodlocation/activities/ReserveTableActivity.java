package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.FoodAdapter;
import com.psteam.foodlocation.adapters.FoodReserveAdapter;
import com.psteam.foodlocation.adapters.MenuAdapter;
import com.psteam.foodlocation.databinding.ActivityReserveTableBinding;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.foodlocation.ultilities.Token;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.InsertReserveFoodModel;
import com.psteam.lib.modeluser.InsertReserveTableModel;
import com.psteam.lib.modeluser.RestaurantModel;
import com.psteam.lib.modeluser.message;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveTableActivity extends AppCompatActivity {

    private ActivityReserveTableBinding binding;
    private PreferenceManager preferenceManager;
    private Token token;
    private ArrayList<MenuAdapter.Menu> menus;

    private FoodReserveAdapter foodReserveAdapter;
    private ArrayList<FoodReserveAdapter.FoodReserve> foodReserves;

    private double totalPrice = 0;
    private int totalCount = 0;

    private String AddressRestaurantReserve;
    private LocalDate DateReserve;
    private int NumberReserve;
    private RestaurantModel restaurantModel;
    private String TimeReserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReserveTableBinding.inflate(getLayoutInflater());
        preferenceManager = new PreferenceManager(getApplicationContext());
        token = new Token(getApplicationContext());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
        initFoodReserve();
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        AddressRestaurantReserve = bundle.getString("AddressRestaurantReserve");
        DateReserve = (LocalDate) bundle.getSerializable("DateReserve");
        NumberReserve = bundle.getInt("NumberReserve");
        restaurantModel = (RestaurantModel) bundle.getSerializable("restaurantModel");
        TimeReserve = bundle.getString("TimeReserve");

        binding.textViewRestaurantAddress.setText(restaurantModel.getAddress());
        binding.textViewCountPeople.setText(String.valueOf(NumberReserve));
        binding.textViewRestaurantName.setText(restaurantModel.getName());
        binding.textViewPromotion.setText(restaurantModel.getName());

        binding.textViewTimeReserve.setText(String.format("%s, %s", TimeReserve, formatToYesterdayOrToday(DateReserve)));

        if (restaurantModel.getPromotionRes().size() > 0) {
            binding.textViewPromotion.setText(String.format("-%s%%", restaurantModel.getPromotionRes().get(0).getValue()));
        } else {
            binding.textViewPromotion.setVisibility(View.GONE);
            binding.line3.setVisibility(View.GONE);
            binding.text5.setVisibility(View.GONE);
            binding.image4.setVisibility(View.GONE);
        }
    }

    public static String formatToYesterdayOrToday(LocalDate localDate) {
        Date dateTime = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);
        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, 1);
        DateFormat timeFormatter = new SimpleDateFormat("dd MMM yyyy");

        if (calendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR)) {
            return "Hôm nay " + timeFormatter.format(dateTime);
        } else if (calendar.get(Calendar.YEAR) == yesterday.get(Calendar.YEAR) && calendar.get(Calendar.DAY_OF_YEAR) == yesterday.get(Calendar.DAY_OF_YEAR)) {
            return "Ngày mai " + timeFormatter.format(dateTime);
        } else {
            return new SimpleDateFormat("EEEE dd MMM yyyy").format(dateTime);
        }
    }

    public static Date formatStringToDate(String date) throws ParseException {
        Date dateTime = new SimpleDateFormat("hh:mm a,yyyy-MM-dd").parse(date);
        return dateTime;
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

    private void setListeners() {
        binding.buttonFoodReserve.setOnClickListener(v -> {
            clickOpenBottomSheetMenuFragment();
        });

        binding.imageViewClose.setOnClickListener(v -> {
            finish();
        });

        binding.buttonReserve.setOnClickListener(v -> {
            if (isValidateReserveTable()) {
                InsertReserveTableModel insertReserveTableModel = new InsertReserveTableModel();
                insertReserveTableModel.setPhone(binding.inputPhoneNumber.getText().toString().trim());
                insertReserveTableModel.setName(binding.inputFullName.getText().toString().trim());
                insertReserveTableModel.setQuantity(String.valueOf(NumberReserve));
                insertReserveTableModel.setTime(String.format("%s,%s", TimeReserve, DateReserve));
                insertReserveTableModel.setUserId(preferenceManager.getString(Constants.USER_ID));
                insertReserveTableModel.setRestaurantId(restaurantModel.getRestaurantId());
                if (restaurantModel.getPromotionRes().size() > 0) {
                    insertReserveTableModel.setPromotionId(restaurantModel.getPromotionRes().get(0).getId());
                }
                ReserveTable(insertReserveTableModel);
            }
        });
    }

    private boolean isValidateReserveTable() {
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (binding.inputFullName.getText().toString().trim().isEmpty()) {
            CustomToast.makeText(getApplicationContext(), "Vui lòng nhập họ và tên người đặt bàn", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (binding.inputPhoneNumber.getText().toString().trim().isEmpty()) {
            CustomToast.makeText(getApplicationContext(), "Vui lòng nhập số điên thoại liên hệ", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (!Patterns.PHONE.matcher(binding.inputPhoneNumber.getText().toString()).matches()) {
            CustomToast.makeText(getApplicationContext(), "Số điện thoại không hợp lệ", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else if (!binding.inputEmail.getText().toString().trim().isEmpty() && !binding.inputEmail.getText().toString().trim().matches(emailPattern)) {
            CustomToast.makeText(getApplicationContext(), "Email không hợp lệ", CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
            return false;
        } else {
            return true;
        }
    }

    private MenuBottomSheetFragment menuBottomSheetFragment;

    private void clickOpenBottomSheetMenuFragment() {
        menus = new ArrayList<>();

        ArrayList<FoodAdapter.Food> foods = new ArrayList<>();
        ArrayList<FoodAdapter.Food> foods2 = new ArrayList<>();
        foods.add(new FoodAdapter.Food(R.drawable.suatuoi, "Lẩu xa tế", 99000, "Sữa tươi mộc Châu", 1));
        foods.add(new FoodAdapter.Food(R.drawable.suatuoi, "Lẩu cua", 99000, "Sữa tươi mộc Châu", 1));
        foods.add(new FoodAdapter.Food(R.drawable.suatuoi, "Bia 333", 99000, "Sữa tươi mộc Châu", 1));
        foods.add(new FoodAdapter.Food(R.drawable.suatuoi, "Cơm chiên trân châu", 99000, "Sữa tươi mộc Châu", 1));
        foods.add(new FoodAdapter.Food(R.drawable.suatuoi, "Lẩu thái", 99000, "Sữa tươi mộc Châu", 1));

        foods2.add(new FoodAdapter.Food(R.drawable.suatuoi, "Ba chỉ nướng ngói", 99000, "Sữa tươi mộc Châu", 2));
        foods2.add(new FoodAdapter.Food(R.drawable.suatuoi, "Cơm gà", 99000, "Sữa tươi mộc Châu", 2));


        menus.add(new MenuAdapter.Menu("Menu 1", 1, foods));
        menus.add(new MenuAdapter.Menu("Menu 2", 2, foods2));


        menuBottomSheetFragment = new MenuBottomSheetFragment(menus, new FoodAdapter.FoodListeners() {
            @Override
            public void onAddFoodClick(FoodAdapter.Food food) {
                CustomToast.makeText(getApplicationContext(), "Đã thêm món ăn vào thực đơn", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                boolean flag = false;
                for (FoodReserveAdapter.FoodReserve foodReserve : foodReserves) {
                    if (foodReserve.getName().equals(food.getName())) {
                        int count = foodReserve.getCount() + 1;
                        foodReserves.remove(foodReserve);
                        foodReserves.add(new FoodReserveAdapter.FoodReserve(food.getImage(), food.getName(), food.getPrice(), food.getInfo(), count));
                        totalPrice += food.getPrice();
                        totalCount++;
                        setTotalPrice(totalCount, totalPrice);
                        foodReserveAdapter.notifyDataSetChanged();
                        flag = true;
                        setVisibilityText11(true);
                        break;
                    }
                }
                if (!flag) {
                    foodReserves.add(new FoodReserveAdapter.FoodReserve(food.getImage(), food.getName(), food.getPrice(), food.getInfo(), 1));
                    totalPrice += food.getPrice();
                    totalCount++;
                    setTotalPrice(totalCount, totalPrice);
                    foodReserveAdapter.notifyDataSetChanged();
                    setVisibilityText11(true);
                }
            }

            @Override
            public void onFoodClick(FoodAdapter.Food food) {
                Toast.makeText(getApplicationContext(), food.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        menuBottomSheetFragment.show(getSupportFragmentManager(), menuBottomSheetFragment.getTag());


    }

    public void setVisibilityText11(boolean b) {
        if (b && binding.text11.getVisibility() == View.GONE) {
            binding.text11.setVisibility(View.VISIBLE);
            binding.layout4.setVisibility(View.VISIBLE);
        } else if (!b && binding.text11.getVisibility() == View.VISIBLE) {
            binding.text11.setVisibility(View.GONE);
            binding.layout4.setVisibility(View.GONE);
        }
    }

    public void setTotalPrice(int totalCount, double totalPrice) {
        binding.textViewTotalCount.setText(String.valueOf(totalCount));
        binding.textViewTotalPrice.setText(NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(totalPrice));
    }

    public void initFoodReserve() {
        foodReserves = new ArrayList<>();
        foodReserveAdapter = new FoodReserveAdapter(foodReserves, new FoodReserveAdapter.FoodReserveListeners() {


            @Override
            public void onAddFoodReserveClick(FoodReserveAdapter.FoodReserve food) {
                totalPrice += food.getPrice();
                totalCount++;
                setTotalPrice(totalCount, totalPrice);
            }

            @Override
            public void onMinusFoodReserveClick(FoodReserveAdapter.FoodReserve food) {
                totalPrice -= food.getPrice();
                totalCount--;
                setTotalPrice(totalCount, totalPrice);
            }

            @Override
            public void onRemoveFoodReserveClick(FoodReserveAdapter.FoodReserve food, int count, double price) {
                totalPrice -= price * count;
                totalCount -= count;
                setTotalPrice(totalCount, totalPrice);
                if (foodReserveAdapter.getItemCount() <= 0) {
                    setVisibilityText11(false);
                }
            }

            @Override
            public void onFoodClick(FoodReserveAdapter.FoodReserve food) {

            }

        });
        binding.recycleViewFoodReserve.setAdapter(foodReserveAdapter);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.divider));
        binding.recycleViewFoodReserve.addItemDecoration(dividerItemDecoration);
    }

    private void ReserveTable(InsertReserveTableModel insertReserveTableModel) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<message> call = serviceAPI.ReserveTable(token.getToken(), insertReserveTableModel);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    CustomToast.makeText(getApplicationContext(), response.body().getNotification(), CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                } else if (response.body() != null) {
                    CustomToast.makeText(getApplicationContext(), response.body().getNotification(), CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }

    private void ReserveFood(InsertReserveFoodModel insertReserveFoodModel) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<message> call = serviceAPI.ReserveFood(token.getToken(), insertReserveFoodModel);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    CustomToast.makeText(getApplicationContext(), response.body().getNotification(), CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                } else {
                    CustomToast.makeText(getApplicationContext(), response.message(), CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                }
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }
}