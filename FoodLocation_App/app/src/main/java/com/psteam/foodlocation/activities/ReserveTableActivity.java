package com.psteam.foodlocation.activities;

import androidx.annotation.NonNull;

import static com.psteam.lib.RetrofitClient.getRetrofit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.FoodAdapter;
import com.psteam.foodlocation.adapters.FoodReserveAdapter;
import com.psteam.foodlocation.databinding.ActivityReserveTableBinding;
import com.psteam.foodlocation.socket.models.BodySenderFromUser;
import com.psteam.foodlocation.socket.models.MessageSenderFromUser;
import com.psteam.foodlocation.socket.setupSocket;

import org.json.JSONObject;

import java.net.URISyntaxException;

import com.psteam.foodlocation.databinding.LayoutReserveTableSuccessDialogBinding;
import com.psteam.foodlocation.databinding.LayoutUpdateUserInfoDialogBinding;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;
import com.psteam.foodlocation.ultilities.Para;
import com.psteam.foodlocation.ultilities.PreferenceManager;
import com.psteam.foodlocation.ultilities.Token;
import com.psteam.lib.Services.ServiceAPI;
import com.psteam.lib.modeluser.FoodModel;
import com.psteam.lib.modeluser.GetMenuResModel;
import com.psteam.lib.modeluser.InsertReserveFoodModel;
import com.psteam.lib.modeluser.InsertReserveTableModel;
import com.psteam.lib.modeluser.MenuModel;
import com.psteam.lib.modeluser.ReserveFood;
import com.psteam.lib.modeluser.RestaurantModel;
import com.psteam.lib.modeluser.message;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveTableActivity extends AppCompatActivity {

    private ActivityReserveTableBinding binding;
    private PreferenceManager preferenceManager;
    private Token token;
    private ArrayList<MenuModel> menus;

    private FoodReserveAdapter foodReserveAdapter;
    private ArrayList<FoodModel> foodReserves;

    private double totalPrice = 0;
    private int totalCount = 0;

    private String user = "user";
    private String AddressRestaurantReserve;
    private LocalDate DateReserve;
    private int NumberReserve;
    private RestaurantModel restaurantModel;
    private String TimeReserve;

    private MenuBottomSheetFragment menuBottomSheetFragment;

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

        socket();
        initData();
        GetMenuRes(restaurantModel.getRestaurantId());

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

        if (Para.userModel != null) {
            binding.inputFullName.setText(Para.userModel.getFullName().toString());
            binding.inputPhoneNumber.setText(Para.userModel.getPhone().toString());
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
            //lấy thời gian và ngày bắt buộc SDK >= 26
            BodySenderFromUser body = new BodySenderFromUser("i", 5, java.time.LocalTime.now() + " " + java.time.LocalDate.now(), "1", "phàm", "0589674321", "1", "hello");
            MessageSenderFromUser message = new MessageSenderFromUser("user", "restaurant", "Thông báo", body);

            setupSocket.notificationFromUser(message, setupSocket.mSocket);

            if (isValidateReserveTable()) {
                loading(true);
                InsertReserveTableModel insertReserveTableModel = new InsertReserveTableModel();
                insertReserveTableModel.setPhone(binding.inputPhoneNumber.getText().toString().trim());
                insertReserveTableModel.setName(binding.inputFullName.getText().toString().trim());
                insertReserveTableModel.setQuantity(String.valueOf(NumberReserve));
                insertReserveTableModel.setTime(String.format("%s,%s", TimeReserve, DateReserve));
                insertReserveTableModel.setUserId(preferenceManager.getString(Constants.USER_ID));
                insertReserveTableModel.setRestaurantId(restaurantModel.getRestaurantId());
                insertReserveTableModel.setNote(binding.inputNote.getText().toString().trim());
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
                foodReserves.remove(food);
                foodReserveAdapter.notifyDataSetChanged();
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


    //socket.io
    private void socket() {
        setupSocket.mSocket.connect();
        // notification login success or fail
        //setupSocket.mSocket.on("noti_login", onLogin);
        // receiver notification when used app
        //setupSocket.mSocket.on("send_notication", onNotification);

        //setupSocket.signIn(user);
    }

    @Override
    protected void onStop() {
        super.onStop();

        //notification when out activity
        setupSocket.mSocket.disconnect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //notification when come back activity
        setupSocket.mSocket.connect();
    }

    //setupSocket.reconnect(user, setupSocket.mSocket);
    private void ReserveTable(InsertReserveTableModel insertReserveTableModel) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<message> call = serviceAPI.ReserveTable(token.getToken(), insertReserveTableModel);
        call.enqueue(new Callback<message>() {
            @Override
            public void onResponse(Call<message> call, Response<message> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    if (foodReserves.size() > 0) {
                        ArrayList<ReserveFood> foods = new ArrayList<>();
                        for (FoodModel foodModel : foodReserves) {
                            foods.add(new ReserveFood(String.valueOf(foodModel.getCount()), String.valueOf(foodModel.getPrice()), foodModel.getFoodId()));
                        }
                        ReserveFood(new InsertReserveFoodModel(response.body().getId(), foods, preferenceManager.getString(Constants.USER_ID)));
                    } else {
                        openDialogReserveDialog("success");
                        loading(false);
                    }
                } else if (response.body() != null) {
                    openDialogReserveDialog("failed");
                    loading(false);
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
                    openDialogReserveDialog("success");
                } else {
                    openDialogReserveDialog("failed");
                }
                loading(false);
            }

            @Override
            public void onFailure(Call<message> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }

    private void GetMenuRes(String restaurantId) {
        menus = new ArrayList<>();
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<GetMenuResModel> call = serviceAPI.GetMenuRes(restaurantId);
        call.enqueue(new Callback<GetMenuResModel>() {
            @Override
            public void onResponse(Call<GetMenuResModel> call, Response<GetMenuResModel> response) {
                if (response.body() != null && response.body().getStatus().equals("1")) {
                    menus = response.body().getMenuList();
                }
            }

            @Override
            public void onFailure(Call<GetMenuResModel> call, Throwable t) {
                Log.d("Log:", t.getMessage());
            }
        });
    }

    private void clickOpenBottomSheetMenuFragment() {

        menuBottomSheetFragment = new MenuBottomSheetFragment(menus, new FoodAdapter.FoodListeners() {
            @Override
            public void onAddFoodClick(FoodModel food) {
                CustomToast.makeText(getApplicationContext(), "Đã thêm món ăn vào thực đơn", CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                boolean flag = false;
                for (FoodModel foodReserve : foodReserves) {
                    if (foodReserve.getName().equals(food.getName())) {
                        food.setCount(foodReserve.getCount() + 1);
                        foodReserves.remove(foodReserve);
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
                totalPrice -= foodModel.getCount() * Double.parseDouble(foodModel.getPrice());
                totalCount -= foodModel.getCount();
                setTotalPrice(totalCount, totalPrice);
                foodModel.setCount(0);
                foodReserves.remove(foodModel);
                foodReserveAdapter.notifyDataSetChanged();
                if (foodReserves.size() <= 0)
                    setVisibilityText11(false);
            }

            @Override
            public void onFoodClick(FoodModel food) {
                Toast.makeText(getApplicationContext(), food.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        menuBottomSheetFragment.show(getSupportFragmentManager(), menuBottomSheetFragment.getTag());
    }

    AlertDialog dialog;

    private void openDialogReserveDialog(String status) {
        final LayoutReserveTableSuccessDialogBinding
                layoutReserveTableSuccessDialogBinding = LayoutReserveTableSuccessDialogBinding.inflate(LayoutInflater.from(ReserveTableActivity.this));

        AlertDialog.Builder builder = new AlertDialog.Builder(ReserveTableActivity.this);
        builder.setView(layoutReserveTableSuccessDialogBinding.getRoot());
        builder.setCancelable(false);
        dialog = builder.create();

        if (!status.equals("success")) {
            layoutReserveTableSuccessDialogBinding.icon.setAnimation(R.raw.failed);
            layoutReserveTableSuccessDialogBinding.textViewTitle.setText("Lỗi khi đặt bàn\nvui lòng thử lại sau");
        }

        layoutReserveTableSuccessDialogBinding.buttonOk.setOnClickListener(v -> {
            Intent intent = new Intent(ReserveTableActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            dialog.dismiss();
            finishAffinity();
        });
        dialog.show();

    }

    private void loading(boolean Loading) {
        if (Loading) {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.buttonReserve.setVisibility(View.GONE);
        } else {
            binding.progressBar.setVisibility(View.GONE);
            binding.buttonReserve.setVisibility(View.VISIBLE);
        }
    }
}