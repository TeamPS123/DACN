package com.psteam.foodlocation.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.FoodAdapter;
import com.psteam.foodlocation.adapters.FoodReserveAdapter;
import com.psteam.foodlocation.adapters.MenuAdapter;
import com.psteam.foodlocation.databinding.ActivityReserveTableBinding;
import com.psteam.foodlocation.models.Socket.BodySender;
import com.psteam.foodlocation.models.Socket.User;
import com.psteam.foodlocation.ultilities.CustomToast;
import com.psteam.foodlocation.ultilities.DividerItemDecorator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ReserveTableActivity extends AppCompatActivity {

    private ActivityReserveTableBinding binding;

    private ArrayList<MenuAdapter.Menu> menus;

    private FoodReserveAdapter foodReserveAdapter;
    private ArrayList<FoodReserveAdapter.FoodReserve> foodReserves;

    private double totalPrice = 0;
    private int totalCount = 0;

    private String deviceId;
    private String user;

    String uriGlobal = "https://food-location.herokuapp.com/";
    String uriLocal = "http://192.168.1.8:3030/";
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(uriGlobal);
        } catch (URISyntaxException e) {
            e.getMessage();
        }
    }

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

        setFCM();
        socket();
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
            Gson gson = new Gson();
            BodySender body = new BodySender("i", "user", "Thông báo", "Có đơn mới nek");
            mSocket.emit("notification", gson.toJson(body));
        });
    }

    private MenuBottomSheetFragment menuBottomSheetFragment;

    private void clickOpenBottomSheetMenuFragment() {
        menus = new ArrayList<>();

        ArrayList<FoodAdapter.Food> foods = new ArrayList<>();
        foods.add(new FoodAdapter.Food(R.drawable.suatuoi, "Món ăn 1", 99000, "Sữa tươi mộc Châu"));
        foods.add(new FoodAdapter.Food(R.drawable.suatuoi, "Món ăn 2", 99000, "Sữa tươi mộc Châu"));
        foods.add(new FoodAdapter.Food(R.drawable.suatuoi, "Món ăn 3", 99000, "Sữa tươi mộc Châu"));
        foods.add(new FoodAdapter.Food(R.drawable.suatuoi, "Món ăn 4", 99000, "Sữa tươi mộc Châu"));
        foods.add(new FoodAdapter.Food(R.drawable.suatuoi, "Món ăn 5", 99000, "Sữa tươi mộc Châu"));
        menus.add(new MenuAdapter.Menu("Menu 1", foods));
        menus.add(new MenuAdapter.Menu("Menu 2", foods));
        menus.add(new MenuAdapter.Menu("Menu 3", foods));
        menus.add(new MenuAdapter.Menu("Menu 4", foods));

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

    //FCM
    private void setFCM(){
        // set notification FCM
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notification_channel", "notification_channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed Successfully";
                        if (!task.isSuccessful()) {
                            msg = "Subscription failed";
                        }
                        Log.e("Notification form FCM",msg);
                    }
                });
    }

    // get device token from FCM
    public void getToken(String user){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.e("notification_getToken", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        deviceId = task.getResult();

                        Gson gson = new Gson();
                        User user1 = new User(user, deviceId);
                        mSocket.emit("login", gson.toJson(user1));
                        // Log and toast
                        Log.e("notification_getToken", deviceId);

                    }
                });
    }

    //socket.io
    private void socket(){
        mSocket.connect();
        // notification login success or fail
        mSocket.on("noti_login", onLogin);
        // receiver notification when used app
        mSocket.on("send_notication", onNotification);
        // receiver notification when start app
        mSocket.on("new_notification", onNewNotification);

        signIn(user);
    }

    private void signIn(String user){
        getToken(user);
    }

    private final Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String notification = data.optString("message");
                    Toast.makeText(ReserveTableActivity.this, notification, Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

    private final Emitter.Listener onNotification = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String sender = data.optString("sender");
                    String title = data.optString("title");
                    String body = data.optString("body");
                    //receiver.setText(sender+": "+body);
                }
            });
        }
    };

    private final Emitter.Listener onNewNotification = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray listNotification = data.optJSONArray("list");

//                    for(int i = 0; i < listNotification.length(); i++){
//                        try {
//                            JSONObject jsonObject = new JSONObject(listNotification.getString(i));
//
//                            BodyReceiver receiver1 = new BodyReceiver(jsonObject.optString("sender"), jsonObject.optString("title"), jsonObject.optString("body"));
//
//                            receiver.setText(receiver1.getTitle()+": "+receiver1.getBody());
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//
//                        }
//                    }
                }
            });
        }
    };

    @Override
    protected void onStop() {
        super.onStop();

        //notification when out activity
        mSocket.disconnect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        //notification when come back activity
        mSocket.connect();

        Gson gson = new Gson();
        User user1 = new User(user, deviceId);
        mSocket.emit("login", gson.toJson(user1));
    }
}