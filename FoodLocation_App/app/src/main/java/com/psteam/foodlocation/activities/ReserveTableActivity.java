package com.psteam.foodlocation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.FoodAdapter;
import com.psteam.foodlocation.adapters.FoodReserveAdapter;
import com.psteam.foodlocation.adapters.MenuAdapter;
import com.psteam.foodlocation.databinding.ActivityReserveTableBinding;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ReserveTableActivity extends AppCompatActivity {

    private ActivityReserveTableBinding binding;

    private ArrayList<MenuAdapter.Menu> menus;

    private FoodReserveAdapter foodReserveAdapter;
    private ArrayList<FoodReserveAdapter.FoodReserve> foodReserves;

    private double totalPrice = 0;
    private int totalCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReserveTableBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        setListeners();
    }

    private void init() {
        setFullScreen();
        initFoodReserve();
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
}