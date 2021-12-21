package com.psteam.foodlocation.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.kofigyan.stateprogressbar.StateProgressBar;
import com.kofigyan.stateprogressbar.components.StateItem;
import com.kofigyan.stateprogressbar.listeners.OnStateItemClickListener;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.adapters.ChooseCityAdapter;
import com.psteam.foodlocation.databinding.ActivitySettingBinding;
import com.psteam.foodlocation.models.DayRecommend;
import com.psteam.foodlocation.ultilities.Constants;
import com.psteam.foodlocation.ultilities.Para;
import com.psteam.foodlocation.ultilities.PreferenceManager;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;
    private PreferenceManager preferenceManager;
    private DayRecommend dayRecommend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        init();
        setListeners();

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

    private void init() {
        setFullScreen();
        String distance = preferenceManager.getString(Constants.TAG_DISTANCE);
        if (distance == null) {
            distance = "20";
            preferenceManager.putString(Constants.TAG_DISTANCE, distance);
        }

        String day_recommend = preferenceManager.getString(Constants.TAG_DAY_RECOMMEND);
        if (day_recommend == null) {
            day_recommend = "14";
            preferenceManager.putString(Constants.TAG_DAY_RECOMMEND, day_recommend);
        }


        ArrayList<DayRecommend> strings = new ArrayList<>();
        strings.add(new DayRecommend("1 tuần", 7));
        strings.add(new DayRecommend("2 tuần", 14));
        strings.add(new DayRecommend("3 tuần", 21));
        strings.add(new DayRecommend("1 tháng", 30));
        strings.add(new DayRecommend("2 tháng", 60));

        ArrayAdapter<DayRecommend> arrayAdapter = new ArrayAdapter<DayRecommend>(getApplicationContext(), android.R.layout.simple_list_item_1, strings);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerChooseTime.setAdapter(arrayAdapter);
        binding.spinnerChooseTime.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                dayRecommend = (DayRecommend) item;
                preferenceManager.putString(Constants.TAG_DAY_RECOMMEND, String.valueOf(dayRecommend.getValue()));
            }
        });

        int i = 0;
        for (DayRecommend dayRecommend : strings) {
            if (String.valueOf(dayRecommend.getValue()).equals(day_recommend)) {
                binding.spinnerChooseTime.setSelectedIndex(i);
                break;
            }
            i++;
        }

        switch (distance) {
            case "5":
                binding.progressBar.setProgress(0, true);
                binding.textviewDistance5km.setBackgroundTintList(getResources().getColorStateList(R.color.ColorTextUnSelected));
                binding.textviewDistance5km.setTextColor(getColor(R.color.white));
                binding.textviewDistance10km.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                binding.textviewDistance10km.setTextColor(getColor(R.color.TextPrimary));
                binding.textviewDistance15km.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                binding.textviewDistance15km.setTextColor(getColor(R.color.TextPrimary));
                binding.textviewDistance20km.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                binding.textviewDistance20km.setTextColor(getColor(R.color.TextPrimary));
                binding.progressBar.setProgressTintList(getResources().getColorStateList(R.color.ColorTextUnSelected));
                break;
            case "10":
                binding.progressBar.setProgress(30, true);
                binding.textviewDistance5km.setBackgroundTintList(getResources().getColorStateList(R.color.ColorTextUnSelected));
                binding.textviewDistance5km.setTextColor(getColor(R.color.white));
                binding.textviewDistance10km.setBackgroundTintList(getResources().getColorStateList(R.color.duskYellow));
                binding.textviewDistance10km.setTextColor(getColor(R.color.white));
                binding.textviewDistance15km.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                binding.textviewDistance15km.setTextColor(getColor(R.color.TextPrimary));
                binding.textviewDistance20km.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                binding.textviewDistance20km.setTextColor(getColor(R.color.TextPrimary));
                binding.progressBar.setProgressTintList(getResources().getColorStateList(R.color.duskYellow));
                break;
            case "15":
                binding.progressBar.setProgress(60, true);
                binding.textviewDistance5km.setBackgroundTintList(getResources().getColorStateList(R.color.ColorTextUnSelected));
                binding.textviewDistance5km.setTextColor(getColor(R.color.white));
                binding.textviewDistance10km.setBackgroundTintList(getResources().getColorStateList(R.color.duskYellow));
                binding.textviewDistance10km.setTextColor(getColor(R.color.white));
                binding.textviewDistance15km.setBackgroundTintList(getResources().getColorStateList(R.color.TextPrimary));
                binding.textviewDistance15km.setTextColor(getColor(R.color.white));
                binding.textviewDistance20km.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                binding.textviewDistance20km.setTextColor(getColor(R.color.TextPrimary));
                binding.progressBar.setProgressTintList(getResources().getColorStateList(R.color.TextPrimary));
                break;
            default:
                binding.progressBar.setProgress(100, true);
                binding.textviewDistance5km.setBackgroundTintList(getResources().getColorStateList(R.color.ColorTextUnSelected));
                binding.textviewDistance5km.setTextColor(getColor(R.color.white));
                binding.textviewDistance10km.setBackgroundTintList(getResources().getColorStateList(R.color.duskYellow));
                binding.textviewDistance10km.setTextColor(getColor(R.color.white));
                binding.textviewDistance15km.setBackgroundTintList(getResources().getColorStateList(R.color.TextPrimary));
                binding.textviewDistance15km.setTextColor(getColor(R.color.white));
                binding.textviewDistance20km.setBackgroundTintList(getResources().getColorStateList(R.color.ColorButtonReserve));
                binding.textviewDistance20km.setTextColor(getColor(R.color.white));
                binding.progressBar.setProgressTintList(getResources().getColorStateList(R.color.ColorButtonReserve));
                break;

        }

        initLocationAdapter();
    }

    private ArrayList<ChooseCityAdapter.City> cities;

    private void initLocationAdapter() {
        cities = new ArrayList<>();
        cities.add(new ChooseCityAdapter.City("Tp.Hồ Chí Minh", "79"));
        cities.add(new ChooseCityAdapter.City("Hà Nội", "1"));
        cities.add(new ChooseCityAdapter.City("Lâm Đồng", "68"));

        ArrayAdapter<ChooseCityAdapter.City> arrayAdapter = new ArrayAdapter<ChooseCityAdapter.City>(getApplicationContext(), android.R.layout.simple_list_item_1, cities);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerLocation.setAdapter(arrayAdapter);
        binding.spinnerLocation.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                ChooseCityAdapter.City city = (ChooseCityAdapter.City) item;
                Para.cityCode = city.getCode();
            }
        });

        for (ChooseCityAdapter.City city:cities){
            if(city.getCode().equals(Para.cityCode)){
                binding.spinnerLocation.setSelectedIndex(arrayAdapter.getPosition(city));
                break;
            }
        }
    }

    private void setListeners() {
        binding.textviewDistance5km.setOnClickListener(v -> {
            binding.progressBar.setProgress(0, true);
            binding.textviewDistance5km.setBackgroundTintList(getResources().getColorStateList(R.color.ColorTextUnSelected));
            binding.textviewDistance5km.setTextColor(getColor(R.color.white));
            binding.textviewDistance10km.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            binding.textviewDistance10km.setTextColor(getColor(R.color.TextPrimary));
            binding.textviewDistance15km.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            binding.textviewDistance15km.setTextColor(getColor(R.color.TextPrimary));
            binding.textviewDistance20km.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            binding.textviewDistance20km.setTextColor(getColor(R.color.TextPrimary));
            binding.progressBar.setProgressTintList(getResources().getColorStateList(R.color.ColorTextUnSelected));
            preferenceManager.putString(Constants.TAG_DISTANCE, "5");
        });

        binding.textviewDistance10km.setOnClickListener(v -> {
            binding.progressBar.setProgress(30, true);
            binding.textviewDistance5km.setBackgroundTintList(getResources().getColorStateList(R.color.ColorTextUnSelected));
            binding.textviewDistance5km.setTextColor(getColor(R.color.white));
            binding.textviewDistance10km.setBackgroundTintList(getResources().getColorStateList(R.color.duskYellow));
            binding.textviewDistance10km.setTextColor(getColor(R.color.white));
            binding.textviewDistance15km.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            binding.textviewDistance15km.setTextColor(getColor(R.color.TextPrimary));
            binding.textviewDistance20km.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            binding.textviewDistance20km.setTextColor(getColor(R.color.TextPrimary));
            binding.progressBar.setProgressTintList(getResources().getColorStateList(R.color.duskYellow));
            preferenceManager.putString(Constants.TAG_DISTANCE, "10");
        });

        binding.textviewDistance15km.setOnClickListener(v -> {
            binding.progressBar.setProgress(60, true);
            binding.textviewDistance5km.setBackgroundTintList(getResources().getColorStateList(R.color.ColorTextUnSelected));
            binding.textviewDistance5km.setTextColor(getColor(R.color.white));
            binding.textviewDistance10km.setBackgroundTintList(getResources().getColorStateList(R.color.duskYellow));
            binding.textviewDistance10km.setTextColor(getColor(R.color.white));
            binding.textviewDistance15km.setBackgroundTintList(getResources().getColorStateList(R.color.TextPrimary));
            binding.textviewDistance15km.setTextColor(getColor(R.color.white));
            binding.textviewDistance20km.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            binding.textviewDistance20km.setTextColor(getColor(R.color.TextPrimary));
            binding.progressBar.setProgressTintList(getResources().getColorStateList(R.color.TextPrimary));
            preferenceManager.putString(Constants.TAG_DISTANCE, "15");
        });

        binding.textviewDistance20km.setOnClickListener(v -> {
            binding.progressBar.setProgress(100, true);
            binding.textviewDistance5km.setBackgroundTintList(getResources().getColorStateList(R.color.ColorTextUnSelected));
            binding.textviewDistance5km.setTextColor(getColor(R.color.white));
            binding.textviewDistance10km.setBackgroundTintList(getResources().getColorStateList(R.color.duskYellow));
            binding.textviewDistance10km.setTextColor(getColor(R.color.white));
            binding.textviewDistance15km.setBackgroundTintList(getResources().getColorStateList(R.color.TextPrimary));
            binding.textviewDistance15km.setTextColor(getColor(R.color.white));
            binding.textviewDistance20km.setBackgroundTintList(getResources().getColorStateList(R.color.ColorButtonReserve));
            binding.textviewDistance20km.setTextColor(getColor(R.color.white));
            binding.progressBar.setProgressTintList(getResources().getColorStateList(R.color.ColorButtonReserve));
            preferenceManager.putString(Constants.TAG_DISTANCE, "20");
        });

        binding.textViewClose.setOnClickListener(v -> {
            finish();
        });
    }
}