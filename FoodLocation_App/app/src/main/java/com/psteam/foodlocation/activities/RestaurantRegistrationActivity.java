package com.psteam.foodlocation.activities;

import static com.psteam.foodlocation.ultilities.RetrofitClient.getRetrofit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.psteam.foodlocation.R;
import com.psteam.foodlocation.databinding.ActivityRestaurantRegistrationBinding;
import com.psteam.foodlocation.models.DistrictModel;
import com.psteam.foodlocation.models.ProvinceModel;
import com.psteam.foodlocation.models.WardModel;
import com.psteam.foodlocation.services.ServiceAPI;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantRegistrationActivity extends AppCompatActivity {

    private ActivityRestaurantRegistrationBinding binding;
    final Calendar myCalendar = Calendar.getInstance();
    private ArrayList<ProvinceModel> provinceModels;
    private ArrayList<DistrictModel> districtModels;
    private ArrayAdapter<DistrictModel> districtAdapter;
    private ArrayAdapter<ProvinceModel> provinceAdapter;
    private ArrayAdapter<WardModel> wardAdapter;
    private ArrayList<WardModel> wardModels;
    private ProvinceModel provinceModel;
    private DistrictModel districtModel;
    private WardModel wardModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestaurantRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        setListeners();
    }

    private void init() {
        provinceModel = new ProvinceModel();
        districtModel = new DistrictModel();
        wardModel = new WardModel();
        districtModels = new ArrayList<>();

        getProvinces();
        getProvince("0");
        getDistrict("0");
    }


    private void getProvinces() {
        provinceModels = new ArrayList<>();
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<ArrayList<ProvinceModel>> call = serviceAPI.GetProvinces();
        call.enqueue(new Callback<ArrayList<ProvinceModel>>() {
            @Override
            public void onResponse(Call<ArrayList<ProvinceModel>> call, Response<ArrayList<ProvinceModel>> response) {
                provinceModels = response.body();
                ArrayAdapter<ProvinceModel> cityAdapter = new ArrayAdapter<ProvinceModel>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, provinceModels);
                cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerCity.setAdapter(cityAdapter);
                binding.spinnerCity.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        ProvinceModel provinceModelItem = (ProvinceModel) item;
                        // If user change the default selection
                        // First item is disable and it is used for hint
                        if (position >= 0) {
                            binding.spinnerCity.setError(null);
                            getProvince(provinceModelItem.getCode());
                            provinceModel = provinceModelItem;
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ArrayList<ProvinceModel>> call, Throwable t) {
                Log.d("log:", t.getMessage());
            }
        });
    }

    private void getProvince(String code) {
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<ProvinceModel> call = serviceAPI.GetDistricts(code);
        call.enqueue(new Callback<ProvinceModel>() {
            @Override
            public void onResponse(Call<ProvinceModel> call, Response<ProvinceModel> response) {
                districtModels.add(new DistrictModel("-1", "huyen", "-1", "Quận / Huyện", "-1"));
                if (response.body() != null && response.body().getDistricts().size() > 0) {
                    districtModels = response.body().getDistricts();
                }
                districtAdapter = new ArrayAdapter<DistrictModel>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, districtModels);
                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerDistrict.setAdapter(districtAdapter);
                binding.spinnerDistrict.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        DistrictModel districtModelItem = (DistrictModel) item;
                        if (position >= 0) {
                            binding.spinnerDistrict.setError(null);
                            getDistrict(districtModelItem.getCode());
                            districtModel = districtModelItem;
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<ProvinceModel> call, Throwable t) {
                Log.d("log:", t.getMessage());
            }
        });
    }

    private void getDistrict(String code) {
        wardModels = new ArrayList<>();
        ServiceAPI serviceAPI = getRetrofit().create(ServiceAPI.class);
        Call<DistrictModel> call = serviceAPI.GetWards(code);
        call.enqueue(new Callback<DistrictModel>() {
            @Override
            public void onResponse(Call<DistrictModel> call, Response<DistrictModel> response) {
                wardModels.add(new WardModel("-1", "phuong", "-1", "-1", "Phường / Xã"));
                if (response.body() != null && response.body().getWards().size() > 0) {
                    wardModels = response.body().getWards();
                }
                ArrayAdapter<WardModel> wardAdapters = new ArrayAdapter<WardModel>(getApplicationContext(), android.R.layout.simple_list_item_1, wardModels);
                wardAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spinnerWard.setAdapter(wardAdapters);
                binding.spinnerWard.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                        WardModel wardModelItem = (WardModel) item;
                        if (position >= 0) {
                            binding.spinnerWard.setError(null);
                            wardModel = wardModelItem;
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<DistrictModel> call, Throwable t) {
                Log.d("log:", t.getMessage());
            }
        });
    }

    private void setListeners() {

        binding.buttonNextStep.setOnClickListener(v -> {
            if (isValidRestaurantRegistration()) {
                startActivity(new Intent(RestaurantRegistrationActivity.this, RestaurantRegistrationStep2Activity.class));
            }
        });

        TimePickerDialog.OnTimeSetListener onTimeOpenSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", new Locale("vi", "VN"));
                binding.inputTimeOpen.setText(sdf.format(myCalendar.getTime()));
            }
        };

        TimePickerDialog.OnTimeSetListener onTimeCloseSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", new Locale("vi", "VN"));
                binding.inputTimeClose.setText(sdf.format(myCalendar.getTime()));
            }
        };

        binding.inputTimeOpen.setOnClickListener(v -> {
            new TimePickerDialog(RestaurantRegistrationActivity.this, onTimeOpenSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
                    myCalendar.get(Calendar.MINUTE), true).show();
            binding.inputTimeOpen.setError(null);
        });

        binding.inputTimeClose.setOnClickListener(v -> {
            new TimePickerDialog(RestaurantRegistrationActivity.this, onTimeCloseSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
                    myCalendar.get(Calendar.MINUTE), true).show();
            binding.inputTimeClose.setError(null);
        });


    }

    private boolean isValidRestaurantRegistration() {
        if (binding.inputNameRestaurant.getText().toString().trim().isEmpty()) {
            binding.inputNameRestaurant.setError("Tên nhà hàng không được để trống");
            return false;
        } else if (provinceModel.getCode() == null) {
            binding.spinnerCity.setError("Tỉnh, thành phố không hợp lệ");
            return false;
        } else if (districtModel.getCode() == null || districtModel.getCode() == "-1") {
            binding.spinnerDistrict.setError("Quận huyện không hợp lệ");
            return false;
        } else if (wardModel.getCode() == null || wardModel.getCode() == "-1") {
            binding.spinnerWard.setError("Phường xã không hợp lệ");
            return false;
        } else if (binding.inputLine.getText().toString().trim().isEmpty()) {
            binding.inputLine.setError("Địa chỉ kinh doanh không được bỏ trống");
            return false;
        } else if (binding.inputTimeOpen.getText().toString().trim().isEmpty()) {
            binding.inputTimeOpen.setError("Giờ mở cửa không được để trống");
            return false;
        } else if (binding.inputTimeClose.getText().toString().trim().isEmpty()) {
            binding.inputTimeClose.setError("Giờ đóng cửa không được để trống");
            return false;
        } else {
            return true;
        }
    }

}