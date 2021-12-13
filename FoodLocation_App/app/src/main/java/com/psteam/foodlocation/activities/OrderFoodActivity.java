package com.psteam.foodlocation.activities;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.FoodAdapter;
import com.psteam.foodlocation.adapters.FoodReserveAdapter;
import com.psteam.foodlocation.databinding.ActivityOrderFoodBinding;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.FoodModel;
import com.psteam.lib.modeluser.GetMenuResModel;
import com.psteam.lib.modeluser.MenuModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFoodActivity extends AppCompatActivity {

    private ActivityOrderFoodBinding binding;

    private MenuBottomSheetFragment menuBottomSheetFragment;

    private ArrayList<MenuModel> menus;

    private FoodReserveAdapter foodReserveAdapter;
    private ArrayList<FoodModel> foodReserves;

    private double totalPrice = 0;
    private int totalCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void setListeners() {
        binding.buttonFoodReserve.setOnClickListener(v -> {
            clickOpenBottomSheetMenuFragment();
        });
    }

    private void init() {
        setFullScreen();
        initFoodReserve();
    }

    public void initFoodReserve() {
        foodReserves = new ArrayList<>();
        foodReserveAdapter = new FoodReserveAdapter(foodReserves, new FoodReserveAdapter.FoodReserveListeners() {

            @Override
            public void onAddFoodReserveClick(FoodModel food) {
                totalPrice += Double.parseDouble(food.getPrice());
                totalCount++;
                setTotalPrice(totalCount, totalPrice);
            }

            @Override
            public void onMinusFoodReserveClick(FoodModel food) {
                totalPrice -= Double.parseDouble(food.getPrice());
                totalCount--;
                setTotalPrice(totalCount, totalPrice);
            }

            @Override
            public void onRemoveFoodReserveClick(FoodModel food, int count, double price) {
                totalPrice -= price * count;
                totalCount -= count;
                setTotalPrice(totalCount, totalPrice);
                if (foodReserveAdapter.getItemCount() <= 0) {
                    setVisibilityText11(false);
                }
            }

            @Override
            public void onFoodClick(FoodModel food) {

            }
        }, getApplicationContext());
        binding.recycleViewFoodReserve.setAdapter(foodReserveAdapter);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(getApplicationContext(), R.drawable.divider));
        binding.recycleViewFoodReserve.addItemDecoration(dividerItemDecoration);
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

    private void clickOpenBottomSheetMenuFragment() {

        menuBottomSheetFragment = new MenuBottomSheetFragment(menus, new FoodAdapter.FoodListeners() {
            @Override
            public void onAddFoodClick(FoodModel food) {
                CustomToast.makeText(getApplicationContext(), "Đã thêm món ăn vào thực đơn", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                boolean flag = false;
                for (FoodModel foodReserve : foodReserves) {
                    if (foodReserve.getName().equals(food.getName())) {
                        int count = foodReserve.getCount() + 1;
                        foodReserves.remove(foodReserve);
                        food.setCount(count);
                        foodReserves.add(food);
                        totalPrice += Double.parseDouble(food.getPrice());
                        totalCount++;
                        setTotalPrice(totalCount, totalPrice);
                        foodReserveAdapter.notifyDataSetChanged();
                        flag = true;
                        setVisibilityText11(true);
                        break;
                    }
                }
                if (!flag) {
                    food.setCount(1);
                    foodReserves.add(food);
                    totalPrice += Double.parseDouble(food.getPrice());
                    totalCount++;
                    setTotalPrice(totalCount, totalPrice);
                    foodReserveAdapter.notifyDataSetChanged();
                    setVisibilityText11(true);
                }
            }

            @Override
            public void onRemoveFoodClick(FoodModel foodModel) {
                foodReserves.remove(foodModel);
                foodReserveAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFoodClick(FoodModel food) {
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

    private void GetMenuRes(String restaurantId) {
        menus = new ArrayList<>();
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<GetMenuResModel> call = serviceAPI.GetMenuRes(restaurantId);
        call.enqueue(new Callback<GetMenuResModel>() {
            @Override
            public void onResponse(Call<GetMenuResModel> call, Response<GetMenuResModel> response) {
                if (response.body()!=null && response.body().getStatus().equals("1")){
                    menus=response.body().getMenuList();
                }
            }

            @Override
            public void onFailure(Call<GetMenuResModel> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }
}